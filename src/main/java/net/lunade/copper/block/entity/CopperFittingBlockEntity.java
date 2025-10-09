package net.lunade.copper.block.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.lunade.copper.block.CopperFittingBlock;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.SimpleCopperPipesBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import net.minecraft.Util;

public class CopperFittingBlockEntity extends AbstractSimpleCopperBlockEntity {

	private static final int MAX_TRANSFER_AMOUNT = 1;

	public int transferCooldown;

	public CopperFittingBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SimpleCopperPipesBlockEntityTypes.COPPER_FITTING, blockPos, blockState, MoveType.FROM_FITTING);
	}

	public static boolean canTransfer(@NotNull Level level, BlockPos pos, Direction direction, boolean to) {
		if (!(level.getBlockEntity(pos) instanceof CopperPipeBlockEntity pipe)) return false;

		final BlockState state = level.getBlockState(pos);
		return (!to || pipe.transferCooldown <= 0) && state.hasProperty(CopperPipeBlock.FACING) && state.getValue(CopperPipeBlock.FACING) == direction;
	}

	@Override
	public void setItem(int i, ItemStack itemStack) {
		this.unpackLootTable(null);
		if (itemStack == null) return;

		this.getItems().set(i, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) itemStack.setCount(this.getMaxStackSize());
	}

	@Override
	public void serverTick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, SimpleCopperPipesConfig config) {
		super.serverTick(level, pos, state, config);
		if (level.isClientSide()) return;

		if (this.transferCooldown > 0) {
			--this.transferCooldown;
		} else {
			this.fittingMove(level, pos, state);
		}
	}

	public void fittingMove(@NotNull Level level, BlockPos pos, @NotNull BlockState state) {
		final boolean movedOut = state.hasProperty(BlockStateProperties.POWERED) && !state.getValue(BlockStateProperties.POWERED) && this.moveOut(level, pos, level.random);
		final boolean movedIn = this.moveIn(level, pos, level.random);
		if (movedOut || movedIn) {
			setCooldown(state);
			setChanged(level, pos, state);
		}
	}

	private boolean moveIn(Level level, @NotNull BlockPos pos, RandomSource random) {
		boolean result = false;
		for (Direction direction : Util.shuffledCopy(Direction.values(), random)) {
			final Direction opposite = direction.getOpposite();
			final BlockPos oppositePos = pos.relative(opposite);
			final Storage<ItemVariant> inventory = CopperPipeBlockEntity.getStorageAt(level, oppositePos, direction);
			final Storage<ItemVariant> fittingInventory = CopperPipeBlockEntity.getStorageAt(level, pos, opposite);
			if (inventory == null || fittingInventory == null || !canTransfer(level, oppositePos, direction, false)) continue;

			for (StorageView<ItemVariant> storageView : inventory) {
				if (storageView.isResourceBlank() || storageView.getAmount() <= 0) continue;

				final Transaction transaction = Transaction.openOuter();
				final var resource = storageView.getResource();
				final long extracted = inventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
				if (extracted > 0) {
					long inserted = CopperPipeBlockEntity.addItem(resource, fittingInventory, transaction);
					if (inserted > 0) {
						transaction.commit(); // applies the changes
						result = true;
					}
				}
				transaction.close(); // if it cant commit, close it.
				// make sure to close instead of commit bc the item would be deleted
			}
		}
		return result;
	}

	private boolean moveOut(Level level, @NotNull BlockPos pos, RandomSource random) {
		boolean result = false;
		for (Direction direction : Util.shuffledCopy(Direction.values(), random)) {
			final BlockPos offsetPos = pos.relative(direction);
			final Direction opposite = direction.getOpposite();
			final Storage<ItemVariant> inventory = ItemStorage.SIDED.find(level, offsetPos, level.getBlockState(offsetPos), level.getBlockEntity(offsetPos), opposite);
			final Storage<ItemVariant> fittingInventory = ItemStorage.SIDED.find(level, pos, level.getBlockState(pos), level.getBlockEntity(pos), direction);
			if (inventory == null || fittingInventory == null || !canTransfer(level, offsetPos, direction, true)) continue;

			for (StorageView<ItemVariant> storageView : fittingInventory) {
				if (storageView.isResourceBlank() || storageView.getAmount() <= 0) continue;

				final Transaction transaction = Transaction.openOuter();
				final var resource = storageView.getResource();
				final long inserted = inventory.insert(resource, MAX_TRANSFER_AMOUNT, transaction);
				if (inserted > 0) { // successfully inserted item
					long extracted = fittingInventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
					if (extracted > 0) {
						transaction.commit(); // applies the changes
						result = true;
					}
				}
				transaction.close(); // if it can't commit, close it.
				// make sure to close instead of commit bc the item would be deleted
			}
		}
		return result;
	}

	public void setCooldown(@NotNull BlockState state) {
		this.transferCooldown = state.getBlock() instanceof CopperFittingBlock fitting ? fitting.getCooldown() : 2;
	}

	@Override
	public boolean canAcceptTransferableData(MoveType moveType, Direction moveDirection, BlockState fromState) {
		return moveType == MoveType.FROM_PIPE && moveDirection == fromState.getValue(BlockStateProperties.FACING);
	}

	@Override
	public void updateBlockEntityValues(LevelReader level, BlockPos pos, @NotNull BlockState state, SimpleCopperPipesConfig config) {
		if (state.getBlock() instanceof CopperFittingBlock) this.canWater = state.getValue(BlockStateProperties.WATERLOGGED) && config.carryWater;
	}

	@Override
	public void loadAdditional(@NotNull ValueInput input) {
		super.loadAdditional(input);
		this.transferCooldown = input.getIntOr("transferCooldown", 0);
	}

	@Override
	protected void saveAdditional(@NotNull ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("transferCooldown", this.transferCooldown);
	}

}

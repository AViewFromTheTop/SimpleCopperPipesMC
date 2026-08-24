package net.lunade.copper.block.entity;

import net.frozenblock.lib.transfer.api.TransferApi;
import net.lunade.copper.block.CopperFittingBlock;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.SimpleCopperPipesBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class CopperFittingBlockEntity extends AbstractSimpleCopperBlockEntity {

	private static final int MAX_TRANSFER_AMOUNT = 1;

	public int transferCooldown;

	public CopperFittingBlockEntity(BlockPos pos, BlockState state) {
		super(SimpleCopperPipesBlockEntityTypes.COPPER_FITTING.get(), pos, state, MoveType.FROM_FITTING);
	}

	public static boolean canTransfer(Level level, BlockPos pos, Direction direction, boolean to) {
		if (!(level.getBlockEntity(pos) instanceof CopperPipeBlockEntity pipe)) return false;

		final BlockState state = level.getBlockState(pos);
		return (!to || pipe.transferCooldown <= 0) && state.hasProperty(CopperPipeBlock.FACING) && state.getValue(CopperPipeBlock.FACING) == direction;
	}

	@Override
	public void setItem(int i, ItemStack stack) {
		this.unpackLootTable(null);
		if (stack == null) return;

		this.getItems().set(i, stack);
		if (stack.getCount() > this.getMaxStackSize()) stack.setCount(this.getMaxStackSize());
	}

	@Override
	public void serverTick(Level level, BlockPos pos, BlockState state) {
		super.serverTick(level, pos, state);
		if (level.isClientSide()) return;

		if (this.transferCooldown > 0) {
			--this.transferCooldown;
		} else {
			this.fittingMove(level, pos, state);
		}
	}

	public void fittingMove(Level level, BlockPos pos, BlockState state) {
		final boolean movedOut = state.hasProperty(BlockStateProperties.POWERED) && !state.getValue(BlockStateProperties.POWERED) && this.moveOut(level, pos, level.getRandom());
		final boolean movedIn = this.moveIn(level, pos, level.getRandom());
		if (!movedOut && !movedIn) return;

		setCooldown(state);
		setChanged(level, pos, state);
	}

	private boolean moveIn(Level level, BlockPos pos, RandomSource random) {
		boolean result = false;
		for (Direction direction : Util.shuffledCopy(Direction.values(), random)) {
			final Direction opposite = direction.getOpposite();
			final BlockPos oppositePos = pos.relative(opposite);
			if (!canTransfer(level, oppositePos, direction, false)) continue;

			if (TransferApi.moveItems(level, oppositePos, direction, pos, opposite, null, MAX_TRANSFER_AMOUNT) > 0) result = true;
		}
		return result;
	}

	private boolean moveOut(Level level, BlockPos pos, RandomSource random) {
		boolean result = false;
		for (Direction direction : Util.shuffledCopy(Direction.values(), random)) {
			final BlockPos offsetPos = pos.relative(direction);
			final Direction opposite = direction.getOpposite();
			if (!canTransfer(level, offsetPos, direction, true)) continue;

			if (TransferApi.moveItems(level, pos, direction, offsetPos, opposite, null, MAX_TRANSFER_AMOUNT) > 0) result = true;
		}
		return result;
	}

	public void setCooldown(BlockState state) {
		this.transferCooldown = state.getBlock() instanceof CopperFittingBlock fitting ? fitting.getCooldown() : 2;
	}

	@Override
	public boolean canAcceptTransferableData(MoveType moveType, Direction moveDirection, BlockState fromState) {
		return moveType == MoveType.FROM_PIPE && moveDirection == fromState.getValue(BlockStateProperties.FACING);
	}

	@Override
	public void updateBlockEntityValues(LevelReader level, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof CopperFittingBlock) this.canWater = state.getValue(BlockStateProperties.WATERLOGGED) && SimpleCopperPipesConfig.CARRY_WATER.get();
	}

	@Override
	public void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.transferCooldown = input.getIntOr("transferCooldown", 0);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("transferCooldown", this.transferCooldown);
	}

}

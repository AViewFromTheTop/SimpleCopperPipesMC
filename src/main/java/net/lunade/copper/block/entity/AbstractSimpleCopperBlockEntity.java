package net.lunade.copper.block.entity;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Optional;
import net.lunade.copper.SimpleCopperPipes;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.block.entity.data.TransferablePipeDataHandler;
import net.lunade.copper.block.properties.PipeFluid;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.SimpleCopperPipesBlockStateProperties;
import net.lunade.copper.registry.TransferablePipeData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AbstractSimpleCopperBlockEntity extends RandomizableContainerBlockEntity implements Container {
	public final MoveType moveType;
	public NonNullList<ItemStack> inventory;
	public int waterCooldown;
	public int electricityCooldown;
	public boolean canWater;
	public boolean canLava;
	public boolean canSmoke;
	//DataFixing
	public int lastFixVersion;
	public TransferablePipeDataHandler transferableDataHandler;

	public AbstractSimpleCopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, MoveType moveType) {
		super(blockEntityType, blockPos, blockState);
		this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
		this.waterCooldown = -1;
		this.electricityCooldown = -1;
		this.transferableDataHandler = new TransferablePipeDataHandler();
		this.moveType = moveType;
	}

	public static void sendElectricity(Level level, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			final BlockPos offsetPos = pos.relative(direction);
			if (!level.hasChunkAt(offsetPos)) continue;

			final BlockState state = level.getBlockState(offsetPos);
			if (!state.hasProperty(SimpleCopperPipesBlockStateProperties.HAS_ELECTRICITY)) continue;

			if (!(level.getBlockEntity(offsetPos) instanceof AbstractSimpleCopperBlockEntity copperBlockEntity)) continue;

			if (copperBlockEntity.electricityCooldown == -1) {
				final int axis = state.hasProperty(CopperPipeBlock.FACING) ? state.getValue(CopperPipeBlock.FACING).getAxis().ordinal() : direction.getAxis().ordinal();
				level.levelEvent(LevelEvent.PARTICLES_ELECTRIC_SPARK, offsetPos, axis);
				level.setBlockAndUpdate(offsetPos, state.setValue(SimpleCopperPipesBlockStateProperties.HAS_ELECTRICITY, true));
			}
		}
	}

	public void serverTick(Level level, BlockPos pos, BlockState originalState) {
		if (level.isClientSide()) return;

		BlockState state = originalState;

		if (this.lastFixVersion < SimpleCopperPipesConstants.CURRENT_FIX_VERSION || SimpleCopperPipes.REFRESH_VALUES) {
			this.updateBlockEntityValues(level, pos, originalState);
			this.lastFixVersion = SimpleCopperPipesConstants.CURRENT_FIX_VERSION;
		}

		if (this.canWater && !this.canLava && SimpleCopperPipesConfig.CARRY_WATER.get()) {
			this.transferableDataHandler.setTransferablePipeData(TransferablePipeData.WATER, new TransferablePipeDataHandler.SaveableTransferablePipeData()
				.withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withID(TransferablePipeData.WATER));
		}

		if (this.canLava && !this.canWater && SimpleCopperPipesConfig.CARRY_LAVA.get()) {
			this.transferableDataHandler.setTransferablePipeData(TransferablePipeData.LAVA, new TransferablePipeDataHandler.SaveableTransferablePipeData()
				.withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withID(TransferablePipeData.LAVA));
		}

		if ((this.canSmoke && !this.canWater && !this.canLava) || (this.canWater && this.canLava) && SimpleCopperPipesConfig.CARRY_SMOKE.get()) {
			this.transferableDataHandler.setTransferablePipeData(TransferablePipeData.SMOKE, new TransferablePipeDataHandler.SaveableTransferablePipeData()
				.withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withID(TransferablePipeData.SMOKE));
		}

		final TransferablePipeDataHandler.SaveableTransferablePipeData waterData = this.transferableDataHandler.getTransferablePipeData(TransferablePipeData.WATER);
		final TransferablePipeDataHandler.SaveableTransferablePipeData lavaData = this.transferableDataHandler.getTransferablePipeData(TransferablePipeData.LAVA);
		final TransferablePipeDataHandler.SaveableTransferablePipeData smokeData = this.transferableDataHandler.getTransferablePipeData(TransferablePipeData.SMOKE);
		boolean validWater = isValidFluidNBT(waterData) && SimpleCopperPipesConfig.CARRY_WATER.get();
		boolean validLava = isValidFluidNBT(lavaData) && SimpleCopperPipesConfig.CARRY_LAVA.get();
		boolean validSmoke = isValidFluidNBT(smokeData) && SimpleCopperPipesConfig.CARRY_SMOKE.get();
		if (this.canSmoke && ((this.canLava && !this.canWater) || (this.canWater && !this.canLava))) validSmoke = false;
		if (this.canWater && this.canLava) {
			validSmoke = SimpleCopperPipesConfig.CARRY_SMOKE.get();
			validWater = false;
			validLava = false;
		}
		if (state.hasProperty(SimpleCopperPipesBlockStateProperties.FLUID)) {
			state = state.setValue(
				SimpleCopperPipesBlockStateProperties.FLUID,
				validWater ? PipeFluid.WATER
					: validLava ? PipeFluid.LAVA
					: validSmoke ? PipeFluid.SMOKE
					: PipeFluid.NONE
			);
		}

		this.tickTransferableData((ServerLevel) level, pos, originalState);
		this.dispenseTransferableData((ServerLevel) level, pos, originalState);
		this.moveTransferableData((ServerLevel) level, pos, originalState);

		if (this.electricityCooldown >= 0) --this.electricityCooldown;
		if (this.electricityCooldown == -1 && state.getValue(SimpleCopperPipesBlockStateProperties.HAS_ELECTRICITY)) {
			this.electricityCooldown = 80;
			final Optional<Block> previous = WeatheringCopper.getPrevious(state.getBlock());
			if (previous.isPresent()) state = previous.get().withPropertiesOf(state);

		}
		if (this.electricityCooldown == 79) sendElectricity(level, pos);
		if (this.electricityCooldown == 0 && state.hasProperty(SimpleCopperPipesBlockStateProperties.HAS_ELECTRICITY)) {
			state = state.setValue(SimpleCopperPipesBlockStateProperties.HAS_ELECTRICITY, false);
		}

		if (state != originalState) level.setBlockAndUpdate(pos, state);
	}

	public boolean isValidFluidNBT(@Nullable TransferablePipeDataHandler.SaveableTransferablePipeData fluidData) {
		if (fluidData != null) return fluidData.vec3d.x() > 0D;
		return false;
	}

	public void updateBlockEntityValues(LevelReader level, BlockPos pos, BlockState state) {
	}

	public boolean canAcceptTransferableData(MoveType moveType, Direction moveDirection, BlockState fromState) {
		return true;
	}

	public boolean canTransferDataInDirection(Direction direction, BlockState state) {
		return true;
	}

	public void tickTransferableData(ServerLevel level, BlockPos pos, BlockState state) {
		ImmutableList.copyOf(this.transferableDataHandler.getSavedDataList()).forEach(data -> {
			data.tick(level, pos, state, this);
		});
	}

	public void dispenseTransferableData(ServerLevel level, BlockPos pos, BlockState state) {
	}

	public void moveTransferableData(ServerLevel level, BlockPos pos, BlockState state) {
		final ArrayList<TransferablePipeDataHandler.SaveableTransferablePipeData> dataList = transferableDataHandler.getSavedDataList();
		final ArrayList<TransferablePipeDataHandler.SaveableTransferablePipeData> usedData = new ArrayList<>();
		if (dataList.isEmpty()) return;

		for (Direction direction : Util.shuffledCopy(Direction.values(), level.getRandom())) {
			if (!this.canTransferDataInDirection(direction, state)) continue;

			final BlockPos offsetPos = pos.relative(direction);
			if (!level.hasChunkAt(offsetPos)) continue;

			final BlockEntity blockEntity = level.getBlockEntity(offsetPos);
			if (!(blockEntity instanceof AbstractSimpleCopperBlockEntity copperBlockEntity)) continue;
			if (!copperBlockEntity.canAcceptTransferableData(this.moveType, direction, state)) continue;

			final BlockState offsetState = level.getBlockState(offsetPos);
			for (TransferablePipeDataHandler.SaveableTransferablePipeData data : dataList) {
				if (data.shouldMove() && (!data.canOnlyGoThroughOnePipe() || !usedData.contains(data)) && data.canMove(level, offsetPos, offsetState, copperBlockEntity)) {
					final TransferablePipeDataHandler.SaveableTransferablePipeData onMove = data.shouldCopy() ? data.copyOf() : data;
					copperBlockEntity.transferableDataHandler.setTransferablePipeData(data.getID(), onMove);
					onMove.onMove(level, offsetPos, offsetState, copperBlockEntity);
					if (!usedData.contains(data)) usedData.add(data);
				}
			}
		}

		this.transferableDataHandler.clearAllButNonMoveable();
		usedData.clear();
		this.setChanged();
	}

	@Override
	public void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(input)) ContainerHelper.loadAllItems(input, this.inventory);
		this.waterCooldown = input.getIntOr("WaterCooldown", 0);
		this.electricityCooldown = input.getIntOr("electricityCooldown", 0);
		this.canWater = input.getBooleanOr("canWater", false);
		this.canLava = input.getBooleanOr("canLava", false);
		this.canSmoke = input.getBooleanOr("canSmoke", false);
		this.lastFixVersion = input.getIntOr("lastFixVersion", 0);
		this.transferableDataHandler.load(input);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (!this.trySaveLootTable(output)) ContainerHelper.saveAllItems(output, this.inventory);
		output.putInt("WaterCooldown", this.waterCooldown);
		output.putInt("electricityCooldown", this.electricityCooldown);
		output.putBoolean("canWater", this.canWater);
		output.putBoolean("canLava", this.canLava);
		output.putBoolean("canSmoke", this.canSmoke);
		output.putInt("lastFixVersion", this.lastFixVersion);
		this.transferableDataHandler.save(output);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> defaultedList) {
		this.inventory = defaultedList;
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
	}

	@Override
	protected AbstractContainerMenu createMenu(int i, Inventory playerInventory) {
		return new HopperMenu(i, playerInventory, this);
	}

	@Override
	public int getContainerSize() {
		return this.inventory.size();
	}

	public enum MoveType {
		FROM_PIPE,
		FROM_FITTING
	}

}

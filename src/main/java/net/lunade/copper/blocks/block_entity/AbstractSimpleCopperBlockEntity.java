package net.lunade.copper.blocks.block_entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.lunade.copper.SimpleCopperPipesMain;
import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.lunade.copper.blocks.block_entity.pipe_nbt.MoveablePipeDataHandler;
import net.lunade.copper.blocks.properties.CopperPipeProperties;
import net.lunade.copper.blocks.properties.PipeFluid;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.RegisterPipeNbtMethods;
import net.lunade.copper.tag.SimpleCopperPipesBlockTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractSimpleCopperBlockEntity extends RandomizableContainerBlockEntity implements Container {

    public final MOVE_TYPE moveType;
    public NonNullList<ItemStack> inventory;
    public int waterCooldown;
    public int electricityCooldown;
    public boolean canWater;
    public boolean canLava;
    public boolean canSmoke;
    //DataFixing
    public int lastFixVersion;
    public MoveablePipeDataHandler moveablePipeDataHandler;

    public AbstractSimpleCopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, MOVE_TYPE moveType) {
        super(blockEntityType, blockPos, blockState);
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
        this.waterCooldown = -1;
        this.electricityCooldown = -1;
        this.moveablePipeDataHandler = new MoveablePipeDataHandler();
        this.moveType = moveType;
    }

    public static void sendElectricity(Level level, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            BlockPos pos = blockPos.relative(direction);
            if (level.hasChunkAt(pos)) {
                BlockState state = level.getBlockState(pos);
                if (state.hasProperty(CopperPipeProperties.HAS_ELECTRICITY)) {
                    BlockEntity entity = level.getBlockEntity(pos);
                    if (entity instanceof AbstractSimpleCopperBlockEntity copperBlockEntity) {
                        int axis = state.hasProperty(BlockStateProperties.FACING) ? state.getValue(BlockStateProperties.FACING).getAxis().ordinal() : direction.getAxis().ordinal();
                        if (copperBlockEntity.electricityCooldown == -1) {
                            level.levelEvent(3002, pos, axis);
                            level.setBlockAndUpdate(pos, state.setValue(CopperPipeProperties.HAS_ELECTRICITY, true));
                        }
                    }
                }
            }
        }
    }

    public void serverTick(@NotNull Level level, BlockPos blockPos, BlockState blockState) {
        BlockState state = blockState;
        if (!level.isClientSide) {
            if (this.lastFixVersion < SimpleCopperPipesSharedConstants.CURRENT_FIX_VERSION || SimpleCopperPipesMain.refreshValues) {
                this.updateBlockEntityValues(level, blockPos, blockState);
                this.lastFixVersion = SimpleCopperPipesSharedConstants.CURRENT_FIX_VERSION;
            }
            if (this.canWater && !this.canLava && SimpleCopperPipesConfig.get().carryWater) {
                this.moveablePipeDataHandler.setMoveablePipeNbt(RegisterPipeNbtMethods.WATER, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                        .withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withNBTID(RegisterPipeNbtMethods.WATER));
            }
            if (this.canLava && !this.canWater && SimpleCopperPipesConfig.get().carryLava) {
                this.moveablePipeDataHandler.setMoveablePipeNbt(RegisterPipeNbtMethods.LAVA, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                        .withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withNBTID(RegisterPipeNbtMethods.LAVA));
            }
            if ((this.canSmoke && !this.canWater && !this.canLava) || (this.canWater && this.canLava) && SimpleCopperPipesConfig.get().carrySmoke) {
                this.moveablePipeDataHandler.setMoveablePipeNbt(RegisterPipeNbtMethods.SMOKE, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                        .withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withNBTID(RegisterPipeNbtMethods.SMOKE));
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt waterNbt = this.moveablePipeDataHandler.getMoveablePipeNbt(RegisterPipeNbtMethods.WATER);
            MoveablePipeDataHandler.SaveableMovablePipeNbt lavaNbt = this.moveablePipeDataHandler.getMoveablePipeNbt(RegisterPipeNbtMethods.LAVA);
            MoveablePipeDataHandler.SaveableMovablePipeNbt smokeNbt = this.moveablePipeDataHandler.getMoveablePipeNbt(RegisterPipeNbtMethods.SMOKE);
            boolean validWater = isValidFluidNBT(waterNbt) && SimpleCopperPipesConfig.get().carryWater;
            boolean validLava = isValidFluidNBT(lavaNbt) && SimpleCopperPipesConfig.get().carryLava;
            boolean validSmoke = isValidFluidNBT(smokeNbt) && SimpleCopperPipesConfig.get().carrySmoke;
            if (this.canSmoke && ((this.canLava && !this.canWater) || (this.canWater && !this.canLava))) {
                validSmoke = false;
            }
            if (this.canWater && this.canLava) {
                validSmoke = SimpleCopperPipesConfig.get().carrySmoke;
                validWater = false;
                validLava = false;
            }
            if (state.hasProperty(CopperPipeProperties.FLUID)) {
                state = state.setValue(CopperPipeProperties.FLUID, validWater ? PipeFluid.WATER : validLava ? PipeFluid.LAVA : validSmoke ? PipeFluid.SMOKE : PipeFluid.NONE);
            }
            this.tickMoveableNbt((ServerLevel) level, blockPos, blockState);
            this.dispenseMoveableNbt((ServerLevel) level, blockPos, blockState);
            this.moveMoveableNbt((ServerLevel) level, blockPos, blockState);
            if (this.isEmpty() == state.getValue(CopperPipeProperties.HAS_ITEM)) {
                state = state.setValue(CopperPipeProperties.HAS_ITEM, !isEmpty());
            }
            if (this.electricityCooldown >= 0) {
                --this.electricityCooldown;
            }
            if (this.electricityCooldown == -1 && state.getValue(CopperPipeProperties.HAS_ELECTRICITY)) {
                this.electricityCooldown = 80;
                Block stateGetBlock = state.getBlock();
                Optional<Block> previous = WeatheringCopper.getPrevious(stateGetBlock);
                if (previous.isPresent() && !state.is(SimpleCopperPipesBlockTags.WAXED)) {
                    state = previous.get().withPropertiesOf(state);
                }
            }
            if (this.electricityCooldown == 79) {
                sendElectricity(level, blockPos);
            }
            if (this.electricityCooldown == 0) {
                if (state.hasProperty(CopperPipeProperties.HAS_ELECTRICITY)) {
                    state = state.setValue(CopperPipeProperties.HAS_ELECTRICITY, false);
                }
            }
            if (state != blockState) {
                level.setBlockAndUpdate(blockPos, state);
            }
        }
    }

    public boolean isValidFluidNBT(@Nullable MoveablePipeDataHandler.SaveableMovablePipeNbt fluidNBT) {
        if (fluidNBT != null) {
            return fluidNBT.vec3d.x() > 0;
        }
        return false;
    }

    public void updateBlockEntityValues(Level level, BlockPos pos, BlockState state) {

    }

    public boolean canAcceptMoveableNbt(MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        return true;
    }

    public boolean canMoveNbtInDirection(Direction direction, BlockState blockState) {
        return true;
    }

    public void tickMoveableNbt(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {
        for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : (List<MoveablePipeDataHandler.SaveableMovablePipeNbt>) this.moveablePipeDataHandler.getSavedNbtList().clone()) {
            nbt.tick(serverLevel, blockPos, blockState, this);
        }
    }

    public void dispenseMoveableNbt(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {

    }

    public void moveMoveableNbt(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {
        ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> nbtList = moveablePipeDataHandler.getSavedNbtList();
        ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> usedNbts = new ArrayList<>();
        if (!nbtList.isEmpty()) {
            List<Direction> dirs = Util.shuffledCopy(Direction.values(), serverLevel.getRandom());
            for (Direction direction : dirs) {
                if (this.canMoveNbtInDirection(direction, blockState)) {
                    BlockPos newPos = blockPos.relative(direction);
                    if (serverLevel.hasChunkAt(newPos)) {
                        BlockState state = serverLevel.getBlockState(newPos);
                        BlockEntity entity = serverLevel.getBlockEntity(newPos);
                        if (entity instanceof AbstractSimpleCopperBlockEntity copperEntity) {
                            if (copperEntity.canAcceptMoveableNbt(this.moveType, direction, blockState)) {
                                for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : nbtList) {
                                    if (nbt.getShouldMove() && (!nbt.getCanOnlyGoThroughOnePipe() || !usedNbts.contains(nbt)) && nbt.canMove(serverLevel, newPos, state, copperEntity)) {
                                        MoveablePipeDataHandler.SaveableMovablePipeNbt onMove;
                                        if (nbt.getShouldCopy()) {
                                            onMove = nbt.copyOf();
                                        } else {
                                            onMove = nbt;
                                        }
                                        copperEntity.moveablePipeDataHandler.setMoveablePipeNbt(nbt.getNbtID(), onMove);
                                        onMove.onMove(serverLevel, newPos, state, copperEntity);
                                        if (!usedNbts.contains(nbt)) {
                                            usedNbts.add(nbt);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.moveablePipeDataHandler.clearAllButNonMoveable();
            usedNbts.clear();
            this.setChanged();
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbtCompound, HolderLookup.@NotNull Provider lookupProvider) {
        super.loadAdditional(nbtCompound, lookupProvider);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbtCompound)) {
            ContainerHelper.loadAllItems(nbtCompound, this.inventory, lookupProvider);
        }
        this.waterCooldown = nbtCompound.getInt("WaterCooldown");
        this.electricityCooldown = nbtCompound.getInt("electricityCooldown");
        this.canWater = nbtCompound.getBoolean("canWater");
        this.canLava = nbtCompound.getBoolean("canLava");
        this.canSmoke = nbtCompound.getBoolean("canSmoke");
        this.lastFixVersion = nbtCompound.getInt("lastFixVersion");
        this.moveablePipeDataHandler.readNbt(nbtCompound);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbtCompound, HolderLookup.@NotNull Provider lookupProvider) {
        super.saveAdditional(nbtCompound, lookupProvider);
        if (!this.trySaveLootTable(nbtCompound)) {
            ContainerHelper.saveAllItems(nbtCompound, this.inventory, lookupProvider);
        }
        nbtCompound.putInt("WaterCooldown", this.waterCooldown);
        nbtCompound.putInt("electricityCooldown", this.electricityCooldown);
        nbtCompound.putBoolean("canWater", this.canWater);
        nbtCompound.putBoolean("canLava", this.canLava);
        nbtCompound.putBoolean("canSmoke", this.canSmoke);
        nbtCompound.putInt("lastFixVersion", this.lastFixVersion);
        this.moveablePipeDataHandler.writeNbt(nbtCompound);
    }

    @Override
    @NotNull
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> defaultedList) {
        this.inventory = defaultedList;
    }

    @Override
    @NotNull
    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    @NotNull
    protected AbstractContainerMenu createMenu(int i, Inventory playerInventory) {
        return new HopperMenu(i, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    public enum MOVE_TYPE {
        FROM_PIPE,
        FROM_FITTING
    }

}

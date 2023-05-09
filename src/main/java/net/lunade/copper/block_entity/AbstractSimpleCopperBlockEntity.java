package net.lunade.copper.block_entity;

import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AbstractSimpleCopperBlockEntity extends RandomizableContainerBlockEntity implements Container {

    public NonNullList<ItemStack> inventory;
    public int waterCooldown;
    public int electricityCooldown;
    public boolean canWater;
    public boolean canSmoke;

    //DataFixing
    public int lastFixVersion;

    public MoveablePipeDataHandler moveablePipeDataHandler;
    public final MOVE_TYPE moveType;

    public AbstractSimpleCopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, MOVE_TYPE moveType) {
        super(blockEntityType, blockPos, blockState);
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
        this.waterCooldown = -1;
        this.electricityCooldown = -1;
        this.moveablePipeDataHandler = new MoveablePipeDataHandler();
        this.moveType = moveType;
    }

    public void serverTick(Level world, BlockPos blockPos, BlockState blockState) {
        BlockState state = blockState;
        if (!world.isClientSide) {
            if (this.lastFixVersion < CopperPipeMain.CURRENT_FIX_VERSION) {
                this.updateBlockEntityValues(world, blockPos, blockState);
                this.lastFixVersion = CopperPipeMain.CURRENT_FIX_VERSION;
            }
            if (this.canWater) {
                this.moveablePipeDataHandler.setMoveablePipeNbt(CopperPipeMain.WATER, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                        .withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withNBTID(CopperPipeMain.WATER));
            }
            if (this.canSmoke) {
                this.moveablePipeDataHandler.setMoveablePipeNbt(CopperPipeMain.SMOKE, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                        .withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withNBTID(CopperPipeMain.SMOKE));
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt waterNbt = this.moveablePipeDataHandler.getMoveablePipeNbt(CopperPipeMain.WATER);
            MoveablePipeDataHandler.SaveableMovablePipeNbt smokeNbt = this.moveablePipeDataHandler.getMoveablePipeNbt(CopperPipeMain.SMOKE);
            if (state.hasProperty(CopperPipeProperties.HAS_WATER)) {
                state = state.setValue(CopperPipeProperties.HAS_WATER, this.canWater || waterNbt != null);
            }
            if (state.hasProperty(CopperPipeProperties.HAS_SMOKE)) {
                state = state.setValue(CopperPipeProperties.HAS_SMOKE, this.canSmoke || smokeNbt != null);
            }
            this.tickMoveableNbt((ServerLevel) world, blockPos, blockState);
            this.dispenseMoveableNbt((ServerLevel) world, blockPos, blockState);
            this.moveMoveableNbt((ServerLevel) world, blockPos, blockState);
            if (this.isEmpty() == state.getValue(CopperPipeProperties.HAS_ITEM)) {
                state = state.setValue(CopperPipeProperties.HAS_ITEM, !isEmpty());
            }
            if (this.electricityCooldown >= 0) {
                --this.electricityCooldown;
            }
            if (this.electricityCooldown == -1 && state.getValue(CopperPipeProperties.HAS_ELECTRICITY)) {
                this.electricityCooldown = 80;
                Block stateGetBlock = state.getBlock();
                if (CopperPipeMain.PREVIOUS_STAGE.containsKey(stateGetBlock) && !state.is(CopperPipeMain.WAXED)) {
                    state = CopperPipeMain.PREVIOUS_STAGE.get(stateGetBlock).withPropertiesOf(state);
                }
            }
            if (this.electricityCooldown == 79) {
                sendElectricity(world, blockPos);
            }
            if (this.electricityCooldown == 0) {
                if (state.hasProperty(CopperPipeProperties.HAS_ELECTRICITY)) {
                    state = state.setValue(CopperPipeProperties.HAS_ELECTRICITY, false);
                }
            }
            if (state != blockState) {
                world.setBlockAndUpdate(blockPos, state);
            }
        }
    }

    public static void sendElectricity(Level world, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            BlockPos pos = blockPos.relative(direction);
            if (world.hasChunkAt(pos)) {
                BlockState state = world.getBlockState(pos);
                if (state.hasProperty(CopperPipeProperties.HAS_ELECTRICITY)) {
                    BlockEntity entity = world.getBlockEntity(pos);
                    if (entity instanceof AbstractSimpleCopperBlockEntity copperBlockEntity) {
                        int axis = state.hasProperty(BlockStateProperties.FACING) ? state.getValue(BlockStateProperties.FACING).getAxis().ordinal() : direction.getAxis().ordinal();
                        if (copperBlockEntity.electricityCooldown == -1) {
                            world.levelEvent(3002, pos, axis);
                            world.setBlockAndUpdate(pos, state.setValue(CopperPipeProperties.HAS_ELECTRICITY, true));
                        }
                    }
                }
            }
        }
    }

    public void updateBlockEntityValues(Level world, BlockPos pos, BlockState state) {

    }

    public boolean canAcceptMoveableNbt(MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        return true;
    }

    public boolean canMoveNbtInDirection(Direction direction, BlockState blockState) {
        return true;
    }

    public void tickMoveableNbt(ServerLevel world, BlockPos blockPos, BlockState blockState) {
        for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : this.moveablePipeDataHandler.getSavedNbtList()) {
            nbt.tick(world, blockPos, blockState, this);
        }
    }

    public void dispenseMoveableNbt(ServerLevel serverWorld, BlockPos blockPos, BlockState blockState) {

    }

    public void moveMoveableNbt(ServerLevel world, BlockPos blockPos, BlockState blockState) {
        ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> nbtList = moveablePipeDataHandler.getSavedNbtList();
        ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> usedNbts = new ArrayList<>();
        if (!nbtList.isEmpty()) {
            List<Direction> dirs = CopperPipeMain.shuffledDirections(world.getRandom());
            for (Direction direction : dirs) {
                if (this.canMoveNbtInDirection(direction, blockState)) {
                    BlockPos newPos = blockPos.relative(direction);
                    if (world.hasChunkAt(newPos)) {
                        BlockState state = world.getBlockState(newPos);
                        BlockEntity entity = world.getBlockEntity(newPos);
                        if (entity instanceof AbstractSimpleCopperBlockEntity copperEntity) {
                            if (copperEntity.canAcceptMoveableNbt(this.moveType, direction, blockState)) {
                                for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : nbtList) {
                                    if (nbt.getShouldMove() && (!nbt.getCanOnlyGoThroughOnePipe() || !usedNbts.contains(nbt)) && nbt.canMove(world, newPos, state, copperEntity)) {
                                        MoveablePipeDataHandler.SaveableMovablePipeNbt onMove;
                                        if (nbt.getShouldCopy()) {
                                            onMove = nbt.copyOf();
                                        } else {
                                            onMove = nbt;
                                        }
                                        copperEntity.moveablePipeDataHandler.setMoveablePipeNbt(nbt.getNbtID(), onMove);
                                        onMove.onMove(world, newPos, state, copperEntity);
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

    public void load(CompoundTag nbtCompound) {
        super.load(nbtCompound);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbtCompound)) {
            ContainerHelper.loadAllItems(nbtCompound, this.inventory);
        }
        this.waterCooldown = nbtCompound.getInt("WaterCooldown");
        this.electricityCooldown = nbtCompound.getInt("electricityCooldown");
        this.canWater = nbtCompound.getBoolean("canWater");
        this.canSmoke = nbtCompound.getBoolean("canSmoke");
        this.lastFixVersion = nbtCompound.getInt("lastFixVersion");
        this.moveablePipeDataHandler.readNbt(nbtCompound);
    }

    protected void saveAdditional(CompoundTag nbtCompound) {
        super.saveAdditional(nbtCompound);
        if (!this.trySaveLootTable(nbtCompound)) {
            ContainerHelper.saveAllItems(nbtCompound, this.inventory);
        }
        nbtCompound.putInt("WaterCooldown", this.waterCooldown);
        nbtCompound.putInt("electricityCooldown", this.electricityCooldown);
        nbtCompound.putBoolean("canWater", this.canWater);
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

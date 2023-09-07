package net.lunade.copper.block_entity;

import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.registry.RegisterCopperBlockEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class CopperFittingEntity extends AbstractSimpleCopperBlockEntity {

    public int transferCooldown;

    public CopperFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(RegisterCopperBlockEntities.COPPER_FITTING_ENTITY, blockPos, blockState, MOVE_TYPE.FROM_FITTING);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.unpackLootTable(null);
        if (itemStack != null) {
            this.getItems().set(i, itemStack);
            if (itemStack.getCount() > this.getMaxStackSize()) {
                itemStack.setCount(this.getMaxStackSize());
            }
        }
    }

    @Override
    public void serverTick(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super.serverTick(level, blockPos, blockState);
        if (!level.isClientSide) {
            if (this.transferCooldown > 0) {
                --this.transferCooldown;
            } else {
                this.fittingMove(level, blockPos, blockState);
            }
        }
    }

    public void fittingMove(Level level, BlockPos blockPos, @NotNull BlockState blockState) {
        boolean bl1 = moveOut(level, blockPos, level.random);
        boolean bl2 = moveIn(level, blockPos, level.random);
        if (bl1 || bl2) {
            setChanged(level, blockPos, blockState);
            setCooldown(blockState);
        }
    }

    public static boolean canTransfer(@NotNull Level level, BlockPos pos, Direction direction, boolean to) {
        BlockState blockState;
        return level.getBlockEntity(pos) instanceof CopperPipeEntity pipe && (!to || pipe.transferCooldown <= 0) && (blockState = level.getBlockState(pos)).hasProperty(BlockStateProperties.FACING) && blockState.getValue(BlockStateProperties.FACING) == direction;
    }

    private boolean moveIn(Level level, @NotNull BlockPos blockPos, RandomSource randomSource) {
        boolean result = false;
        for (Direction direction : Util.shuffledCopy(Direction.values(), randomSource)) {
            Direction opposite = direction.getOpposite();
            BlockPos offsetOppPos = blockPos.relative(opposite);
            Container container = CopperPipeEntity.getContainerAt(level, offsetOppPos);
            if (container instanceof CopperPipeEntity && canTransfer(level, offsetOppPos, opposite, false)) {
               result |= !HopperBlockEntity.isEmptyContainer(container, opposite) &&
                       HopperBlockEntity.getSlots(container, opposite).anyMatch(i -> tryTakeInItemFromSlot(container, i, opposite));
            }
        }
        return result;
    }

    private boolean tryTakeInItemFromSlot(@NotNull Container container, int i, Direction direction) {
        ItemStack itemStack = container.getItem(i);
        if (!itemStack.isEmpty() && HopperBlockEntity.canTakeItemFromContainer(this, container, itemStack, i, direction)) {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = CopperPipeEntity.addItem(container, this, container.removeItem(i, 1), null);
            if (itemStack3.isEmpty()) {
                container.setChanged();
                return true;
            }
            container.setItem(i, itemStack2);
        }
        return false;
    }

    private boolean moveOut(Level level, BlockPos blockPos, RandomSource random) {
        boolean result = false;
        for (Direction direction : Util.shuffledCopy(Direction.values(), random)) {
            BlockPos offsetPos = blockPos.relative(direction);
            Container inventory2 = CopperPipeEntity.getContainerAt(level, offsetPos);
            if (inventory2 != null && canTransfer(level, offsetPos, direction, true)) {
                Direction opposite = direction.getOpposite();
                for (int i = 0; i < this.getContainerSize(); ++i) {
                    if (!HopperBlockEntity.isFullContainer(inventory2, direction)) {
                        ItemStack stack = this.getItem(i);
                        if (!stack.isEmpty()) {
                            CopperPipeEntity.setCooldown(level, offsetPos);
                            ItemStack itemStack = stack.copy();
                            ItemStack itemStack2 = CopperPipeEntity.addItem(this, inventory2, this.removeItem(i, 1), opposite);
                            if (itemStack2.isEmpty()) {
                                inventory2.setChanged();
                                result = true;
                            }
                            this.setItem(i, itemStack);
                        }
                    }
                }
            }
        }
        return result;
    }

    public void setCooldown(@NotNull BlockState state) {
        int i = 2;
        if (state.getBlock() instanceof CopperFitting fitting) {
            i = fitting.cooldown;
        }
        this.transferCooldown = i;
    }

    @Override
    public boolean canAcceptMoveableNbt(MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MOVE_TYPE.FROM_FITTING) {
            return false;
        } else if (moveType == MOVE_TYPE.FROM_PIPE) {
            return moveDirection == fromState.getValue(BlockStateProperties.FACING);
        }
        return false;
    }

    @Override
    public void updateBlockEntityValues(Level level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            this.canWater = state.getValue(BlockStateProperties.WATERLOGGED);
        }
    }

    @Override
    public void load(CompoundTag nbtCompound) {
        super.load(nbtCompound);
        this.transferCooldown = nbtCompound.getInt("transferCooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag nbtCompound) {
        super.saveAdditional(nbtCompound);
        nbtCompound.putInt("transferCooldown", this.transferCooldown);
    }

}

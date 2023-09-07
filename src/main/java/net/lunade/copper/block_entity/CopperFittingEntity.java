package net.lunade.copper.block_entity;

import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopperFittingEntity extends AbstractSimpleCopperBlockEntity {

    public CopperFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(CopperBlockEntities.COPPER_FITTING_ENTITY, blockPos, blockState, MOVE_TYPE.FROM_FITTING);
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
            this.fittingMove(level, blockPos, blockState);
        }
    }

    public void fittingMove(Level level, BlockPos blockPos, @NotNull BlockState blockState) {
        boolean bl1 = moveOut(level, blockPos, level.random);
        boolean bl2 = moveIn(level, blockPos, level.random);
        if (bl1 || bl2) {
            setChanged(level, blockPos, blockState);
        }
    }

    public static boolean canTransfer(@NotNull Level level, BlockPos pos, Direction direction) {
        BlockState blockState;
        return level.getBlockEntity(pos) instanceof CopperPipeEntity && (blockState = level.getBlockState(pos)).hasProperty(BlockStateProperties.FACING) && blockState.getValue(BlockStateProperties.FACING) == direction;
    }

    private boolean moveIn(Level level, @NotNull BlockPos blockPos, RandomSource randomSource) {
        boolean result = false;
        for (Direction direction : Util.shuffledCopy(Direction.values(), randomSource)) {
            Direction opposite = direction.getOpposite();
            BlockPos offsetOppPos = blockPos.relative(opposite);
            Container container = HopperBlockEntity.getContainerAt(level, offsetOppPos);
            BlockState blockState = level.getBlockState(offsetOppPos);
            if (container instanceof CopperPipeEntity && blockState.hasProperty(BlockStateProperties.FACING) && blockState.getValue(BlockStateProperties.FACING) == opposite) {
               result = (result) || (!HopperBlockEntity.isEmptyContainer(container, opposite) && HopperBlockEntity.getSlots(container, opposite).anyMatch((i) -> tryTakeInItemFromSlot(container, i, opposite)));
            }
        }
        return result;
    }

    private boolean tryTakeInItemFromSlot(Container container, int i, Direction direction) {
        ItemStack itemStack = container.getItem(i);
        if (!itemStack.isEmpty() && HopperBlockEntity.canTakeItemFromContainer(this, container, itemStack, i, direction)) {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = addItem(this, container.removeItem(i, 1), null);
            if (itemStack3.isEmpty()) {
                container.setChanged();
                return true;
            }
            container.setItem(i, itemStack2);
        }
        return false;
    }

    private boolean moveOut(Level world, BlockPos blockPos, RandomSource random) {
        boolean result = false;
        for (Direction direction : Util.shuffledCopy(Direction.values(), random)) {
            BlockPos offsetPos = blockPos.relative(direction);
            Container inventory2 = HopperBlockEntity.getContainerAt(world, offsetPos);
            if (inventory2 != null && canTransfer(world, offsetPos, direction)) {
                Direction opposite = direction.getOpposite();
                for (int i = 0; i < this.getContainerSize(); ++i) {
                    if (HopperBlockEntity.isFullContainer(inventory2, direction)) {
                        return false;
                    }
                    ItemStack stack = this.getItem(i);
                    if (!stack.isEmpty()) {
                        CopperPipeEntity.setCooldown(world, offsetPos);
                        ItemStack itemStack = stack.copy();
                        ItemStack itemStack2 = addItem(inventory2, this.removeItem(i, 1), opposite);
                        if (itemStack2.isEmpty()) {
                            inventory2.setChanged();
                            result = true;
                        }
                        this.setItem(i, itemStack);
                    }
                }
            }
        }
        return result;
    }

    public static ItemStack addItem(Container container2, ItemStack itemStack, @Nullable Direction direction) {
        int i;
        if (container2 instanceof WorldlyContainer worldlyContainer) {
            if (direction != null) {
                int[] is = worldlyContainer.getSlotsForFace(direction);
                for(i = 0; i < is.length && !itemStack.isEmpty(); ++i) {
                    itemStack = tryMoveInItem(container2, itemStack, is[i], direction);
                }
                return itemStack;
            }
        }
        int j = container2.getContainerSize();
        for(i = 0; i < j && !itemStack.isEmpty(); ++i) {
            itemStack = tryMoveInItem(container2, itemStack, i, direction);
        }
        return itemStack;
    }

    private static ItemStack tryMoveInItem(@NotNull Container container2, ItemStack itemStack, int i, @Nullable Direction direction) {
        ItemStack itemStack2 = container2.getItem(i);
        if (HopperBlockEntity.canPlaceItemInContainer(container2, itemStack, i, direction)) {
            boolean bl = false;
            if (itemStack2.isEmpty()) {
                container2.setItem(i, itemStack);
                itemStack = ItemStack.EMPTY;
                bl = true;
            } else if (HopperBlockEntity.canMergeItems(itemStack2, itemStack)) {
                int j = itemStack.getMaxStackSize() - itemStack2.getCount();
                int k = Math.min(itemStack.getCount(), j);
                itemStack.shrink(k);
                itemStack2.grow(k);
                bl = k > 0;
            }
            if (bl) {
                container2.setChanged();
            }
        }
        return itemStack;
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

}

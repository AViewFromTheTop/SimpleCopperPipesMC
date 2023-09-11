package net.lunade.copper.block_entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.registry.RegisterCopperBlockEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

    public void fittingMove(@NotNull Level level, BlockPos blockPos, @NotNull BlockState blockState) {
        boolean bl1 = blockState.hasProperty(BlockStateProperties.POWERED) && !blockState.getValue(BlockStateProperties.POWERED) && this.moveOut(level, blockPos, level.random);
        boolean bl2 = this.moveIn(level, blockPos, level.random);
        if (bl1 || bl2) {
            setCooldown(blockState);
            setChanged(level, blockPos, blockState);
        }
    }

    public static boolean canTransfer(@NotNull Level level, BlockPos pos, Direction direction, boolean to) {
        BlockState blockState = level.getBlockState(pos);
        return level.getBlockEntity(pos) instanceof CopperPipeEntity pipe && (!to || pipe.transferCooldown <= 0) && blockState.hasProperty(BlockStateProperties.FACING) && blockState.getValue(BlockStateProperties.FACING) == direction;
    }

    private boolean moveIn(Level level, @NotNull BlockPos blockPos, RandomSource randomSource) {
        boolean result = false;
        for (Direction direction : Util.shuffledCopy(Direction.values(), randomSource)) {
            Direction opposite = direction.getOpposite();
            BlockPos offsetOppPos = blockPos.relative(opposite);
            Storage<ItemVariant> inventory = ItemStorage.SIDED.find(level, offsetOppPos, level.getBlockState(offsetOppPos), level.getBlockEntity(offsetOppPos), direction);
            Storage<ItemVariant> fittingInventory = ItemStorage.SIDED.find(level, blockPos, level.getBlockState(blockPos), level.getBlockEntity(blockPos), opposite);
            if (inventory != null && fittingInventory != null && canTransfer(level, offsetOppPos, direction, false)) {
                for (StorageView<ItemVariant> storageView : inventory) {
                    if (!storageView.isResourceBlank() && storageView.getAmount() > 0) {
                        Transaction transaction = Transaction.openOuter();
                        var resource = storageView.getResource();
                        long extracted = inventory.extract(resource, 1, transaction);
                        if (extracted > 0) {
                            CopperPipeEntity.addItem(resource, fittingInventory, transaction);
                            transaction.commit(); // applies the changes
                            result = true;
                        }
                        transaction.close(); // if it cant commit, close it.
                    }
                }
            }
        }
        return result;
    }

    private boolean moveOut(Level level, @NotNull BlockPos blockPos, RandomSource random) {
        boolean result = false;
        for (Direction direction : Util.shuffledCopy(Direction.values(), random)) {
            BlockPos offsetPos = blockPos.relative(direction);
            Direction opposite = direction.getOpposite();
            Storage<ItemVariant> inventory = ItemStorage.SIDED.find(level, offsetPos, level.getBlockState(offsetPos), level.getBlockEntity(offsetPos), opposite);
            Storage<ItemVariant> fittingInventory = ItemStorage.SIDED.find(level, blockPos, level.getBlockState(blockPos), level.getBlockEntity(blockPos), direction);
            if (inventory != null && fittingInventory != null && canTransfer(level, offsetPos, direction, true)) {
                for (StorageView<ItemVariant> storageView : fittingInventory) {
                    if (!storageView.isResourceBlank() && storageView.getAmount() > 0) {
                        Transaction transaction = Transaction.openOuter();
                        var resource = storageView.getResource();
                        long inserted = inventory.insert(resource, 1, transaction);
                        if (inserted > 0) { // successfully inserted item
                            fittingInventory.extract(resource, 1, transaction);
                            transaction.commit(); // applies the changes
                            result = true;
                        }
                        transaction.close(); // if it can't commit, close it.
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

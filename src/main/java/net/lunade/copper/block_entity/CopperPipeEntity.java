package net.lunade.copper.block_entity;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.FittingPipeDispenses;
import net.lunade.copper.PipeMovementRestrictions;
import net.lunade.copper.PoweredPipeDispenses;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.lunade.copper.registry.RegisterCopperBlockEntities;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiFunction;

public class CopperPipeEntity extends AbstractSimpleCopperBlockEntity implements GameEventListener.Holder<VibrationSystem.Listener>, VibrationSystem {

    private static final int VIBRATION_RANGE = 8;
    private static final int MAX_TRANSFER_AMOUNT = 1;

    public int transferCooldown;
    public int dispenseCooldown;
    public int noteBlockCooldown;
    public boolean canDispense;
    public boolean corroded;
    public boolean shootsControlled;
    public boolean shootsSpecial;
    public boolean canAccept;

    private VibrationSystem.Data vibrationData;
    private final VibrationSystem.Listener vibrationListener;
    private final VibrationSystem.User vibrationUser;

    public BlockPos inputGameEventPos;
    public Vec3 gameEventNbtVec3;

    public CopperPipeEntity(BlockPos blockPos, BlockState blockState) {
        super(RegisterCopperBlockEntities.COPPER_PIPE_ENTITY, blockPos, blockState, MOVE_TYPE.FROM_PIPE);
        this.noteBlockCooldown = 0;
        this.vibrationUser = this.createVibrationUser();
        this.vibrationData = new VibrationSystem.Data();
        this.vibrationListener = new VibrationSystem.Listener(this);
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
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState) {
        VibrationSystem.Ticker.tick(this.level, this.getVibrationData(), this.createVibrationUser());
        super.serverTick(level, blockPos, blockState);
        if (!level.isClientSide) {
            if (this.noteBlockCooldown > 0) {
                --this.noteBlockCooldown;
            }
            if (this.dispenseCooldown > 0) {
                --this.dispenseCooldown;
            } else {
                this.dispense((ServerLevel) level, blockPos, blockState);
                int i = 0;
                if (level.getBlockState(blockPos.relative(blockState.getValue(BlockStateProperties.FACING).getOpposite())).getBlock() instanceof CopperFitting fitting) {
                    i = fitting.cooldown;
                } else {
                    if (blockState.getBlock() instanceof CopperPipe pipe) {
                        i = Mth.floor(pipe.cooldown * 0.5);
                    }
                }
                this.dispenseCooldown = i;
            }

            if (this.transferCooldown > 0) {
                --this.transferCooldown;
            } else {
                this.pipeMove(level, blockPos, blockState);
            }
            if (blockState.getValue(CopperPipeProperties.HAS_WATER) && blockState.getValue(BlockStateProperties.FACING) != Direction.UP) {
                LeakingPipeManager.addPos(level, blockPos);
            }
        }
    }

    @Override
    public void updateBlockEntityValues(Level level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            Direction directionOpp = direction.getOpposite();
            Block dirBlock = level.getBlockState(pos.relative(direction)).getBlock();
            BlockState oppState = level.getBlockState(pos.relative(directionOpp));
            Block oppBlock = oppState.getBlock();
            this.canDispense = (dirBlock == Blocks.AIR || dirBlock == Blocks.WATER) && (oppBlock != Blocks.AIR && oppBlock != Blocks.WATER);
            this.corroded = oppBlock == CopperFitting.CORRODED_FITTING || state.getBlock() == CopperPipe.CORRODED_PIPE;
            this.shootsControlled = oppBlock == Blocks.DROPPER;
            this.shootsSpecial = oppBlock == Blocks.DISPENSER;
            this.canAccept = !(oppBlock instanceof CopperPipe) && !(oppBlock instanceof CopperFitting) && !oppState.isRedstoneConductor(level, pos);
        }
    }

    public void pipeMove(Level level, BlockPos blockPos, @NotNull BlockState blockState) {
        Direction facing = blockState.getValue(BlockStateProperties.FACING);
        boolean bl1 = this.moveOut(level, blockPos, facing);
        int bl2 = this.moveIn(level, blockPos, blockState, facing);
        if (bl1 || bl2 >= 2) {
            setCooldown(blockState);
            setChanged(level, blockPos, blockState);
            if (bl2 == 3) {
                level.playSound(null, blockPos, CopperPipeMain.ITEM_IN, SoundSource.BLOCKS, 0.2F, (level.random.nextFloat() * 0.25F) + 0.8F);
            }
        }
    }

    public static boolean canTransfer(Level level, BlockPos pos, boolean to, @NotNull CopperPipeEntity copperPipe, @Nullable Storage<ItemVariant> inventory, @Nullable Storage<ItemVariant> pipeInventory) {
        if (copperPipe.transferCooldown > 0) {
            return false;
        }
        boolean transferApiCheck = true;
        boolean usingTransferApi = false;
        if (inventory != null) {
            usingTransferApi = true;
            transferApiCheck = to
                    ? inventory.supportsInsertion() && (pipeInventory == null || pipeInventory.supportsExtraction())
                    : inventory.supportsExtraction() && (pipeInventory == null || pipeInventory.supportsInsertion());
        }
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity != null) {
            if (entity instanceof CopperPipeEntity pipe) {
                return (to || pipe.transferCooldown <= 0) && transferApiCheck;
            }
            if (entity instanceof CopperFittingEntity) {
                return false;
            }
            if (to) {
                PipeMovementRestrictions.CanTransferTo<BlockEntity> canTransfer = PipeMovementRestrictions.getCanTransferTo(entity);
                if (canTransfer != null) {
                    return canTransfer.canTransfer((ServerLevel)level, pos, level.getBlockState(pos), copperPipe, entity) && transferApiCheck;
                }
            } else {
                PipeMovementRestrictions.CanTakeFrom<BlockEntity> canTake = PipeMovementRestrictions.getCanTakeFrom(entity);
                if (canTake != null) {
                    return canTake.canTake((ServerLevel)level, pos, level.getBlockState(pos), copperPipe, entity) && transferApiCheck;
                }
            }
        }
        return usingTransferApi && transferApiCheck;
    }

    private int moveIn(Level level, @NotNull BlockPos blockPos, BlockState blockState, @NotNull Direction facing) {
        Direction opposite = facing.getOpposite();
        BlockPos offsetOppPos = blockPos.relative(opposite);
        Storage<ItemVariant> inventory = getStorageAt(level, offsetOppPos, facing);
        Storage<ItemVariant> pipeInventory = getStorageAt(level, blockPos, opposite);
        if (inventory != null && pipeInventory != null && canTransfer(level, offsetOppPos, false, this, inventory, pipeInventory)) {
            for (StorageView<ItemVariant> storageView : inventory) {
                if (!storageView.isResourceBlank() && storageView.getAmount() > 0) {
                    Transaction transaction = Transaction.openOuter();
                    var resource = storageView.getResource();
                    long extracted = inventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
                    if (extracted > 0) { // successfully extracted item
                        long inserted = addItem(resource, pipeInventory, transaction);
                        if (inserted > 0) { // successfully inserted item
                            transaction.commit(); // applies the changes
                            if (blockState.is(CopperPipeMain.SILENT_PIPES)) {
                                return 2;
                            }

                            Block block = level.getBlockState(offsetOppPos).getBlock();
                            if (!(block instanceof CopperPipe) && !(block instanceof CopperFitting)) {
                                return 3;
                            }
                            return 2;
                        }
                    }
                    transaction.close(); // if it cant commit, close it.
                }
            }
        }
        return 0;
    }

    public static long addItem(ItemVariant resource, @NotNull Storage<ItemVariant> inventory, Transaction transaction) {
        if (inventory.supportsInsertion()) {
            return inventory.insert(resource, MAX_TRANSFER_AMOUNT, transaction);
        }
        return 0L;
    }

    private boolean moveOut(Level level, @NotNull BlockPos blockPos, Direction facing) {
        BlockPos offsetPos = blockPos.relative(facing);
        Storage<ItemVariant> inventory = getStorageAt(level, offsetPos, facing.getOpposite());
        Storage<ItemVariant> pipeInventory = getStorageAt(level, blockPos, facing);
        if (inventory != null && pipeInventory != null && canTransfer(level, offsetPos, true, this, inventory, pipeInventory)) {
            boolean canMove = true;
            BlockState state = level.getBlockState(offsetPos);
            if (state.getBlock() instanceof CopperPipe) {
                canMove = state.getValue(BlockStateProperties.FACING) != facing;
            }
            if (canMove) {
                for (StorageView<ItemVariant> storageView : pipeInventory) {
                    if (!storageView.isResourceBlank() && storageView.getAmount() > 0) {
                        Transaction transaction = Transaction.openOuter();
                        setCooldown(level, offsetPos);
                        var resource = storageView.getResource();
                        long inserted = inventory.insert(resource, MAX_TRANSFER_AMOUNT, transaction);
                        if (inserted > 0) { // successfully inserted item
                            long extracted = pipeInventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
                            if (extracted > 0) { // successfully extracted item
                                transaction.commit(); // applies the changes
                                return true;
                            }
                        }
                        transaction.close(); // if it can't commit, close it.
                    }
                }
            }
        }
        return false;
    }

    private boolean dispense(ServerLevel serverLevel, BlockPos blockPos, @NotNull BlockState blockState) {
        Direction direction = blockState.getValue(BlockStateProperties.FACING);
        Direction directionOpp = direction.getOpposite();
        boolean powered = blockState.getValue(CopperPipe.POWERED);
        if (this.canDispense) {
            BlockSourceImpl blockPointerImpl = new BlockSourceImpl(serverLevel, blockPos);
            int i = this.chooseNonEmptySlot(serverLevel.random);
            if (i >= 0) {
                ItemStack itemStack = this.getItem(i);
                if (!itemStack.isEmpty()) {
                    ItemStack itemStack2;
                    int shotLength = 4;
                    if (this.shootsControlled) { //If Dropper
                        shotLength = 10;
                        serverLevel.playSound(null, blockPos, CopperPipeMain.LAUNCH, SoundSource.BLOCKS, 0.2F, (serverLevel.random.nextFloat()*0.25F) + 0.8F);
                    } else if (this.shootsSpecial) { //If Dispenser, Use Pipe-Specific Launch Length
                        if (blockState.getBlock() instanceof CopperPipe pipe) {
                            shotLength = pipe.dispenserShotLength;
                            serverLevel.playSound(null, blockPos, CopperPipeMain.LAUNCH, SoundSource.BLOCKS, 0.2F, (serverLevel.random.nextFloat()*0.25F) + 0.8F);
                        } else {
                            shotLength= 12;
                        }
                    }
                    boolean silent = blockState.is(CopperPipeMain.SILENT_PIPES);
                    if (serverLevel.getBlockState(blockPos.relative(directionOpp)).getBlock() instanceof CopperFitting) {
                        itemStack2 = canonShoot(blockPointerImpl, itemStack, blockState, shotLength, powered, true, silent, this.corroded);
                    } else {
                        itemStack2 = canonShoot(blockPointerImpl, itemStack, blockState, shotLength, powered, false, silent, this.corroded);
                        blockPointerImpl.getLevel().levelEvent(LevelEvent.PARTICLES_SHOOT, blockPointerImpl.getPos(), direction.get3DDataValue());
                    }
                    this.setItem(i, itemStack2);
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack canonShoot(@NotNull BlockSource blockPointer, ItemStack itemStack, @NotNull BlockState state, int shotLength, boolean powered, boolean fitting, boolean silent, boolean corroded) {
        ServerLevel serverLevel = blockPointer.getLevel();
        BlockPos pos = blockPointer.getPos();
        Direction direction = state.getValue(BlockStateProperties.FACING);
        Position position = CopperPipe.getOutputLocation(blockPointer, direction);
        ItemStack itemStack2 = itemStack;
        if (powered) { //Special Behavior When Powered
            PoweredPipeDispenses.PoweredDispense poweredDispense = PoweredPipeDispenses.getDispense(itemStack2.getItem());
            if (poweredDispense != null) {
                itemStack2=itemStack.split(1);
                poweredDispense.dispense(serverLevel, itemStack2, shotLength, direction, position, state, corroded, pos, this);
                if (!fitting && !silent) {
                    serverLevel.playSound(null, pos, CopperPipeMain.ITEM_OUT, SoundSource.BLOCKS, 0.2F, (serverLevel.random.nextFloat()*0.25F) + 0.8F);
                    serverLevel.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
                }
                return itemStack;
            }
        }
        if (fitting) {
            FittingPipeDispenses.FittingDispense fittingDispense = FittingPipeDispenses.getDispense(itemStack2.getItem());
            if (fittingDispense != null) { //Particle Emitters With Fitting
                if (SimpleCopperPipesConfig.get().specialEffectDispensing) {
                    fittingDispense.dispense(serverLevel, itemStack2, shotLength, direction, position, state, corroded, pos, this);
                }
            } else { //Spawn Item W/O Sound With Fitting
                if (SimpleCopperPipesConfig.get().dispensing) {
                    itemStack2 = itemStack.split(1);
                    spawnItem(serverLevel, itemStack2, shotLength, direction, position, direction, corroded);
                    serverLevel.levelEvent(LevelEvent.PARTICLES_SHOOT, pos, direction.get3DDataValue());
                }
            }
        } else {
            if (SimpleCopperPipesConfig.get().dispensing) {
                itemStack2 = itemStack.split(1);
                serverLevel.levelEvent(LevelEvent.PARTICLES_SHOOT, blockPointer.getPos(), direction.get3DDataValue());
                spawnItem(serverLevel, itemStack2, shotLength, direction, position, direction, corroded);
                if (!silent) {
                    serverLevel.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
                    serverLevel.playSound(null, blockPointer.getPos(), CopperPipeMain.ITEM_OUT, SoundSource.BLOCKS, 0.2F, (serverLevel.random.nextFloat() * 0.25F) + 0.8F);
                }
            }
        }
        return itemStack;
    }

    public static void spawnItem(Level level, ItemStack itemStack, int shotLength, @NotNull Direction direction, @NotNull Position position, Direction facing, boolean corroded) { //Simply Spawn An Item
        double d = position.x();
        double e = position.y();
        double f = position.z();
        if (direction.getAxis() == Direction.Axis.Y) {
            e -= 0.125D;
        } else {
            e -= 0.15625D;
        }
        double x = 0;
        double y = 0;
        double z = 0;
        Direction.Axis axis = facing.getAxis();
        BiFunction<Boolean, Double, Double> fun = (corr, pos) -> Boolean.TRUE.equals(corr) ? (level.random.nextDouble() * 0.6) - 0.3 : pos;
        x = axis == Direction.Axis.X ? (shotLength * facing.getStepX()) * 0.1 : fun.apply(corroded, x);
        y = axis == Direction.Axis.Y ? (shotLength * facing.getStepY()) * 0.1 : fun.apply(corroded, y);
        z = axis == Direction.Axis.Z ? (shotLength * facing.getStepZ()) * 0.1 : fun.apply(corroded, z);
        ItemEntity itemEntity = new ItemEntity(level, d, e, f, itemStack);
        itemEntity.setDeltaMovement(x, y, z);
        level.addFreshEntity(itemEntity);
    }

    public int chooseNonEmptySlot(RandomSource random) {
        this.unpackLootTable(null);
        int i = -1;
        int j = 1;
        for (int k = 0; k < this.inventory.size(); ++k) {
            if (!this.inventory.get(k).isEmpty() && random.nextInt(j++) == 0) {
                i = k;
            }
        } return i;
    }

    public void setCooldown(@NotNull BlockState state) {
        int i = 2;
        if (state.getBlock() instanceof CopperPipe pipe) {
            i = pipe.cooldown;
        }
        this.transferCooldown = i;
    }

    public static void setCooldown(@NotNull Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        BlockState state = level.getBlockState(blockPos);
        if (entity instanceof CopperPipeEntity pipe) {
            pipe.setCooldown(state);
        }
    }

    public static Storage<ItemVariant> getStorageAt(Level level, BlockPos blockPos, Direction direction) {
        return ItemStorage.SIDED.find(level, blockPos, level.getBlockState(blockPos), level.getBlockEntity(blockPos), direction);
    }

    @Override
    public void load(CompoundTag nbtCompound) {
        super.load(nbtCompound);
        this.transferCooldown = nbtCompound.getInt("transferCooldown");
        this.dispenseCooldown = nbtCompound.getInt("dispenseCooldown");
        this.noteBlockCooldown = nbtCompound.getInt("noteBlockCooldown");
        this.canDispense = nbtCompound.getBoolean("canDispense");
        this.corroded = nbtCompound.getBoolean("corroded");
        this.shootsControlled = nbtCompound.getBoolean("shootsControlled");
        this.shootsSpecial = nbtCompound.getBoolean("shootsSpecial");
        this.canAccept = nbtCompound.getBoolean("canAccept");
        if (nbtCompound.contains("listener", 10)) {
            DataResult<Data> var10000 = Data.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.getCompound("listener")));
            Objects.requireNonNull(CopperPipeMain.LOGGER);
            var10000.resultOrPartial(CopperPipeMain.LOGGER::error).ifPresent((data) -> this.vibrationData = data);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbtCompound) {
        super.saveAdditional(nbtCompound);
        nbtCompound.putInt("transferCooldown", this.transferCooldown);
        nbtCompound.putInt("dispenseCooldown", this.dispenseCooldown);
        nbtCompound.putInt("noteBlockCooldown", this.noteBlockCooldown);
        nbtCompound.putBoolean("canDispense", this.canDispense);
        nbtCompound.putBoolean("corroded", this.corroded);
        nbtCompound.putBoolean("shootsControlled", this.shootsControlled);
        nbtCompound.putBoolean("shootsSpecial", this.shootsSpecial);
        nbtCompound.putBoolean("canAccept", this.canAccept);
        DataResult<Tag> dataResult = Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData);
        Objects.requireNonNull(CopperPipeMain.LOGGER);
        dataResult.resultOrPartial(CopperPipeMain.LOGGER::error).ifPresent((tag) -> nbtCompound.put("listener", tag));
    }

    public VibrationSystem.User createVibrationUser() {
        return new VibrationUser(this.getBlockPos());
    }

    @Override
    @NotNull
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    @NotNull
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    @NotNull
    public VibrationSystem.Listener getListener() {
        return this.vibrationListener;
    }

    public class VibrationUser implements VibrationSystem.User {
        protected final BlockPos blockPos;
        private final PositionSource positionSource;

        public VibrationUser(BlockPos blockPos) {
            this.blockPos = blockPos;
            this.positionSource = new BlockPositionSource(blockPos);
        }

        @Override
        public int getListenerRadius() {
            return VIBRATION_RANGE;
        }

        @Override
        @NotNull
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canTriggerAvoidVibration() {
            return false;
        }

        @Override
        public boolean canReceiveVibration(@NotNull ServerLevel serverLevel, BlockPos blockPos, GameEvent gameEvent, @Nullable GameEvent.Context context) {
            boolean placeDestroy = gameEvent == GameEvent.BLOCK_DESTROY || gameEvent == GameEvent.BLOCK_PLACE;
            if ((serverLevel.getBlockState(blockPos).getBlock() instanceof CopperPipe) || (blockPos == CopperPipeEntity.this.getBlockPos() && placeDestroy)) {
                return false;
            }
            if (CopperPipeEntity.this.canAccept) {
                CopperPipeEntity.this.moveablePipeDataHandler.addSaveableMoveablePipeNbt(new MoveablePipeDataHandler.SaveableMovablePipeNbt(gameEvent, Vec3.atCenterOf(blockPos), context, CopperPipeEntity.this.getBlockPos()).withShouldMove(true).withShouldSave(true));
                return true;
            }
            return false;
        }

        @Override
        public void onReceiveVibration(ServerLevel serverLevel, BlockPos blockPos, GameEvent gameEvent, @Nullable Entity entity, @Nullable Entity entity2, float f) {

        }

        @Override
        public void onDataChanged() {
            CopperPipeEntity.this.setChanged();
        }

        @Override
        public boolean requiresAdjacentChunksToBeTicking() {
            return true;
        }
    }

    @Override
    public boolean canAcceptMoveableNbt(MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MOVE_TYPE.FROM_FITTING) {
            return this.getBlockState().getValue(BlockStateProperties.FACING) == moveDirection;
        }
        return this.getBlockState().getValue(BlockStateProperties.FACING) == moveDirection || moveDirection == fromState.getValue(BlockStateProperties.FACING);
    }

    @Override
    public boolean canMoveNbtInDirection(Direction direction, @NotNull BlockState state) {
        return direction != state.getValue(BlockStateProperties.FACING).getOpposite();
    }

    @Override
    public void dispenseMoveableNbt(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {
        if (this.canDispense) {
            ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> nbtList = this.moveablePipeDataHandler.getSavedNbtList();
            if (!nbtList.isEmpty()) {
                for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : nbtList) {
                    if (nbt.getShouldMove()) {
                        nbt.dispense(serverLevel, blockPos, blockState, this);
                    }
                }
                this.moveMoveableNbt(serverLevel, blockPos, blockState);
            }
        }
    }

}

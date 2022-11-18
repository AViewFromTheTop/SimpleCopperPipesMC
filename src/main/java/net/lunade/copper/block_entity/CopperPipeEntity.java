package net.lunade.copper.block_entity;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.FittingPipeDispenses;
import net.lunade.copper.PipeMovementRestrictions;
import net.lunade.copper.PoweredPipeDispenses;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class CopperPipeEntity extends AbstractSimpleCopperBlockEntity implements VibrationListener.VibrationListenerConfig {

    public int transferCooldown;
    public int dispenseCooldown;
    public int noteBlockCooldown;
    public boolean canDispense;
    public boolean corroded;
    public boolean shootsControlled;
    public boolean shootsSpecial;
    public boolean canAccept;

    private CopperPipeListener listener;

    public BlockPos inputGameEventPos;
    public Vec3 gameEventNbtVec3;

    public CopperPipeEntity(BlockPos blockPos, BlockState blockState) {
        super(CopperPipeMain.COPPER_PIPE_ENTITY, blockPos, blockState, MOVE_TYPE.FROM_PIPE);
        this.noteBlockCooldown = 0;
        this.listener = new CopperPipeListener(new BlockPositionSource(this.worldPosition), 8, this, null, 0, 0);
    }

    public void setItem(int i, ItemStack itemStack) {
        this.unpackLootTable(null);
        this.getItems().set(i, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }
    }

    public void serverTick(Level world, BlockPos blockPos, BlockState blockState) {
        this.listener.tick(world);
        super.serverTick(world, blockPos, blockState);
        if (!world.isClientSide) {
            if (this.noteBlockCooldown > 0) {
                --this.noteBlockCooldown;
            }
            if (this.dispenseCooldown > 0) {
                --this.dispenseCooldown;
            } else {
                this.dispense((ServerLevel) world, blockPos, blockState);
                int i = 0;
                if (world.getBlockState(blockPos.relative(blockState.getValue(BlockStateProperties.FACING).getOpposite())).getBlock() instanceof CopperFitting fitting) {
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
                this.pipeMove(world, blockPos, blockState);
            }
            if (blockState.getValue(CopperPipeProperties.HAS_WATER) && blockState.getValue(BlockStateProperties.FACING) != Direction.UP) {
                LeakingPipeManager.addPos(world, blockPos);
            }
        }
    }

    public void updateBlockEntityValues(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            Direction directionOpp = direction.getOpposite();
            Block dirBlock = world.getBlockState(pos.relative(direction)).getBlock();
            BlockState oppState = world.getBlockState(pos.relative(directionOpp));
            Block oppBlock = oppState.getBlock();
            this.canDispense = (dirBlock == Blocks.AIR || dirBlock == Blocks.WATER) && (oppBlock != Blocks.AIR && oppBlock != Blocks.WATER);
            this.corroded = oppBlock == CopperFitting.CORRODED_FITTING || state.getBlock() == CopperPipe.CORRODED_PIPE;
            this.shootsControlled = oppBlock == Blocks.DROPPER;
            this.shootsSpecial = oppBlock == Blocks.DISPENSER;
            this.canAccept = !(oppBlock instanceof CopperPipe) && !(oppBlock instanceof CopperFitting) && !oppState.isRedstoneConductor(world, pos);
        }
    }

    public void pipeMove(Level world, BlockPos blockPos, BlockState blockState) {
        Direction facing = blockState.getValue(BlockStateProperties.FACING);
        boolean bl1 = moveOut(world, blockPos, facing);
        int bl2 = moveIn(world, blockPos, blockState, facing);
        if (bl1 || bl2 >= 2) {
            setChanged(world, blockPos, blockState);
        }
        if (bl2 == 1 || bl2 == 3) {
            world.playSound(null, blockPos, CopperPipeMain.ITEM_IN, SoundSource.BLOCKS, 0.2F, (world.random.nextFloat() * 0.25F) + 0.8F);
        }
    }

    public static boolean canTransfer(Level world, BlockPos pos, boolean out, CopperPipeEntity copperPipe) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity != null) {
            if (entity instanceof CopperPipeEntity pipe) {
                return pipe.transferCooldown <= 0;
            }
            if (entity instanceof CopperFittingEntity) {
                return out || !world.getBlockState(pos).getValue(BlockStateProperties.POWERED);
            }
            if (out) {
                PipeMovementRestrictions.CanTransferTo canTransfer = PipeMovementRestrictions.getCanTransferTo(entity);
                if (canTransfer != null) {
                    return canTransfer.canTransfer((ServerLevel)world, pos, world.getBlockState(pos), copperPipe, entity);
                }
            } else {
                PipeMovementRestrictions.CanTakeFrom canTake = PipeMovementRestrictions.getCanTakeFrom(entity);
                if (canTake != null) {
                    return canTake.canTake((ServerLevel)world, pos, world.getBlockState(pos), copperPipe, entity);
                }
            }
        }
        return true;
    }

    private int moveIn(Level world, BlockPos blockPos, BlockState blockState, Direction facing) {
        BlockPos offsetOppPos = blockPos.relative(facing.getOpposite());
        Container inventory2 = getInventoryAt(world, offsetOppPos);
        if (inventory2 != null) {
            if (!isInventoryFull(this, facing) && canTransfer(world, offsetOppPos, false, this)) {
                for (int i = 0; i < inventory2.getContainerSize(); ++i) {
                    if (!inventory2.getItem(i).isEmpty()) {
                        this.setCooldown(blockState);
                        ItemStack itemStack = inventory2.getItem(i).copy();
                        ItemStack itemStack2 = transfer(this, inventory2.removeItem(i, 1), facing);
                        if (itemStack2.isEmpty()) {
                            this.setChanged();
                            if (blockState.is(CopperPipeMain.SILENT_PIPES)) {
                                return 2;
                            }
                            Block block = world.getBlockState(offsetOppPos).getBlock();
                            if (!(block instanceof CopperPipe) && !(block instanceof CopperFitting)) {
                                return 3;
                            }
                            return 2;
                        }
                        inventory2.setItem(i, itemStack);
                    }
                }
            }
        }
        return 0;
    }

    private boolean moveOut(Level world, BlockPos blockPos, Direction facing) {
        BlockPos offsetPos = blockPos.relative(facing);
        Container inventory2 = getInventoryAt(world, offsetPos);
        if (inventory2 != null && canTransfer(world, offsetPos, true, this)) {
            Direction opp = facing.getOpposite();
            boolean canMove = true;
            BlockState state = world.getBlockState(offsetPos);
            if (state.getBlock() instanceof CopperPipe) {
                canMove = state.getValue(BlockStateProperties.FACING) != facing;
            }
            if (canMove && !isInventoryFull(inventory2, opp)) {
                for (int i = 0; i < this.getContainerSize(); ++i) {
                    if (!this.getItem(i).isEmpty()) {
                        setCooldown(world, offsetPos);
                        ItemStack itemStack = this.getItem(i).copy();
                        ItemStack itemStack2 = transfer(inventory2, this.removeItem(i, 1), opp);
                        if (itemStack2.isEmpty()) {
                            inventory2.setChanged();
                            return true;
                        }
                        this.setItem(i, itemStack);
                    }
                }

            }
        }
        return false;
    }

    private boolean dispense(ServerLevel serverWorld, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(BlockStateProperties.FACING);
        Direction directionOpp = direction.getOpposite();
        boolean powered = blockState.getValue(CopperPipe.POWERED);
        if (this.canDispense) {
            BlockSourceImpl blockPointerImpl = new BlockSourceImpl(serverWorld, blockPos);
            int i = this.chooseNonEmptySlot(serverWorld.random);
            if (!(i < 0)) {
                ItemStack itemStack = this.getItem(i);
                if (!itemStack.isEmpty()) {
                    ItemStack itemStack2;
                    int o=4;
                    if (this.shootsControlled) { //If Dropper
                        o=10;
                        serverWorld.playSound(null, blockPos, CopperPipeMain.LAUNCH, SoundSource.BLOCKS, 0.2F, (serverWorld.random.nextFloat()*0.25F) + 0.8F);
                    } else if (this.shootsSpecial) { //If Dispenser, Use Pipe-Specific Launch Length
                        if (blockState.getBlock() instanceof CopperPipe pipe) {
                            o = pipe.dispenserShotLength;
                            serverWorld.playSound(null, blockPos, CopperPipeMain.LAUNCH, SoundSource.BLOCKS, 0.2F, (serverWorld.random.nextFloat()*0.25F) + 0.8F);
                        } else {o=12;}
                    }
                    boolean silent = blockState.is(CopperPipeMain.SILENT_PIPES);
                    if (serverWorld.getBlockState(blockPos.relative(directionOpp)).getBlock() instanceof CopperFitting) {
                        itemStack2 = canonShoot(blockPointerImpl, itemStack, blockState, o, powered, true, silent, this.corroded);
                    } else {
                        itemStack2 = canonShoot(blockPointerImpl, itemStack, blockState, o, powered, false, silent, this.corroded);
                        blockPointerImpl.getLevel().levelEvent(2000, blockPointerImpl.getPos(), direction.get3DDataValue());
                    }
                    this.setItem(i, itemStack2);
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack canonShoot(BlockSource blockPointer, ItemStack itemStack, BlockState state, int i, boolean powered, boolean fitting, boolean silent, boolean corroded) {
        ServerLevel world = blockPointer.getLevel();
        BlockPos pos = blockPointer.getPos();
        Direction direction = state.getValue(BlockStateProperties.FACING);
        Position position = CopperPipe.getOutputLocation(blockPointer, direction);
        ItemStack itemStack2 = itemStack;
        if (powered) { //Special Behavior When Powered
            PoweredPipeDispenses.PoweredDispense<?> poweredDispense = PoweredPipeDispenses.getDispense(itemStack2.getItem());
            if (poweredDispense != null) {
                itemStack2=itemStack.split(1);
                poweredDispense.dispense(world, itemStack2, i, direction, position, state, corroded, pos, this);
                if (!fitting && !silent) {
                    world.playSound(null, pos, CopperPipeMain.ITEM_OUT, SoundSource.BLOCKS, 0.2F, (world.random.nextFloat()*0.25F) + 0.8F);
                    world.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
                }
                return itemStack;
            }
        }
        if (fitting) {
            FittingPipeDispenses.FittingDispense<?> fittingDispense = FittingPipeDispenses.getDispense(itemStack2.getItem());
            if (fittingDispense != null) { //Particle Emitters With Fitting
                fittingDispense.dispense(world, itemStack2, i, direction, position, state, corroded, pos, this);
            } else { //Spawn Item W/O Sound With Fitting
                itemStack2=itemStack.split(1);
                spawnItem(world, itemStack2, i, direction, position, direction, corroded);
                world.levelEvent(2000, pos, direction.get3DDataValue());
            }
        } else {
            itemStack2=itemStack.split(1);
            world.levelEvent(2000, blockPointer.getPos(), direction.get3DDataValue());
            spawnItem(world, itemStack2, i, direction, position, direction, corroded);
            if (!silent) {
                world.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
                world.playSound(null, blockPointer.getPos(), CopperPipeMain.ITEM_OUT, SoundSource.BLOCKS, 0.2F, (world.random.nextFloat() * 0.25F) + 0.8F);
            }
        }
        return itemStack;
    }

    public static void spawnItem(Level world, ItemStack itemStack, int i, Direction direction, Position position, Direction facing, boolean corroded) { //Simply Spawn An Item
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
        x = axis == Direction.Axis.X ? (i * facing.getStepX()) * 0.1 : corroded ? (world.random.nextDouble() * 0.6) - 0.3 : x;
        y = axis == Direction.Axis.Y ? (i * facing.getStepY()) * 0.1 : corroded ? (world.random.nextDouble() * 0.6) - 0.3 : y;
        z = axis == Direction.Axis.Z ? (i * facing.getStepZ()) * 0.1 : corroded ? (world.random.nextDouble() * 0.6) - 0.3 : z;
        ItemEntity itemEntity = new ItemEntity(world, d, e, f, itemStack);
        itemEntity.setDeltaMovement(x, y, z);
        world.addFreshEntity(itemEntity);
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

    private static IntStream getAvailableSlots(Container inventory, Direction direction) {
        return inventory instanceof WorldlyContainer ? IntStream.of(((WorldlyContainer)inventory).getSlotsForFace(direction)) : IntStream.range(0, inventory.getContainerSize());
    }

    private static boolean isInventoryFull(Container inventory, Direction direction) {
        return getAvailableSlots(inventory, direction).allMatch((i) -> {
            ItemStack itemStack = inventory.getItem(i);
            return itemStack.getCount() >= itemStack.getMaxStackSize();
        });
    }

    public static ItemStack transfer(Container inventory2, ItemStack itemStack, @Nullable Direction direction) {
        if (inventory2 instanceof WorldlyContainer sidedInventory && direction != null) {
            int[] is = sidedInventory.getSlotsForFace(direction);
            for(int i = 0; i < is.length && !itemStack.isEmpty(); ++i) {
                itemStack = transfer(inventory2, itemStack, is[i], direction);
            }
        } else {
            int sidedInventory = inventory2.getContainerSize();
            for(int is = 0; is < sidedInventory && !itemStack.isEmpty(); ++is) {
                itemStack = transfer(inventory2, itemStack, is, direction);
            }
        } return itemStack;
    }

    private static boolean canInsert(Container inventory, ItemStack itemStack, int i, @Nullable Direction direction) {
        return inventory.canPlaceItem(i, itemStack);
    }

    private static ItemStack transfer(Container inventory2, ItemStack itemStack, int i, @Nullable Direction direction) {
        ItemStack itemStack2 = inventory2.getItem(i);
        if (canInsert(inventory2, itemStack, i, direction)) {
            boolean bl = false;
            if (itemStack2.isEmpty()) {
                inventory2.setItem(i, itemStack);
                itemStack = ItemStack.EMPTY;
                bl = true;
            } else if (canMergeItems(itemStack2, itemStack)) {
                int j = itemStack.getMaxStackSize() - itemStack2.getCount();
                int k = Math.min(itemStack.getCount(), j);
                itemStack.shrink(k);
                itemStack2.grow(k);
                bl = k > 0;
            }
            if (bl) {
                inventory2.setChanged();
            }
        }
        return itemStack;
    }

    private boolean isFull() {
        Iterator<ItemStack> var1 = this.inventory.iterator();
        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }
            itemStack = var1.next();
        } while(!itemStack.isEmpty() && itemStack.getCount() == itemStack.getMaxStackSize());
        return false;
    }

    @Nullable
    public static Container getInventoryAt(Level world, BlockPos blockPos) {
        return getInventoryAt(world, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D);
    }

    @Nullable
    private static Container getInventoryAt(Level world, double d, double e, double f) {
        Container inventory = null;
        BlockPos blockPos = new BlockPos(d, e, f);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof WorldlyContainerHolder) {
            inventory = ((WorldlyContainerHolder)block).getContainer(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof Container) {
                inventory = (Container)blockEntity;
                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getContainer((ChestBlock)block, blockState, world, blockPos, true);
                }
            }
        }
        if (inventory == null) {
            Entity nullEntity = null;
            List<Entity> blockEntity = world.getEntities(nullEntity, new AABB(d - 0.5D, e - 0.5D, f - 0.5D, d + 0.5D, e + 0.5D, f + 0.5D), EntitySelector.CONTAINER_ENTITY_SELECTOR);
            if (!blockEntity.isEmpty()) {
                inventory = (Container)blockEntity.get(world.random.nextInt(blockEntity.size()));
            }
        }

        return inventory;
    }

    private static boolean canMergeItems(ItemStack itemStack, ItemStack itemStack2) {
        if (!itemStack.is(itemStack2.getItem())) {
            return false;
        } else if (itemStack.getDamageValue() != itemStack2.getDamageValue()) {
            return false;
        } else if (itemStack.getCount() > itemStack.getMaxStackSize()) {
            return false;
        } else {
            return ItemStack.tagMatches(itemStack, itemStack2);
        }
    }

    public void setCooldown(BlockState state) {
        int i = 2;
        if (state.getBlock() instanceof CopperPipe pipe) {
            i = pipe.cooldown;
        }
        this.transferCooldown = i;
    }

    public static void setCooldown(Level world, BlockPos blockPos) {
        BlockEntity entity = world.getBlockEntity(blockPos);
        BlockState state = world.getBlockState(blockPos);
        if (state.getBlock() instanceof CopperPipe && entity instanceof CopperPipeEntity pipe) {
            pipe.setCooldown(state);
        }
    }

    public GameEventListener getGameEventListener() {
        return this.listener;
    }

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
            DataResult<?> var10000 = CopperPipeListener.createPipeCodec(this).parse(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.getCompound("listener")));
            Logger var10001 = CopperPipeMain.LOGGER;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((vibrationListener) -> this.listener = (CopperPipeListener) vibrationListener);
        }
    }

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
        DataResult<?> var10000 = CopperPipeListener.createPipeCodec(this).encodeStart(NbtOps.INSTANCE, this.listener);
        Logger var10001 = CopperPipeMain.LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> nbtCompound.put("listener", (Tag)nbtElement));
    }

    @Override
    public boolean shouldListen(ServerLevel serverWorld, GameEventListener gameEventListener, BlockPos blockPos, GameEvent gameEvent, GameEvent.Context emitter) {
        boolean placeDestroy = gameEvent == GameEvent.BLOCK_DESTROY || gameEvent == GameEvent.BLOCK_PLACE;
        if ((serverWorld.getBlockState(blockPos).getBlock() instanceof CopperPipe) || (blockPos == this.getBlockPos() && placeDestroy)) {
            return false;
        }
        if (this.canAccept) {
            this.moveablePipeDataHandler.addSaveableMoveablePipeNbt(new MoveablePipeDataHandler.SaveableMovablePipeNbt(gameEvent, Vec3.atCenterOf(blockPos), emitter, this.getBlockPos()).withShouldMove(true).withShouldSave(true));
            return true;
        }
        return false;
    }

    @Override
    public void onSignalReceive(ServerLevel serverWorld, GameEventListener gameEventListener, BlockPos blockPos, GameEvent gameEvent, @Nullable Entity entity, @Nullable Entity entity2, float f) {

    }

    @Override
    public boolean isValidVibration(GameEvent gameEvent, GameEvent.Context emitter) {
        Entity entity = emitter.sourceEntity();
        if (entity != null) {
            if (entity.isSpectator()) {
                return false;
            }

            if (entity.isSteppingCarefully() && gameEvent.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
                return false;
            }

            if (entity.dampensVibrations()) {
                return false;
            }
        }

        if (emitter.affectedState() != null) {
            return !emitter.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
        } else {
            return true;
        }
    }

    public void onSignalSchedule() {
        this.setChanged();
    }

    public boolean canAcceptMoveableNbt(MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MOVE_TYPE.FROM_FITTING) {
            return this.getBlockState().getValue(BlockStateProperties.FACING) == moveDirection;
        }
        return this.getBlockState().getValue(BlockStateProperties.FACING) == moveDirection || moveDirection == fromState.getValue(BlockStateProperties.FACING);
    }

    public boolean canMoveNbtInDirection(Direction direction, BlockState state) {
        return direction != state.getValue(BlockStateProperties.FACING).getOpposite();
    }

    public void dispenseMoveableNbt(ServerLevel serverWorld, BlockPos blockPos, BlockState blockState) {
        if (this.canDispense) {
            ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> nbtList = this.moveablePipeDataHandler.getSavedNbtList();
            if (!nbtList.isEmpty()) {
                for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : nbtList) {
                    if (nbt.getShouldMove()) {
                        nbt.dispense(serverWorld, blockPos, blockState, this);
                    }
                }
                this.moveMoveableNbt(serverWorld, blockPos, blockState);
            }
        }
    }

}

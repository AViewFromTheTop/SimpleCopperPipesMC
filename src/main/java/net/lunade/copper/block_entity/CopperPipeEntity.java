package net.lunade.copper.block_entity;

import net.lunade.copper.FittingPipeDispenses;
import net.lunade.copper.Main;
import net.lunade.copper.PipeMovementRestrictions;
import net.lunade.copper.PoweredPipeDispenses;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.pipe_nbt.ExtraPipeData;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.GameEventTags;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CopperPipeEntity extends AbstractSimpleCopperBlockEntity implements CopperPipeListener.Callback {

    public int transferCooldown;
    public int dispenseCooldown;
    public int noteBlockCooldown;
    public boolean canDispense;
    public boolean corroded;
    public boolean shootsControlled;
    public boolean shootsSpecial;
    public boolean canAccept;

    private final CopperPipeListener listener;
    public ExtraPipeData extraPipeData;

    public CopperPipeEntity(BlockPos blockPos, BlockState blockState) {
        super(Main.COPPER_PIPE_ENTITY, blockPos, blockState, MoveablePipeDataHandler.MOVE_TYPE.FROM_PIPE);
        this.noteBlockCooldown = 0;
        this.listener = new CopperPipeListener(new BlockPositionSource(this.pos), 8, this);
        this.extraPipeData = null;
    }

    public void setStack(int i, ItemStack itemStack) {
        this.checkLootInteraction(null);
        this.getInvStackList().set(i, itemStack);
        if (itemStack.getCount() > this.getMaxCountPerStack()) {
            itemStack.setCount(this.getMaxCountPerStack());
        }
    }

    public void serverTick(World world, BlockPos blockPos, BlockState blockState) {
        this.listener.tick(world);
        super.serverTick(world, blockPos, blockState);
        if (!world.isClient) {
            if (this.noteBlockCooldown > 0) {
                --this.noteBlockCooldown;
            }
            if (this.dispenseCooldown > 0) {
                --this.dispenseCooldown;
            } else {
                this.dispense((ServerWorld) world, blockPos, blockState);
                int i = 0;
                if (world.getBlockState(blockPos.offset(blockState.get(Properties.FACING).getOpposite())).getBlock() instanceof CopperFitting fitting) {
                    i = fitting.cooldown;
                } else {
                    if (blockState.getBlock() instanceof CopperPipe pipe) {
                        i = MathHelper.floor(pipe.cooldown * 0.5);
                    }
                }
                this.dispenseCooldown = i;
            }

            if (this.transferCooldown > 0) {
                --this.transferCooldown;
            } else {
                this.pipeMove(world, blockPos, blockState);
            }
        }
    }

    public void updateBlockEntityValues(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.get(Properties.FACING);
            Direction directionOpp = direction.getOpposite();
            Block dirBlock = world.getBlockState(pos.offset(direction)).getBlock();
            BlockState oppState = world.getBlockState(pos.offset(directionOpp));
            Block oppBlock = oppState.getBlock();
            this.canDispense = (dirBlock == Blocks.AIR || dirBlock == Blocks.WATER) && (oppBlock != Blocks.AIR && oppBlock != Blocks.WATER);
            this.corroded = oppBlock == CopperFitting.CORRODED_FITTING || state.getBlock() == CopperPipe.CORRODED_PIPE;
            this.shootsControlled = oppBlock == Blocks.DROPPER;
            this.shootsSpecial = oppBlock == Blocks.DISPENSER;
            this.canAccept = !(oppBlock instanceof CopperPipe) && !(oppBlock instanceof CopperFitting) && !oppState.isSolidBlock(world, pos);
        }
    }

    public void pipeMove(World world, BlockPos blockPos, BlockState blockState) {
        Direction facing = blockState.get(Properties.FACING);
        boolean bl1 = moveOut(world, blockPos, facing);
        int bl2 = moveIn(world, blockPos, blockState, facing);
        if (bl1 || bl2 >= 2) {
            markDirty(world, blockPos, blockState);
        }
        if (bl2 == 1 || bl2 == 3) {
            world.playSound(null, blockPos, Main.ITEM_IN, SoundCategory.BLOCKS, 0.2F, (world.random.nextFloat() * 0.25F) + 0.8F);
        }
    }

    public static boolean canTransfer(World world, BlockPos pos, boolean out, CopperPipeEntity copperPipe) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity != null) {
            if (entity instanceof CopperPipeEntity pipe) { return pipe.transferCooldown <= 0; }
            if (entity instanceof CopperFittingEntity) { return out || !world.getBlockState(pos).get(Properties.POWERED); }
            if (out) {
                PipeMovementRestrictions.CanTransferTo canTransfer = PipeMovementRestrictions.getCanTransferTo(entity);
                if (canTransfer != null) {
                    return canTransfer.canTransfer((ServerWorld)world, pos, world.getBlockState(pos), copperPipe, entity);
                }
            } else {
                PipeMovementRestrictions.CanTakeFrom canTake = PipeMovementRestrictions.getCanTakeFrom(entity);
                if (canTake != null) {
                    return canTake.canTake((ServerWorld)world, pos, world.getBlockState(pos), copperPipe, entity);
                }
            }
        } return true;
    }

    private int moveIn(World world, BlockPos blockPos, BlockState blockState, Direction facing) {
        BlockPos offsetOppPos = blockPos.offset(facing.getOpposite());
        Inventory inventory2 = getInventoryAt(world, offsetOppPos);
        if (inventory2 != null) {
            if (!isInventoryFull(this, facing) && canTransfer(world, offsetOppPos, false, this)) {
                for (int i = 0; i < inventory2.size(); ++i) {
                    if (!inventory2.getStack(i).isEmpty()) {
                        this.setCooldown(blockState);
                        ItemStack itemStack = inventory2.getStack(i).copy();
                        ItemStack itemStack2 = transfer(this, inventory2.removeStack(i, 1), facing);
                        if (itemStack2.isEmpty()) {
                            this.markDirty();
                            if (blockState.isIn(Main.SILENT_PIPES)) {
                                return 2;
                            }
                            Block block = world.getBlockState(offsetOppPos).getBlock();
                            if (!(block instanceof CopperPipe) && !(block instanceof CopperFitting)) {
                                return 3;
                            }
                            return 2;
                        }
                        inventory2.setStack(i, itemStack);
                    }
                }
            }
        } return 0;
    }

    private boolean moveOut(World world, BlockPos blockPos, Direction facing) {
        BlockPos offsetPos = blockPos.offset(facing);
        Inventory inventory2 = getInventoryAt(world, offsetPos);
        if (inventory2 != null && canTransfer(world, offsetPos, true, this)) {
            Direction opp = facing.getOpposite();
            boolean canMove = true;
            BlockState state = world.getBlockState(offsetPos);
            if (state.getBlock() instanceof CopperPipe) {
                canMove = state.get(Properties.FACING) != facing;
            }
            if (canMove && !isInventoryFull(inventory2, opp)) {
                for (int i = 0; i < this.size(); ++i) {
                    if (!this.getStack(i).isEmpty()) {
                        setCooldown(world, offsetPos);
                        ItemStack itemStack = this.getStack(i).copy();
                        ItemStack itemStack2 = transfer(inventory2, this.removeStack(i, 1), opp);
                        if (itemStack2.isEmpty()) {
                            inventory2.markDirty();
                            return true;
                        }
                        this.setStack(i, itemStack);
                    }
                }

            }
        } return false;
    }

    private boolean dispense(ServerWorld serverWorld, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.get(Properties.FACING);
        Direction directionOpp = direction.getOpposite();
        boolean powered = blockState.get(CopperPipe.POWERED);
        if (this.canDispense) {
            BlockPointerImpl blockPointerImpl = new BlockPointerImpl(serverWorld, blockPos);
            int i = this.chooseNonEmptySlot(serverWorld.random);
            if (!(i < 0)) {
                ItemStack itemStack = this.getStack(i);
                if (!itemStack.isEmpty()) {
                    ItemStack itemStack2;
                    int o=4;
                    if (this.shootsControlled) { //If Dropper
                        o=10;
                        serverWorld.playSound(null, blockPos, Main.LAUNCH, SoundCategory.BLOCKS, 0.2F, (serverWorld.random.nextFloat()*0.25F) + 0.8F);
                    } else if (this.shootsSpecial) { //If Dispenser, Use Pipe-Specific Launch Length
                        if (blockState.getBlock() instanceof CopperPipe pipe) {
                            o = pipe.dispenserShotLength;
                            serverWorld.playSound(null, blockPos, Main.LAUNCH, SoundCategory.BLOCKS, 0.2F, (serverWorld.random.nextFloat()*0.25F) + 0.8F);
                        } else {o=12;}
                    }
                    boolean silent = blockState.isIn(Main.SILENT_PIPES);
                    if (serverWorld.getBlockState(blockPos.offset(directionOpp)).getBlock() instanceof CopperFitting) {
                        itemStack2 = canonShoot(blockPointerImpl, itemStack, blockState, o, powered, true, silent, this.corroded);
                    } else {
                        itemStack2 = canonShoot(blockPointerImpl, itemStack, blockState, o, powered, false, silent, this.corroded);
                        blockPointerImpl.getWorld().syncWorldEvent(2000, blockPointerImpl.getPos(), direction.getId());
                    }
                    this.setStack(i, itemStack2);
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack canonShoot(BlockPointer blockPointer, ItemStack itemStack, BlockState state, int i, boolean powered, boolean fitting, boolean silent, boolean corroded) {
        ServerWorld world = blockPointer.getWorld();
        BlockPos pos = blockPointer.getPos();
        Direction direction = state.get(Properties.FACING);
        Position position = CopperPipe.getOutputLocation(blockPointer, direction);
        ItemStack itemStack2 = itemStack;
        if (powered) { //Special Behavior When Powered
            PoweredPipeDispenses.PoweredDispense<?> poweredDispense = PoweredPipeDispenses.getDispense(itemStack2.getItem());
            if (poweredDispense != null) {
                itemStack2=itemStack.split(1);
                poweredDispense.dispense(world, itemStack2, i, direction, position, state, corroded, pos, this);
                if (!fitting && !silent) {
                    world.playSound(null, pos, Main.ITEM_OUT, SoundCategory.BLOCKS, 0.2F, (world.random.nextFloat()*0.25F) + 0.8F);
                    world.emitGameEvent(null, GameEvent.ENTITY_PLACE, pos);
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
                world.syncWorldEvent(2000, pos, direction.getId());
            }
        } else {
            itemStack2=itemStack.split(1);
            world.syncWorldEvent(2000, blockPointer.getPos(), direction.getId());
            spawnItem(world, itemStack2, i, direction, position, direction, corroded);
            if (!silent) {
                world.emitGameEvent(null, GameEvent.ENTITY_PLACE, pos);
                world.playSound(null, blockPointer.getPos(), Main.ITEM_OUT, SoundCategory.BLOCKS, 0.2F, (world.random.nextFloat() * 0.25F) + 0.8F);
            }
        }
        return itemStack;
    }

    public static void spawnItem(World world, ItemStack itemStack, int i, Direction direction, Position position, Direction facing, boolean corroded) { //Simply Spawn An Item
        double d = position.getX();
        double e = position.getY();
        double f = position.getZ();
        if (direction.getAxis() == Direction.Axis.Y) {
            e -= 0.125D;
        } else {
            e -= 0.15625D;
        }
        double x = 0;
        double y = 0;
        double z = 0;
        Direction.Axis axis = facing.getAxis();
        x = axis == Direction.Axis.X ? (i * facing.getOffsetX()) * 0.1 : corroded ? (world.random.nextDouble()*0.6) - 0.3 : x;
        y = axis == Direction.Axis.Y ? (i * facing.getOffsetY()) * 0.1 : corroded ? (world.random.nextDouble()*0.6) - 0.3 : y;
        z = axis == Direction.Axis.Z ? (i * facing.getOffsetZ()) * 0.1 : corroded ? (world.random.nextDouble()*0.6) - 0.3 : z;
        ItemEntity itemEntity = new ItemEntity(world, d, e, f, itemStack);
        itemEntity.setVelocity(x, y, z);
        world.spawnEntity(itemEntity);
    }

    public int chooseNonEmptySlot(Random random) {
        this.checkLootInteraction(null);
        int i = -1;
        int j = 1;
        for(int k = 0; k < this.inventory.size(); ++k) {
            if (!this.inventory.get(k).isEmpty() && random.nextInt(j++) == 0) {
                i = k;
            }
        } return i;
    }

    private static IntStream getAvailableSlots(Inventory inventory, Direction direction) {
        return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory)inventory).getAvailableSlots(direction)) : IntStream.range(0, inventory.size());
    }

    private static boolean isInventoryFull(Inventory inventory, Direction direction) {
        return getAvailableSlots(inventory, direction).allMatch((i) -> {
            ItemStack itemStack = inventory.getStack(i);
            return itemStack.getCount() >= itemStack.getMaxCount();
        });
    }

    public static ItemStack transfer(Inventory inventory2, ItemStack itemStack, @Nullable Direction direction) {
        if (inventory2 instanceof SidedInventory sidedInventory && direction != null) {
            int[] is = sidedInventory.getAvailableSlots(direction);
            for(int i = 0; i < is.length && !itemStack.isEmpty(); ++i) {
                itemStack = transfer(inventory2, itemStack, is[i], direction);
            }
        } else {
            int sidedInventory = inventory2.size();
            for(int is = 0; is < sidedInventory && !itemStack.isEmpty(); ++is) {
                itemStack = transfer(inventory2, itemStack, is, direction);
            }
        } return itemStack;
    }

    private static boolean canInsert(Inventory inventory, ItemStack itemStack, int i, @Nullable Direction direction) {
        return inventory.isValid(i, itemStack);
    }

    private static ItemStack transfer(Inventory inventory2, ItemStack itemStack, int i, @Nullable Direction direction) {
        ItemStack itemStack2 = inventory2.getStack(i);
        if (canInsert(inventory2, itemStack, i, direction)) {
            boolean bl = false;
            if (itemStack2.isEmpty()) {
                inventory2.setStack(i, itemStack);
                itemStack = ItemStack.EMPTY;
                bl = true;
            } else if (canMergeItems(itemStack2, itemStack)) {
                int j = itemStack.getMaxCount() - itemStack2.getCount();
                int k = Math.min(itemStack.getCount(), j);
                itemStack.decrement(k);
                itemStack2.increment(k);
                bl = k > 0;
            }
            if (bl) {
                inventory2.markDirty();
            }
        } return itemStack;
    }

    private boolean isFull() {
        Iterator<ItemStack> var1 = this.inventory.iterator();
        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }
            itemStack = var1.next();
        } while(!itemStack.isEmpty() && itemStack.getCount() == itemStack.getMaxCount());
        return false;
    }

    @Nullable
    public static Inventory getInventoryAt(World world, BlockPos blockPos) {
        return getInventoryAt(world, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D);
    }

    @Nullable
    private static Inventory getInventoryAt(World world, double d, double e, double f) {
        Inventory inventory = null;
        BlockPos blockPos = new BlockPos(d, e, f);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider)block).getInventory(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof Inventory) {
                inventory = (Inventory)blockEntity;
                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
                }
            }
        }
        if (inventory == null) {
            List<Entity> blockEntity = world.getOtherEntities(null, new Box(d - 0.5D, e - 0.5D, f - 0.5D, d + 0.5D, e + 0.5D, f + 0.5D), EntityPredicates.VALID_INVENTORIES);
            if (!blockEntity.isEmpty()) {
                inventory = (Inventory)blockEntity.get(world.random.nextInt(blockEntity.size()));
            }
        }

        return inventory;
    }

    private static boolean canMergeItems(ItemStack itemStack, ItemStack itemStack2) {
        if (!itemStack.isOf(itemStack2.getItem())) {
            return false;
        } else if (itemStack.getDamage() != itemStack2.getDamage()) {
            return false;
        } else if (itemStack.getCount() > itemStack.getMaxCount()) {
            return false;
        } else {
            return ItemStack.areNbtEqual(itemStack, itemStack2);
        }
    }

    public void setCooldown(BlockState state) {
        int i=2;
        if (state.getBlock() instanceof CopperPipe pipe) {i=pipe.cooldown;}
        this.transferCooldown=i;
    }

    public static void setCooldown(World world, BlockPos blockPos) {
        BlockEntity entity = world.getBlockEntity(blockPos);
        BlockState state = world.getBlockState(blockPos);
        if (state.getBlock() instanceof CopperPipe && entity instanceof CopperPipeEntity pipe) {
            pipe.setCooldown(state);
        }
    }

    public GameEventListener getGameEventListener() {
        return this.listener;
    }

    public void readNbt(NbtCompound nbtCompound) {
        super.readNbt(nbtCompound);
        this.transferCooldown = nbtCompound.getInt("transferCooldown");
        this.dispenseCooldown = nbtCompound.getInt("dispenseCooldown");
        this.noteBlockCooldown = nbtCompound.getInt("noteBlockCooldown");
        this.canDispense = nbtCompound.getBoolean("canDispense");
        this.corroded = nbtCompound.getBoolean("corroded");
        this.shootsControlled = nbtCompound.getBoolean("shootsControlled");
        this.shootsSpecial = nbtCompound.getBoolean("shootsSpecial");
        this.canAccept = nbtCompound.getBoolean("canAccept");
        this.extraPipeData = ExtraPipeData.readNbt(nbtCompound);
    }

    protected void writeNbt(NbtCompound nbtCompound) {
        super.writeNbt(nbtCompound);
        nbtCompound.putInt("transferCooldown", this.transferCooldown);
        nbtCompound.putInt("dispenseCooldown", this.dispenseCooldown);
        nbtCompound.putInt("noteBlockCooldown", this.noteBlockCooldown);
        nbtCompound.putBoolean("canDispense", this.canDispense);
        nbtCompound.putBoolean("corroded", this.corroded);
        nbtCompound.putBoolean("shootsControlled", this.shootsControlled);
        nbtCompound.putBoolean("shootsSpecial", this.shootsSpecial);
        nbtCompound.putBoolean("canAccept", this.canAccept);
        ExtraPipeData.writeNbt(nbtCompound, this.extraPipeData);
    }

    public static boolean notCubeNorPipe(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return !(block instanceof CopperPipe) && !(block instanceof CopperFitting) && !state.isSolidBlock(world, pos);
    }

    @Override
    public boolean accepts(World world, GameEventListener gameEventListener, BlockPos blockPos, GameEvent gameEvent, @Nullable Entity entity) {
        if (world instanceof ServerWorld serverWorld) {
            boolean placeDestroy = gameEvent == GameEvent.BLOCK_DESTROY || gameEvent == GameEvent.BLOCK_PLACE;
            if ((serverWorld.getBlockState(blockPos).getBlock() instanceof CopperPipe || blockPos == this.getPos()) && placeDestroy) {
                return false;
            }
            if (this.canAccept) {
                this.moveablePipeDataHandler.addSaveableMoveablePipeNbt(new MoveablePipeDataHandler.SaveableMovablePipeNbt(gameEvent, Vec3d.ofCenter(blockPos), entity, this.getPos()).withShouldMove(true).withShouldSave(true));
                return true;
            } return false;
        } return false;
    }

    @Override
    public void accept(World world, GameEventListener gameEventListener, GameEvent gameEvent, int i) {

    }

    @Override
    public boolean canAccept(GameEvent gameEvent, @Nullable Entity entity) {
        if (!gameEvent.isIn(GameEventTags.VIBRATIONS)) {
            return false;
        } else {
            if (entity != null) {
                if (gameEvent.isIn(GameEventTags.IGNORE_VIBRATIONS_SNEAKING) && entity.bypassesSteppingEffects()) {
                    return false;
                }

                if (entity.occludeVibrationSignals()) {
                    return false;
                }
            }

            return entity == null || !entity.isSpectator();
        }
    }

    public boolean canAcceptMoveableNbt(MoveablePipeDataHandler.MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MoveablePipeDataHandler.MOVE_TYPE.FROM_FITTING) {
            return this.getCachedState().get(Properties.FACING) == moveDirection;
        }
        return this.getCachedState().get(Properties.FACING) == moveDirection || moveDirection == fromState.get(Properties.FACING);
    }

    public boolean canMoveNbtInDirection(Direction direction, BlockState state) {
        return direction != state.get(Properties.FACING).getOpposite();
    }

    public void dispenseMoveableNbt(ServerWorld serverWorld, BlockPos blockPos, BlockState blockState) {
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

    public boolean listenersNearby(World world, BlockPos pos) {
        if (this.extraPipeData != null) {
            if (world.getBlockState(this.extraPipeData.listenerPos).isIn(Main.BLOCK_LISTENERS)) {
                return true;
            } else {
                this.extraPipeData = null;
            }
        }
        int bx = pos.getX();
        int by = pos.getY();
        int bz = pos.getZ();
        for (int x = bx - 8; x <= bx + 8; x++) {
            for (int y = by - 8; y <= by + 8; y++) {
                for (int z = bz - 8; z <= bz + 8; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < 81) {
                        BlockPos l = new BlockPos(x, y, z);
                        if (world.getBlockState(l).isIn(Main.BLOCK_LISTENERS)) {
                            this.extraPipeData = new ExtraPipeData(l);
                            return true;
                        }
                    }
                }
            }
        }
        this.extraPipeData = null;
        List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, new Box(pos.add(-18, -18, -18), pos.add(18, 18, 18)));
        for (Entity entity : entities) {
            if (entity.getType().isIn(Main.ENTITY_LISTENERS) && Math.floor(Math.sqrt(entity.getBlockPos().getSquaredDistance(pos))) <= 16) {
                return true;
            }
        }
        return false;
    }

}
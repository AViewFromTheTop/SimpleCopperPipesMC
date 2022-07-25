package net.lunade.copper.block_entity;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.lunade.copper.Main;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.lunade.copper.blocks.Copyable;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AbstractSimpleCopperBlockEntity extends LootableContainerBlockEntity implements Inventory {
    public static final Logger LOGGER = LogUtils.getLogger();

    public DefaultedList<ItemStack> inventory;
    public int waterCooldown;
    public int waterLevel;
    public int smokeLevel;
    public int electricityCooldown;

    public MoveablePipeDataHandler moveablePipeDataHandler;
    public final MoveablePipeDataHandler.MOVE_TYPE moveType;

    public AbstractSimpleCopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, MoveablePipeDataHandler.MOVE_TYPE moveType) {
        super(blockEntityType, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.waterCooldown = -1;
        this.waterLevel = 0;
        this.smokeLevel = 0;
        this.electricityCooldown = -1;
        this.moveablePipeDataHandler = new MoveablePipeDataHandler();
        this.moveType = moveType;
    }

    public void serverTick(World world, BlockPos blockPos, BlockState blockState) {
        BlockState state = blockState;
        if (!world.isClient) {
            this.tickMoveableNbt((ServerWorld) world, blockPos, blockState);
            this.dispenseMoveableNbt((ServerWorld) world, blockPos, blockState);
            this.moveMoveableNbt((ServerWorld) world, blockPos, blockState);
        }
        if (this.waterCooldown>0) {
            --this.waterCooldown;
        } else {
            this.waterCooldown=60;
            int water = this.canWater(world, blockPos, blockState);
            int smoke = this.canSmoke(world, blockPos, blockState);
            this.waterLevel = water;
            this.smokeLevel = smoke;
            if (state.contains(CopperPipeProperties.HAS_WATER)) {
                state = state.with(CopperPipeProperties.HAS_WATER, water > 0);
            }
            if (state.contains(CopperPipeProperties.HAS_SMOKE)) {
                state = state.with(CopperPipeProperties.HAS_SMOKE, smoke > 0);
            }
        }
        if (this.isEmpty() == state.get(CopperPipeProperties.HAS_ITEM)) {state = state.with(CopperPipeProperties.HAS_ITEM, !isEmpty());}
        if (this.electricityCooldown>=0) { --electricityCooldown; }
        if (this.electricityCooldown==-1 && state.get(CopperPipeProperties.HAS_ELECTRICITY)) {
            this.electricityCooldown=80;
            Block stateGetBlock = state.getBlock();
            if (stateGetBlock instanceof Copyable copyable) {
                if (Main.PREVIOUS_STAGE.containsKey(stateGetBlock) && !state.isIn(Main.WAXED)) {
                    state = copyable.makeCopyOf(state, Main.PREVIOUS_STAGE.get(stateGetBlock));
                }
            }
        }
        if (this.electricityCooldown==79) {
            sendElectricity(world, blockPos);
        }
        if (this.electricityCooldown==0) {
            if (state != null) {
                if (state.contains(CopperPipeProperties.HAS_ELECTRICITY)) {
                    state = state.with(CopperPipeProperties.HAS_ELECTRICITY, false);
                }
            }
        }
        if (state!=blockState) {
            world.setBlockState(blockPos, state);
        }
    }

    public static void sendElectricity(World world, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            BlockPos pos = blockPos.offset(direction);
            if (world.isChunkLoaded(pos)) {
                BlockState state = world.getBlockState(pos);
                if (state.contains(CopperPipeProperties.HAS_ELECTRICITY)) {
                    BlockEntity entity = world.getBlockEntity(pos);
                    if (entity instanceof AbstractSimpleCopperBlockEntity copperBlockEntity) {
                        int axis = state.contains(Properties.FACING) ? state.get(Properties.FACING).getAxis().ordinal() : direction.getAxis().ordinal();
                        if (copperBlockEntity.electricityCooldown == -1) {
                            world.syncWorldEvent(3002, pos, axis);
                            world.setBlockState(pos, state.with(CopperPipeProperties.HAS_ELECTRICITY, true));
                        }
                    }
                }
            }
        }
    }

    public boolean canAcceptMoveableNbt(MoveablePipeDataHandler.MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        return true;
    }

    public boolean canMoveNbtInDirection(Direction direction, BlockState blockState) {
        return true;
    }

    public void tickMoveableNbt(ServerWorld world, BlockPos blockPos, BlockState blockState) {
        for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : this.moveablePipeDataHandler.getSavedNbtList()) {
            nbt.tick(world, blockPos, blockState, this);
        }
    }

    public void dispenseMoveableNbt(ServerWorld serverWorld, BlockPos blockPos, BlockState blockState) {

    }

    public void moveMoveableNbt(ServerWorld world, BlockPos blockPos, BlockState blockState) {
        ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> nbtList = moveablePipeDataHandler.getSavedNbtList();
        ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> usedNbts = new ArrayList<>();
        if (!nbtList.isEmpty()) {
            List<Direction> dirs = Main.shuffledDirections(world.getRandom());
            for (Direction direction : dirs) {
                if (this.canMoveNbtInDirection(direction, blockState)) {
                    BlockPos newPos = blockPos.offset(direction);
                    if (world.isChunkLoaded(newPos)) {
                        BlockState state = world.getBlockState(newPos);
                        BlockEntity entity = world.getBlockEntity(newPos);
                        if (entity instanceof AbstractSimpleCopperBlockEntity copperEntity) {
                            if (copperEntity.canAcceptMoveableNbt(this.moveType, direction, blockState)) {
                                for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : nbtList) {
                                    if (nbt.getShouldMove() && (!nbt.getCanOnlyGoThroughOnePipe() || !usedNbts.contains(nbt))) {
                                        if (nbt.getShouldCopy()) {
                                            copperEntity.moveablePipeDataHandler.setMoveablePipeNbt(nbt.getNbtID(), nbt.copyOf());
                                        } else {
                                            copperEntity.moveablePipeDataHandler.setMoveablePipeNbt(nbt.getNbtID(), nbt);
                                        }
                                        nbt.onMove(world, newPos, state, this);
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
            this.markDirty();
        }
    }

    public int canWater(World world, BlockPos blockPos, BlockState blockState) {
        BlockPos p;
        IntArrayList nums = new IntArrayList();
        for (Direction direction : Direction.values()) {
            p = blockPos.offset(direction);
            if (world.isChunkLoaded(p)) {
                BlockState state = world.getBlockState(p);
                if (state.getBlock() instanceof CopperPipe) {
                    if (state.get(Properties.FACING) == direction.getOpposite()) {
                        nums.add(CopperPipe.canWaterFitting(world, p, state));
                    }
                }
            }
        }
        return getHighest(nums) > 0 ? 12 : 0;
    }

    public int canSmoke(World world, BlockPos blockPos, BlockState blockState) {
        BlockPos p;
        IntArrayList nums = new IntArrayList();
        for (Direction direction : Direction.values()) {
            p = blockPos.offset(direction);
            if (world.isChunkLoaded(p)) {
                BlockState state = world.getBlockState(p);
                if (state.getBlock() instanceof CopperPipe) {
                    if (state.get(Properties.FACING) == direction.getOpposite()) {
                        nums.add(CopperPipe.canSmokeFitting(world, p, state));
                    }
                }
            }
        }
        return getHighest(nums) > 0 ? 12 : 0;
    }

    public int getDecreasedSmoke() {
        return this.smokeLevel > 0 ? 12 : 0;
    }

    public int getDecreasedWater() {
        return this.waterLevel > 0 ? 12 : 0;
    }

    public static int getHighest(IntArrayList list) {
        int highest = 0;
        for (int i : list.elements()) {
            highest = Math.max(i, highest);
        } return highest;
    }

    public void readNbt(NbtCompound nbtCompound) {
        super.readNbt(nbtCompound);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbtCompound)) {
            Inventories.readNbt(nbtCompound, this.inventory);
        }
        this.waterCooldown = nbtCompound.getInt("WaterCooldown");
        this.waterLevel = nbtCompound.getInt("waterLevel");
        this.smokeLevel = nbtCompound.getInt("smokeLevel");
        this.electricityCooldown = nbtCompound.getInt("electricityCooldown");
        this.moveablePipeDataHandler.readNbt(nbtCompound);
    }

    protected void writeNbt(NbtCompound nbtCompound) {
        super.writeNbt(nbtCompound);
        if (!this.serializeLootTable(nbtCompound)) {
            Inventories.writeNbt(nbtCompound, this.inventory);
        }
        nbtCompound.putInt("WaterCooldown", this.waterCooldown);
        nbtCompound.putInt("waterLevel", this.waterLevel);
        nbtCompound.putInt("smokeLevel", this.smokeLevel);
        nbtCompound.putInt("electricityCooldown", this.electricityCooldown);
        this.moveablePipeDataHandler.writeNbt(nbtCompound);
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> defaultedList) {
        this.inventory = defaultedList;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText(this.getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected ScreenHandler createScreenHandler(int i, PlayerInventory playerInventory) {
        return new HopperScreenHandler(i, playerInventory, this);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }
}
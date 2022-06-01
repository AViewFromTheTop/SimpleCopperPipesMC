package net.lunade.copper.block_entity;

import net.lunade.copper.Main;
import net.lunade.copper.blocks.CopperFitting;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.lunade.copper.blocks.CopperFitting.sendElectricity;
import static net.lunade.copper.blocks.CopperPipeProperties.HAS_SMOKE;
import static net.lunade.copper.blocks.CopperPipeProperties.HAS_WATER;

public class CopperFittingEntity extends LootableContainerBlockEntity implements Inventory {
    private DefaultedList<ItemStack> inventory;
    private int waterCooldown;
    public int waterLevel;
    public int smokeLevel;
    public int electricityCooldown;

    public CopperFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(Main.COPPER_FITTING_ENTITY, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.waterCooldown = -1;
        this.waterLevel = 0;
        this.smokeLevel = 0;
        this.electricityCooldown = -1;
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
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.lunade.copper_fitting");
    }

    public int size() {
        return this.inventory.size();
    }

    public void setStack(int i, ItemStack itemStack) {
        this.checkLootInteraction(null);
        this.getInvStackList().set(i, itemStack);
        if (itemStack.getCount() > this.getMaxCountPerStack()) {
            itemStack.setCount(this.getMaxCountPerStack());
        }
    }

    public static void serverTick(World world, BlockPos blockPos, BlockState blockState, CopperFittingEntity copperFittingEntity) {
        BlockState state = blockState;
        if (copperFittingEntity.waterCooldown>0) {
            --copperFittingEntity.waterCooldown;
        } else {
            copperFittingEntity.waterCooldown=60;
            int water = CopperFitting.canWater(world, blockPos);
            int smoke = CopperFitting.canSmoke(world, blockPos);
            boolean canWater = water>0;
            boolean canSmoke = smoke>0;
            if (canWater) { copperFittingEntity.waterLevel=12; } else { copperFittingEntity.waterLevel=0; }
            if (canSmoke) { copperFittingEntity.smokeLevel=12; } else { copperFittingEntity.smokeLevel=0; }
            if (canWater != state.get(HAS_WATER) || canSmoke != state.get(HAS_SMOKE)) {
                state = state.with(HAS_WATER, canWater).with(HAS_SMOKE, canSmoke);
            }
        }
        if (copperFittingEntity.isEmpty() == state.get(CopperFitting.HAS_ITEM)) {state = state.with(CopperFitting.HAS_ITEM, !copperFittingEntity.isEmpty());}
        if (copperFittingEntity.electricityCooldown>=0) {--copperFittingEntity.electricityCooldown;}
        if (copperFittingEntity.electricityCooldown==-1 && state.get(CopperFitting.HAS_ELECTRICITY)) {
            copperFittingEntity.electricityCooldown=80;
            if (state.getBlock() instanceof CopperFitting fitting) {
                if (CopperFitting.getPreviousStage(world, blockPos) != null && !fitting.waxed) {
                    state = CopperFitting.makeCopyOf(state, CopperFitting.getPreviousStage(world, blockPos));
                }
            }
        }
        if (copperFittingEntity.electricityCooldown==79) {
            sendElectricity(world, blockPos);
        }
        if (copperFittingEntity.electricityCooldown==0) {
            assert state != null;
            state=state.with(CopperFitting.HAS_ELECTRICITY, false);
        }
        if (state!=blockState) {
            world.setBlockState(blockPos, state);
        }
    }

    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    protected void setInvStackList(DefaultedList<ItemStack> defaultedList) {
        this.inventory = defaultedList;
    }

    protected ScreenHandler createScreenHandler(int i, PlayerInventory playerInventory) {
        return new HopperScreenHandler(i, playerInventory, this);
    }
}

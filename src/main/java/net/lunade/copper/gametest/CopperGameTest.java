package net.lunade.copper.gametest;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CopperGameTest implements FabricGameTest {

    private static final String STORAGE_UNIFICATION = "copper_pipe:storage_unification";

    @GameTest(template = STORAGE_UNIFICATION, timeoutTicks = 518)
    public void storageUnification(GameTestHelper helper) {
        ItemVariant resource = ItemVariant.of(Blocks.MANGROVE_LOG);
        for (int z = 0; z < 10; z +=  2) {
            BlockPos chestPos = new BlockPos(9, 2, z);
            helper.assertBlockPresent(Blocks.CHEST, chestPos);
            moveResources(helper, chestPos, resource, 128, MoveDirection.IN, false);
        }
        helper.onEachTick(() -> {
            BlockPos chestPos = new BlockPos(9, 4, 4); // unified chest
            helper.assertBlockPresent(Blocks.CHEST, chestPos);
            long amountInChest = moveResources(helper, chestPos, resource, 640, MoveDirection.OUT, true);
            if (amountInChest == 640) helper.succeed();
        });
    }

    private static long moveResources(GameTestHelper helper, BlockPos inventoryPos, ItemVariant resource, long amount, MoveDirection direction, boolean simulate) {
        BlockState blockState = helper.getBlockState(inventoryPos);
        BlockEntity blockEntity = helper.getBlockEntity(inventoryPos);

        Storage<ItemVariant> inventory = ItemStorage.SIDED.find(helper.getLevel(), helper.absolutePos(inventoryPos), blockState, blockEntity, null);
        helper.assertTrue(inventory != null, "Inventory not found at " + inventoryPos);
        Transaction transaction = Transaction.openOuter();
        long inserted = switch (direction) {
            case IN -> insert(inventory, resource, amount, transaction, simulate);
            case OUT -> extract(inventory, resource, amount, transaction, simulate);
        };
        transaction.commit();
        return inserted;
    }

    private static long insert(Storage<ItemVariant> inventory, ItemVariant resource, long amount, Transaction transaction, boolean simulate) {
        return simulate ? StorageUtil.simulateInsert(inventory, resource, amount, transaction) : inventory.insert(resource, amount, transaction);
    }

    private static long extract(Storage<ItemVariant> inventory, ItemVariant resource, long amount, Transaction transaction, boolean simulate) {
        return simulate ? StorageUtil.simulateExtract(inventory, resource, amount, transaction) : inventory.extract(resource, amount, transaction);
    }

    private enum MoveDirection {
        IN,
        OUT
    }
}
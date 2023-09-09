package net.lunade.copper.gametest;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CopperGameTest implements FabricGameTest {

    private static final String AXE_INTERACTION = "copper_pipe:axe_interaction";
    private static final String DIRECT_PIPE_TRANSFER = "copper_pipe:direct_pipe_transfer";
    private static final String STORAGE_UNIFICATION = "copper_pipe:storage_unification";

    @GameTest(template = AXE_INTERACTION)
    public void axeInteraction(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setPos(helper.absoluteVec(new Vec3(9.5, 3.0, 0.5)));
        ItemStack stack = new ItemStack(Items.DIAMOND_AXE);

        // variable names reflect the end result after axe interaction
        Vec3 copperPipe = helper.absoluteVec(new Vec3(9.5, 2.5, 0.5));
        Vec3 exposedPipe = helper.absoluteVec(new Vec3(9.5, 2.5, 1.5));
        Vec3 weatheredPipe = helper.absoluteVec(new Vec3(9.5, 2.5, 2.5));
        Vec3 copperPipe2 = helper.absoluteVec(new Vec3(8.5, 2.5, 0.5));
        Vec3 exposedPipe2 = helper.absoluteVec(new Vec3(8.5, 2.5, 1.5));
        Vec3 weatheredPipe2 = helper.absoluteVec(new Vec3(8.5, 2.5, 2.5));
        Vec3 oxidizedPipe = helper.absoluteVec(new Vec3(8.5, 2.5, 3.5));

        Vec3 copperFitting = helper.absoluteVec(new Vec3(9.5, 2.5, 7.5));
        Vec3 exposedFitting = helper.absoluteVec(new Vec3(9.5, 2.5, 8.5));
        Vec3 weatheredFitting = helper.absoluteVec(new Vec3(9.5, 2.5, 9.5));
        Vec3 copperFitting2 = helper.absoluteVec(new Vec3(8.5, 2.5, 6.5));
        Vec3 exposedFitting2 = helper.absoluteVec(new Vec3(8.5, 2.5, 7.5));
        Vec3 weatheredFitting2 = helper.absoluteVec(new Vec3(8.5, 2.5, 8.5));
        Vec3 oxidizedFitting = helper.absoluteVec(new Vec3(8.5, 2.5, 9.5));
        List<Vec3> copperLocations = List.of(
                copperPipe, exposedPipe, weatheredPipe, copperPipe2, exposedPipe2, weatheredPipe2, oxidizedPipe,
                copperFitting, exposedFitting, weatheredFitting, copperFitting2, exposedFitting2, weatheredFitting2, oxidizedFitting
        );
        helper.runAfterDelay(1L, () -> {
            for (Vec3 location : copperLocations) {
                player.setPos(location.add(0, 0.5, 0));
                player.lookAt(EntityAnchorArgument.Anchor.EYES, location);
                BlockHitResult hitResult = (BlockHitResult) player.pick(2, 1.0F, false);
                UseOnContext context = new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult);
                stack.useOn(context); // simulates a player using an axe on the pipe
            }
        });
        helper.runAfterDelay(2L, () -> {
            // remove fake player
            helper.getLevel().getServer().getPlayerList().remove(player);
            player.remove(Entity.RemovalReason.DISCARDED);

            // check if all pipes and fittings have been interacted with
            helper.assertBlockPresent(CopperPipe.COPPER_PIPE, BlockPos.containing(copperPipe));
            helper.assertBlockPresent(CopperPipe.EXPOSED_PIPE, BlockPos.containing(exposedPipe));
            helper.assertBlockPresent(CopperPipe.WEATHERED_PIPE, BlockPos.containing(weatheredPipe));
            helper.assertBlockPresent(CopperPipe.COPPER_PIPE, BlockPos.containing(copperPipe2));
            helper.assertBlockPresent(CopperPipe.EXPOSED_PIPE, BlockPos.containing(exposedPipe2));
            helper.assertBlockPresent(CopperPipe.WEATHERED_PIPE, BlockPos.containing(weatheredPipe2));
            helper.assertBlockPresent(CopperPipe.OXIDIZED_PIPE, BlockPos.containing(oxidizedPipe));

            helper.assertBlockPresent(CopperFitting.COPPER_FITTING, BlockPos.containing(copperFitting));
            helper.assertBlockPresent(CopperFitting.EXPOSED_FITTING, BlockPos.containing(exposedFitting));
            helper.assertBlockPresent(CopperFitting.WEATHERED_FITTING, BlockPos.containing(weatheredFitting));
            helper.assertBlockPresent(CopperFitting.COPPER_FITTING, BlockPos.containing(copperFitting2));
            helper.assertBlockPresent(CopperFitting.EXPOSED_FITTING, BlockPos.containing(exposedFitting2));
            helper.assertBlockPresent(CopperFitting.WEATHERED_FITTING, BlockPos.containing(weatheredFitting2));
            helper.assertBlockPresent(CopperFitting.OXIDIZED_FITTING, BlockPos.containing(oxidizedFitting));

            helper.succeed();
        });
    }

    @GameTest(template = DIRECT_PIPE_TRANSFER)
    public void directPipeTransfer(GameTestHelper helper) {
        ItemVariant resource = ItemVariant.of(Blocks.MANGROVE_LOG);
        BlockPos sourceChest = new BlockPos(9, 2, 4);
        BlockPos targetChest = new BlockPos(0, 2, 4);
        helper.assertBlockPresent(Blocks.CHEST, sourceChest);
        helper.assertBlockPresent(Blocks.CHEST, targetChest);

        moveResources(helper, sourceChest, resource, 32, MoveDirection.IN, false);
        helper.onEachTick(() -> {
            long amountInChest = moveResources(helper, targetChest, resource, 32, MoveDirection.OUT, true);
            if (amountInChest == 32) helper.succeed();
        });
    }

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

    private static long moveResources(GameTestHelper helper, BlockPos inventoryPos, ItemVariant resource, long maxAmount, MoveDirection direction, boolean simulate) {
        BlockState blockState = helper.getBlockState(inventoryPos);
        BlockEntity blockEntity = helper.getBlockEntity(inventoryPos);

        Storage<ItemVariant> inventory = ItemStorage.SIDED.find(helper.getLevel(), helper.absolutePos(inventoryPos), blockState, blockEntity, null);
        helper.assertTrue(inventory != null, "Inventory not found at " + inventoryPos);
        Transaction transaction = Transaction.openOuter();
        long inserted = switch (direction) {
            case IN -> insert(inventory, resource, maxAmount, transaction, simulate);
            case OUT -> extract(inventory, resource, maxAmount, transaction, simulate);
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
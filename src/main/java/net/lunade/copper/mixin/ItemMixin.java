package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(at = @At("TAIL"), method = "useOnBlock")
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext, CallbackInfoReturnable info) {
        World world = itemUsageContext.getWorld();
        BlockPos blockPos = itemUsageContext.getBlockPos();
        PlayerEntity playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getStack();
        boolean glowPipe = false;
        boolean glowFitting = false;
        if (blockState!=null && blockState.getBlock() instanceof CopperPipe && itemStack.isOf(Items.GLOW_INK_SAC)) {
            if (CopperPipe.getGlowingStage(world, blockPos)!=null) {
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
                glowPipe = true;
            }
        }
        if (blockState!=null && blockState.getBlock() instanceof CopperFitting && itemStack.isOf(Items.GLOW_INK_SAC)) {
            if (CopperFitting.getGlowingStage(world, blockPos)!=null) {
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
                glowFitting = true;
            }
        }
        if (glowPipe) {
            if (playerEntity instanceof ServerPlayerEntity) { Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack); }

            Block block = blockState.getBlock();
            if (block instanceof CopperPipe) {
                if (CopperPipe.getGlowingStage(world, blockPos)!=null) {
                    CopperPipe.makeCopyOf(blockState, world, blockPos, CopperPipe.getGlowingStage(world, blockPos));
                }
            }
            if (playerEntity != null) { itemStack.decrement(1); }
            return ActionResult.success(world.isClient);
        } else if (glowFitting) {
            if (playerEntity instanceof ServerPlayerEntity) { Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack); }
            Block block = blockState.getBlock();
            if (block instanceof CopperFitting) {
                if (CopperFitting.getGlowingStage(world, blockPos)!=null) {
                    CopperFitting.makeCopyOf(blockState, world, blockPos, CopperFitting.getGlowingStage(world, blockPos));
                }
            }
            if (playerEntity != null) { itemStack.decrement(1); }

            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

}

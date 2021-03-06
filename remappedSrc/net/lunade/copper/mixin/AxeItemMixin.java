package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
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

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(at = @At("TAIL"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext itemUsageContext, CallbackInfoReturnable<ActionResult> info) {
        World world = itemUsageContext.getWorld();
        BlockPos blockPos = itemUsageContext.getBlockPos();
        PlayerEntity playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getStack();
        boolean go = false;
        boolean fit = false;
        if (blockState!=null) {
            Block block = blockState.getBlock();
            if (block instanceof CopperPipe pipe) {
                if (CopperPipe.PREVIOUS_STAGE.containsKey(block)) {
                    if (!pipe.waxed) {
                        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
                    } else {
                        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.syncWorldEvent(playerEntity, 3004, blockPos, 0);
                    }
                    go = true;
                }
            }
            if (block instanceof CopperFitting fitting) {
                if (CopperFitting.PREVIOUS_STAGE.containsKey(block)) {
                    if (!fitting.waxed) {
                        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
                    } else {
                        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.syncWorldEvent(playerEntity, 3004, blockPos, 0);
                    }
                    fit = true;
                }
            }
        }
        if (go) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            Block block = blockState.getBlock();
            if (CopperPipe.PREVIOUS_STAGE.containsKey(block)) {
                CopperPipe.makeCopyOf(blockState, world, blockPos, CopperPipe.PREVIOUS_STAGE.get(block));
            }
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, (playerEntityx) -> playerEntityx.sendToolBreakStatus(itemUsageContext.getHand()));
            }

            info.setReturnValue(ActionResult.success(world.isClient));
            info.cancel();
        } else if (fit) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            Block block = blockState.getBlock();
            if (CopperFitting.PREVIOUS_STAGE.containsKey(block)) {
                CopperFitting.makeCopyOf(blockState, world, blockPos, CopperFitting.PREVIOUS_STAGE.get(block));
            }
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, (playerEntityx) -> playerEntityx.sendToolBreakStatus(itemUsageContext.getHand()));
            }

            info.setReturnValue(ActionResult.success(world.isClient));
            info.cancel();
        }
    }

}

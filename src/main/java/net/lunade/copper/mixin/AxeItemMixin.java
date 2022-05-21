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

    @Inject(at = @At("TAIL"), method = "useOnBlock")
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext, CallbackInfoReturnable info) {
        World world = itemUsageContext.getWorld();
        BlockPos blockPos = itemUsageContext.getBlockPos();
        PlayerEntity playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getStack();
        boolean go = false;
        boolean fit = false;
        if (blockState!=null && blockState.getBlock() instanceof CopperPipe pipe) {
            if (CopperPipe.getPreviousStage(world, blockPos)!=null) {
                if (!pipe.waxed && blockState.getBlock() != CopperPipe.COPPER_PIPE && blockState.getBlock() != CopperPipe.CORRODED_PIPE) {
                    world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
                    go = true;
                } else if (pipe.waxed) {
                    world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.syncWorldEvent(playerEntity, 3004, blockPos, 0);
                    go = true;
                }
            }
        }
        if (blockState!=null && blockState.getBlock() instanceof CopperFitting fitting) {
            if (CopperFitting.getPreviousStage(world, blockPos)!=null) {
                if (!fitting.waxed && blockState.getBlock() != CopperFitting.COPPER_FITTING && blockState.getBlock() != CopperFitting.CORRODED_FITTING) {
                    world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
                    fit = true;
                } else if (fitting.waxed) {
                    world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.syncWorldEvent(playerEntity, 3004, blockPos, 0);
                    fit = true;
                }
            }
        }
        if (go) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            Block block = blockState.getBlock();
            if (block instanceof CopperPipe) {
                if (CopperPipe.getPreviousStage(world, blockPos)!=null) {
                    CopperPipe.makeCopyOf(blockState, world, blockPos, CopperPipe.getPreviousStage(world, blockPos));
                }
            }
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, (playerEntityx) -> {
                    playerEntityx.sendToolBreakStatus(itemUsageContext.getHand());
                });
            }

            return ActionResult.success(world.isClient);
        } else if (fit) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            Block block = blockState.getBlock();
            if (block instanceof CopperFitting) {
                if (CopperFitting.getPreviousStage(world, blockPos)!=null) {
                    CopperFitting.makeCopyOf(blockState, world, blockPos, CopperFitting.getPreviousStage(world, blockPos));
                }
            }
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, (playerEntityx) -> {
                    playerEntityx.sendToolBreakStatus(itemUsageContext.getHand());
                });
            }

            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

}

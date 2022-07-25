package net.lunade.copper.mixin;

import net.lunade.copper.Main;
import net.lunade.copper.blocks.Copyable;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoneycombItem;
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

@Mixin(HoneycombItem.class)
public class HoneyCombItemMixin {

    @Inject(at = @At("TAIL"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext itemUsageContext, CallbackInfoReturnable<ActionResult> info) {
        World world = itemUsageContext.getWorld();
        BlockPos blockPos = itemUsageContext.getBlockPos();
        PlayerEntity playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getStack();
        boolean canWax = false;
        if (blockState!=null) {
            Block block = blockState.getBlock();
            if (Main.WAX_STAGE.containsKey(block)) {
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HONEYCOMB_WAX_ON, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.syncWorldEvent(playerEntity, 3003, blockPos, 0);
                canWax = true;
            }
        }
        if (canWax) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            Block block = blockState.getBlock();
            if (Main.WAX_STAGE.containsKey(block)) {
                Block waxStage = Main.WAX_STAGE.get(block);
                if (block instanceof Copyable copyable) {
                    copyable.makeCopyOf(blockState, world, blockPos, waxStage);
                }
            }
            itemStack.decrement(1);

            info.setReturnValue(ActionResult.success(world.isClient));
            info.cancel();
        }
    }

}
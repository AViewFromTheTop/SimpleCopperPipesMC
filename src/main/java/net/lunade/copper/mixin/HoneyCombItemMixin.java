package net.lunade.copper.mixin;

import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.blocks.Copyable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoneycombItem.class)
public class HoneyCombItemMixin {

    @Inject(at = @At("TAIL"), method = "useOn", cancellable = true)
    public void useOn(UseOnContext itemUsageContext, CallbackInfoReturnable<InteractionResult> info) {
        Level world = itemUsageContext.getLevel();
        BlockPos blockPos = itemUsageContext.getClickedPos();
        Player playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getItemInHand();
        boolean canWax = false;

        if (blockState != null) {
            Block block = blockState.getBlock();
            if (CopperPipeMain.WAX_STAGE.containsKey(block)) {
                world.playSound(playerEntity, blockPos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.levelEvent(playerEntity, 3003, blockPos, 0);
                canWax = true;
            }
        }
        if (canWax) {
            if (playerEntity instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)playerEntity, blockPos, itemStack);
            }

            Block block = blockState.getBlock();
            if (CopperPipeMain.WAX_STAGE.containsKey(block)) {
                Block waxStage = CopperPipeMain.WAX_STAGE.get(block);
                if (block instanceof Copyable copyable) {
                    copyable.makeCopyOf(blockState, world, blockPos, waxStage);
                }
            }
            itemStack.shrink(1);

            info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
            info.cancel();
        }
    }

}

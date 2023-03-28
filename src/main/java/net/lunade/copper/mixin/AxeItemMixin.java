package net.lunade.copper.mixin;

import net.lunade.copper.CopperPipeMain;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(at = @At("TAIL"), method = "useOn", cancellable = true)
    public void simpleCopperPipes$useOn(UseOnContext itemUsageContext, CallbackInfoReturnable<InteractionResult> info) {
        Level world = itemUsageContext.getLevel();
        BlockPos blockPos = itemUsageContext.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);

        Block block = blockState.getBlock();
        if (CopperPipeMain.PREVIOUS_STAGE.containsKey(block) && !blockState.is(CopperPipeMain.UNSCRAPEABLE)) {
            Player playerEntity = itemUsageContext.getPlayer();
            ItemStack itemStack = itemUsageContext.getItemInHand();
            if (!blockState.is(CopperPipeMain.WAXED)) {
                world.playSound(playerEntity, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.levelEvent(playerEntity, 3005, blockPos, 0);
            } else {
                world.playSound(playerEntity, blockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.levelEvent(playerEntity, 3004, blockPos, 0);
            }
            if (playerEntity instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) playerEntity, blockPos, itemStack);
            }
            if (CopperPipeMain.PREVIOUS_STAGE.containsKey(block)) {
                world.setBlockAndUpdate(blockPos, CopperPipeMain.PREVIOUS_STAGE.get(block).withPropertiesOf(blockState));
            }
            if (playerEntity != null) {
                itemStack.hurtAndBreak(1, playerEntity, (playerEntityx) -> playerEntityx.broadcastBreakEvent(itemUsageContext.getHand()));
            }
            info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }

}

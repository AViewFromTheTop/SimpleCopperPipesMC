package net.lunade.copper.mixin;

import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    public void useOn(UseOnContext itemUsageContext, CallbackInfoReturnable<InteractionResult> info) {
        Level world = itemUsageContext.getLevel();
        BlockPos blockPos = itemUsageContext.getClickedPos();
        Player playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getItemInHand();

        if (blockState != null && blockState.getBlock() instanceof CopperPipe) {
            if (playerEntity instanceof ServerPlayer player) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, blockPos, itemStack);
            }

            Direction face = itemUsageContext.getClickedFace();
            if (face != blockState.getValue(CopperPipe.FACING)) {
                BlockState state = blockState.setValue(CopperPipe.FACING, face)
                        .setValue(CopperPipe.BACK_CONNECTED, CopperPipe.canConnectBack(world, blockPos, face))
                        .setValue(CopperPipe.FRONT_CONNECTED, CopperPipe.canConnectFront(world, blockPos, face))
                        .setValue(CopperPipe.SMOOTH, CopperPipe.isSmooth(world, blockPos, face));

                world.setBlockAndUpdate(blockPos, state);
                world.playSound(null, blockPos, CopperPipeMain.TURN, SoundSource.BLOCKS, 0.5F, 1F);
                if (playerEntity != null) {
                    itemStack.hurtAndBreak(1, playerEntity, (playerEntityx) -> playerEntityx.broadcastBreakEvent(itemUsageContext.getHand()));
                }
            }
            info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }

}

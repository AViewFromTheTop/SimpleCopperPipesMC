package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.registry.RegisterSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/HoeItem;TILLABLES:Ljava/util/Map;", opcode = Opcodes.GETSTATIC, ordinal = 0),
            method = "useOn",
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void simpleCopperPipes$useOn(UseOnContext itemUsageContext, CallbackInfoReturnable<InteractionResult> info, Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.getBlock() instanceof CopperPipe) {
            Player playerEntity = itemUsageContext.getPlayer();
            ItemStack itemStack = itemUsageContext.getItemInHand();
            if (playerEntity instanceof ServerPlayer player) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, blockPos, itemStack);
            }

            Direction face = itemUsageContext.getClickedFace();
            if (face != blockState.getValue(CopperPipe.FACING)) {
                BlockState state = blockState.setValue(CopperPipe.FACING, face)
                        .setValue(CopperPipe.BACK_CONNECTED, CopperPipe.canConnectBack(level, blockPos, face))
                        .setValue(CopperPipe.FRONT_CONNECTED, CopperPipe.canConnectFront(level, blockPos, face))
                        .setValue(CopperPipe.SMOOTH, CopperPipe.isSmooth(level, blockPos, face));

                level.setBlockAndUpdate(blockPos, state);
                level.playSound(null, blockPos, RegisterSoundEvents.TURN, SoundSource.BLOCKS, 0.5F, 1F);
                if (playerEntity != null) {
                    itemUsageContext.getItemInHand().hurtAndBreak(1, playerEntity, LivingEntity.getSlotForHand(itemUsageContext.getHand()));
                }
            }
            info.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }

}

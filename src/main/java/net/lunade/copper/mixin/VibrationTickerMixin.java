package net.lunade.copper.mixin;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VibrationSystem.Ticker.class)
public interface VibrationTickerMixin {

    @Inject(at = @At("HEAD"), method = "method_51408", cancellable = true)
    private static void simpleCopperPipes$trySelectAndScheduleVibration(VibrationSystem.Data data, VibrationSystem.User user, ServerLevel serverLevel, VibrationInfo vibrationInfo, CallbackInfo info) {
        if (user instanceof CopperPipeEntity.VibrationUser) {
            info.cancel();
            data.setCurrentVibration(vibrationInfo);
            data.setTravelTimeInTicks(user.calculateTravelTimeInTicks(vibrationInfo.distance()));
            user.onDataChanged();
            data.getSelectionStrategy().startOver();
        }
    }

    @Inject(at = @At("HEAD"), method = "tryReloadVibrationParticle", cancellable = true)
    private static void simpleCopperPipes$tryReloadVibrationParticle(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user, CallbackInfo info) {
        if (user instanceof CopperPipeEntity.VibrationUser) {
            info.cancel();
        }
    }

}

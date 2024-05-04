package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.lunade.copper.blocks.block_entity.CopperPipeEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VibrationSystem.Ticker.class)
public interface VibrationTickerMixin {

    @WrapWithCondition(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"),
            method = "method_51408"
    )
    private static boolean simpleCopperPipes$trySelectAndScheduleVibration(
            ServerLevel serverLevel, ParticleOptions particleOptions, double a, double b, double c, int d, double e, double f, double g, double h,
            VibrationSystem.Data data, VibrationSystem.User user
    ) {
        return !(user instanceof CopperPipeEntity.VibrationUser);
    }

    @Inject(at = @At("HEAD"), method = "tryReloadVibrationParticle", cancellable = true)
    private static void simpleCopperPipes$tryReloadVibrationParticle(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user, CallbackInfo info) {
        if (user instanceof CopperPipeEntity.VibrationUser) {
            info.cancel();
        }
    }

}

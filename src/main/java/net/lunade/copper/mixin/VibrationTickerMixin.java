package net.lunade.copper.mixin;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Optional;

@Mixin(VibrationSystem.Ticker.class)
public class VibrationTickerMixin {

    @Unique
    private static final BlockPositionSource simpleCopperPipes$hiddenBlockSource = new BlockPositionSource(new BlockPos(0, -99999, 0));

    @Unique
    private static boolean simpleCopperPipes$isPipeUser;


    @Inject(at = @At("HEAD"), method = "method_51408")
    private static void simpleCopperPipes$trySelectAndScheduleVibration(VibrationSystem.Data data, VibrationSystem.User user, ServerLevel serverLevel, VibrationInfo vibrationInfo, CallbackInfo info) {
        simpleCopperPipes$isPipeUser = user instanceof CopperPipeEntity.VibrationUser;
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/particles/VibrationParticleOption;<init>(Lnet/minecraft/world/level/gameevent/PositionSource;I)V"), method = "method_51408")
    private static void simpleCopperPipes$hideVibrationOptionSource(Args args) {
        if (simpleCopperPipes$isPipeUser) {
            args.set(0, simpleCopperPipes$hiddenBlockSource);
        }
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"), method = "method_51408")
    private static void simpleCopperPipes$hideVibrationDestination(Args args) {
        if (simpleCopperPipes$isPipeUser) {
            args.set(1, 0D);
            args.set(2, -99999D);
            args.set(3, 0D);
        }
    }


    @Inject(at = @At("HEAD"), method = "tryReloadVibrationParticle", cancellable = true)
    private static void simpleCopperPipes$tryReloadVibrationParticle(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user, CallbackInfo info) {
        if (user instanceof CopperPipeEntity.VibrationUser) {
            info.cancel();
        }
    }

}

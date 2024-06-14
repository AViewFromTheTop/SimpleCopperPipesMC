package net.lunade.copper.mixin;

import java.util.Optional;
import net.lunade.copper.blocks.block_entity.CopperPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(VibrationSystem.Listener.class)
public class VibrationListenerMixin {

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/gameevent/vibrations/VibrationSystem$Listener;scheduleVibration(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/gameevent/vibrations/VibrationSystem$Data;Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/gameevent/GameEvent$Context;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;)V",
                    shift = At.Shift.AFTER
            ),
            method = "handleGameEvent",
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void simpleCopperPipes$handleGameEvent(
            ServerLevel serverLevel, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 vec3, CallbackInfoReturnable<Boolean> info,
            VibrationSystem.Data data, VibrationSystem.User user, Optional optional, Vec3 vec32
    ) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(BlockPos.containing(vec3));
        if (blockEntity instanceof CopperPipeEntity pipeEntity) {
            if (pipeEntity.inputGameEventPos != null && pipeEntity.gameEventNbtVec3 != null && pipeEntity.noteBlockCooldown <= 0) {
                serverLevel.sendParticles(new VibrationParticleOption(new BlockPositionSource(pipeEntity.inputGameEventPos), 5), pipeEntity.gameEventNbtVec3.x(), pipeEntity.gameEventNbtVec3.y(), pipeEntity.gameEventNbtVec3.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                pipeEntity.inputGameEventPos = null;
                pipeEntity.gameEventNbtVec3 = null;
            }
        }
    }

}

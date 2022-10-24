package net.lunade.copper.mixin;

import net.lunade.copper.RegisterPipeNbtMethods;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.SculkSensorListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkSensorListener.class)
public class VibrationListenerMixin {

    @Inject(at = @At("RETURN"), method = "listen")
    public void listen(World world, GameEvent gameEvent, @Nullable Entity entity, BlockPos blockPos, CallbackInfoReturnable<Boolean> infoReturnable) {
        if (infoReturnable.getReturnValue() && world instanceof ServerWorld serverWorld) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity pipeEntity) {
                if (pipeEntity.inputGameEventPos != null && pipeEntity.gameEventNbtVec3 != null && pipeEntity.noteBlockCooldown <= 0) {
                    RegisterPipeNbtMethods.spawnDelayedVibration(serverWorld, new BlockPos(pipeEntity.gameEventNbtVec3), pipeEntity.inputGameEventPos, 5);
                    pipeEntity.inputGameEventPos = null;
                    pipeEntity.gameEventNbtVec3 = null;
                }
            }
        }
    }

}

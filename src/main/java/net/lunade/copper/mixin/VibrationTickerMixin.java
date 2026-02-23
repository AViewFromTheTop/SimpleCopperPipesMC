package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VibrationSystem.Ticker.class)
public interface VibrationTickerMixin {

	@Inject(method = "lambda$trySelectAndScheduleVibration$0", at = @At("TAIL"))
	private static void simpleCopperPipes$trySelectAndScheduleVibration(
		VibrationSystem.Data data, VibrationSystem.User user, ServerLevel level, VibrationInfo vibrationInfo, CallbackInfo info,
		@Local(name = "origin") Vec3 pos
	) {
		if (!(level.getBlockEntity(BlockPos.containing(pos)) instanceof CopperPipeBlockEntity pipeEntity)) return;
		if (pipeEntity.inputGameEventPos == null || pipeEntity.gameEventNbtVec3 == null || pipeEntity.noteBlockCooldown > 0) return;
		level.sendParticles(
			new VibrationParticleOption(new BlockPositionSource(pipeEntity.inputGameEventPos), 5),
			pipeEntity.gameEventNbtVec3.x(), pipeEntity.gameEventNbtVec3.y(), pipeEntity.gameEventNbtVec3.z(),
			1, 0D, 0D, 0D, 0D
		);
		pipeEntity.inputGameEventPos = null;
		pipeEntity.gameEventNbtVec3 = null;
	}

}

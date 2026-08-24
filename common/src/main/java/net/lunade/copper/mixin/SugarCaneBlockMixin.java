package net.lunade.copper.mixin;

import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {

	@Inject(
		method = "canSurvive",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;",
			ordinal = 1,
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	public void simpleCopperPipes$canSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		if (LeakingPipeManager.isWaterPipeNearbyBlockGetter(level, pos, 3)) info.setReturnValue(true);
	}

}

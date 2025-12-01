package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CoralBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CoralBlock.class)
public class CoralBlockBlockMixin {

	@ModifyReturnValue(method = "scanForWater", at = @At("TAIL"))
	protected boolean simpleCopperPipes$isInWater(boolean original, BlockGetter level, BlockPos pos) {
		return original || LeakingPipeManager.isWaterPipeNearbyBlockGetter(level, pos, 2);
	}

}

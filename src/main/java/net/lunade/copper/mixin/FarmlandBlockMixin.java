package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin {

	@ModifyReturnValue(method = "isNearWater", at = @At("RETURN"))
	private static boolean simpleCopperPipes$isNearWater(boolean original, LevelReader level, BlockPos pos) {
		return original || LeakingPipeManager.isWaterPipeNearbyBlockGetter(level, pos, 6);
	}

}

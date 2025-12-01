package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCoralPlantTypeBlock.class)
public class CoralParentBlockMixin {

	@ModifyReturnValue(method = "scanForWater", at = @At("TAIL"))
	private static boolean simpleCopperPipes$isInWater(boolean original, BlockState state, BlockGetter level, BlockPos pos) {
		return original || LeakingPipeManager.isWaterPipeNearbyBlockGetter(level, pos, 2);
	}

}

package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.copper.blocks.block_entity.leaking_pipes.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCoralPlantTypeBlock.class)
public class CoralParentBlockMixin {

    @ModifyReturnValue(at = @At("TAIL"), method = "scanForWater")
    private static boolean simpleCopperPipes$isInWater(boolean original, BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        return original || LeakingPipeManager.isWaterPipeNearbyBlockGetter(blockView, blockPos, 2);
    }

}

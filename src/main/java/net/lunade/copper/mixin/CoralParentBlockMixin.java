package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCoralPlantTypeBlock.class)
public class CoralParentBlockMixin {

    @Inject(at = @At("TAIL"), method = "scanForWater", cancellable = true)
    private static void simpleCopperPipes$isInWater(BlockState blockState, BlockGetter blockView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (LeakingPipeManager.isWaterPipeNearbyBlockGetter(blockView, blockPos, 2)) {
            info.setReturnValue(true);
        }
    }

}

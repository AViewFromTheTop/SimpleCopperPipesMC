package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.block.CoralBlockBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CoralBlockBlock.class)
public class CoralBlockBlockMixin {

    @Inject(at = @At("TAIL"), method = "isInWater", cancellable = true)
    protected void isInWater(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (LeakingPipeManager.isWaterPipeNearbyBlockGetter(blockView, blockPos, 2)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}

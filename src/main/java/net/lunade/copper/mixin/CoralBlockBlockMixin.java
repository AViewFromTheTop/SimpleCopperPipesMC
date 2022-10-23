package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CoralBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CoralBlock.class)
public class CoralBlockBlockMixin {

    @Inject(at = @At("TAIL"), method = "scanForWater", cancellable = true)
    protected void isInWater(BlockGetter blockView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (CopperPipe.isWaterPipeNearby(blockView, blockPos, 2)) {
            info.setReturnValue(true);
        }
    }
}

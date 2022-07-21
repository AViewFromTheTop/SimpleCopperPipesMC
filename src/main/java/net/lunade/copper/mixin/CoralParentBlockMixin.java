package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CoralParentBlock.class)
public class CoralParentBlockMixin {

    @Inject(at = @At("TAIL"), method = "isInWater", cancellable = true)
    private static void isInWater(BlockState blockState, BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (CopperPipe.isWaterPipeNearby(blockView, blockPos, 2)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

}

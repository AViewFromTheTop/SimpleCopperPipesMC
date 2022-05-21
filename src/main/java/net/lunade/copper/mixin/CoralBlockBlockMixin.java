package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.block.CoralBlockBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(CoralBlockBlock.class)
public class CoralBlockBlockMixin {
    /** Check if leaking pipe is nearby
     * @author Mojang (original code) Lunade (added leaking pipe check)
     * @reason actually make pipes work */
    @Inject(at = @At("TAIL"), method = "isInWater")
    protected boolean isInWater(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable info) {
        return CopperPipe.isWaterPipeNearby(blockView, blockPos, 2);
    }
}

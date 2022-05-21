package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {

    @Inject(at = @At("TAIL"), method = "isWaterNearby")
    private static boolean isWaterNearby(WorldView worldView, BlockPos blockPos, CallbackInfoReturnable info) {
        return CopperPipe.isWaterPipeNearby(worldView, blockPos, 6);
    }

}

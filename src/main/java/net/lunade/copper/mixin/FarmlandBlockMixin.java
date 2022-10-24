package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {

    @Inject(at = @At("TAIL"), method = "isWaterNearby", cancellable = true)
    private static void isWaterNearby(WorldView worldView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            info.setReturnValue(LeakingPipeManager.isWaterPipeNearbyBlockGetter(worldView, blockPos, 6));
            info.cancel();
        }
    }

}

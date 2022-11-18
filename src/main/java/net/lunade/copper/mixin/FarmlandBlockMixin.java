package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin {

    @Inject(at = @At("RETURN"), method = "isNearWater", cancellable = true)
    private static void isNearWater(LevelReader worldView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue() && LeakingPipeManager.isWaterPipeNearbyBlockGetter(worldView, blockPos, 6)) {
            info.setReturnValue(true);
        }
    }

}

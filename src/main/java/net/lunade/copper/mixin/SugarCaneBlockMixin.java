package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos", shift = At.Shift.AFTER), method = "canSurvive", cancellable = true)
    public void canSurvive(BlockState blockState, LevelReader worldView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (LeakingPipeManager.isWaterPipeNearbyBlockGetter(worldView, blockPos, 3)) {
            info.setReturnValue(true);
        }
    }

}

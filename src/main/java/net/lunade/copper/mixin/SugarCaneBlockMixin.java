package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;down()Lnet/minecraft/util/math/BlockPos;", ordinal = 1, shift = At.Shift.AFTER), method = "canPlaceAt", cancellable = true)
    public void canPlaceAt(BlockState blockState, WorldView worldView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (LeakingPipeManager.isWaterPipeNearbyBlockGetter(worldView, blockPos, 3)) {
            info.setReturnValue(true);
        }
    }
}

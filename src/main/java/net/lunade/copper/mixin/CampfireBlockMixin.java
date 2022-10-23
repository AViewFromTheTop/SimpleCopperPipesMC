package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Inject(at = @At("TAIL"), method = "isSmokeyPos", cancellable = true)
    private static void isSmokeyPos(Level world, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        for(int i = 1; i <= 5; ++i) {
            BlockPos blockPos2 = blockPos.below(i);
            BlockState blockState = world.getBlockState(blockPos2);
            if (blockState.getBlock() instanceof CopperPipe) {
                if (blockState.getValue(CopperPipeProperties.HAS_SMOKE)) {
                    info.setReturnValue(true);
                }
            }
        }
    }

}

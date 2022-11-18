package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Inject(at = @At("TAIL"), method = "isLitCampfireInRange", cancellable = true)
    private static void isLitCampfireInRange(World world, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        for(int i = 1; i <= 5; ++i) {
            BlockPos blockPos2 = blockPos.down(i);
            BlockState blockState = world.getBlockState(blockPos2);
            if (blockState.getBlock() instanceof CopperPipe && blockState.get(CopperPipeProperties.HAS_SMOKE)) {
                info.setReturnValue(true);
            }
        }
    }

}

package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {

    @Inject(at = @At("TAIL"), method = "canSurvive", cancellable = true)
    public void canSurvive(BlockState blockState, LevelReader worldView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        BlockState blockState2 = worldView.getBlockState(blockPos.below());
        if (blockState2.is(Blocks.SUGAR_CANE)) {
            info.setReturnValue(true);
            info.cancel();
        } else if (blockState2.is(BlockTags.DIRT) || blockState2.is(Blocks.SAND) || blockState2.is(Blocks.RED_SAND)) {
            info.setReturnValue(CopperPipe.isWaterPipeNearby(worldView, blockPos, 3));
            info.cancel();
        }
    }
}

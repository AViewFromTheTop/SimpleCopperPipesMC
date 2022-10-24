package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {

    @Inject(at = @At("TAIL"), method = "canPlaceAt", cancellable = true)
    public void canPlaceAt(BlockState blockState, WorldView worldView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        BlockState blockState2 = worldView.getBlockState(blockPos.down());
        if (blockState2.isOf(Blocks.SUGAR_CANE)) {
            info.setReturnValue(true);
            info.cancel();
        } else if (blockState2.isIn(BlockTags.DIRT) || blockState2.isOf(Blocks.SAND) || blockState2.isOf(Blocks.RED_SAND)) {
            info.setReturnValue(LeakingPipeManager.isWaterPipeNearbyBlockGetter(worldView, blockPos, 3));
            info.cancel();
        }
    }
}

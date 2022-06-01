package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
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
    /** Check if leaking pipe is nearby
     * @author Mojang (original code) Lunade (added leaking pipe check)
     * @reason actually make pipes work */
    @Inject(at = @At("TAIL"), method = "canPlaceAt")
    public boolean canPlaceAt(BlockState blockState, WorldView worldView, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        BlockState blockState2 = worldView.getBlockState(blockPos.down());
        if (blockState2.isOf(Blocks.SUGAR_CANE)) {
            return true;
        } else if (blockState2.isIn(BlockTags.DIRT) || blockState2.isOf(Blocks.SAND) || blockState2.isOf(Blocks.RED_SAND)) {
            return CopperPipe.isWaterPipeNearby(worldView, blockPos, 3);
        }
        return info.getReturnValueZ();
    }
}

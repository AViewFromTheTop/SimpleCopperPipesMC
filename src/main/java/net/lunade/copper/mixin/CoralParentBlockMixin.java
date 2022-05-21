package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CoralParentBlock.class)
public class CoralParentBlockMixin {
    /** Check if leaking pipe is nearby
     * @author Mojang (original code) Lunade (added leaking pipe check)
     * @reason actually make pipes work */
    @Overwrite
    public static boolean isInWater(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        if (blockState.get(Properties.WATERLOGGED)) {
            return true;
        } else {
            Direction[] var3 = Direction.values();

            for (Direction direction : var3) {
                if (blockView.getFluidState(blockPos.offset(direction)).isIn(FluidTags.WATER)) {
                    return true;
                }
            }

            return CopperPipe.isWaterPipeNearby(blockView, blockPos, 2);
        }
    }
}

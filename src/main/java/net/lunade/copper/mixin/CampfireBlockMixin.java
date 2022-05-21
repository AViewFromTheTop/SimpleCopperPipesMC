package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static net.minecraft.block.CampfireBlock.isLitCampfire;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {
    private static final VoxelShape SMOKEY_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    /** Check if smoking pipe is nearby
     * @author Mojang (original code) Lunade (added smoking pipe check)
     * @reason actually make pipes work */
    @Overwrite
    public static boolean isLitCampfireInRange(World world, BlockPos blockPos) {
        for(int i = 1; i <= 5; ++i) {
            BlockPos blockPos2 = blockPos.down(i);
            BlockState blockState = world.getBlockState(blockPos2);
            if (isLitCampfire(blockState)) { return true; }
            if (blockState.getBlock() instanceof CopperPipe) {
                if (blockState.get(CopperPipeProperties.HAS_SMOKE)) {return true;}
            }
            if (VoxelShapes.matchesAnywhere(SMOKEY_SHAPE, blockState.getCollisionShape(world, blockPos, ShapeContext.absent()), BooleanBiFunction.AND)) {
                BlockState blockState2 = world.getBlockState(blockPos2.down());
                return isLitCampfire(blockState2);
            }
        } return false;
    }
}

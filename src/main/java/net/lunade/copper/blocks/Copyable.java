package net.lunade.copper.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface Copyable {

    void makeCopyOf(BlockState state, Level world, BlockPos blockPos, Block block);

    BlockState makeCopyOf(BlockState state, Block block);

}

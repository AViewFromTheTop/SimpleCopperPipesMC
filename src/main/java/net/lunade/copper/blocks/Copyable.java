package net.lunade.copper.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Copyable {

    void makeCopyOf(BlockState state, World world, BlockPos blockPos, Block block);

    BlockState makeCopyOf(BlockState state, Block block);

}
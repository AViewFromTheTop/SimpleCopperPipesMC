package net.lunade.copper.block_entity;

import net.lunade.copper.Main;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public class CopperFittingEntity extends AbstractSimpleCopperBlockEntity {

    public CopperFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(Main.COPPER_FITTING_ENTITY, blockPos, blockState, MoveablePipeDataHandler.MOVE_TYPE.FROM_FITTING);
    }

    @Override
    public void serverTick(World world, BlockPos blockPos, BlockState blockState) {
        super.serverTick(world, blockPos, blockState);
    }

    public boolean canAcceptMoveableNbt(MoveablePipeDataHandler.MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MoveablePipeDataHandler.MOVE_TYPE.FROM_FITTING) {
            return false;
        } else if (moveType == MoveablePipeDataHandler.MOVE_TYPE.FROM_PIPE) {
            return moveDirection == fromState.get(Properties.FACING);
        }
        return false;
    }

    public void updateBlockEntityValues(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            this.canWater = state.get(Properties.WATERLOGGED);
        }
    }

}
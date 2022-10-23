package net.lunade.copper.block_entity;

import net.lunade.copper.Main;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CopperFittingEntity extends AbstractSimpleCopperBlockEntity {

    public CopperFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(Main.COPPER_FITTING_ENTITY, blockPos, blockState, MoveablePipeDataHandler.MOVE_TYPE.FROM_FITTING);
    }

    @Override
    public void serverTick(Level world, BlockPos blockPos, BlockState blockState) {
        super.serverTick(world, blockPos, blockState);
    }

    public boolean canAcceptMoveableNbt(MoveablePipeDataHandler.MOVE_TYPE moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MoveablePipeDataHandler.MOVE_TYPE.FROM_FITTING) {
            return false;
        } else if (moveType == MoveablePipeDataHandler.MOVE_TYPE.FROM_PIPE) {
            return moveDirection == fromState.getValue(BlockStateProperties.FACING);
        }
        return false;
    }

    public void updateBlockEntityValues(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            this.canWater = state.getValue(BlockStateProperties.WATERLOGGED);
        }
    }

}

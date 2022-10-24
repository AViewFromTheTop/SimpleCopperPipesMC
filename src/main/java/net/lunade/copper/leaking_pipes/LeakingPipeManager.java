package net.lunade.copper.leaking_pipes;

import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;

public class LeakingPipeManager {

    private static final ArrayList<LeakingPipePos> leakingPipePosesOne = new ArrayList<>();
    private static final ArrayList<LeakingPipePos> leakingPipePosesTwo = new ArrayList<>();

    private static boolean isAlt;

    public static boolean isWaterPipeNearby(Level level, BlockPos blockPos, int i) {
        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getPoses().clone();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        ResourceLocation dimension = level.dimension().location();
        for (LeakingPipePos leakingPos : copiedList) {
            if (leakingPos.dimension.equals(dimension)) {
                int xVal = leakingPos.pos.getX() - x;
                if (xVal >= -i && xVal <= i) {
                    int zVal = leakingPos.pos.getZ() - z;
                    if (zVal >= -i && zVal <= i) {
                        int leakY = leakingPos.pos.getY();
                        return (y < leakY && y >= leakY - 12);
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWaterPipeNearbyBlockGetter(BlockGetter blockGetter, BlockPos blockPos, int i) {
        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getPoses().clone();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        BlockState state;
        for (LeakingPipePos leakingPos : copiedList) {
            int xVal = leakingPos.pos.getX() - x;
            if (xVal >= -i && xVal <= i) {
                int zVal = leakingPos.pos.getZ() - z;
                if (zVal >= -i && zVal <= i) {
                    int leakY = leakingPos.pos.getY();
                    if (y < leakY && y >= leakY - 12) {
                        state = blockGetter.getBlockState(leakingPos.pos);
                        if (state.getBlock() instanceof CopperPipe) {
                            return state.getValue(BlockStateProperties.FACING) != Direction.UP && state.getValue(CopperPipeProperties.HAS_WATER);
                        }
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<LeakingPipePos> getPoses() {
        return !isAlt ? leakingPipePosesOne : leakingPipePosesTwo;
    }

    public static ArrayList<LeakingPipePos> getAltList() {
        return isAlt ? leakingPipePosesOne : leakingPipePosesTwo;
    }

    public static void clear() {
        getPoses().clear();
    }

    public static void clearAll() {
        leakingPipePosesOne.clear();
        leakingPipePosesTwo.clear();
    }

    public static void clearAndSwitch() {
        clear();
        isAlt = !isAlt;
    }

    public static void addPos(Level level, BlockPos pos) {
        getAltList().add(new LeakingPipePos(pos, level.dimension().location()));
    }

    public record LeakingPipePos(BlockPos pos, ResourceLocation dimension) {

    }

}

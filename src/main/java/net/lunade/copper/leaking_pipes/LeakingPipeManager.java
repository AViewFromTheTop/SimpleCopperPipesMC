package net.lunade.copper.leaking_pipes;

import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;

public class LeakingPipeManager {

    private static final ArrayList<LeakingPipePos> leakingPipePosesOne = new ArrayList<>();
    private static final ArrayList<LeakingPipePos> leakingPipePosesTwo = new ArrayList<>();

    private static boolean isAlt;

    public static boolean isWaterPipeNearby(World level, BlockPos blockPos, int i) {
        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getPoses().clone();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        Identifier dimension = level.getRegistryKey().getValue();
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

    public static boolean isWaterPipeNearbyBlockGetter(BlockView blockGetter, BlockPos blockPos, int i) {
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
                            return state.get(Properties.FACING) != Direction.UP && state.get(CopperPipeProperties.HAS_WATER);
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

    public static void addPos(World level, BlockPos pos) {
        getAltList().add(new LeakingPipePos(pos, level.getRegistryKey().getValue()));
    }

    public record LeakingPipePos(BlockPos pos, Identifier dimension) {

    }

}

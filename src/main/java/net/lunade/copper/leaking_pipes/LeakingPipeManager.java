package net.lunade.copper.leaking_pipes;

import net.lunade.copper.CopperPipeMain;
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
        for (LeakingPipePos leakingPos : copiedList) {
            if (leakingPos.dimension.equals(level.dimension().location())) {
                int xVal = leakingPos.pos.getX() - blockPos.getX();
                if (xVal >= -i && xVal <= i) {
                    int zVal = leakingPos.pos.getZ() - blockPos.getZ();
                    if (zVal >= -i && zVal <= i) {
                        int y = leakingPos.pos.getY();
                        return (blockPos.getY() < y && blockPos.getY() >= y - 12);
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWaterPipeNearbyBlockGetter(BlockGetter blockGetter, BlockPos blockPos, int i) {
        int posX = blockPos.getX();
        int posY = blockPos.getY();
        int posZ = blockPos.getZ();
        int l;
        BlockState state;
        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getPoses().clone();
        for (LeakingPipePos leakingPos : copiedList) {
            l = leakingPos.pos.getX();
            if (posX > l - i && posX < l + i) {
                l = leakingPos.pos.getZ();
                if (posZ > l - i && posZ < l + i) {
                    l = leakingPos.pos.getY();
                    if (posY < l && posY >= l - 12) {
                        state = blockGetter.getBlockState(blockPos);
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
        getAltList().clear();
    }

    public static void clearAll() {
        leakingPipePosesOne.clear();
        leakingPipePosesTwo.clear();
    }

    public static void clearAndSwitch() {
        getAltList().clear();
        isAlt = !isAlt;
    }

    public static void addPos(Level level, BlockPos pos) {
        getPoses().add(new LeakingPipePos(pos, level.dimension().location()));
    }

    public record LeakingPipePos(BlockPos pos, ResourceLocation dimension) {

    }

}

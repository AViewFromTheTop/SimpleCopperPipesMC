package net.lunade.copper.leaking_pipes;

import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;

public class LeakingPipeManager {

    private static final ArrayList<LeakingPipePos> leakingPipePosesOne = new ArrayList<>();
    private static final ArrayList<LeakingPipePos> leakingPipePosesTwo = new ArrayList<>();

    private static boolean isAlt;

    public static boolean isWaterPipeNearby(Entity entity, int i) {
        ArrayList<LeakingPipePos> copiedList = (ArrayList<LeakingPipePos>) getPoses().clone();
        int x = entity.getBlockX();
        int y = entity.getBlockY();
        int z = entity.getBlockZ();
        Vec3d entityPos = entity.getEyePos();
        Identifier dimension = entity.world.getRegistryKey().getValue();
        BlockPos leakPos;
        for (LeakingPipePos leakingPos : copiedList) {
            if (leakingPos.dimension.equals(dimension)) {
                leakPos = leakingPos.pos;
                double xVal = leakPos.getX() - x;
                if (xVal >= -i && xVal <= i) {
                    double zVal = leakPos.getZ() - z;
                    if (zVal >= -i && zVal <= i) {
                        int leakY = leakPos.getY();
                        if (y < leakY && y >= leakY - 12) {
                            BlockHitResult hitResult = entity.world.raycast(new RaycastContext(entityPos, new Vec3d(leakPos.getX() + 0.5, leakPos.getY() + 0.5, leakPos.getZ() + 0.5), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
                            if (hitResult.getBlockPos().equals(leakPos)) {
                                return true;
                            }
                        }
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

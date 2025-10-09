package net.lunade.copper.block.entity.leaking;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.block.properties.PipeFluid;
import net.lunade.copper.registry.SimpleCopperPipesBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LeakingPipeManager {
	private static final ArrayList<LeakingPipePos> LEAK_POSES_A = new ArrayList<>();
	private static final ArrayList<LeakingPipePos> LEAK_POSES_B = new ArrayList<>();
	private static boolean IS_ALT_LIST;

	public static boolean isWaterPipeNearby(@NotNull Entity entity, int i) {
		final int x = entity.getBlockX();
		final int y = entity.getBlockY();
		final int z = entity.getBlockZ();
		final Vec3 entityPos = entity.getEyePosition();
		final ResourceLocation dimension = entity.level().dimension().location();

		BlockPos leakPos;
		for (LeakingPipePos leakingPos : ImmutableList.copyOf(getPoses())) {
			if (leakingPos.dimension.equals(dimension)) {
				leakPos = leakingPos.pos;
				double xVal = leakPos.getX() - x;
				if (xVal >= -i && xVal <= i) {
					double zVal = leakPos.getZ() - z;
					if (zVal >= -i && zVal <= i) {
						int leakY = leakPos.getY();
						if (y < leakY && y >= leakY - 12) {
							BlockHitResult hitResult = entity.level().clip(
								new ClipContext(
									entityPos,
									Vec3.atCenterOf(leakPos),
									ClipContext.Block.COLLIDER,
									ClipContext.Fluid.NONE, entity)
							);
							if (hitResult.getBlockPos().equals(leakPos)) return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean isWaterPipeNearbyBlockGetter(BlockGetter blockGetter, @NotNull BlockPos blockPos, int i) {
		final int x = blockPos.getX();
		final int y = blockPos.getY();
		final int z = blockPos.getZ();

		BlockState state;
		for (LeakingPipePos leakingPos : ImmutableList.copyOf(getPoses())) {
			int xVal = leakingPos.pos.getX() - x;
			if (xVal >= -i && xVal <= i) {
				int zVal = leakingPos.pos.getZ() - z;
				if (zVal >= -i && zVal <= i) {
					int leakY = leakingPos.pos.getY();
					if (y < leakY && y >= leakY - 12) {
						state = blockGetter.getBlockState(leakingPos.pos);
						if (!(state.getBlock() instanceof CopperPipeBlock)) continue;
						return state.getValue(BlockStateProperties.FACING) != Direction.UP && state.getValue(SimpleCopperPipesBlockStateProperties.FLUID) == PipeFluid.WATER;
					}
				}
			}
		}
		return false;
	}

	public static ArrayList<LeakingPipePos> getPoses() {
		return !IS_ALT_LIST ? LEAK_POSES_A : LEAK_POSES_B;
	}

	public static ArrayList<LeakingPipePos> getAltList() {
		return IS_ALT_LIST ? LEAK_POSES_A : LEAK_POSES_B;
	}

	public static void clear() {
		getPoses().clear();
	}

	public static void clearAll() {
		LEAK_POSES_A.clear();
		LEAK_POSES_B.clear();
	}

	public static void clearAndSwitch() {
		clear();
		IS_ALT_LIST = !IS_ALT_LIST;
	}

	public static void addPos(@NotNull Level level, BlockPos pos) {
		getAltList().add(new LeakingPipePos(pos, level.dimension().location()));
	}

	public record LeakingPipePos(BlockPos pos, ResourceLocation dimension) {
	}

}

package net.lunade.copper.block.entity.leaking;

import java.util.HashMap;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class LeakingPipeDripBehaviors {
	private static final Map<Block, DripOn> BLOCKS_TO_DRIPS = new Object2ObjectLinkedOpenHashMap<>();

	public static void register(Block block, DripOn drip) {
		BLOCKS_TO_DRIPS.put(block, drip);
	}

	@Nullable
	public static DripOn getDrip(Block block) {
		return BLOCKS_TO_DRIPS.get(block);
	}

	public static void init() {
		register(Blocks.CAULDRON, ((lava, world, pos, state) -> {
			if (!lava) {
				world.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 1));
			} else {
				world.setBlockAndUpdate(pos, Blocks.LAVA_CAULDRON.defaultBlockState());
			}
		}));

		register(Blocks.WATER_CAULDRON, ((lava, world, pos, state) -> {
			if (state.getValue(BlockStateProperties.LEVEL_CAULDRON) != 3 && !lava) {
				world.setBlockAndUpdate(pos, state.cycle(BlockStateProperties.LEVEL_CAULDRON));
			}
		}));

		register(Blocks.DIRT, ((lava, world, pos, state) -> {
			if (!lava) world.setBlockAndUpdate(pos, Blocks.MUD.defaultBlockState());
		}));

		register(Blocks.FIRE, ((lava, world, pos, state) -> {
			if (!lava) world.destroyBlock(pos, true);
		}));
	}

	@FunctionalInterface
	public interface DripOn {
		void dripOn(boolean lava, ServerLevel world, BlockPos pos, BlockState state);
	}

}

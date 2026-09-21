/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Simple Copper Pipes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

package net.lunade.copper.block.entity.leaking;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Map;
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
		register(Blocks.CAULDRON, ((lava, level, pos, state) -> {
			if (!lava) {
				level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 1));
			} else {
				level.setBlockAndUpdate(pos, Blocks.LAVA_CAULDRON.defaultBlockState());
			}
		}));

		register(Blocks.WATER_CAULDRON, ((lava, level, pos, state) -> {
			if (state.getValue(BlockStateProperties.LEVEL_CAULDRON) != 3 && !lava) {
				level.setBlockAndUpdate(pos, state.cycle(BlockStateProperties.LEVEL_CAULDRON));
			}
		}));

		register(Blocks.DIRT, ((lava, level, pos, state) -> {
			if (!lava) level.setBlockAndUpdate(pos, Blocks.MUD.defaultBlockState());
		}));

		register(Blocks.FIRE, ((lava, level, pos, state) -> {
			if (!lava) level.destroyBlock(pos, true);
		}));
	}

	@FunctionalInterface
	public interface DripOn {
		void dripOn(boolean lava, ServerLevel level, BlockPos pos, BlockState state);
	}
}

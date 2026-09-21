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

package net.lunade.copper.tag;

import net.lunade.copper.SCPConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class SCPBlockTags {
	public static final TagKey<Block> SILENT_COPPER_PIPES = bind("silent_copper_pipes");
	public static final TagKey<Block> COPPER_PIPE_CHECKS_SUPPORT_SHAPE = bind("copper_pipe_checks_support_shape");

	private static TagKey<Block> bind(String name) {
		return TagKey.create(Registries.BLOCK, SCPConstants.id(name));
	}

	private SCPBlockTags() {}
}

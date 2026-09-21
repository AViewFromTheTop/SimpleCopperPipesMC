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
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTagId;

public final class SCPBlockItemTags {
	public static final BlockItemTagId COPPER_PIPES = bind("copper_pipes");
	public static final BlockItemTagId COPPER_FITTINGS = bind("copper_fittings");

	private static BlockItemTagId bind(String name) {
		final Identifier id = SCPConstants.id(name);
		return BlockItemTagId.create(id, id);
	}

	private SCPBlockItemTags() {}
}

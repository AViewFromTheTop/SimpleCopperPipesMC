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

package net.lunade.copper.references;

import net.lunade.copper.SCPConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class SCPBlockEntityTypeIDs {
	public static final ResourceKey<BlockEntityType<?>> COPPER_PIPE = create("copper_pipe");
	public static final ResourceKey<BlockEntityType<?>> COPPER_FITTING = create("copper_fitting");

	private static ResourceKey<BlockEntityType<?>> create(String name) {
		return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, SCPConstants.id(name));
	}

	private SCPBlockEntityTypeIDs() {}
}

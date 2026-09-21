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
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public final class SCPBlockItemIDs {
	public static final WeatheringCopperCollection<BlockItemId> COPPER_PIPE = createSimpleCopper("copper_pipe");
	public static final WeatheringCopperCollection<BlockItemId> COPPER_FITTING = createSimpleCopper("copper_fitting");

	private static BlockItemId create(String name) {
		final Identifier id = SCPConstants.id(name);
		return BlockItemId.create(id, id);
	}

	private static WeatheringCopperCollection<BlockItemId> createSimpleCopper(String name) {
		return WeatheringCopperCollection.prefixWithState(WeatheringCopperCollection.create(name)).map(SCPBlockItemIDs::create);
	}

	private SCPBlockItemIDs() {}
}

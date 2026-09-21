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

package net.lunade.copper.registry;

import net.frozenblock.lib.platform.api.registry.DeferredItem;
import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.lunade.copper.SCPConstants;
import net.lunade.copper.references.SCPBlockItemIDs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public final class SCPItems {
	private static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(SCPConstants.MOD_ID);

	public static final WeatheringCopperCollection<DeferredItem<BlockItem>> COPPER_PIPE = REGISTER.registerSimpleWeatheringCopperCollection(
		SCPBlockItemIDs.COPPER_PIPE,
		SCPBlocks.COPPER_PIPE
	);

	public static final WeatheringCopperCollection<DeferredItem<BlockItem>> COPPER_FITTING = REGISTER.registerSimpleWeatheringCopperCollection(
		SCPBlockItemIDs.COPPER_FITTING,
		SCPBlocks.COPPER_FITTING
	);

	static {
		REGISTER.register();
	}

	public static void init() {}

	private SCPItems() {}
}

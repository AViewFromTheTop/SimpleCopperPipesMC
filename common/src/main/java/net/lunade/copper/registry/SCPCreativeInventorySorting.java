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

import net.frozenblock.lib.item.api.creative.CreativeModeTabSorter;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public final class SCPCreativeInventorySorting {

	public static void setup() {
		WeatheringCopperCollection.zipApply(Items.COPPER_BULB, SCPItems.COPPER_PIPE, SCPCreativeInventorySorting::addInBuildingBlocksAfter);
		WeatheringCopperCollection.zipApply(Items.COPPER_BULB.waxed(), SCPItems.COPPER_PIPE.waxed(), SCPCreativeInventorySorting::addInRedstoneAfter);
		WeatheringCopperCollection.zipApply(SCPItems.COPPER_PIPE, SCPItems.COPPER_FITTING, SCPCreativeInventorySorting::addInBuildingBlocksAfter);
		WeatheringCopperCollection.zipApply(SCPItems.COPPER_PIPE.waxed(), SCPItems.COPPER_FITTING.waxed(), SCPCreativeInventorySorting::addInRedstoneAfter);
	}

	private static void addInRedstoneAfter(ItemLike comparedItem, ItemLike item) {
		CreativeModeTabSorter.insertAfter(comparedItem, item, CreativeModeTabs.REDSTONE_BLOCKS);
	}

	private static void addInBuildingBlocksAfter(ItemLike comparedItem, ItemLike item) {
		CreativeModeTabSorter.insertAfter(comparedItem, item, CreativeModeTabs.BUILDING_BLOCKS);
	}

	private SCPCreativeInventorySorting() {}
}

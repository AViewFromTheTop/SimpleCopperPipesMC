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

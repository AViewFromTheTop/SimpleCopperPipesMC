package net.lunade.copper.registry;

import net.frozenblock.lib.item.api.creative.CreativeModeTabSorter;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public class SimpleCopperPipesCreativeInventorySorting {

	public static void setup() {
		WeatheringCopperCollection.zipApply(Items.COPPER_BULB, SimpleCopperPipesItems.COPPER_PIPE, SimpleCopperPipesCreativeInventorySorting::addInBuildingBlocksAfter);
		WeatheringCopperCollection.zipApply(Items.COPPER_BULB.waxed(), SimpleCopperPipesItems.COPPER_PIPE.waxed(), SimpleCopperPipesCreativeInventorySorting::addInRedstoneAfter);
		WeatheringCopperCollection.zipApply(SimpleCopperPipesItems.COPPER_PIPE, SimpleCopperPipesItems.COPPER_FITTING, SimpleCopperPipesCreativeInventorySorting::addInBuildingBlocksAfter);
		WeatheringCopperCollection.zipApply(SimpleCopperPipesItems.COPPER_PIPE.waxed(), SimpleCopperPipesItems.COPPER_FITTING.waxed(), SimpleCopperPipesCreativeInventorySorting::addInRedstoneAfter);
	}

	private static void addInRedstoneAfter(ItemLike comparedItem, ItemLike item) {
		CreativeModeTabSorter.insertAfter(comparedItem, item, CreativeModeTabs.REDSTONE_BLOCKS);
	}

	private static void addInBuildingBlocksAfter(ItemLike comparedItem, ItemLike item) {
		CreativeModeTabSorter.insertAfter(comparedItem, item, CreativeModeTabs.BUILDING_BLOCKS);
	}
}

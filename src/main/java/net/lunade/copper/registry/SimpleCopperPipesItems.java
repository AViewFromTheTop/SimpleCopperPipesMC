package net.lunade.copper.registry;

import net.lunade.copper.references.SimpleCopperPipesBlockItemIDs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public final class SimpleCopperPipesItems {
	public static final WeatheringCopperCollection<Item> COPPER_PIPE = WeatheringCopperCollection.registerItems(
		SimpleCopperPipesBlockItemIDs.COPPER_PIPE,
		SimpleCopperPipesBlocks.COPPER_PIPE,
		Items::registerBlock
	);

	public static final WeatheringCopperCollection<Item> COPPER_FITTING = WeatheringCopperCollection.registerItems(
		SimpleCopperPipesBlockItemIDs.COPPER_FITTING,
		SimpleCopperPipesBlocks.COPPER_FITTING,
		Items::registerBlock
	);

	public static void init() {}
}

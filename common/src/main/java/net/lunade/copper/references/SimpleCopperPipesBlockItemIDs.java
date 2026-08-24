package net.lunade.copper.references;

import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public class SimpleCopperPipesBlockItemIDs {
	public static final WeatheringCopperCollection<BlockItemId> COPPER_PIPE = createSimpleCopper("copper_pipe");
	public static final WeatheringCopperCollection<BlockItemId> COPPER_FITTING = createSimpleCopper("copper_fitting");

	private static BlockItemId create(String name) {
		final Identifier id = SimpleCopperPipesConstants.id(name);
		return BlockItemId.create(id, id);
	}

	private static WeatheringCopperCollection<BlockItemId> createSimpleCopper(String name) {
		return WeatheringCopperCollection.prefixWithState(WeatheringCopperCollection.create(name)).map(SimpleCopperPipesBlockItemIDs::create);
	}
}

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

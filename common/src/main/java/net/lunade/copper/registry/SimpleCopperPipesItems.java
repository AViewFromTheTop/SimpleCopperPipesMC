package net.lunade.copper.registry;

import net.frozenblock.lib.platform.api.registry.DeferredItem;
import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.references.SimpleCopperPipesBlockItemIDs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public final class SimpleCopperPipesItems {
	private static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(SimpleCopperPipesConstants.MOD_ID);

	public static final WeatheringCopperCollection<DeferredItem<BlockItem>> COPPER_PIPE = REGISTER.registerSimpleWeatheringCopperCollection(
		SimpleCopperPipesBlockItemIDs.COPPER_PIPE,
		SimpleCopperPipesBlocks.COPPER_PIPE
	);

	public static final WeatheringCopperCollection<DeferredItem<BlockItem>> COPPER_FITTING = REGISTER.registerSimpleWeatheringCopperCollection(
		SimpleCopperPipesBlockItemIDs.COPPER_FITTING,
		SimpleCopperPipesBlocks.COPPER_FITTING
	);

	static {
		REGISTER.register();
	}

	public static void init() {}
}

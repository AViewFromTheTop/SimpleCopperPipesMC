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

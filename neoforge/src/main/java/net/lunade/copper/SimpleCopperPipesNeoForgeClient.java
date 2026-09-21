package net.lunade.copper;

import net.frozenblock.lib.FrozenBools;
import net.lunade.copper.config.gui.SCPConfigGui;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SCPConstants.MOD_ID, dist = Dist.CLIENT)
public final class SimpleCopperPipesNeoForgeClient {

	public SimpleCopperPipesNeoForgeClient(IEventBus modBus) {
		SimpleCopperPipesClient.init();

		// AFTER register event
		modBus.addListener(FMLClientSetupEvent.class, event -> {
			SimpleCopperPipesClient.setup();
		});

		if (FrozenBools.HAS_CLOTH_CONFIG) {
			ModLoadingContext.get().registerExtensionPoint(
				IConfigScreenFactory.class,
				() -> (container, parent) -> SCPConfigGui.buildScreen(parent)
			);
		}
	}
}

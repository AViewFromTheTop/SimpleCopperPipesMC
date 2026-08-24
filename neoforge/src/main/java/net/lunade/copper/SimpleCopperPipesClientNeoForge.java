package net.lunade.copper;

import net.lunade.copper.config.gui.SimpleCopperPipesConfigGui;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SimpleCopperPipesConstants.MOD_ID, dist = Dist.CLIENT)
public class SimpleCopperPipesClientNeoForge {

	public SimpleCopperPipesClientNeoForge(IEventBus modBus) {
		SimpleCopperPipesClient.init();

		// SETUP
		modBus.addListener(FMLClientSetupEvent.class, event -> {
			SimpleCopperPipesClient.setup();
		});

		// add config gui to mod list screen
		ModLoadingContext.get().registerExtensionPoint(
			IConfigScreenFactory.class,
			() -> (container, parent) ->
				SimpleCopperPipesConfigGui.buildScreen(parent)
		);
	}
}

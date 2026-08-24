package net.lunade.copper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(SimpleCopperPipesConstants.MOD_ID)
public class SimpleCopperPipesNeoForge {

	public SimpleCopperPipesNeoForge(IEventBus modBus) {
		SimpleCopperPipes.init();

		modBus.addListener(FMLCommonSetupEvent.class, event -> {
			SimpleCopperPipes.setup();
		});
	}
}

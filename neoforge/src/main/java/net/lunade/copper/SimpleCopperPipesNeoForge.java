package net.lunade.copper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(SCPConstants.MOD_ID)
public final class SimpleCopperPipesNeoForge {

	public SimpleCopperPipesNeoForge(IEventBus modBus) {
		SimpleCopperPipes.init();

		// AFTER register event
		modBus.addListener(FMLCommonSetupEvent.class, event -> {
			SimpleCopperPipes.setup();
		});
	}
}

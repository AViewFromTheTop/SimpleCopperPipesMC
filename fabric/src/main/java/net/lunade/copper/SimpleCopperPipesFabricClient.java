package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class SimpleCopperPipesFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SimpleCopperPipesClient.init();
		SimpleCopperPipesClient.setup();
	}
}

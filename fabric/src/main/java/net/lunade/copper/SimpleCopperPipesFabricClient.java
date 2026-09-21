package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SimpleCopperPipesFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SimpleCopperPipesClient.init();
		SimpleCopperPipesClient.setup();
	}
}

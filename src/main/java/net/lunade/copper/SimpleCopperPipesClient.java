package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.copper.networking.SimpleCopperPipesClientNetworking;

@Environment(EnvType.CLIENT)
public class SimpleCopperPipesClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SimpleCopperPipesClientNetworking.registerPacketReceivers();
	}
}

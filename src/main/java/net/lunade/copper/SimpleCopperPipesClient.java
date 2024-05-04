package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.lunade.copper.networking.SimpleCopperPipesClientNetworking;

public class SimpleCopperPipesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SimpleCopperPipesClientNetworking.registerPacketReceivers();
    }
}

package net.lunade.copper.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.lunade.copper.networking.packet.SimpleCopperPipesNoteParticlePacket;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class SimpleCopperPipesNetworking {

    public static void init() {
        PayloadTypeRegistry<RegistryFriendlyByteBuf> registry = PayloadTypeRegistry.playS2C();
        registry.register(SimpleCopperPipesNoteParticlePacket.PACKET_TYPE, SimpleCopperPipesNoteParticlePacket.CODEC);
    }

}

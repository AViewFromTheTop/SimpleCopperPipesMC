package net.lunade.copper.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.lunade.copper.networking.packet.CopperPipeNoteParticlePacket;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class CopperPipeNetworking {

    public static void init() {
        PayloadTypeRegistry<RegistryFriendlyByteBuf> registry = PayloadTypeRegistry.playS2C();
        registry.register(CopperPipeNoteParticlePacket.PACKET_TYPE, CopperPipeNoteParticlePacket.CODEC);
    }

}

package net.lunade.copper.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lunade.copper.networking.packet.SimpleCopperPipesNoteParticlePacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;

@Environment(EnvType.CLIENT)
public class SimpleCopperPipesClientNetworking {

    public static void registerPacketReceivers() {
        receiveNoteParticlePacket();
    }

    public static void receiveNoteParticlePacket() {
        ClientPlayNetworking.registerGlobalReceiver(SimpleCopperPipesNoteParticlePacket.PACKET_TYPE, (packet, ctx) -> {
            ClientLevel clientLevel = ctx.client().level;
            BlockPos pos = packet.blockPos();
            Direction direction = packet.direction();
            double x = direction.getStepX() * 0.6D;
            double y = direction.getStepY() * 0.6D;
            double z = direction.getStepZ() * 0.6D;
            clientLevel.addParticle(
                    ParticleTypes.NOTE,
                    (double) pos.getX() + 0.5D + x,
                    (double) pos.getY() + 0.5D + y,
                    (double) pos.getZ() + 0.5D + z,
                    (double) packet.pitch() / 24D,
                    0D,
                    0D
            );
        });
    }
}

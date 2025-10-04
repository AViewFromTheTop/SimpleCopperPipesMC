package net.lunade.copper.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lunade.copper.networking.packet.SimpleCopperPipesNoteParticlePacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class SimpleCopperPipesClientNetworking {

	public static void registerPacketReceivers() {
		receiveNoteParticlePacket();
	}

	public static void receiveNoteParticlePacket() {
		ClientPlayNetworking.registerGlobalReceiver(SimpleCopperPipesNoteParticlePacket.PACKET_TYPE, (packet, ctx) -> {
			final ClientLevel level = ctx.client().level;
			if (level == null) return;

			final Direction direction = packet.direction();
			final Vec3 pos = packet.blockPos().getCenter().relative(direction, 0.6D);
			level.addParticle(
				ParticleTypes.NOTE,
				pos.x(),
				pos.y(),
				pos.z(),
				(double) packet.pitch() / 24D,
				0D,
				0D
			);
		});
	}
}

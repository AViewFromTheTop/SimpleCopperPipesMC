package net.lunade.copper.particle.server;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lunade.copper.Main;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EasyParticlePacket {

    public static void createParticle(World world, Vec3d pos, int count, double xVel, double yVel, double zVel, int color) {
        if (world.isClient)
            throw new IllegalStateException("Particle spawning attempted on client!");
        PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
        byteBuf.writeDouble(pos.x);
        byteBuf.writeDouble(pos.y);
        byteBuf.writeDouble(pos.z);
        byteBuf.writeDouble(xVel);
        byteBuf.writeDouble(yVel);
        byteBuf.writeDouble(zVel);
        byteBuf.writeVarInt(count);
        byteBuf.writeVarInt(color);
        for (ServerPlayerEntity player : PlayerLookup.around((ServerWorld)world, pos, 32)) {
            ServerPlayNetworking.send(player, Main.PIPE_INK_PACKET, byteBuf);
        }
    }

}

package net.lunade.copper.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public record SimpleCopperPipesNoteParticlePacket(BlockPos blockPos, int pitch,
                                                  Direction direction) implements CustomPacketPayload {

    public static final Type<SimpleCopperPipesNoteParticlePacket> PACKET_TYPE = new Type<>(
            SimpleCopperPipesSharedConstants.id("note_particle")
    );

    public static final StreamCodec<FriendlyByteBuf, SimpleCopperPipesNoteParticlePacket> CODEC = StreamCodec.ofMember(SimpleCopperPipesNoteParticlePacket::write, SimpleCopperPipesNoteParticlePacket::new);

    public SimpleCopperPipesNoteParticlePacket(@NotNull FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readVarInt(), buf.readEnum(Direction.class));
    }

    public static void sendToAll(ServerLevel serverLevel, BlockPos pos, int pitch, Direction direction) {
        for (ServerPlayer player : PlayerLookup.tracking(serverLevel, pos)) {
            ServerPlayNetworking.send(
                    player,
                    new SimpleCopperPipesNoteParticlePacket(
                            pos,
                            pitch,
                            direction
                    )
            );
        }
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeVarInt(this.pitch);
        buf.writeEnum(this.direction);
    }

    @NotNull
    public Type<?> type() {
        return PACKET_TYPE;
    }
}

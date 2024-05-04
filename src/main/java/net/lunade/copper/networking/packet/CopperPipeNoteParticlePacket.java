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

public record CopperPipeNoteParticlePacket(BlockPos blockPos, int pitch, Direction direction) implements CustomPacketPayload {

    public static final Type<CopperPipeNoteParticlePacket> PACKET_TYPE = CustomPacketPayload.createType(
            SimpleCopperPipesSharedConstants.id("note_particle").toString()
    );

    public static final StreamCodec<FriendlyByteBuf, CopperPipeNoteParticlePacket> CODEC = StreamCodec.ofMember(CopperPipeNoteParticlePacket::write, CopperPipeNoteParticlePacket::new);

    public CopperPipeNoteParticlePacket(@NotNull FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readVarInt(), buf.readEnum(Direction.class));
    }

    public static void sendToAll(ServerLevel serverLevel, BlockPos pos, int pitch, Direction direction) {
        for (ServerPlayer player : PlayerLookup.tracking(serverLevel, pos)) {
            ServerPlayNetworking.send(
                    player,
                    new CopperPipeNoteParticlePacket(
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

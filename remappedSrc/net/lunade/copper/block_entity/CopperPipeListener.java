package net.lunade.copper.block_entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lunade.copper.Main;
import net.minecraft.particle.VibrationParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.VibrationListener;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CopperPipeListener
        extends VibrationListener {

    public static Codec<CopperPipeListener> createPipeCodec(CopperPipeListener.Callback callback) {
        return RecordCodecBuilder.create((instance) -> {
            return instance.group(PositionSource.CODEC.fieldOf("source").forGetter((vibrationListener) -> {
                return vibrationListener.positionSource;
            }), Codecs.NONNEGATIVE_INT.fieldOf("range").forGetter((vibrationListener) -> {
                return vibrationListener.range;
            }), VibrationListener.Vibration.CODEC.optionalFieldOf("event").forGetter((vibrationListener) -> {
                return Optional.ofNullable(vibrationListener.vibration);
            }), Codecs.NONNEGATIVE_INT.fieldOf("event_distance").orElse(0).forGetter((vibrationListener) -> {
                return vibrationListener.distance;
            }), Codecs.NONNEGATIVE_INT.fieldOf("event_delay").orElse(0).forGetter((vibrationListener) -> {
                return vibrationListener.delay;
            })).apply(instance, (positionSource, integer, optional, integer2, integer3) -> {
                return new CopperPipeListener(positionSource, integer, callback, optional.orElse(null), integer2, integer3);
            });
        });
    }

    @Override
    public boolean listen(ServerWorld serverWorld, GameEvent gameEvent, GameEvent.Emitter emitter, Vec3d vec3d) {
        if (this.vibration != null) {
            return false;
        } else if (!this.callback.canAccept(gameEvent, emitter)) {
            return false;
        } else {
            Optional<Vec3d> optional = this.positionSource.getPos(serverWorld);
            if (optional.isEmpty()) {
                return false;
            } else {
                Vec3d vec3d2 = optional.get();
                if (!this.callback.accepts(serverWorld, this, new BlockPos(vec3d), gameEvent, emitter)) {
                    return false;
                } else {
                    this.listen(serverWorld, gameEvent, emitter, vec3d, vec3d2);
                    return true;
                }
            }
        }
    }

    private void listen(ServerWorld serverWorld, GameEvent gameEvent, GameEvent.Emitter emitter, Vec3d vec3d, Vec3d vec3d2) {
        this.distance = MathHelper.floor(vec3d.distanceTo(vec3d2));
        this.vibration = new VibrationListener.Vibration(gameEvent, this.distance, vec3d, emitter.sourceEntity());
        if (gameEvent!=GameEvent.NOTE_BLOCK_PLAY) {
            this.delay = this.distance;
            serverWorld.spawnParticles(new VibrationParticleEffect(this.positionSource, this.delay), vec3d.x, vec3d.y, vec3d.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        } else {
            this.delay = 1;
            serverWorld.spawnParticles(new VibrationParticleEffect(this.positionSource, 5), vec3d.x, vec3d.y, vec3d.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        this.callback.onListen();
    }

    public CopperPipeListener(PositionSource positionSource, int i, Callback callback, @Nullable VibrationListener.Vibration vibration, int j, int k) {
        super(positionSource, i, callback, vibration, j, k);
    }
}


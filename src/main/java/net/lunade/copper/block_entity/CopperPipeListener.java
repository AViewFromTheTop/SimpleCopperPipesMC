package net.lunade.copper.block_entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.phys.Vec3;

public class CopperPipeListener
        extends VibrationListener {

    public static Codec<CopperPipeListener> createPipeCodec(CopperPipeListener.VibrationListenerConfig callback) {
        return RecordCodecBuilder.create((instance) -> {
            return instance.group(PositionSource.CODEC.fieldOf("source").forGetter((vibrationListener) -> {
                return vibrationListener.listenerSource;
            }), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("range").forGetter((vibrationListener) -> {
                return vibrationListener.listenerRange;
            }), VibrationListener.ReceivingEvent.CODEC.optionalFieldOf("event").forGetter((vibrationListener) -> {
                return Optional.ofNullable(vibrationListener.receivingEvent);
            }), Codec.floatRange(0.0F, 3.4028235E38F).fieldOf("event_distance").orElse(0.0F).forGetter((vibrationListener) -> {
                return vibrationListener.receivingDistance;
            }), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("event_delay").orElse(0).forGetter((vibrationListener) -> {
                return vibrationListener.travelTimeInTicks;
            })).apply(instance, (positionSource, integer, optional, float_, integer2) -> {
                return new CopperPipeListener(positionSource, integer, callback, optional.orElse(null), float_, integer2);
            });
        });
    }

    public boolean handleGameEvent(ServerLevel serverWorld, GameEvent.Message message) {
        if (this.receivingEvent != null) {
            return false;
        } else {
            GameEvent gameEvent = message.gameEvent();
            GameEvent.Context emitter = message.context();
            if (!this.config.isValidVibration(gameEvent, emitter)) {
                return false;
            } else {
                Optional<Vec3> optional = this.listenerSource.getPosition(serverWorld);
                if (optional.isEmpty()) {
                    return false;
                } else {
                    Vec3 vec3d = message.source();
                    Vec3 vec3d2 = optional.get();
                    if (!this.config.shouldListen(serverWorld, this, new BlockPos(vec3d), gameEvent, emitter)) {
                        return false;
                    } else {
                        this.scheduleSignal(serverWorld, gameEvent, emitter, vec3d, vec3d2);
                        return true;
                    }
                }
            }
        }
    }

    private void scheduleSignal(ServerLevel serverWorld, GameEvent gameEvent, GameEvent.Context emitter, Vec3 vec3d, Vec3 vec3d2) {
        this.receivingDistance = Mth.floor(vec3d.distanceTo(vec3d2));
        this.receivingEvent = new VibrationListener.ReceivingEvent(gameEvent, this.receivingDistance, vec3d, emitter.sourceEntity());
        if (gameEvent!=GameEvent.NOTE_BLOCK_PLAY) {
            this.travelTimeInTicks = Mth.floor(this.receivingDistance);
            //serverWorld.spawnParticles(new VibrationParticleEffect(this.positionSource, this.delay), vec3d.x, vec3d.y, vec3d.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        } else {
            this.travelTimeInTicks = 0;
            //serverWorld.spawnParticles(new VibrationParticleEffect(this.positionSource, 5), vec3d.x, vec3d.y, vec3d.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        this.config.onSignalSchedule();
    }

    public CopperPipeListener(PositionSource positionSource, int i, VibrationListenerConfig callback, @Nullable VibrationListener.ReceivingEvent vibration, float f, int j) {
        super(positionSource, i, callback, vibration, f, j);
    }
}


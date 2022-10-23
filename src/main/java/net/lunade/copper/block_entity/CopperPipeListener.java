package net.lunade.copper.block_entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSelector;
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

public class CopperPipeListener extends VibrationListener {

    public static Codec<CopperPipeListener> createPipeCodec(CopperPipeListener.VibrationListenerConfig vibrationListenerConfig) {
        return RecordCodecBuilder.create(
                instance -> instance.group(
                                PositionSource.CODEC.fieldOf("source").forGetter(vibrationListener -> vibrationListener.listenerSource),
                                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("range").forGetter(vibrationListener -> vibrationListener.listenerRange),
                                VibrationInfo.CODEC.optionalFieldOf("event").forGetter(vibrationListener -> Optional.ofNullable(vibrationListener.currentVibration)),
                                VibrationSelector.CODEC.fieldOf("selector").forGetter(vibrationListener -> vibrationListener.selectionStrategy),
                                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("event_delay").orElse(0).forGetter(vibrationListener -> vibrationListener.travelTimeInTicks)
                        )
                        .apply(
                                instance,
                                (positionSource, integer, optional, vibrationSelector, integer2) -> new CopperPipeListener(
                                        positionSource, integer, vibrationListenerConfig, optional.orElse(null), vibrationSelector, integer2
                                )
                        )
        );
    }

    public CopperPipeListener(
            PositionSource positionSource,
            int listenerRange,
            VibrationListener.VibrationListenerConfig vibrationListenerConfig,
            @Nullable VibrationInfo vibrationInfo,
            VibrationSelector vibrationSelector,
            int j
    ) {
        super(positionSource, listenerRange, vibrationListenerConfig, vibrationInfo, vibrationSelector, j);
    }

    public CopperPipeListener(PositionSource positionSource, int i, VibrationListenerConfig callback) {
        super(positionSource, i, callback);
    }

    @Override
    public boolean handleGameEvent(ServerLevel serverLevel, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3) {
        if (this.currentVibration != null) {
            return false;
        } else if (!this.config.isValidVibration(gameEvent, context)) {
            return false;
        } else {
            Optional<Vec3> optional = this.listenerSource.getPosition(serverLevel);
            if (optional.isEmpty()) {
                return false;
            } else {
                Vec3 vec3d2 = optional.get();
                if (!this.config.shouldListen(serverLevel, this, new BlockPos(vec3), gameEvent, context)) {
                    return false;
                } else {
                    this.scheduleVibration(serverLevel, gameEvent, context, vec3, vec3d2);
                    return true;
                }
            }
        }
    }
}


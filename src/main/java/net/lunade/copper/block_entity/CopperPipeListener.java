package net.lunade.copper.block_entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSelector;
import net.minecraft.world.phys.HitResult;
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

    public static Codec<CopperPipeListener> createPipeCodec(VibrationListenerConfig vibrationListenerConfig) {
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
            VibrationListenerConfig vibrationListenerConfig,
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
                Vec3 vec32 = optional.get();
                //this.scheduleVibration(serverLevel, gameEvent, context, vec3, vec32);
                if (!this.config.shouldListen(serverLevel, this, new BlockPos(vec3), gameEvent, context)) {
                    return false;
                } else return !isOccluded(serverLevel, vec3, vec32);
            }
        }
    }

    private static boolean isOccluded(Level level, Vec3 vec3, Vec3 vec32) {
        Vec3 vec33 = new Vec3((double)Mth.floor(vec3.x) + 0.5D, (double)Mth.floor(vec3.y) + 0.5D, (double)Mth.floor(vec3.z) + 0.5D);
        Vec3 vec34 = new Vec3((double)Mth.floor(vec32.x) + 0.5D, (double)Mth.floor(vec32.y) + 0.5D, (double)Mth.floor(vec32.z) + 0.5D);
        Direction[] var5 = Direction.values();

        for (Direction direction : var5) {
            Vec3 vec35 = vec33.relative(direction, 9.999999747378752E-6D);
            if (level.isBlockInLine(new ClipBlockStateContext(vec35, vec34, (blockState) -> blockState.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK) {
                return false;
            }
        }

        return true;
    }

}


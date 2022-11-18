package net.lunade.copper.block_entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
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

    public static Codec<CopperPipeListener> createPipeCodec(CopperPipeListener.VibrationListenerConfig callback) {
        return RecordCodecBuilder.create((instance) -> instance.group(
                        PositionSource.CODEC.fieldOf("source").forGetter((vibrationListener) -> vibrationListener.listenerSource),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("range").forGetter((vibrationListener) -> vibrationListener.listenerRange),
                        ReceivingEvent.CODEC.optionalFieldOf("event").forGetter((vibrationListener) -> Optional.ofNullable(vibrationListener.receivingEvent)),
                        Codec.floatRange(0.0F, 3.4028235E38F).fieldOf("event_distance").orElse(0.0F).forGetter((vibrationListener) -> vibrationListener.receivingDistance),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("event_delay").orElse(0).forGetter((vibrationListener) -> vibrationListener.travelTimeInTicks))
                .apply(instance, (positionSource, integer, optional, float_, integer2)
                        -> new CopperPipeListener(positionSource, integer, callback, optional.orElse(null), float_, integer2)));
    }

    public CopperPipeListener(PositionSource positionSource, int i, VibrationListenerConfig callback, @Nullable VibrationListener.ReceivingEvent vibration, float f, int j) {
        super(positionSource, i, callback, vibration, f, j);
    }

    @Override
    public boolean handleGameEvent(ServerLevel serverLevel, GameEvent.Message message) {
        if (this.receivingEvent != null) {
            return false;
        } else {
            GameEvent gameEvent = message.gameEvent();
            GameEvent.Context context = message.context();
            if (!this.config.isValidVibration(gameEvent, context)) {
                return false;
            } else {
                Optional<Vec3> optional = this.listenerSource.getPosition(serverLevel);
                if (optional.isEmpty()) {
                    return false;
                } else {
                    Vec3 vec3 = message.source();
                    Vec3 vec32 = optional.get();
                    if (!this.config.shouldListen(serverLevel, this, new BlockPos(vec3), gameEvent, context)) {
                        return false;
                    } else if (isOccluded(serverLevel, vec3, vec32)) {
                        return false;
                    } else {
                        this.scheduleSignal(serverLevel, gameEvent, context, vec3, vec32);
                        return true;
                    }
                }
            }
        }
    }

    private void scheduleSignal(ServerLevel serverLevel, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3, Vec3 vec32) {
        this.receivingDistance = (float)vec3.distanceTo(vec32);
        this.receivingEvent = new VibrationListener.ReceivingEvent(gameEvent, this.receivingDistance, vec3, context.sourceEntity());
        if (gameEvent!=GameEvent.NOTE_BLOCK_PLAY) {
            this.travelTimeInTicks = Mth.floor(this.receivingDistance);
        } else {
            this.travelTimeInTicks = 0;
        }
        this.config.onSignalSchedule();
    }

    private static boolean isOccluded(Level level, Vec3 vec3, Vec3 vec32) {
        Vec3 vec33 = new Vec3((double)Mth.floor(vec3.x) + 0.5D, (double)Mth.floor(vec3.y) + 0.5D, (double)Mth.floor(vec3.z) + 0.5D);
        Vec3 vec34 = new Vec3((double)Mth.floor(vec32.x) + 0.5D, (double)Mth.floor(vec32.y) + 0.5D, (double)Mth.floor(vec32.z) + 0.5D);

        for (Direction direction : Direction.values()) {
            Vec3 vec35 = vec33.relative(direction, 9.999999747378752E-6D);
            if (level.isBlockInLine(new ClipBlockStateContext(vec35, vec34, (blockState) -> blockState.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK) {
                return false;
            }
        }

        return true;
    }

}

package net.lunade.copper.game_event;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.VibrationParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class SaveablePipeGameEvent {

    private static final Logger LOGGER = LogUtils.getLogger();
    public Identifier event;
    public Vec3d originPos;
    public String uuid;
    public boolean hasEmittedParticle;
    public BlockPos pipePos;

    //TEMP STORAGE
    public Entity foundEntity;

    public static final Codec<SaveablePipeGameEvent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Identifier.CODEC.fieldOf("eventID").forGetter(SaveablePipeGameEvent::getEventID),
            Vec3d.CODEC.fieldOf("originPos").forGetter(SaveablePipeGameEvent::getOriginPos),
            Codec.STRING.fieldOf("uuid").forGetter(SaveablePipeGameEvent::getUUID),
            Codec.BOOL.fieldOf("hasEmittedParticle").forGetter(SaveablePipeGameEvent::getHasEmittedParticle),
            BlockPos.CODEC.fieldOf("pipePos").forGetter(SaveablePipeGameEvent::getPipePos)
    ).apply(instance, SaveablePipeGameEvent::new));

    public SaveablePipeGameEvent(Identifier event, Vec3d originPos, String uuid, boolean hasEmittedParticle, BlockPos pipePos) {
        this.event = event;
        this.originPos = originPos;
        this.uuid = uuid;
        this.hasEmittedParticle = hasEmittedParticle;
        this.pipePos = pipePos;
    }

    public SaveablePipeGameEvent(GameEvent event, Vec3d originPos, GameEvent.Emitter emitter, BlockPos pipePos) {
        this.event = Registry.GAME_EVENT.getId(event);
        this.originPos = originPos;
        if (emitter.comp_713() != null) {
            this.uuid = emitter.comp_713().getUuid().toString();
        } else {
            this.uuid = "noEntity";
        }
        this.hasEmittedParticle = false;
        this.pipePos = pipePos;
    }

    public SaveablePipeGameEvent(GameEvent event, Vec3d originPos, @Nullable Entity entity, BlockPos pipePos) {
        this.event = Registry.GAME_EVENT.getId(event);
        this.originPos = originPos;
        if (entity != null) {
            this.uuid = entity.getUuid().toString();
        } else {
            this.uuid = "noEntity";
        }
        this.hasEmittedParticle = false;
        this.pipePos = pipePos;
    }

    public void emitGameEvent(World world, BlockPos exitPos) {
        world.emitGameEvent(this.getEntity(world), this.getGameEvent(), exitPos);
    }

    public void emitGameEvent(World world, Vec3d exitPos) {
        world.emitGameEvent(this.getEntity(world), this.getGameEvent(), exitPos);
    }

    public void spawnPipeVibrationParticles(ServerWorld world) {
        if (!this.hasEmittedParticle) {
            world.spawnParticles(new VibrationParticleEffect(new BlockPositionSource(this.pipePos), 5), this.originPos.x, this.originPos.y, this.originPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.hasEmittedParticle = true;
        }
    }

    public GameEvent getGameEvent() {
        return Registry.GAME_EVENT.get(this.event);
    }

    @Nullable
    public Entity getEntity(World world) {
        if (!this.uuid.equals("noEntity")) {
            if (this.foundEntity != null) {
                if (this.foundEntity.getUuid().toString().equals(this.uuid)) {
                    return this.foundEntity;
                } else {
                    this.foundEntity = null;
                }
            }
            Box box = new Box(this.originPos.add(-32, -32, -32), this.originPos.add(32, 32, 32));
            List<Entity> entities = world.getNonSpectatingEntities(Entity.class, box);
            for (Entity entity : entities) {
                if (entity.getUuid().toString().equals(this.uuid)) {
                    this.foundEntity = entity;
                    return entity;
                }
            }
        }
        return null;
    }

    public Identifier getEventID() {
        return this.event;
    }

    public Vec3d getOriginPos() {
        return this.originPos;
    }

    public String getUUID() {
        return this.uuid;
    }

    public boolean getHasEmittedParticle() {
        return this.hasEmittedParticle;
    }

    public BlockPos getPipePos() {
        return this.pipePos;
    }

    public static SaveablePipeGameEvent readNbt(NbtCompound nbt) {
        Optional<SaveablePipeGameEvent> event = Optional.empty();
        if (nbt.contains("savedPipeGameEvent", 10)) {
            event = SaveablePipeGameEvent.CODEC
                    .parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("savedPipeGameEvent")))
                    .resultOrPartial(LOGGER::error);
        }
        return event.orElse(null);
    }

    public static void writeNbt(NbtCompound nbt, @Nullable SaveablePipeGameEvent saveableGameEvent) {
        if (saveableGameEvent != null) {
            SaveablePipeGameEvent.CODEC
                    .encodeStart(NbtOps.INSTANCE, saveableGameEvent)
                    .resultOrPartial(LOGGER::error)
                    .ifPresent(saveableGameEventNbt -> nbt.put("savedPipeGameEvent", saveableGameEventNbt));
        } else {
            if (nbt.contains("savedPipeGameEvent", 10)) {
                nbt.remove("savedPipeGameEvent");
            }
        }
    }

    public SaveablePipeGameEvent copyOf() {
        return new SaveablePipeGameEvent(this.event, this.originPos, this.uuid, this.hasEmittedParticle, this.pipePos);
    }
}

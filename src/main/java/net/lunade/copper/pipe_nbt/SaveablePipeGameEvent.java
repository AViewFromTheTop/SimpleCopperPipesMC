package net.lunade.copper.pipe_nbt;

import com.mojang.logging.LogUtils;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.entity.Entity;
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

public class SaveablePipeGameEvent extends MoveablePipeDataHandler.SaveableMovablePipeNbt {
    private static final Logger LOGGER = LogUtils.getLogger();

    //TEMP STORAGE
    public Entity foundEntity;

    public SaveablePipeGameEvent(Identifier event, Vec3d originPos, String uuid, boolean hasEmittedParticle, BlockPos pipePos) {
        super(event, originPos, uuid, hasEmittedParticle, pipePos);
        this.id = event;
        this.originPos = originPos;
        this.uuid = uuid;
        this.hasEmittedParticle = hasEmittedParticle;
        this.pipePos = pipePos;
        this.setNbtId(CopperPipeEntity.SaveableGameEventID);
    }

    public SaveablePipeGameEvent(GameEvent event, Vec3d originPos, GameEvent.Emitter emitter, BlockPos pipePos) {
        super(event, originPos, emitter, pipePos);
        this.id = Registry.GAME_EVENT.getId(event);
        this.originPos = originPos;
        if (emitter.comp_713() != null) {
            this.uuid = emitter.comp_713().getUuid().toString();
        } else {
            this.uuid = "noEntity";
        }
        this.hasEmittedParticle = false;
        this.pipePos = pipePos;
        this.setNbtId(CopperPipeEntity.SaveableGameEventID);
    }

    public SaveablePipeGameEvent(GameEvent event, Vec3d originPos, @Nullable Entity entity, BlockPos pipePos) {
        super(event, originPos, entity, pipePos);
        this.id = Registry.GAME_EVENT.getId(event);
        this.originPos = originPos;
        if (entity != null) {
            this.uuid = entity.getUuid().toString();
        } else {
            this.uuid = "noEntity";
        }
        this.hasEmittedParticle = false;
        this.pipePos = pipePos;
        this.setNbtId(CopperPipeEntity.SaveableGameEventID);
    }

    public void dispense(World world, BlockPos exitPos) {
        world.emitGameEvent(this.getEntity(world), this.getGameEvent(), exitPos);
    }

    public void dispense(World world, Vec3d exitPos) {
        world.emitGameEvent(this.getEntity(world), this.getGameEvent(), exitPos);
    }

    public void spawnPipeVibrationParticles(ServerWorld world) {
        if (!this.hasEmittedParticle) {
            world.spawnParticles(new VibrationParticleEffect(new BlockPositionSource(this.pipePos), 5), this.originPos.x, this.originPos.y, this.originPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.hasEmittedParticle = true;
        }
    }

    public GameEvent getGameEvent() {
        return Registry.GAME_EVENT.get(this.id);
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


    public SaveablePipeGameEvent copyOf() {
        return new SaveablePipeGameEvent(this.id, this.originPos, this.uuid, this.hasEmittedParticle, this.pipePos);
    }
}

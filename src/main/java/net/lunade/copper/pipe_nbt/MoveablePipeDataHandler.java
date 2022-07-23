package net.lunade.copper.pipe_nbt;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lunade.copper.Main;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MoveablePipeDataHandler {

    public ArrayList<SaveableMovablePipeNbt> savedList = new ArrayList<>();
    public ArrayList<Identifier> savedIds = new ArrayList<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    public MoveablePipeDataHandler() {

    }

    public void readNbt(NbtCompound nbtCompound) {
        if (nbtCompound.contains("saveableMoveableNbtList", 9)) {
            this.savedList.clear();
            this.savedIds.clear();
            DataResult<?> var10000 = SaveableMovablePipeNbt.CODEC.listOf().parse(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.getList("saveableMoveableNbtList", 10)));
            Logger var10001 = LOGGER;
            Objects.requireNonNull(var10001);
            Optional<List<SaveableMovablePipeNbt>> list = (Optional<List<SaveableMovablePipeNbt>>) var10000.resultOrPartial(var10001::error);

            if (list.isPresent()) {
                for (SaveableMovablePipeNbt saveableMovablePipeNbt : list.get()) {
                    Class<? extends SaveableMovablePipeNbt> properClass = Main.getProperClass(saveableMovablePipeNbt);
                    this.addSaveableMoveablePipeNbt(properClass.cast(saveableMovablePipeNbt));
                }
            }
        }
    }

    public void writeNbt(NbtCompound nbtCompound) {
        DataResult<?> var10000 = SaveableMovablePipeNbt.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.savedList);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> nbtCompound.put("saveableMoveableNbtList", (NbtElement) nbtElement));
    }

    public void addSaveableMoveablePipeNbt(SaveableMovablePipeNbt nbt) {
        if (!this.savedIds.contains(nbt.nbtId)) {
            this.savedList.add(nbt);
            this.savedIds.add(nbt.nbtId);
        } else {
            LOGGER.error("CANNOT ADD DUPLICATE PIPE NBT");
        }
    }

    @Nullable
    public SaveableMovablePipeNbt getMoveablePipeNbt(Identifier id) {
        if (this.savedIds.contains(id) && !this.savedList.isEmpty()) {
            return this.savedList.get(this.savedIds.indexOf(id));
        }
        return null;
    }

    public void removeMoveablePipeNbt(Identifier id) {
        if (this.savedIds.contains(id)) {
            this.savedList.remove(this.savedIds.indexOf(id));
            this.savedIds.remove(id);
        }
    }

    public void setMoveablePipeNbt(Identifier id, SaveableMovablePipeNbt nbt) {
        if (this.savedIds.contains(id)) {
            this.savedList.set(this.savedIds.indexOf(id), nbt);
        } else {
            this.savedIds.add(id);
            this.savedList.add(nbt);
        }
    }

    public static class SaveableMovablePipeNbt {

        private Identifier nbtId;
        public Identifier id;
        public Vec3d originPos;
        public String uuid;
        public boolean hasEmittedParticle;
        public BlockPos pipePos;

        //TEMP STORAGE
        public Entity foundEntity;

        public static final Codec<SaveableMovablePipeNbt> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Identifier.CODEC.fieldOf("eventID").forGetter(SaveableMovablePipeNbt::getId),
                Vec3d.CODEC.fieldOf("originPos").forGetter(SaveableMovablePipeNbt::getOriginPos),
                Codec.STRING.fieldOf("uuid").forGetter(SaveableMovablePipeNbt::getUUID),
                Codec.BOOL.fieldOf("hasEmittedParticle").forGetter(SaveableMovablePipeNbt::getHasEmittedParticle),
                BlockPos.CODEC.fieldOf("pipePos").forGetter(SaveableMovablePipeNbt::getPipePos),
                Identifier.CODEC.fieldOf("nbtId").forGetter(SaveableMovablePipeNbt::getNbtId)
        ).apply(instance, SaveableMovablePipeNbt::new));

        public SaveableMovablePipeNbt(Identifier id, Vec3d originPos, String uuid, boolean hasEmittedParticle, BlockPos pipePos) {
            this.id = id;
            this.originPos = originPos;
            this.uuid = uuid;
            this.hasEmittedParticle = hasEmittedParticle;
            this.pipePos = pipePos;
            this.nbtId = new Identifier("lunade", "default");
        }

        public SaveableMovablePipeNbt(Identifier id, Vec3d originPos, String uuid, boolean hasEmittedParticle, BlockPos pipePos, Identifier nbtId) {
            this.id = id;
            this.originPos = originPos;
            this.uuid = uuid;
            this.hasEmittedParticle = hasEmittedParticle;
            this.pipePos = pipePos;
            this.nbtId = nbtId;
        }

        public SaveableMovablePipeNbt(GameEvent event, Vec3d originPos, GameEvent.Emitter emitter, BlockPos pipePos) {
            this.id = Registry.GAME_EVENT.getId(event);
            this.originPos = originPos;
            if (emitter.comp_713() != null) {
                this.uuid = emitter.comp_713().getUuid().toString();
            } else {
                this.uuid = "noEntity";
            }
            this.hasEmittedParticle = false;
            this.pipePos = pipePos;
            this.nbtId = new Identifier("lunade", "default");
        }

        public SaveableMovablePipeNbt(GameEvent event, Vec3d originPos, @Nullable Entity entity, BlockPos pipePos) {
            this.id = Registry.GAME_EVENT.getId(event);
            this.originPos = originPos;
            if (entity != null) {
                this.uuid = entity.getUuid().toString();
            } else {
                this.uuid = "noEntity";
            }
            this.hasEmittedParticle = false;
            this.pipePos = pipePos;
            this.nbtId = new Identifier("lunade", "default");
        }

        public void dispense(World world, BlockPos exitPos) {

        }

        public void dispense(World world, Vec3d exitPos) {

        }

        public void spawnPipeVibrationParticles(ServerWorld world) {
            if (!this.hasEmittedParticle) {
                world.spawnParticles(new VibrationParticleEffect(new BlockPositionSource(this.pipePos), 5), this.originPos.x, this.originPos.y, this.originPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                this.hasEmittedParticle = true;
            }
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

        public Identifier getId() {
            return this.id;
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

        public Identifier getNbtId() {
            return this.nbtId;
        }

        public void setNbtId(Identifier id) {
            this.nbtId = id;
        }

        public SaveablePipeGameEvent copyOf() {
            return new SaveablePipeGameEvent(this.id, this.originPos, this.uuid, this.hasEmittedParticle, this.pipePos);
        }
    }
}

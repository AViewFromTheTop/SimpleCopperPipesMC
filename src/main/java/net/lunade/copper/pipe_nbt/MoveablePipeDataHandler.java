package net.lunade.copper.pipe_nbt;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lunade.copper.RegisterPipeNbtMethods;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
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
                    this.addSaveableMoveablePipeNbt(saveableMovablePipeNbt);
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

    public void clear() {
        this.savedList.clear();
        this.savedIds.clear();
    }

    public ArrayList<SaveableMovablePipeNbt> getSavedNbtList() {
        return this.savedList;
    }

    public static class SaveableMovablePipeNbt {

        public Identifier id;
        public double originX;
        public double originY;
        public double originZ;
        public String uuid;
        public int useCount;
        public BlockPos pipePos;
        private boolean canOnlyBeUsedOnce;
        private boolean canOnlyGoThroughOnePipe;
        private Identifier nbtId;

        //TEMP STORAGE
        public Entity foundEntity;

        public static final Codec<SaveableMovablePipeNbt> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Identifier.CODEC.fieldOf("eventID").forGetter(SaveableMovablePipeNbt::getId),
                Codec.DOUBLE.fieldOf("originX").forGetter(SaveableMovablePipeNbt::getX),
                Codec.DOUBLE.fieldOf("originY").forGetter(SaveableMovablePipeNbt::getY),
                Codec.DOUBLE.fieldOf("originZ").forGetter(SaveableMovablePipeNbt::getZ),
                Codec.STRING.fieldOf("uuid").forGetter(SaveableMovablePipeNbt::getUUID),
                Codec.INT.fieldOf("useCount").forGetter(SaveableMovablePipeNbt::getUseCount),
                BlockPos.CODEC.fieldOf("pipePos").forGetter(SaveableMovablePipeNbt::getPipePos),
                Codec.BOOL.fieldOf("canOnlyBeUsedOnce").forGetter(SaveableMovablePipeNbt::getCanOnlyBeUsedOnce),
                Codec.BOOL.fieldOf("canOnlyGoThroughOnePipe").forGetter(SaveableMovablePipeNbt::getCanOnlyGoThroughOnePipe),
                Identifier.CODEC.fieldOf("nbtId").forGetter(SaveableMovablePipeNbt::getNbtId)
        ).apply(instance, SaveableMovablePipeNbt::new));

        public SaveableMovablePipeNbt(Identifier id, double x, double y, double z, String uuid, int useCount, BlockPos pipePos, boolean canOnlyBeUsedOnce, boolean canOnlyGoThroughOnePipe, Identifier nbtID) {
            this.id = id;
            this.originX = x;
            this.originY = y;
            this.originZ = z;
            this.uuid = uuid;
            this.useCount = useCount;
            this.pipePos = pipePos;
            this.canOnlyBeUsedOnce = canOnlyBeUsedOnce;
            this.canOnlyGoThroughOnePipe = canOnlyGoThroughOnePipe;
            this.nbtId = nbtID;
        }

        public SaveableMovablePipeNbt(Identifier id, Vec3d originPos, String uuid, BlockPos pipePos) {
            this.id = id;
            this.originX = originPos.x;
            this.originY = originPos.y;
            this.originZ = originPos.z;
            this.uuid = uuid;
            this.useCount = 0;
            this.pipePos = pipePos;
            this.nbtId = new Identifier("lunade", "default");
        }

        public SaveableMovablePipeNbt(Identifier id, Vec3d originPos, String uuid, BlockPos pipePos, Identifier nbtId) {
            this.id = id;
            this.originX = originPos.x;
            this.originY = originPos.y;
            this.originZ = originPos.z;
            this.uuid = uuid;
            this.useCount = 0;
            this.pipePos = pipePos;
            this.nbtId = nbtId;
        }

        public SaveableMovablePipeNbt(GameEvent event, Vec3d originPos, @Nullable Entity entity, BlockPos pipePos) {
            this.id = Registry.GAME_EVENT.getId(event);
            this.originX = originPos.x;
            this.originY = originPos.y;
            this.originZ = originPos.z;
            if (entity != null) {
                this.uuid = entity.getUuid().toString();
            } else {
                this.uuid = "noEntity";
            }
            this.useCount = 0;
            this.pipePos = pipePos;
            this.nbtId = new Identifier("lunade", "default");
        }

        public SaveableMovablePipeNbt withId(Identifier id) {
            this.setNbtId(id);
            return this;
        }

        public SaveableMovablePipeNbt withOnlyUseableOnce() {
            this.canOnlyBeUsedOnce = true;
            return this;
        }

        public SaveableMovablePipeNbt withOnlyThroughOnePipe() {
            this.canOnlyGoThroughOnePipe = true;
            return this;
        }

        public void dispense(ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipeEntity) {
            RegisterPipeNbtMethods.DispenseMethod<?> method = RegisterPipeNbtMethods.getDispense(this.nbtId);
            if (method!=null) {
                method.dispense(this, world, pos, state, pipeEntity);
            } else {
                LOGGER.error("Unable to find dispense method for Moveable Pipe Nbt " + this.getNbtId() + "!");
            }
        }

        public Vec3d getOriginPos() {
            return new Vec3d(this.originX, this.originY, this.originZ);
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
                Box box = new Box(this.getOriginPos().add(-32, -32, -32), this.getOriginPos().add(32, 32, 32));
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

        public double getX() {
            return this.originX;
        }

        public double getY() {
            return this.originY;
        }

        public double getZ() {
            return this.originZ;
        }

        public String getUUID() {
            return this.uuid;
        }

        public int getUseCount() {
            return this.useCount;
        }

        public BlockPos getPipePos() {
            return this.pipePos;
        }

        public Identifier getNbtId() {
            return this.nbtId;
        }

        public boolean getCanOnlyBeUsedOnce() {
            return this.canOnlyBeUsedOnce;
        }

        public boolean getCanOnlyGoThroughOnePipe() {
            return this.canOnlyGoThroughOnePipe;
        }

        public void setNbtId(Identifier id) {
            this.nbtId = id;
        }

        public SaveableMovablePipeNbt copyOf() {
            return new SaveableMovablePipeNbt(this.id, this.originX, this.originY, this.originZ, this.uuid, this.useCount, this.pipePos, this.canOnlyBeUsedOnce, this.canOnlyGoThroughOnePipe, this.nbtId);
        }
    }
}

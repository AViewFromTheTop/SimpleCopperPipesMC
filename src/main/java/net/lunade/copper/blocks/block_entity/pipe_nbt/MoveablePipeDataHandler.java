package net.lunade.copper.blocks.block_entity.pipe_nbt;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.lunade.copper.blocks.block_entity.AbstractSimpleCopperBlockEntity;
import net.lunade.copper.blocks.block_entity.CopperPipeEntity;
import net.lunade.copper.registry.RegisterPipeNbtMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class MoveablePipeDataHandler {

    private static final Logger LOGGER = LogUtils.getLogger();
    public ArrayList<SaveableMovablePipeNbt> savedList = new ArrayList<>();
    public ArrayList<ResourceLocation> savedIds = new ArrayList<>();

    public MoveablePipeDataHandler() {

    }

    public void readNbt(@NotNull CompoundTag nbtCompound) {
        if (nbtCompound.contains("saveableMoveableNbtList", 9)) {
            this.clear();
            DataResult<List<SaveableMovablePipeNbt>> var10000 = SaveableMovablePipeNbt.CODEC.listOf().parse(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.getList("saveableMoveableNbtList", 10)));
            Logger var10001 = LOGGER;
            Objects.requireNonNull(var10001);
            Optional<List<SaveableMovablePipeNbt>> list = var10000.resultOrPartial(var10001::error);

            if (list.isPresent()) {
                for (SaveableMovablePipeNbt saveableMovablePipeNbt : list.get()) {
                    if (saveableMovablePipeNbt.shouldSave) {
                        this.addSaveableMoveablePipeNbt(saveableMovablePipeNbt);
                    }
                }
            }
        }
    }

    public void writeNbt(CompoundTag nbtCompound) {
        DataResult<Tag> var10000 = SaveableMovablePipeNbt.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.savedList);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> nbtCompound.put("saveableMoveableNbtList", nbtElement));
    }

    public void addSaveableMoveablePipeNbt(@NotNull SaveableMovablePipeNbt nbt) {
        if (!this.savedIds.contains(nbt.getNbtID())) {
            this.savedList.add(nbt);
            this.savedIds.add(nbt.getNbtID());
        }
    }

    @Nullable
    public SaveableMovablePipeNbt getMoveablePipeNbt(ResourceLocation id) {
        if (this.savedIds.contains(id) && !this.savedList.isEmpty()) {
            return this.savedList.get(this.savedIds.indexOf(id));
        }
        return null;
    }

    public void removeMoveablePipeNbt(ResourceLocation id) {
        if (this.savedIds.contains(id)) {
            this.savedList.remove(this.savedIds.indexOf(id));
            this.savedIds.remove(id);
        }
    }

    public void setMoveablePipeNbt(ResourceLocation id, SaveableMovablePipeNbt nbt) {
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

    public void clearAllButNonMoveable() {
        ArrayList<SaveableMovablePipeNbt> nbtToRemove = new ArrayList<>();
        this.savedList.clear();
        this.savedIds.clear();
        for (SaveableMovablePipeNbt nbt : this.savedList) {
            if (nbt.getShouldMove()) {
                nbtToRemove.add(nbt);
            }
        }
        for (SaveableMovablePipeNbt nbt : nbtToRemove) {
            if (this.savedList.contains(nbt)) {
                int index = this.savedList.indexOf(nbt);
                this.savedList.remove(index);
                this.savedIds.remove(index);
            }
        }
    }

    public void clearAllButMoveable() {
        ArrayList<SaveableMovablePipeNbt> nbtToRemove = new ArrayList<>();
        this.savedList.clear();
        this.savedIds.clear();
        for (SaveableMovablePipeNbt nbt : this.savedList) {
            if (!nbt.getShouldMove()) {
                nbtToRemove.add(nbt);
            }
        }
        for (SaveableMovablePipeNbt nbt : nbtToRemove) {
            if (this.savedList.contains(nbt)) {
                int index = this.savedList.indexOf(nbt);
                this.savedList.remove(index);
                this.savedIds.remove(index);
            }
        }
    }

    public ArrayList<SaveableMovablePipeNbt> getSavedNbtList() {
        return this.savedList;
    }

    public static class SaveableMovablePipeNbt {

        public static final Codec<SaveableMovablePipeNbt> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ResourceLocation.CODEC.fieldOf("savedID").forGetter(SaveableMovablePipeNbt::getSavedID),
                Vec3.CODEC.fieldOf("vec3d").forGetter(SaveableMovablePipeNbt::getVec3d),
                Vec3.CODEC.fieldOf("vec3d2").forGetter(SaveableMovablePipeNbt::getVec3d2),
                Codec.STRING.fieldOf("string").forGetter(SaveableMovablePipeNbt::getString),
                Codec.INT.fieldOf("useCount").forGetter(SaveableMovablePipeNbt::getUseCount),
                BlockPos.CODEC.fieldOf("blockPos").forGetter(SaveableMovablePipeNbt::getBlockPos),
                Codec.BOOL.fieldOf("shouldSave").forGetter(SaveableMovablePipeNbt::getShouldSave),
                Codec.BOOL.fieldOf("shouldMove").forGetter(SaveableMovablePipeNbt::getShouldMove),
                Codec.BOOL.fieldOf("canOnlyBeUsedOnce").forGetter(SaveableMovablePipeNbt::getCanOnlyBeUsedOnce),
                Codec.BOOL.fieldOf("canOnlyGoThroughOnePipe").forGetter(SaveableMovablePipeNbt::getCanOnlyGoThroughOnePipe),
                Codec.BOOL.fieldOf("shouldCopy").forGetter(SaveableMovablePipeNbt::getShouldCopy),
                ResourceLocation.CODEC.fieldOf("nbtId").forGetter(SaveableMovablePipeNbt::getNbtID)
        ).apply(instance, SaveableMovablePipeNbt::new));
        public ResourceLocation savedID;
        public Vec3 vec3d;
        public Vec3 vec3d2;
        public String string;
        public int useCount;
        public BlockPos blockPos;
        public boolean shouldSave;
        public boolean shouldMove;
        //TEMP STORAGE
        public Entity foundEntity;
        private boolean canOnlyBeUsedOnce;
        private boolean canOnlyGoThroughOnePipe;
        private boolean shouldCopy;
        private ResourceLocation nbtID;

        public SaveableMovablePipeNbt(ResourceLocation id, Vec3 vec3d, Vec3 vec3d2, String string, int useCount, BlockPos blockPos, boolean shouldSave, boolean shouldMove, boolean canOnlyBeUsedOnce, boolean canOnlyGoThroughOnePipe, boolean shouldCopy, ResourceLocation nbtId) {
            this.savedID = id;
            this.vec3d = vec3d;
            this.vec3d2 = vec3d2;
            this.string = string;
            this.useCount = useCount;
            this.blockPos = blockPos;
            this.shouldSave = shouldSave;
            this.shouldMove = shouldMove;
            this.canOnlyBeUsedOnce = canOnlyBeUsedOnce;
            this.canOnlyGoThroughOnePipe = canOnlyGoThroughOnePipe;
            this.shouldCopy = shouldCopy;
            this.nbtID = nbtId;
        }

        public SaveableMovablePipeNbt(GameEvent event, Vec3 originPos, @Nullable GameEvent.Context emitter, BlockPos pipePos) {
            this.savedID = BuiltInRegistries.GAME_EVENT.getKey(event);
            this.vec3d = originPos;
            this.vec3d2 = originPos;
            if (emitter != null && emitter.sourceEntity() != null) {
                this.string = emitter.sourceEntity().getUUID().toString();
            } else {
                this.string = "noEntity";
            }
            this.blockPos = pipePos;
            this.nbtID = ResourceLocation.tryBuild("lunade", "default");
            this.useCount = 0;
            this.canOnlyGoThroughOnePipe = false;
            this.canOnlyBeUsedOnce = false;
            this.shouldSave = true;
            this.shouldMove = true;
            this.shouldCopy = false;
        }

        public SaveableMovablePipeNbt(GameEvent event, Vec3 originPos, @Nullable Entity entity, BlockPos pipePos) {
            this.savedID = BuiltInRegistries.GAME_EVENT.getKey(event);
            this.vec3d = originPos;
            this.vec3d2 = originPos;
            if (entity != null) {
                this.string = entity.getUUID().toString();
            } else {
                this.string = "noEntity";
            }
            this.blockPos = pipePos;
            this.nbtID = ResourceLocation.tryBuild("lunade", "default");
            this.useCount = 0;
            this.canOnlyGoThroughOnePipe = false;
            this.canOnlyBeUsedOnce = false;
            this.shouldSave = true;
            this.shouldMove = true;
            this.shouldCopy = false;
        }

        public SaveableMovablePipeNbt() {
            this.savedID = ResourceLocation.tryBuild("lunade", "none");
            this.vec3d = new Vec3(0, -64, 0);
            this.vec3d2 = new Vec3(0, -64, 0);
            this.string = "none";
            this.blockPos = new BlockPos(0, -64, 0);
            this.nbtID = ResourceLocation.tryBuild("lunade", "none");
            this.useCount = 0;
            this.canOnlyGoThroughOnePipe = false;
            this.canOnlyBeUsedOnce = false;
            this.shouldSave = true;
            this.shouldMove = true;
            this.shouldCopy = false;
        }

        public SaveableMovablePipeNbt withSavedId(ResourceLocation id) {
            this.setNbtID(id);
            return this;
        }

        public SaveableMovablePipeNbt withVec3d(Vec3 pos) {
            this.vec3d = pos;
            return this;
        }

        public SaveableMovablePipeNbt withVec3d2(Vec3 pos) {
            this.vec3d2 = pos;
            return this;
        }

        public SaveableMovablePipeNbt withString(String string) {
            this.string = string;
            return this;
        }

        public SaveableMovablePipeNbt withUseCount(int count) {
            this.useCount = count;
            return this;
        }

        public SaveableMovablePipeNbt withBlockPos(BlockPos pos) {
            this.blockPos = pos;
            return this;
        }

        public SaveableMovablePipeNbt withShouldSave(boolean bool) {
            this.shouldSave = bool;
            return this;
        }

        public SaveableMovablePipeNbt withShouldMove(boolean bool) {
            this.shouldMove = bool;
            return this;
        }

        public SaveableMovablePipeNbt withOnlyUseableOnce(boolean bool) {
            this.canOnlyBeUsedOnce = bool;
            return this;
        }

        public SaveableMovablePipeNbt withOnlyThroughOnePipe(boolean bool) {
            this.canOnlyGoThroughOnePipe = bool;
            return this;
        }

        public SaveableMovablePipeNbt withShouldCopy(boolean bool) {
            this.shouldCopy = bool;
            return this;
        }

        public SaveableMovablePipeNbt withNBTID(ResourceLocation id) {
            this.setNbtID(id);
            return this;
        }

        public void dispense(ServerLevel world, BlockPos pos, BlockState state, CopperPipeEntity pipeEntity) {
            RegisterPipeNbtMethods.DispenseMethod method = RegisterPipeNbtMethods.getDispense(this.getNbtID());
            if (method != null) {
                method.dispense(this, world, pos, state, pipeEntity);
            }
        }

        public void onMove(ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity) {
            RegisterPipeNbtMethods.OnMoveMethod method = RegisterPipeNbtMethods.getMove(this.getNbtID());
            if (method != null) {
                method.onMove(this, world, pos, state, blockEntity);
            }
        }

        public void tick(ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity) { //Will be called at the CURRENT location, not the Pipe/Fitting it moves to on that tick - it can run this method and be dispensed on the same tick.
            RegisterPipeNbtMethods.TickMethod method = RegisterPipeNbtMethods.getTick(this.getNbtID());
            if (method != null) {
                method.tick(this, world, pos, state, blockEntity);
            }
        }

        public boolean canMove(ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity) {
            RegisterPipeNbtMethods.CanMoveMethod method = RegisterPipeNbtMethods.getCanMove(this.getNbtID());
            if (method != null) {
                return method.canMove(this, world, pos, state, blockEntity);
            } else {
                return true;
            }
        }

        @Nullable
        public Entity getEntity(Level world) {
            if (!this.string.equals("noEntity")) {
                if (this.foundEntity != null) {
                    if (this.foundEntity.getUUID().toString().equals(this.string)) {
                        return this.foundEntity;
                    } else {
                        this.foundEntity = null;
                    }
                }
                AABB box = new AABB(this.vec3d2.add(-32, -32, -32), this.vec3d2.add(32, 32, 32));
                List<Entity> entities = world.getEntitiesOfClass(Entity.class, box);
                for (Entity entity : entities) {
                    if (entity.getUUID().toString().equals(this.string)) {
                        this.foundEntity = entity;
                        this.vec3d2 = entity.position();
                        return entity;
                    }
                }
            }
            return null;
        }

        public ResourceLocation getSavedID() {
            return this.savedID;
        }

        public Vec3 getVec3d() {
            return this.vec3d;
        }

        public Vec3 getVec3d2() {
            return this.vec3d2;
        }

        public String getString() {
            return this.string;
        }

        public int getUseCount() {
            return this.useCount;
        }

        public BlockPos getBlockPos() {
            return this.blockPos;
        }

        public boolean getShouldSave() {
            return this.shouldSave;
        }

        public boolean getShouldMove() {
            return this.shouldMove;
        }

        public boolean getCanOnlyBeUsedOnce() {
            return this.canOnlyBeUsedOnce;
        }

        public boolean getCanOnlyGoThroughOnePipe() {
            return this.canOnlyGoThroughOnePipe;
        }

        public boolean getShouldCopy() {
            return this.shouldCopy;
        }

        public ResourceLocation getNbtID() {
            return this.nbtID;
        }

        public void setNbtID(ResourceLocation id) {
            this.nbtID = id;
        }

        public SaveableMovablePipeNbt copyOf() {
            return new SaveableMovablePipeNbt(this.savedID, this.vec3d, this.vec3d2, this.string, this.useCount, this.blockPos, this.shouldSave, this.shouldMove, this.canOnlyBeUsedOnce, this.canOnlyGoThroughOnePipe, this.shouldCopy, this.nbtID);
        }
    }

}

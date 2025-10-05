package net.lunade.copper.block.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.block.entity.AbstractSimpleCopperBlockEntity;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.lunade.copper.registry.TransferablePipeData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransferablePipeDataHandler {
	public ArrayList<SaveableTransferablePipeData> savedData = new ArrayList<>();
	public ArrayList<ResourceLocation> savedIds = new ArrayList<>();

	public TransferablePipeDataHandler() {
	}

	public void load(@NotNull ValueInput input) {
		input.read("transferablePipeData", SaveableTransferablePipeData.CODEC.listOf()).ifPresent(list -> {
			for (SaveableTransferablePipeData saveableTransferablePipeData : list) {
				if (saveableTransferablePipeData.shouldSave) this.addSaveableMoveablePipeNbt(saveableTransferablePipeData);
			}
		});
	}

	public void save(@NotNull ValueOutput output) {
		output.store("transferablePipeData", SaveableTransferablePipeData.CODEC.listOf(), this.savedData);
	}

	public void addSaveableMoveablePipeNbt(@NotNull TransferablePipeDataHandler.SaveableTransferablePipeData nbt) {
		if (this.savedIds.contains(nbt.getID())) return;
		this.savedData.add(nbt);
		this.savedIds.add(nbt.getID());
	}

	@Nullable
	public TransferablePipeDataHandler.SaveableTransferablePipeData getTransferablePipeData(ResourceLocation id) {
		if (this.savedIds.contains(id) && !this.savedData.isEmpty()) return this.savedData.get(this.savedIds.indexOf(id));
		return null;
	}

	public void removeTransferablePipeData(ResourceLocation id) {
		if (!this.savedIds.contains(id)) return;
		this.savedData.remove(this.savedIds.indexOf(id));
		this.savedIds.remove(id);
	}

	public void setTransferablePipeData(ResourceLocation id, SaveableTransferablePipeData nbt) {
		if (this.savedIds.contains(id)) {
			this.savedData.set(this.savedIds.indexOf(id), nbt);
		} else {
			this.savedIds.add(id);
			this.savedData.add(nbt);
		}
	}

	public void clear() {
		this.savedData.clear();
		this.savedIds.clear();
	}

	public void clearAllButNonMoveable() {
		ArrayList<SaveableTransferablePipeData> nbtToRemove = new ArrayList<>();
		this.savedData.clear();
		this.savedIds.clear();
		for (SaveableTransferablePipeData nbt : this.savedData) {
			if (nbt.shouldMove()) nbtToRemove.add(nbt);
		}
		for (SaveableTransferablePipeData nbt : nbtToRemove) {
			if (this.savedData.contains(nbt)) {
				int index = this.savedData.indexOf(nbt);
				this.savedData.remove(index);
				this.savedIds.remove(index);
			}
		}
	}

	public void clearAllButMoveable() {
		ArrayList<SaveableTransferablePipeData> nbtToRemove = new ArrayList<>();
		this.savedData.clear();
		this.savedIds.clear();
		for (SaveableTransferablePipeData nbt : this.savedData) {
			if (!nbt.shouldMove()) nbtToRemove.add(nbt);
		}
		for (SaveableTransferablePipeData nbt : nbtToRemove) {
			if (this.savedData.contains(nbt)) {
				int index = this.savedData.indexOf(nbt);
				this.savedData.remove(index);
				this.savedIds.remove(index);
			}
		}
	}

	public ArrayList<SaveableTransferablePipeData> getSavedDataList() {
		return this.savedData;
	}

	public static class SaveableTransferablePipeData {
		public static final Codec<SaveableTransferablePipeData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ResourceLocation.CODEC.fieldOf("savedID").forGetter(SaveableTransferablePipeData::getSavedID),
			Vec3.CODEC.fieldOf("vec3d").forGetter(SaveableTransferablePipeData::getVec3d),
			Vec3.CODEC.fieldOf("vec3d2").forGetter(SaveableTransferablePipeData::getVec3d2),
			Codec.STRING.fieldOf("string").forGetter(SaveableTransferablePipeData::getString),
			Codec.INT.fieldOf("useCount").forGetter(SaveableTransferablePipeData::getUseCount),
			BlockPos.CODEC.fieldOf("blockPos").forGetter(SaveableTransferablePipeData::blockPos),
			Codec.BOOL.fieldOf("shouldSave").forGetter(SaveableTransferablePipeData::shouldSave),
			Codec.BOOL.fieldOf("shouldMove").forGetter(SaveableTransferablePipeData::shouldMove),
			Codec.BOOL.fieldOf("canOnlyBeUsedOnce").forGetter(SaveableTransferablePipeData::canOnlyBeUsedOnce),
			Codec.BOOL.fieldOf("canOnlyGoThroughOnePipe").forGetter(SaveableTransferablePipeData::canOnlyGoThroughOnePipe),
			Codec.BOOL.fieldOf("shouldCopy").forGetter(SaveableTransferablePipeData::shouldCopy),
			ResourceLocation.CODEC.fieldOf("nbtId").forGetter(SaveableTransferablePipeData::getID)
		).apply(instance, SaveableTransferablePipeData::new));
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
		private ResourceLocation ID;

		public SaveableTransferablePipeData(ResourceLocation id, Vec3 vec3d, Vec3 vec3d2, String string, int useCount, BlockPos blockPos, boolean shouldSave, boolean shouldMove, boolean canOnlyBeUsedOnce, boolean canOnlyGoThroughOnePipe, boolean shouldCopy, ResourceLocation nbtId) {
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
			this.ID = nbtId;
		}

		public SaveableTransferablePipeData(GameEvent event, Vec3 originPos, @Nullable GameEvent.Context emitter, BlockPos pipePos) {
			this.savedID = BuiltInRegistries.GAME_EVENT.getKey(event);
			this.vec3d = originPos;
			this.vec3d2 = originPos;
			if (emitter != null && emitter.sourceEntity() != null) {
				this.string = emitter.sourceEntity().getUUID().toString();
			} else {
				this.string = "noEntity";
			}
			this.blockPos = pipePos;
			this.ID = ResourceLocation.tryBuild(SimpleCopperPipesConstants.MOD_ID, "default");
			this.useCount = 0;
			this.canOnlyGoThroughOnePipe = false;
			this.canOnlyBeUsedOnce = false;
			this.shouldSave = true;
			this.shouldMove = true;
			this.shouldCopy = false;
		}

		public SaveableTransferablePipeData(GameEvent event, Vec3 originPos, @Nullable Entity entity, BlockPos pipePos) {
			this.savedID = BuiltInRegistries.GAME_EVENT.getKey(event);
			this.vec3d = originPos;
			this.vec3d2 = originPos;
			if (entity != null) {
				this.string = entity.getUUID().toString();
			} else {
				this.string = "noEntity";
			}
			this.blockPos = pipePos;
			this.ID = ResourceLocation.tryBuild(SimpleCopperPipesConstants.MOD_ID, "default");
			this.useCount = 0;
			this.canOnlyGoThroughOnePipe = false;
			this.canOnlyBeUsedOnce = false;
			this.shouldSave = true;
			this.shouldMove = true;
			this.shouldCopy = false;
		}

		public SaveableTransferablePipeData() {
			this.savedID = ResourceLocation.tryBuild(SimpleCopperPipesConstants.MOD_ID, "none");
			this.vec3d = new Vec3(0, -64, 0);
			this.vec3d2 = new Vec3(0, -64, 0);
			this.string = "none";
			this.blockPos = new BlockPos(0, -64, 0);
			this.ID = ResourceLocation.tryBuild(SimpleCopperPipesConstants.MOD_ID, "none");
			this.useCount = 0;
			this.canOnlyGoThroughOnePipe = false;
			this.canOnlyBeUsedOnce = false;
			this.shouldSave = true;
			this.shouldMove = true;
			this.shouldCopy = false;
		}

		public SaveableTransferablePipeData withSavedId(ResourceLocation id) {
			this.setID(id);
			return this;
		}

		public SaveableTransferablePipeData withVec3d(Vec3 pos) {
			this.vec3d = pos;
			return this;
		}

		public SaveableTransferablePipeData withVec3d2(Vec3 pos) {
			this.vec3d2 = pos;
			return this;
		}

		public SaveableTransferablePipeData withString(String string) {
			this.string = string;
			return this;
		}

		public SaveableTransferablePipeData withUseCount(int count) {
			this.useCount = count;
			return this;
		}

		public SaveableTransferablePipeData withBlockPos(BlockPos pos) {
			this.blockPos = pos;
			return this;
		}

		public SaveableTransferablePipeData withShouldSave(boolean shouldSave) {
			this.shouldSave = shouldSave;
			return this;
		}

		public SaveableTransferablePipeData withShouldMove(boolean shouldMove) {
			this.shouldMove = shouldMove;
			return this;
		}

		public SaveableTransferablePipeData withOnlyUseableOnce(boolean onlyUseableOnce) {
			this.canOnlyBeUsedOnce = onlyUseableOnce;
			return this;
		}

		public SaveableTransferablePipeData withOnlyThroughOnePipe(boolean onlyThroughOnePipe) {
			this.canOnlyGoThroughOnePipe = onlyThroughOnePipe;
			return this;
		}

		public SaveableTransferablePipeData withShouldCopy(boolean shouldCopy) {
			this.shouldCopy = shouldCopy;
			return this;
		}

		public SaveableTransferablePipeData withID(ResourceLocation id) {
			this.setID(id);
			return this;
		}

		public void dispense(ServerLevel world, BlockPos pos, BlockState state, CopperPipeBlockEntity pipeEntity) {
			TransferablePipeData.Dispsense method = TransferablePipeData.getDispenseBehavior(this.getID());
			if (method != null) {
				method.dispense(this, world, pos, state, pipeEntity);
			}
		}

		public void onMove(ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity) {
			TransferablePipeData.Move method = TransferablePipeData.getMoveBehavior(this.getID());
			if (method != null) method.onMove(this, world, pos, state, blockEntity);
		}

		public void tick(ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity) { //Will be called at the CURRENT location, not the Pipe/Fitting it moves to on that tick - it can run this method and be dispensed on the same tick.
			TransferablePipeData.Tick method = TransferablePipeData.getTickBehavior(this.getID());
			if (method != null) method.tick(this, world, pos, state, blockEntity);
		}

		public boolean canMove(ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity) {
			TransferablePipeData.CanMove method = TransferablePipeData.getCanMovePredicate(this.getID());
			if (method != null) return method.canMove(this, world, pos, state, blockEntity);
			return true;
		}

		@Nullable
		public Entity getEntity(Level world) {
			if (!this.string.equals("noEntity")) {
				if (this.foundEntity != null) {
					if (this.foundEntity.getUUID().toString().equals(this.string)) return this.foundEntity;
					this.foundEntity = null;
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

		public BlockPos blockPos() {
			return this.blockPos;
		}

		public boolean shouldSave() {
			return this.shouldSave;
		}

		public boolean shouldMove() {
			return this.shouldMove;
		}

		public boolean canOnlyBeUsedOnce() {
			return this.canOnlyBeUsedOnce;
		}

		public boolean canOnlyGoThroughOnePipe() {
			return this.canOnlyGoThroughOnePipe;
		}

		public boolean shouldCopy() {
			return this.shouldCopy;
		}

		public ResourceLocation getID() {
			return this.ID;
		}

		public void setID(ResourceLocation id) {
			this.ID = id;
		}

		public SaveableTransferablePipeData copyOf() {
			return new SaveableTransferablePipeData(
				this.savedID,
				this.vec3d,
				this.vec3d2,
				this.string,
				this.useCount,
				this.blockPos,
				this.shouldSave,
				this.shouldMove,
				this.canOnlyBeUsedOnce,
				this.canOnlyGoThroughOnePipe,
				this.shouldCopy,
				this.ID
			);
		}
	}

}

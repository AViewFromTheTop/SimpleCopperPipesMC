package net.lunade.copper.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Map;
import java.util.Optional;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.block.entity.AbstractSimpleCopperBlockEntity;
import net.lunade.copper.block.entity.CopperFittingBlockEntity;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.lunade.copper.block.entity.data.TransferablePipeDataHandler;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.networking.packet.SimpleCopperPipesNoteParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import static net.minecraft.world.level.block.NoteBlock.INSTRUMENT;
import static net.minecraft.world.level.block.NoteBlock.NOTE;
import net.minecraft.world.level.block.state.BlockState;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TransferablePipeData {
	public static final Identifier WATER = SimpleCopperPipesConstants.id("water");
	public static final Identifier LAVA = SimpleCopperPipesConstants.id("lava");
	public static final Identifier SMOKE = SimpleCopperPipesConstants.id("smoke");
	private static final Map<Identifier, Data> PIPE_DATA = new Object2ObjectLinkedOpenHashMap<>();

	public static void register(Identifier id, Dispense dispense, Move move, Tick tick, CanMove canMove) {
		PIPE_DATA.put(id, new Data(dispense, move, tick, canMove));
	}

	@Nullable
	public static TransferablePipeData.Data getPipeData(Identifier id) {
		return PIPE_DATA.get(id);
	}

	@Nullable
	public static TransferablePipeData.Dispense getDispenseBehavior(Identifier id) {
		final Data data = getPipeData(id);
		if (data != null) return data.dispense();
		return null;
	}

	@Nullable
	public static TransferablePipeData.Move getMoveBehavior(Identifier id) {
		final Data data = getPipeData(id);
		if (data != null) return data.move();
		return null;
	}

	@Nullable
	public static TransferablePipeData.Tick getTickBehavior(Identifier id) {
		final Data data = getPipeData(id);
		if (data != null) return data.tick();
		return null;
	}

	@Nullable
	public static TransferablePipeData.CanMove getCanMovePredicate(Identifier id) {
		final Data data = getPipeData(id);
		if (data != null) return data.canMove();
		return null;
	}

	public static void init() {
		register(Identifier.tryBuild(SimpleCopperPipesConstants.MOD_ID, "default"), (data, level, pos, state, pipe) -> {
            pipe.inputGameEventPos = data.blockPos();
            pipe.gameEventNbtVec3 = data.getVec3d();
            boolean noteBlock = false;

			final Optional<Holder.Reference<GameEvent>> optionalGameEvent = BuiltInRegistries.GAME_EVENT.get(data.getSavedID());
			if (optionalGameEvent.isPresent() && optionalGameEvent.get().value() == GameEvent.NOTE_BLOCK_PLAY.value()) {
				pipe.noteBlockCooldown = 40;
				final BlockPos originPos = BlockPos.containing(data.getVec3d());
				final BlockState originState = level.getBlockState(originPos);
				noteBlock = originState.is(Blocks.NOTE_BLOCK);
				if (noteBlock) {
					final int note = originState.getValue(NOTE);
					final float pitch = (float) Math.pow(2D, (note - 12D) / 12D);
					level.playSound(null, pos, originState.getValue(INSTRUMENT).getSoundEvent().value(), SoundSource.RECORDS, 3F, pitch);
					//Send NoteBlock Particle Packet To Client
					SimpleCopperPipesNoteParticlePacket.sendToAll(level, pos, note, level.getBlockState(pos).getValue(FACING));
				}
			}

			level.gameEvent(data.getEntity(level), optionalGameEvent.orElse(GameEvent.BLOCK_CHANGE), pos);

			if ((noteBlock || pipe.noteBlockCooldown > 0) && data.useCount == 0) {
				level.sendParticles(
					new VibrationParticleOption(new BlockPositionSource(data.blockPos()), 5),
					data.getVec3d().x, data.getVec3d().y, data.getVec3d().z,
					1, 0D, 0D, 0D, 0D
				);
				data.useCount = 1;
			}
		}, (data, level, pos, state, blockEntity) -> {

		}, (data, level, pos, state, blockEntity) -> {
			if (data.foundEntity != null) data.vec3d2 = data.foundEntity.position();
		}, (data, level, pos, state, blockEntity) -> true);

		register(WATER, (data, level, pos, state, pipe) -> {

		}, (data, level, pos, state, blockEntity) -> {
			if (blockEntity instanceof CopperFittingBlockEntity) {
				data.vec3d = new Vec3(11, 0, 0);
				return;
			}
			if (blockEntity.canWater || blockEntity.moveType != AbstractSimpleCopperBlockEntity.MoveType.FROM_PIPE) return;

			data.vec3d = data.getVec3d().add(-1, 0, 0);
			if (data.getVec3d().x() > 0) return;

			data.shouldSave = false;
			data.shouldMove = false;
		}, (data, level, pos, state, blockEntity) -> {

		}, (data, level, pos, state, blockEntity) -> {
			if (!SimpleCopperPipesConfig.get().carryWater) return false;
			TransferablePipeDataHandler.SaveableTransferablePipeData waterData = blockEntity.transferableDataHandler.getTransferablePipeData(WATER);
			if (waterData != null) return waterData.getVec3d() == null || waterData.getVec3d().x() <= data.getVec3d().x() - 1;
			return true;
		});

		register(LAVA, (data, level, pos, state, pipe) -> {

		}, (data, level, pos, state, blockEntity) -> {
			if (blockEntity.transferableDataHandler.getTransferablePipeData(WATER) != null) {
				data.vec3d = Vec3.ZERO;
				data.shouldSave = false;
				data.shouldMove = false;
				return;
			}

			if (blockEntity instanceof CopperFittingBlockEntity) {
				data.vec3d = new Vec3(11, 0, 0);
				return;
			}

			if (blockEntity.canSmoke || blockEntity.moveType != AbstractSimpleCopperBlockEntity.MoveType.FROM_PIPE) return;

			data.vec3d = data.getVec3d().add(-1, 0, 0);
			if (data.getVec3d().x() > 0) return;

			data.shouldSave = false;
			data.shouldMove = false;
		}, (data, level, pos, state, blockEntity) -> {
			final TransferablePipeDataHandler.SaveableTransferablePipeData lavaData = blockEntity.transferableDataHandler.getTransferablePipeData(LAVA);
			final TransferablePipeDataHandler.SaveableTransferablePipeData waterData = blockEntity.transferableDataHandler.getTransferablePipeData(WATER);
			final TransferablePipeDataHandler.SaveableTransferablePipeData smokeData = blockEntity.transferableDataHandler.getTransferablePipeData(SMOKE);
			if (waterData != null && lavaData != null) {
				lavaData.vec3d = Vec3.ZERO;
				lavaData.shouldSave = false;
				lavaData.shouldMove = false;
				waterData.vec3d = Vec3.ZERO;
				waterData.shouldSave = false;
				waterData.shouldMove = false;
				if (smokeData == null) {
					blockEntity.transferableDataHandler.setTransferablePipeData(
						SMOKE,
						new TransferablePipeDataHandler.SaveableTransferablePipeData()
						.withVec3d(new Vec3(11D, 0D, 0D))
							.withShouldCopy(true)
							.withID(SMOKE)
					);
				} else {
					smokeData.vec3d = new Vec3(11D, 0D, 0D);
				}
			}
		}, (data, level, pos, state, blockEntity) -> {
			if (!SimpleCopperPipesConfig.get().carryLava) return false;
			final TransferablePipeDataHandler.SaveableTransferablePipeData lavaData = blockEntity.transferableDataHandler.getTransferablePipeData(LAVA);
			if (lavaData != null) return lavaData.getVec3d() == null || lavaData.getVec3d().x() <= data.getVec3d().x() - 1;
			return true;
		});

		register(SMOKE, (data, level, pos, state, pipe) -> {

		}, (data, level, pos, state, blockEntity) -> {
			if (blockEntity instanceof CopperFittingBlockEntity) {
				data.vec3d = new Vec3(11D, 0D, 0D);
				return;
			}

			if (blockEntity.canSmoke || blockEntity.moveType != AbstractSimpleCopperBlockEntity.MoveType.FROM_PIPE) return;

			data.vec3d = data.getVec3d().add(-1D, 0D, 0D);
			if (data.getVec3d().x() > 0D) return;

			data.shouldSave = false;
			data.shouldMove = false;
		}, (data, level, pos, state, blockEntity) -> {

		}, (data, level, pos, state, blockEntity) -> {
			if (!SimpleCopperPipesConfig.get().carrySmoke) return false;
			final TransferablePipeDataHandler.SaveableTransferablePipeData smokeData = blockEntity.transferableDataHandler.getTransferablePipeData(SMOKE);
			if (smokeData != null) return smokeData.getVec3d() == null || smokeData.getVec3d().x() <= data.getVec3d().x() - 1;
			return true;
		});

	}

	@FunctionalInterface
	public interface Dispense {
		void dispense(TransferablePipeDataHandler.SaveableTransferablePipeData data, ServerLevel level, BlockPos pos, BlockState state, CopperPipeBlockEntity pipe);
	}

	@FunctionalInterface
	public interface Move {
		void onMove(TransferablePipeDataHandler.SaveableTransferablePipeData data, ServerLevel level, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
	}

	@FunctionalInterface
	public interface Tick {
		void tick(TransferablePipeDataHandler.SaveableTransferablePipeData data, ServerLevel level, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
	}

	@FunctionalInterface
	public interface CanMove {
		boolean canMove(TransferablePipeDataHandler.SaveableTransferablePipeData data, ServerLevel level, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
	}

	public record Data(Dispense dispense, Move move, Tick tick, CanMove canMove) {
	}
}

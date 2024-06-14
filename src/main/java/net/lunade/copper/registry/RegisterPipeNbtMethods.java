package net.lunade.copper.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Map;
import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.lunade.copper.blocks.block_entity.AbstractSimpleCopperBlockEntity;
import net.lunade.copper.blocks.block_entity.CopperFittingEntity;
import net.lunade.copper.blocks.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.block_entity.pipe_nbt.MoveablePipeDataHandler;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.networking.packet.SimpleCopperPipesNoteParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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

public class RegisterPipeNbtMethods {
    public static final ResourceLocation WATER = SimpleCopperPipesSharedConstants.id("water");
    public static final ResourceLocation LAVA = SimpleCopperPipesSharedConstants.id("lava");
    public static final ResourceLocation SMOKE = SimpleCopperPipesSharedConstants.id("smoke");
    private static final Map<ResourceLocation, UniquePipeNbt> UNIQUE_PIPE_NBTS = new Object2ObjectLinkedOpenHashMap<>();

    public static void register(ResourceLocation id, DispenseMethod dispense, OnMoveMethod move, TickMethod tick, CanMoveMethod canMove) {
        UNIQUE_PIPE_NBTS.put(id, new UniquePipeNbt(dispense, move, tick, canMove));
    }

    @Nullable
    public static UniquePipeNbt getUniquePipeNbt(ResourceLocation id) {
        if (UNIQUE_PIPE_NBTS.containsKey(id)) {
            return UNIQUE_PIPE_NBTS.get(id);
        }
        return null;
    }

    @Nullable
    public static DispenseMethod getDispense(ResourceLocation id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.dispenseMethod();
        }
        return null;
    }

    @Nullable
    public static OnMoveMethod getMove(ResourceLocation id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.onMoveMethod();
        }
        return null;
    }

    @Nullable
    public static TickMethod getTick(ResourceLocation id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.tickMethod();
        }
        return null;
    }

    @Nullable
    public static CanMoveMethod getCanMove(ResourceLocation id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.canMoveMethod();
        }
        return null;
    }

    public static void init() {
        register(ResourceLocation.tryBuild("lunade", "default"), (nbt, world, pos, blockState, pipe) -> {
            boolean noteBlock = false;
            if (BuiltInRegistries.GAME_EVENT.get(nbt.getSavedID()) == GameEvent.NOTE_BLOCK_PLAY.value()) {
                pipe.noteBlockCooldown = 40;
                float volume = 3.0F;
                BlockPos originPos = BlockPos.containing(nbt.getVec3d());
                BlockState state = world.getBlockState(originPos);
                noteBlock = state.is(Blocks.NOTE_BLOCK);
                if (noteBlock) {
                    int k = state.getValue(NOTE);
                    float f = (float) Math.pow(2.0D, (double) (k - 12) / 12.0D);
                    world.playSound(null, pos, state.getValue(INSTRUMENT).getSoundEvent().value(), SoundSource.RECORDS, volume, f);
                    //Send NoteBlock Particle Packet To Client
                    SimpleCopperPipesNoteParticlePacket.sendToAll(world, pos, k, world.getBlockState(pos).getValue(FACING));
                }
            }
            world.gameEvent(nbt.getEntity(world), BuiltInRegistries.GAME_EVENT.getHolder(nbt.getSavedID()).orElse(GameEvent.BLOCK_CHANGE), pos);
            if (noteBlock || pipe.noteBlockCooldown > 0) {
                if (nbt.useCount == 0) {
                    world.sendParticles(new VibrationParticleOption(new BlockPositionSource(nbt.getBlockPos()), 5), nbt.getVec3d().x, nbt.getVec3d().y, nbt.getVec3d().z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    nbt.useCount = 1;
                }
            }
            pipe.inputGameEventPos = nbt.getBlockPos();
            pipe.gameEventNbtVec3 = nbt.getVec3d();
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (nbt.foundEntity != null) {
                nbt.vec3d2 = nbt.foundEntity.position();
            }
        }, (nbt, world, pos, blockState, blockEntity) -> true);


        register(WATER, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CopperFittingEntity) {
                nbt.vec3d = new Vec3(11, 0, 0);
            } else if (!blockEntity.canWater && blockEntity.moveType == AbstractSimpleCopperBlockEntity.MOVE_TYPE.FROM_PIPE) {
                nbt.vec3d = nbt.getVec3d().add(-1, 0, 0);
                if (nbt.getVec3d().x() <= 0) {
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (!SimpleCopperPipesConfig.get().carryWater) {
                return false;
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER);
            if (movablePipeNbt != null) {
                return movablePipeNbt.getVec3d() == null || movablePipeNbt.getVec3d().x() <= nbt.getVec3d().x() - 1;
            }
            return true;
        });

        register(LAVA, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER) == null) {
                if (blockEntity instanceof CopperFittingEntity) {
                    nbt.vec3d = new Vec3(11, 0, 0);
                } else if (!blockEntity.canSmoke && blockEntity.moveType == AbstractSimpleCopperBlockEntity.MOVE_TYPE.FROM_PIPE) {
                    nbt.vec3d = nbt.getVec3d().add(-1, 0, 0);
                    if (nbt.getVec3d().x() <= 0) {
                        nbt.shouldSave = false;
                        nbt.shouldMove = false;
                    }
                }
            } else {
                nbt.vec3d = Vec3.ZERO;
                nbt.shouldSave = false;
                nbt.shouldMove = false;
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {
            MoveablePipeDataHandler.SaveableMovablePipeNbt lavaNBT = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(LAVA);
            MoveablePipeDataHandler.SaveableMovablePipeNbt waterNBT = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(WATER);
            MoveablePipeDataHandler.SaveableMovablePipeNbt smokeNBT = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(SMOKE);
            if (waterNBT != null && lavaNBT != null) {
                lavaNBT.vec3d = Vec3.ZERO;
                lavaNBT.shouldSave = false;
                lavaNBT.shouldMove = false;
                waterNBT.vec3d = Vec3.ZERO;
                waterNBT.shouldSave = false;
                waterNBT.shouldMove = false;
                if (smokeNBT == null) {
                    blockEntity.moveablePipeDataHandler.setMoveablePipeNbt(SMOKE, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                            .withVec3d(new Vec3(11, 0, 0)).withShouldCopy(true).withNBTID(SMOKE));
                } else {
                    smokeNBT.vec3d = new Vec3(11, 0, 0);
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (!SimpleCopperPipesConfig.get().carryLava) {
                return false;
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(LAVA);
            if (movablePipeNbt != null) {
                return movablePipeNbt.getVec3d() == null || movablePipeNbt.getVec3d().x() <= nbt.getVec3d().x() - 1;
            }
            return true;
        });


        register(SMOKE, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CopperFittingEntity) {
                nbt.vec3d = new Vec3(11, 0, 0);
            } else if (!blockEntity.canSmoke && blockEntity.moveType == AbstractSimpleCopperBlockEntity.MOVE_TYPE.FROM_PIPE) {
                nbt.vec3d = nbt.getVec3d().add(-1, 0, 0);
                if (nbt.getVec3d().x() <= 0) {
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (!SimpleCopperPipesConfig.get().carrySmoke) {
                return false;
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(SMOKE);
            if (movablePipeNbt != null) {
                return movablePipeNbt.getVec3d() == null || movablePipeNbt.getVec3d().x() <= nbt.getVec3d().x() - 1;
            }
            return true;
        });

    }

    @FunctionalInterface
    public interface DispenseMethod {
        void dispense(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerLevel world, BlockPos pos, BlockState state, CopperPipeEntity pipe);
    }

    @FunctionalInterface
    public interface OnMoveMethod {
        void onMove(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface TickMethod {
        void tick(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface CanMoveMethod {
        boolean canMove(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerLevel world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
    }

    public record UniquePipeNbt(
            DispenseMethod dispenseMethod,
            OnMoveMethod onMoveMethod,
            TickMethod tickMethod,
            CanMoveMethod canMoveMethod
    ) {
    }


}

package net.lunade.copper;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lunade.copper.block_entity.AbstractSimpleCopperBlockEntity;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.lunade.copper.registry.SimpleCopperRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static net.lunade.copper.blocks.CopperFitting.CORRODED_FITTING;
import static net.minecraft.world.level.block.NoteBlock.INSTRUMENT;
import static net.minecraft.world.level.block.NoteBlock.NOTE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class RegisterPipeNbtMethods {

    public static void register(ResourceLocation id, DispenseMethod dispense, OnMoveMethod move, TickMethod tick, CanMoveMethod canMove) {
        Registry.register(SimpleCopperRegistries.UNIQUE_PIPE_NBTS, id, new UniquePipeNbt(dispense, move, tick, canMove));
    }

    public record UniquePipeNbt(
            DispenseMethod dispenseMethod,
            OnMoveMethod onMoveMethod,
            TickMethod tickMethod,
            CanMoveMethod canMoveMethod
    ) {}
    
    @Nullable
    public static UniquePipeNbt getUniquePipeNbt(ResourceLocation id) {
        if (SimpleCopperRegistries.UNIQUE_PIPE_NBTS.containsKey(id)) {
            return SimpleCopperRegistries.UNIQUE_PIPE_NBTS.get(id);
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

    public static void init() {
        register(new ResourceLocation("lunade", "default"), (nbt, world, pos, blockState, pipe) -> {
            Direction direction = blockState.getValue(FACING);
            Direction directionOpp = direction.getOpposite();
            boolean noteBlock = false;
            if (BuiltInRegistries.GAME_EVENT.get(nbt.getSavedID()) == GameEvent.NOTE_BLOCK_PLAY) {
                pipe.noteBlockCooldown = 40;
                boolean corroded;
                float volume = 3.0F;
                if (blockState.getBlock() instanceof CopperPipe) { //Corroded Pipes Increase Instrument Sound Volume
                    corroded = blockState.getBlock() == CopperPipe.CORRODED_PIPE || world.getBlockState(pos.relative(directionOpp)).getBlock() == CORRODED_FITTING;
                    if (corroded) {
                        volume = 4.5F;
                    }
                }
                BlockPos originPos = new BlockPos(nbt.getVec3d());
                noteBlock = world.getBlockState(originPos).is(Blocks.NOTE_BLOCK);
                if (noteBlock) {
                    BlockState state = world.getBlockState(originPos);
                    int k = state.getValue(NOTE);
                    float f = (float) Math.pow(2.0D, (double) (k - 12) / 12.0D);
                    world.playSound(null, pos, state.getValue(INSTRUMENT).getSoundEvent().value(), SoundSource.RECORDS, volume, f);
                    //Send NoteBlock Particle Packet To Client
                    FriendlyByteBuf buf = PacketByteBufs.create();
                    buf.writeBlockPos(pos);
                    buf.writeInt(k);
                    buf.writeInt(getDirection(world.getBlockState(pos).getValue(FACING)));
                    for (ServerPlayer player : PlayerLookup.tracking(world, pos)) {
                        ServerPlayNetworking.send(player, CopperPipeMain.NOTE_PACKET, buf);
                    }
                }
            }
            world.gameEvent(nbt.getEntity(world), BuiltInRegistries.GAME_EVENT.get(nbt.getSavedID()), pos);
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


        register(CopperPipeMain.WATER, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CopperFittingEntity) {
                nbt.vec3d = new Vec3(11, 0, 0);
            } else if (!blockEntity.canSmoke && blockEntity.moveType == AbstractSimpleCopperBlockEntity.MOVE_TYPE.FROM_PIPE) {
                nbt.vec3d = nbt.vec3d.add(-1, 0, 0);
                if (nbt.getVec3d().x() <= 0) {
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(CopperPipeMain.WATER);
            if (movablePipeNbt != null) {
                return movablePipeNbt.getVec3d() == null || movablePipeNbt.getVec3d().x() <= nbt.getVec3d().x() - 1;
            }
            return true;
        });


        register(CopperPipeMain.SMOKE, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CopperFittingEntity) {
                nbt.vec3d = new Vec3(11, 0, 0);
            } else if (!blockEntity.canSmoke && blockEntity.moveType == AbstractSimpleCopperBlockEntity.MOVE_TYPE.FROM_PIPE) {
                nbt.vec3d = nbt.vec3d.add(-1, 0, 0);
                if (nbt.getVec3d().x() <= 0) {
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(CopperPipeMain.SMOKE);
            if (movablePipeNbt != null) {
                return movablePipeNbt.getVec3d() == null || movablePipeNbt.getVec3d().x() <= nbt.getVec3d().x() - 1;
            }
            return true;
        });

        }

    public static int getDirection(Direction direction) {
        if (direction == Direction.UP) {
            return 1;
        }
        if (direction == Direction.DOWN) {
            return 2;
        }
        if (direction == Direction.NORTH) {
            return 3;
        }
        if (direction == Direction.SOUTH) {
            return 4;
        }
        if (direction == Direction.EAST) {
            return 5;
        }
        if (direction == Direction.WEST) {
            return 6;
        }
        return 3;
    }


}

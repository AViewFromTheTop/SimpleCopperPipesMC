package net.lunade.copper;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lunade.copper.block_entity.AbstractSimpleCopperBlockEntity;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.pipe_nbt.MoveablePipeDataHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Vibration;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.lunade.copper.registry.SimpleCopperRegistries;
import org.jetbrains.annotations.Nullable;

import static net.lunade.copper.blocks.CopperFitting.CORRODED_FITTING;
import static net.minecraft.block.NoteBlock.INSTRUMENT;
import static net.minecraft.block.NoteBlock.NOTE;
import static net.minecraft.state.property.Properties.FACING;

public class RegisterPipeNbtMethods {

    public static void register(Identifier id, DispenseMethod dispense, OnMoveMethod move, TickMethod tick, CanMoveMethod canMove) {
        Registry.register(SimpleCopperRegistries.UNIQUE_PIPE_NBTS, id, new UniquePipeNbt(dispense, move, tick, canMove));
    }

    public record UniquePipeNbt(
            DispenseMethod dispenseMethod,
            OnMoveMethod onMoveMethod,
            TickMethod tickMethod,
            CanMoveMethod canMoveMethod
    ) {}

    @Nullable
    public static UniquePipeNbt getUniquePipeNbt(Identifier id) {
        if (SimpleCopperRegistries.UNIQUE_PIPE_NBTS.containsId(id)) {
            return SimpleCopperRegistries.UNIQUE_PIPE_NBTS.get(id);
        }
        return null;
    }

    @Nullable
    public static DispenseMethod getDispense(Identifier id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.dispenseMethod();
        }
        return null;
    }

    @Nullable
    public static OnMoveMethod getMove(Identifier id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.onMoveMethod();
        }
        return null;
    }

    @Nullable
    public static TickMethod getTick(Identifier id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.tickMethod();
        }
        return null;
    }

    @Nullable
    public static CanMoveMethod getCanMove(Identifier id) {
        UniquePipeNbt uniquePipeNbt = getUniquePipeNbt(id);
        if (uniquePipeNbt != null) {
            return uniquePipeNbt.canMoveMethod();
        }
        return null;
    }

    public interface DispenseMethod {
        void dispense(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipe);
    }

    @FunctionalInterface
    public interface OnMoveMethod {
        void onMove(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerWorld world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface TickMethod {
        void tick(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerWorld world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
    }

    @FunctionalInterface
    public interface CanMoveMethod {
        boolean canMove(MoveablePipeDataHandler.SaveableMovablePipeNbt nbt, ServerWorld world, BlockPos pos, BlockState state, AbstractSimpleCopperBlockEntity blockEntity);
    }

    public static void init() {
        register(new Identifier("lunade", "default"), (nbt, world, pos, blockState, pipe) -> {
            Direction direction = blockState.get(FACING);
            Direction directionOpp = direction.getOpposite();
            boolean noteBlock = false;
            if (Registry.GAME_EVENT.get(nbt.getSavedID()) == CopperPipeMain.NOTE_BLOCK_PLAY) {
                pipe.noteBlockCooldown = 40;
                boolean corroded;
                float volume = 3.0F;
                if (blockState.getBlock() instanceof CopperPipe) { //Corroded Pipes Increase Instrument Sound Volume
                    corroded = blockState.getBlock() == CopperPipe.CORRODED_PIPE || world.getBlockState(pos.offset(directionOpp)).getBlock() == CORRODED_FITTING;
                    if (corroded) {
                        volume = 4.5F;
                    }
                }
                BlockPos originPos = new BlockPos(nbt.getVec3d());
                noteBlock = world.getBlockState(originPos).isOf(Blocks.NOTE_BLOCK);
                if (noteBlock) {
                    BlockState state = world.getBlockState(originPos);
                    int k = state.get(NOTE);
                    float f = (float) Math.pow(2.0D, (double) (k - 12) / 12.0D);
                    world.playSound(null, pos, state.get(INSTRUMENT).getSound(), SoundCategory.RECORDS, volume, f);
                    //Send NoteBlock Particle Packet To Client
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeBlockPos(pos);
                    buf.writeInt(k);
                    buf.writeInt(getDirection(world.getBlockState(pos).get(FACING)));
                    for (ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
                        ServerPlayNetworking.send(player, CopperPipeMain.NOTE_PACKET, buf);
                    }
                }
            }
            world.emitGameEvent(nbt.getEntity(world), Registry.GAME_EVENT.get(nbt.getSavedID()), pos);
            if (noteBlock || pipe.noteBlockCooldown > 0) {
                if (nbt.useCount == 0) {
                    spawnDelayedVibration(world, new BlockPos(nbt.getVec3d()), nbt.getBlockPos(), 5);
                    nbt.useCount = 1;
                }
            }
            pipe.inputGameEventPos = nbt.getBlockPos();
            pipe.gameEventNbtVec3 = nbt.getVec3d();
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (nbt.foundEntity != null) {
                nbt.vec3d2 = nbt.foundEntity.getPos();
            }
        }, (nbt, world, pos, blockState, blockEntity) -> true);


        register(CopperPipeMain.WATER, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CopperFittingEntity) {
                nbt.vec3d = new Vec3d(11, 0, 0);
            } else if (!blockEntity.canSmoke && blockEntity.moveType == MoveablePipeDataHandler.MOVE_TYPE.FROM_PIPE) {
                nbt.vec3d = nbt.vec3d.add(-1, 0, 0);
                if (nbt.getVec3d().getX() <= 0) {
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(CopperPipeMain.WATER);
            if (movablePipeNbt != null) {
                return movablePipeNbt.getVec3d() == null || movablePipeNbt.getVec3d().getX() <= nbt.getVec3d().getX() - 1;
            }
            return true;
        });


        register(CopperPipeMain.SMOKE, (nbt, world, pos, blockState, pipe) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CopperFittingEntity) {
                nbt.vec3d = new Vec3d(11, 0, 0);
            } else if (!blockEntity.canSmoke && blockEntity.moveType == MoveablePipeDataHandler.MOVE_TYPE.FROM_PIPE) {
                nbt.vec3d = nbt.vec3d.add(-1, 0, 0);
                if (nbt.getVec3d().getX() <= 0) {
                    nbt.shouldSave = false;
                    nbt.shouldMove = false;
                }
            }
        }, (nbt, world, pos, blockState, blockEntity) -> {

        }, (nbt, world, pos, blockState, blockEntity) -> {
            MoveablePipeDataHandler.SaveableMovablePipeNbt movablePipeNbt = blockEntity.moveablePipeDataHandler.getMoveablePipeNbt(CopperPipeMain.SMOKE);
            if (movablePipeNbt != null) {
                return movablePipeNbt.getVec3d() == null || movablePipeNbt.getVec3d().getX() <= nbt.getVec3d().getX() - 1;
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

    public static void spawnVibration(World world, BlockPos blockPos, BlockPos target) {
        if (world instanceof ServerWorld server) {
            int delay = MathHelper.floor(Math.sqrt(blockPos.getSquaredDistance(target)));
            server.sendVibrationPacket(new Vibration(blockPos, new BlockPositionSource(target), delay));
        }
    }

    public static void spawnDelayedVibration(World world, BlockPos blockPos, BlockPos target, int delay) {
        if (world instanceof ServerWorld server) {
            server.sendVibrationPacket(new Vibration(blockPos, new BlockPositionSource(target), delay));
        }
    }

}

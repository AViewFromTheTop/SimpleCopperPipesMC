package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PipeMovementRestrictions {

    private static final ArrayList<Identifier> blockEntityIds = new ArrayList<>();
    private static final ArrayList<CanTransferTo> canTransferTos = new ArrayList<>();
    private static final ArrayList<CanTakeFrom> canTakeFroms = new ArrayList<>();

    public static void register(Identifier id, CanTransferTo canTransferTo, CanTakeFrom canTakeFrom) {
        if (!blockEntityIds.contains(id)) {
            blockEntityIds.add(id);
            canTransferTos.add(canTransferTo);
            canTakeFroms.add(canTakeFrom);
        } else {
            canTransferTos.set(blockEntityIds.indexOf(id), canTransferTo);
            canTakeFroms.set(blockEntityIds.indexOf(id), canTakeFrom);
        }
    }

    @Nullable
    public static CanTransferTo getCanTransferTo(Identifier id) {
        if (blockEntityIds.contains(id)) {
            int index = blockEntityIds.indexOf(id);
            return canTransferTos.get(index);
        }
        return null;
    }

    @Nullable
    public static CanTakeFrom getCanTakeFrom(Identifier id) {
        if (blockEntityIds.contains(id)) {
            int index = blockEntityIds.indexOf(id);
            return canTakeFroms.get(index);
        }
        return null;
    }

    @Nullable
    public static CanTransferTo getCanTransferTo(BlockEntity entity) {
        Identifier id = Registry.BLOCK_ENTITY_TYPE.getId(entity.getType());
        if (blockEntityIds.contains(id)) {
            int index = blockEntityIds.indexOf(id);
            return canTransferTos.get(index);
        }
        return null;
    }

    @Nullable
    public static CanTakeFrom getCanTakeFrom(BlockEntity entity) {
        Identifier id = Registry.BLOCK_ENTITY_TYPE.getId(entity.getType());
        if (blockEntityIds.contains(id)) {
            int index = blockEntityIds.indexOf(id);
            return canTakeFroms.get(index);
        }
        return null;
    }

    //Don't forget to cast the BlockEntity to your desired class!
    @FunctionalInterface
    public interface CanTransferTo {
        boolean canTransfer(ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipe, BlockEntity toEntity);
    }

    @FunctionalInterface
    public interface CanTakeFrom {
        boolean canTake(ServerWorld world, BlockPos pos, BlockState state, CopperPipeEntity pipe, BlockEntity toEntity);
    }

    public static void init() {

    }
}

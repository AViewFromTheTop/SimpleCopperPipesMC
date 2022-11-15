package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PipeMovementRestrictions {

    private static final ArrayList<ResourceLocation> blockEntityIds = new ArrayList<>();
    private static final ArrayList<CanTransferTo> canTransferTos = new ArrayList<>();
    private static final ArrayList<CanTakeFrom> canTakeFroms = new ArrayList<>();

    public static void register(ResourceLocation id, CanTransferTo canTransferTo, CanTakeFrom canTakeFrom) {
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
    public static CanTransferTo getCanTransferTo(ResourceLocation id) {
        if (blockEntityIds.contains(id)) {
            int index = blockEntityIds.indexOf(id);
            return canTransferTos.get(index);
        }
        return null;
    }

    @Nullable
    public static CanTakeFrom getCanTakeFrom(ResourceLocation id) {
        if (blockEntityIds.contains(id)) {
            int index = blockEntityIds.indexOf(id);
            return canTakeFroms.get(index);
        }
        return null;
    }

    @Nullable
    public static CanTransferTo getCanTransferTo(BlockEntity entity) {
        ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType());
        if (blockEntityIds.contains(id)) {
            int index = blockEntityIds.indexOf(id);
            return canTransferTos.get(index);
        }
        return null;
    }

    @Nullable
    public static CanTakeFrom getCanTakeFrom(BlockEntity entity) {
        ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType());
        if (blockEntityIds.contains(id)) {
            int index = blockEntityIds.indexOf(id);
            return canTakeFroms.get(index);
        }
        return null;
    }

    //Don't forget to cast the BlockEntity to your desired class!
    @FunctionalInterface
    public interface CanTransferTo {
        boolean canTransfer(ServerLevel world, BlockPos pos, BlockState state, CopperPipeEntity pipe, BlockEntity toEntity);
    }

    @FunctionalInterface
    public interface CanTakeFrom {
        boolean canTake(ServerLevel world, BlockPos pos, BlockState state, CopperPipeEntity pipe, BlockEntity toEntity);
    }

    public static void init() {

    }
}

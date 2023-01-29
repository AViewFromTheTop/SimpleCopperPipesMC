package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.registry.SimpleCopperRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PipeMovementRestrictions {

    public record PipeMovementRestriction<T extends BlockEntity>(
            CanTransferTo<T> canTransferTo,
            CanTakeFrom<T> canTakeFrom
    ) {}

    public static <T extends BlockEntity> void register(ResourceLocation id, CanTransferTo<T> canTransferTo, CanTakeFrom<T> canTakeFrom) {
        Registry.register(SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS, id, new PipeMovementRestriction<T>(canTransferTo, canTakeFrom));
    }

    @Nullable
    public static <T extends BlockEntity> CanTransferTo<T> getCanTransferTo(ResourceLocation id) {
        if (SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS.containsKey(id)) {
            return SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS.get(id).canTransferTo;
        }
        return null;
    }

    @Nullable
    public static <T extends BlockEntity> CanTakeFrom<T> getCanTakeFrom(ResourceLocation id) {
        if (SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS.containsKey(id)) {
            return SimpleCopperRegistries.PIPE_MOVEMENT_RESTRICTIONS.get(id).canTakeFrom;
        }
        return null;
    }

    @Nullable
    public static <T extends BlockEntity> CanTransferTo<T> getCanTransferTo(T entity) {
        return getCanTransferTo(Registry.BLOCK_ENTITY_TYPE.getKey(entity.getType()));
    }

    @Nullable
    public static <T extends BlockEntity> CanTakeFrom<T> getCanTakeFrom(T entity) {
        return getCanTakeFrom(Registry.BLOCK_ENTITY_TYPE.getKey(entity.getType()));
    }

    @FunctionalInterface
    public interface CanTransferTo<T extends BlockEntity> {
        boolean canTransfer(ServerLevel world, BlockPos pos, BlockState state, CopperPipeEntity pipe, T toEntity);
    }

    @FunctionalInterface
    public interface CanTakeFrom<T extends BlockEntity> {
        boolean canTake(ServerLevel world, BlockPos pos, BlockState state, CopperPipeEntity pipe, T toEntity);
    }

    public static void init() {

    }
}

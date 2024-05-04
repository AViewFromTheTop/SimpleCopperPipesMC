package net.lunade.copper.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.lunade.copper.blocks.block_entity.CopperPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PipeMovementRestrictions {

    public static Map<ResourceLocation, PipeMovementRestriction> PIPE_MOVEMENT_RESTRICTIONS = new Object2ObjectLinkedOpenHashMap<>();

    public static <T extends BlockEntity> void register(ResourceLocation id, CanTransferTo<T> canTransferTo, CanTakeFrom<T> canTakeFrom) {
        PIPE_MOVEMENT_RESTRICTIONS.put(id, new PipeMovementRestriction<T>(canTransferTo, canTakeFrom));
    }

    @Nullable
    public static <T extends BlockEntity> CanTransferTo<T> getCanTransferTo(ResourceLocation id) {
        if (PIPE_MOVEMENT_RESTRICTIONS.containsKey(id)) {
            return PIPE_MOVEMENT_RESTRICTIONS.get(id).canTransferTo;
        }
        return null;
    }

    @Nullable
    public static <T extends BlockEntity> CanTakeFrom<T> getCanTakeFrom(ResourceLocation id) {
        if (PIPE_MOVEMENT_RESTRICTIONS.containsKey(id)) {
            return PIPE_MOVEMENT_RESTRICTIONS.get(id).canTakeFrom;
        }
        return null;
    }

    @Nullable
    public static <T extends BlockEntity> CanTransferTo<T> getCanTransferTo(@NotNull T entity) {
        return getCanTransferTo(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType()));
    }

    @Nullable
    public static <T extends BlockEntity> CanTakeFrom<T> getCanTakeFrom(@NotNull T entity) {
        return getCanTakeFrom(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType()));
    }

    public static void init() {

    }

    @FunctionalInterface
    public interface CanTransferTo<T extends BlockEntity> {
        boolean canTransfer(ServerLevel world, BlockPos pos, BlockState state, CopperPipeEntity pipe, T toEntity);
    }

    @FunctionalInterface
    public interface CanTakeFrom<T extends BlockEntity> {
        boolean canTake(ServerLevel world, BlockPos pos, BlockState state, CopperPipeEntity pipe, T toEntity);
    }

    public record PipeMovementRestriction<T extends BlockEntity>(
            CanTransferTo<T> canTransferTo,
            CanTakeFrom<T> canTakeFrom
    ) {
    }
}

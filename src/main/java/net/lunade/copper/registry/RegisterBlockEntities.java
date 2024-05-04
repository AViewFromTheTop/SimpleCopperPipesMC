package net.lunade.copper.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.lunade.copper.blocks.block_entity.CopperFittingEntity;
import net.lunade.copper.blocks.block_entity.CopperPipeEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public final class RegisterBlockEntities {

    public static void init() {
    }

    @NotNull
    private static <T extends BlockEntity> BlockEntityType<T> register(@NotNull String path, @NotNull FabricBlockEntityTypeBuilder.Factory<T> blockEntity, @NotNull Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, SimpleCopperPipesSharedConstants.id(path), FabricBlockEntityTypeBuilder.create(blockEntity, blocks).build(null));
    }

    public static final BlockEntityType<CopperPipeEntity> COPPER_PIPE_ENTITY = register(
            "copper_pipe",
            CopperPipeEntity::new,
            RegisterBlocks.COPPER_PIPE,
            RegisterBlocks.EXPOSED_COPPER_PIPE,
            RegisterBlocks.WEATHERED_COPPER_PIPE,
            RegisterBlocks.OXIDIZED_COPPER_PIPE,
            RegisterBlocks.WAXED_COPPER_PIPE,
            RegisterBlocks.WAXED_EXPOSED_COPPER_PIPE,
            RegisterBlocks.WAXED_WEATHERED_COPPER_PIPE,
            RegisterBlocks.WAXED_OXIDIZED_COPPER_PIPE
    );

    public static final BlockEntityType<CopperFittingEntity> COPPER_FITTING_ENTITY = register(
            "copper_fitting",
            CopperFittingEntity::new,
            RegisterBlocks.COPPER_FITTING,
            RegisterBlocks.EXPOSED_COPPER_FITTING,
            RegisterBlocks.WEATHERED_COPPER_FITTING,
            RegisterBlocks.OXIDIZED_COPPER_FITTING,
            RegisterBlocks.WAXED_COPPER_FITTING,
            RegisterBlocks.WAXED_EXPOSED_COPPER_FITTING,
            RegisterBlocks.WAXED_WEATHERED_COPPER_FITTING,
            RegisterBlocks.WAXED_OXIDIZED_COPPER_FITTING
    );


}

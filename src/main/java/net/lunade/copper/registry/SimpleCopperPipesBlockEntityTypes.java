package net.lunade.copper.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.block.entity.CopperFittingEntity;
import net.lunade.copper.block.entity.CopperPipeEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public final class SimpleCopperPipesBlockEntityTypes {

    public static void init() {
    }

    @NotNull
    private static <T extends BlockEntity> BlockEntityType<T> register(@NotNull String path, @NotNull FabricBlockEntityTypeBuilder.Factory<T> blockEntity, @NotNull Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, SimpleCopperPipesConstants.id(path), FabricBlockEntityTypeBuilder.create(blockEntity, blocks).build(null));
    }

    public static final BlockEntityType<CopperPipeEntity> COPPER_PIPE_ENTITY = register(
            "copper_pipe",
            CopperPipeEntity::new,
            SimpleCopperPipesBlocks.COPPER_PIPE,
            SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE,
            SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE,
            SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE,
            SimpleCopperPipesBlocks.WAXED_COPPER_PIPE,
            SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE,
            SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE,
            SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE
    );

    public static final BlockEntityType<CopperFittingEntity> COPPER_FITTING_ENTITY = register(
            "copper_fitting",
            CopperFittingEntity::new,
            SimpleCopperPipesBlocks.COPPER_FITTING,
            SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING,
            SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING,
            SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING,
            SimpleCopperPipesBlocks.WAXED_COPPER_FITTING,
            SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING,
            SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING,
            SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING
    );


}

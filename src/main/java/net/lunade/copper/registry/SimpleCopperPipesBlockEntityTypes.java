package net.lunade.copper.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.block.entity.CopperFittingBlockEntity;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class SimpleCopperPipesBlockEntityTypes {
	public static final BlockEntityType<CopperPipeBlockEntity> COPPER_PIPE = register(
		"copper_pipe",
		CopperPipeBlockEntity::new,
		SimpleCopperPipesBlocks.COPPER_PIPE,
		SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE,
		SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE,
		SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE,
		SimpleCopperPipesBlocks.WAXED_COPPER_PIPE,
		SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE,
		SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE,
		SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE
	);

	public static final BlockEntityType<CopperFittingBlockEntity> COPPER_FITTING = register(
		"copper_fitting",
		CopperFittingBlockEntity::new,
		SimpleCopperPipesBlocks.COPPER_FITTING,
		SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING,
		SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING,
		SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING,
		SimpleCopperPipesBlocks.WAXED_COPPER_FITTING,
		SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING,
		SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING,
		SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING
	);

	public static void init() {
	}

	private static <T extends BlockEntity> BlockEntityType<T> register(String path, FabricBlockEntityTypeBuilder.Factory<T> blockEntity, Block... blocks) {
		return Registry.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE,
			SimpleCopperPipesConstants.id(path),
			FabricBlockEntityTypeBuilder.create(blockEntity, blocks).build()
		);
	}

}

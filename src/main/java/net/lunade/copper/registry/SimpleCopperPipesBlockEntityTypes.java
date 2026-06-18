package net.lunade.copper.registry;

import java.util.Collection;
import java.util.Set;
import net.lunade.copper.block.entity.CopperFittingBlockEntity;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.lunade.copper.references.SimpleCopperPipesBlockEntityTypeIDs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class SimpleCopperPipesBlockEntityTypes {
	public static final BlockEntityType<CopperPipeBlockEntity> COPPER_PIPE = register(
		SimpleCopperPipesBlockEntityTypeIDs.COPPER_PIPE,
		CopperPipeBlockEntity::new,
		SimpleCopperPipesBlocks.COPPER_PIPE.asList()
	);

	public static final BlockEntityType<CopperFittingBlockEntity> COPPER_FITTING = register(
		SimpleCopperPipesBlockEntityTypeIDs.COPPER_FITTING,
		CopperFittingBlockEntity::new,
		SimpleCopperPipesBlocks.COPPER_FITTING.asList()
	);

	public static void init() {}

	private static <T extends BlockEntity> BlockEntityType<T> register(
		ResourceKey<BlockEntityType<?>> id,
		BlockEntityType.BlockEntitySupplier<T> builder,
		Collection<Block> blocks
	) {
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, new BlockEntityType<>(builder, Set.copyOf(blocks)));
	}
}

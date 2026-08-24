package net.lunade.copper.references;

import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SimpleCopperPipesBlockEntityTypeIDs {
	public static final ResourceKey<BlockEntityType<?>> COPPER_PIPE = create("copper_pipe");
	public static final ResourceKey<BlockEntityType<?>> COPPER_FITTING = create("copper_fitting");

	private static ResourceKey<BlockEntityType<?>> create(String name) {
		return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, SimpleCopperPipesConstants.id(name));
	}
}

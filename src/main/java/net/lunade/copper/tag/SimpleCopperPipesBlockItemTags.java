package net.lunade.copper.tag;

import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTagId;

public final class SimpleCopperPipesBlockItemTags {
	public static final BlockItemTagId COPPER_PIPES = bind("copper_pipes");
	public static final BlockItemTagId COPPER_FITTINGS = bind("copper_fittings");

	private static BlockItemTagId bind(String name) {
		final Identifier id = SimpleCopperPipesConstants.id(name);
		return BlockItemTagId.create(id, id);
	}
}

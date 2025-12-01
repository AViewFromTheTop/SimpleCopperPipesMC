package net.lunade.copper.tag;

import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SimpleCopperPipesBlockTags {
	public static final TagKey<Block> COPPER_PIPES = bind("copper_pipes");
	public static final TagKey<Block> COPPER_FITTINGS = bind("copper_fittings");
	public static final TagKey<Block> SILENT_COPPER_PIPES = bind("silent_copper_pipes");
	public static final TagKey<Block> COPPER_PIPE_CHECKS_SUPPORT_SHAPE = bind("copper_pipe_checks_support_shape");

	private static TagKey<Block> bind(String path) {
		return TagKey.create(Registries.BLOCK, SimpleCopperPipesConstants.id(path));
	}
}

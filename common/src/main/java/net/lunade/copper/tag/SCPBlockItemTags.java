package net.lunade.copper.tag;

import net.lunade.copper.SCPConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTagId;

public final class SCPBlockItemTags {
	public static final BlockItemTagId COPPER_PIPES = bind("copper_pipes");
	public static final BlockItemTagId COPPER_FITTINGS = bind("copper_fittings");

	private static BlockItemTagId bind(String name) {
		final Identifier id = SCPConstants.id(name);
		return BlockItemTagId.create(id, id);
	}

	private SCPBlockItemTags() {}
}

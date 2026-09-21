package net.lunade.copper.tag;

import net.lunade.copper.SCPConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class SCPItemTags {
	public static final TagKey<Item> IGNORES_COPPER_PIPE_MENU = bind("ignores_copper_pipe_menu");

	private static TagKey<Item> bind(String name) {
		return TagKey.create(Registries.ITEM, SCPConstants.id(name));
	}

	private SCPItemTags() {}
}

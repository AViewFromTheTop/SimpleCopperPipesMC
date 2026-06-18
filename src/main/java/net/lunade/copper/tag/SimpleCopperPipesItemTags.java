package net.lunade.copper.tag;

import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SimpleCopperPipesItemTags {
	public static final TagKey<Item> IGNORES_COPPER_PIPE_MENU = bind("ignores_copper_pipe_menu");

	private static TagKey<Item> bind(String name) {
		return TagKey.create(Registries.ITEM, SimpleCopperPipesConstants.id(name));
	}
}

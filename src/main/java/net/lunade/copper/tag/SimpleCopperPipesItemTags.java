package net.lunade.copper.tag;

import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class SimpleCopperPipesItemTags {
    public static final TagKey<Item> COPPER_PIPES = bind("copper_pipes");
    public static final TagKey<Item> COPPER_FITTINGS = bind("copper_fittings");
    public static final TagKey<Item> IGNORES_COPPER_PIPE_MENU = bind("ignores_copper_pipe_menu");

    @NotNull
    private static TagKey<Item> bind(@NotNull String path) {
        return TagKey.create(Registries.ITEM, SimpleCopperPipesSharedConstants.id(path));
    }
}

package net.lunade.copper.tag;

import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class SimpleCopperPipesBlockTags {
    public static final TagKey<Block> COPPER_PIPES = bind("copper_pipes");
    public static final TagKey<Block> COPPER_FITTINGS = bind("copper_fittings");
    public static final TagKey<Block> WAXED = bind("waxed");
    public static final TagKey<Block> SILENT_PIPES = bind("silent_pipes");

    @NotNull
    private static TagKey<Block> bind(@NotNull String path) {
        return TagKey.create(Registries.BLOCK, SimpleCopperPipesSharedConstants.id(path));
    }
}

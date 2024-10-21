package net.lunade.copper.registry;

import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class SimpleCopperPipesStats {
    public static final ResourceLocation INSPECT_PIPE = SimpleCopperPipesConstants.id("inspect_copper_pipe");
    public static final ResourceLocation INSPECT_FITTING = SimpleCopperPipesConstants.id("inspect_copper_fitting");

    public static void init() {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, INSPECT_PIPE, INSPECT_PIPE);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, INSPECT_FITTING, INSPECT_FITTING);
        Stats.CUSTOM.get(INSPECT_PIPE, StatFormatter.DEFAULT);
        Stats.CUSTOM.get(INSPECT_FITTING, StatFormatter.DEFAULT);
    }
}

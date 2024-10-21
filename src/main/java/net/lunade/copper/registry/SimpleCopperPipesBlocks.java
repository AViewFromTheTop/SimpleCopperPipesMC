package net.lunade.copper.registry;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.block.CopperFitting;
import net.lunade.copper.block.CopperPipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public final class SimpleCopperPipesBlocks {
    public static final Block COPPER_PIPE = register("copper_pipe",
            properties -> new CopperPipe(WeatheringCopper.WeatherState.UNAFFECTED, properties, 2, 20),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block EXPOSED_COPPER_PIPE = register("exposed_copper_pipe",
            properties -> new CopperPipe(WeatheringCopper.WeatherState.EXPOSED, properties, 2, 18),
            BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block WEATHERED_COPPER_PIPE = register("weathered_copper_pipe",
            properties -> new CopperPipe(WeatheringCopper.WeatherState.WEATHERED, properties, 2, 15),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block OXIDIZED_COPPER_PIPE = register("oxidized_copper_pipe",
            properties -> new CopperPipe(WeatheringCopper.WeatherState.WEATHERED, properties, 2, 12),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );

    public static final Block WAXED_COPPER_PIPE = register("waxed_copper_pipe",
            properties -> new CopperPipe(WeatheringCopper.WeatherState.UNAFFECTED, properties, 1, 20),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block WAXED_EXPOSED_COPPER_PIPE = register("waxed_exposed_copper_pipe",
            properties -> new CopperPipe(WeatheringCopper.WeatherState.EXPOSED, properties, 1, 18),
            BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block WAXED_WEATHERED_COPPER_PIPE = register("waxed_weathered_copper_pipe",
            properties -> new CopperPipe(WeatheringCopper.WeatherState.WEATHERED, properties, 1, 15),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block WAXED_OXIDIZED_COPPER_PIPE = register("waxed_oxidized_copper_pipe",
            properties -> new CopperPipe(WeatheringCopper.WeatherState.WEATHERED, properties, 1, 12),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );

    public static final Block COPPER_FITTING = register("copper_fitting",
            properties -> new CopperFitting(WeatheringCopper.WeatherState.UNAFFECTED, properties, 1),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block EXPOSED_COPPER_FITTING = register("exposed_copper_fitting",
            properties -> new CopperFitting(WeatheringCopper.WeatherState.EXPOSED, properties, 1),
            BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block WEATHERED_COPPER_FITTING = register("weathered_copper_fitting",
            properties -> new CopperFitting(WeatheringCopper.WeatherState.WEATHERED, properties, 1),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block OXIDIZED_COPPER_FITTING = register("oxidized_copper_fitting",
            properties -> new CopperFitting(WeatheringCopper.WeatherState.OXIDIZED, properties, 1),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    
    public static final Block WAXED_COPPER_FITTING = register("waxed_copper_fitting",
            properties -> new CopperFitting(WeatheringCopper.WeatherState.UNAFFECTED, properties, 0),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block WAXED_EXPOSED_COPPER_FITTING = register("waxed_exposed_copper_fitting",
            properties -> new CopperFitting(WeatheringCopper.WeatherState.EXPOSED, properties, 0),
            BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block WAXED_WEATHERED_COPPER_FITTING = register("waxed_weathered_copper_fitting",
            properties -> new CopperFitting(WeatheringCopper.WeatherState.WEATHERED, properties, 0),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    public static final Block WAXED_OXIDIZED_COPPER_FITTING = register("waxed_oxidized_copper_fitting",
            properties -> new CopperFitting(WeatheringCopper.WeatherState.OXIDIZED, properties, 0),
            BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
    );
    
    public static void init() {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(COPPER_PIPE, EXPOSED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(EXPOSED_COPPER_PIPE, WEATHERED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_COPPER_PIPE, OXIDIZED_COPPER_PIPE);

        OxidizableBlocksRegistry.registerOxidizableBlockPair(COPPER_FITTING, EXPOSED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(EXPOSED_COPPER_FITTING, WEATHERED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_COPPER_FITTING, OXIDIZED_COPPER_FITTING);

        OxidizableBlocksRegistry.registerWaxableBlockPair(COPPER_PIPE, WAXED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(EXPOSED_COPPER_PIPE, WAXED_EXPOSED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_COPPER_PIPE, WAXED_WEATHERED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(OXIDIZED_COPPER_PIPE, WAXED_OXIDIZED_COPPER_PIPE);

        OxidizableBlocksRegistry.registerWaxableBlockPair(COPPER_FITTING, WAXED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(EXPOSED_COPPER_FITTING, WAXED_EXPOSED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_COPPER_FITTING, WAXED_WEATHERED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(OXIDIZED_COPPER_FITTING, WAXED_OXIDIZED_COPPER_FITTING);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.tryBuild(SimpleCopperPipesConstants.NAMESPACE, path);
    }

    public static ResourceLocation legacyId(String path) {
        return ResourceLocation.tryBuild(SimpleCopperPipesConstants.LEGACY_NAMESPACE, path);
    }

    public static ResourceLocation legacyColoredPipe(String colour) {
        return legacyId(colour + "_pipe");
    }

    public static ResourceLocation legacyGlowingPipe(String colour) {
        return legacyId("glowing_" + colour + "_pipe");
    }

    public static ResourceLocation legacyColoredFitting(String colour) {
        return legacyId(colour + "_fitting");
    }

    public static ResourceLocation legacyGlowingFitting(String colour) {
        return legacyId("glowing_" + colour + "_fitting");
    }

    private static <T extends Block> T registerWithoutItem(String path, Function<BlockBehaviour.Properties, T> block, BlockBehaviour.Properties properties) {
        ResourceLocation id = SimpleCopperPipesConstants.id(path);
        return doRegister(id, makeBlock(block, properties, id));
    }

    private static <T extends Block> T register(String path, Function<BlockBehaviour.Properties, T> block, BlockBehaviour.Properties properties) {
        T registered = registerWithoutItem(path, block, properties);
        Items.registerBlock(registered);
        return registered;
    }

    private static <T extends Block> T doRegister(ResourceLocation id, T block) {
        if (BuiltInRegistries.BLOCK.getOptional(id).isEmpty()) {
            return Registry.register(BuiltInRegistries.BLOCK, id, block);
        }
        throw new IllegalArgumentException("Block with id " + id + " is already in the block registry.");
    }

    private static <T extends Block> T makeBlock(Function<BlockBehaviour.Properties, T> function, BlockBehaviour.Properties properties, ResourceLocation id) {
        return function.apply(properties.setId(ResourceKey.create(Registries.BLOCK, id)));
    }
}

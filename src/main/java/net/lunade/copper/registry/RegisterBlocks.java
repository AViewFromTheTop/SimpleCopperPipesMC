package net.lunade.copper.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class RegisterBlocks {
    public static final Block COPPER_PIPE = new CopperPipe(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, 20);
    public static final Block EXPOSED_COPPER_PIPE = new CopperPipe(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, 18);
    public static final Block WEATHERED_COPPER_PIPE = new CopperPipe(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, 15);
    public static final Block OXIDIZED_COPPER_PIPE = new CopperPipe(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, 12);
    public static final Block WAXED_COPPER_PIPE = new CopperPipe(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, 20);
    public static final Block WAXED_EXPOSED_COPPER_PIPE = new CopperPipe(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, 18);
    public static final Block WAXED_WEATHERED_COPPER_PIPE = new CopperPipe(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, 15);
    public static final Block WAXED_OXIDIZED_COPPER_PIPE = new CopperPipe(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, 12);
    public static final Block COPPER_FITTING = new CopperFitting(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1);
    public static final Block EXPOSED_COPPER_FITTING = new CopperFitting(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1);
    public static final Block WEATHERED_COPPER_FITTING = new CopperFitting(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1);
    public static final Block OXIDIZED_COPPER_FITTING = new CopperFitting(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1);
    public static final Block WAXED_COPPER_FITTING = new CopperFitting(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0);
    public static final Block WAXED_EXPOSED_COPPER_FITTING = new CopperFitting(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0);
    public static final Block WAXED_WEATHERED_COPPER_FITTING = new CopperFitting(BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0);
    public static final Block WAXED_OXIDIZED_COPPER_FITTING = new CopperFitting(BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0);

    public static void register() {
        //PIPES

        registerBlock(COPPER_PIPE, id("copper_pipe"));
        registerBlock(EXPOSED_COPPER_PIPE, id("exposed_copper_pipe"));
        registerBlock(WEATHERED_COPPER_PIPE, id("weathered_copper_pipe"));
        registerBlock(OXIDIZED_COPPER_PIPE, id("oxidized_copper_pipe"));
        //WAXED
        registerBlock(WAXED_COPPER_PIPE, id("waxed_copper_pipe"));
        registerBlock(WAXED_EXPOSED_COPPER_PIPE, id("waxed_exposed_copper_pipe"));
        registerBlock(WAXED_WEATHERED_COPPER_PIPE, id("waxed_weathered_copper_pipe"));
        registerBlock(WAXED_OXIDIZED_COPPER_PIPE, id("waxed_oxidized_copper_pipe"));

        //FITTINGS

        registerBlock(COPPER_FITTING, id("copper_fitting"));
        registerBlock(EXPOSED_COPPER_FITTING, id("exposed_copper_fitting"));
        registerBlock(WEATHERED_COPPER_FITTING, id("weathered_copper_fitting"));
        registerBlock(OXIDIZED_COPPER_FITTING, id("oxidized_copper_fitting"));
        //WAXED
        registerBlock(WAXED_COPPER_FITTING, id("waxed_copper_fitting"));
        registerBlock(WAXED_EXPOSED_COPPER_FITTING, id("waxed_exposed_copper_fitting"));
        registerBlock(WAXED_WEATHERED_COPPER_FITTING, id("waxed_weathered_copper_fitting"));
        registerBlock(WAXED_OXIDIZED_COPPER_FITTING, id("waxed_oxidized_copper_fitting"));

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
        return ResourceLocation.tryBuild(SimpleCopperPipesSharedConstants.NAMESPACE, path);
    }

    public static ResourceLocation legacyId(String path) {
        return ResourceLocation.tryBuild(SimpleCopperPipesSharedConstants.LEGACY_NAMESPACE, path);
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

    public static Block registerBlock(Block block, ResourceLocation resourceLocation) {
        var registered = Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        Item item = new BlockItem(block, new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((entries) -> entries.accept(item));
        return registered;
    }

    public static Block registerBlock(ResourceLocation resourceLocation, Block block) {
        return registerBlock(block, resourceLocation);
    }

    public static Block registerColoured(Block block, ResourceLocation resourceLocation) {
        var registered = Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        Item item = new BlockItem(block, new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register((entries) -> entries.accept(item));
        return registered;
    }

    public static Block registerColoured(ResourceLocation resourceLocation, Block block) {
        return registerColoured(block, resourceLocation);
    }

}

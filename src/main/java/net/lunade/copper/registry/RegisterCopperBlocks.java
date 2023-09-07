package net.lunade.copper.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

// blocks need to be in here because class initialization is weird
// you know what im saying?
public final class RegisterCopperBlocks {
    private RegisterCopperBlocks() {
    }

    public static void register() {
        //PIPE
        registerBlock(CopperPipe.COPPER_PIPE, id("copper_pipe"));
        registerBlock(CopperPipe.EXPOSED_PIPE, id("exposed_copper_pipe"));
        registerBlock(CopperPipe.WEATHERED_PIPE, id("weathered_copper_pipe"));
        registerBlock(CopperPipe.OXIDIZED_PIPE, id("oxidized_copper_pipe"));

        //WAXED
        registerBlock(CopperPipe.WAXED_COPPER_PIPE, id("waxed_copper_pipe"));
        registerBlock(CopperPipe.WAXED_EXPOSED_PIPE, id("waxed_exposed_copper_pipe"));
        registerBlock(CopperPipe.WAXED_WEATHERED_PIPE, id("waxed_weathered_copper_pipe"));
        registerBlock(CopperPipe.WAXED_OXIDIZED_PIPE, id("waxed_oxidized_copper_pipe"));

        //CORRODED
        registerBlock(CopperPipe.CORRODED_PIPE, id("corroded_pipe"));

        //COLOURED
        registerBlock(CopperPipe.BLACK_PIPE, colourPipe("black"));
        registerBlock(CopperPipe.GLOWING_BLACK_PIPE, glowingPipe("black"));
        registerBlock(CopperPipe.RED_PIPE, colourPipe("red"));
        registerBlock(CopperPipe.GLOWING_RED_PIPE, glowingPipe("red"));
        registerBlock(CopperPipe.GREEN_PIPE, colourPipe("green"));
        registerBlock(CopperPipe.GLOWING_GREEN_PIPE, glowingPipe("green"));
        registerBlock(CopperPipe.BROWN_PIPE, colourPipe("brown"));
        registerBlock(CopperPipe.GLOWING_BROWN_PIPE, glowingPipe("brown"));
        registerBlock(CopperPipe.BLUE_PIPE, colourPipe("blue"));
        registerBlock(CopperPipe.GLOWING_BLUE_PIPE, glowingPipe("blue"));
        registerBlock(CopperPipe.PURPLE_PIPE, colourPipe("purple"));
        registerBlock(CopperPipe.GLOWING_PURPLE_PIPE, glowingPipe("purple"));
        registerBlock(CopperPipe.CYAN_PIPE, colourPipe("cyan"));
        registerBlock(CopperPipe.GLOWING_CYAN_PIPE, glowingPipe("cyan"));
        registerBlock(CopperPipe.LIGHT_GRAY_PIPE, colourPipe("light_gray"));
        registerBlock(CopperPipe.GLOWING_LIGHT_GRAY_PIPE, glowingPipe("light_gray"));
        registerBlock(CopperPipe.GRAY_PIPE, colourPipe("gray"));
        registerBlock(CopperPipe.GLOWING_GRAY_PIPE, glowingPipe("gray"));
        registerBlock(CopperPipe.PINK_PIPE, colourPipe("pink"));
        registerBlock(CopperPipe.GLOWING_PINK_PIPE, glowingPipe("pink"));
        registerBlock(CopperPipe.LIME_PIPE, colourPipe("lime"));
        registerBlock(CopperPipe.GLOWING_LIME_PIPE, glowingPipe("lime"));
        registerBlock(CopperPipe.YELLOW_PIPE, colourPipe("yellow"));
        registerBlock(CopperPipe.GLOWING_YELLOW_PIPE, glowingPipe("yellow"));
        registerBlock(CopperPipe.LIGHT_BLUE_PIPE, colourPipe("light_blue"));
        registerBlock(CopperPipe.GLOWING_LIGHT_BLUE_PIPE, glowingPipe("light_blue"));
        registerBlock(CopperPipe.MAGENTA_PIPE, colourPipe("magenta"));
        registerBlock(CopperPipe.GLOWING_MAGENTA_PIPE, glowingPipe("magenta"));
        registerBlock(CopperPipe.ORANGE_PIPE, colourPipe("orange"));
        registerBlock(CopperPipe.GLOWING_ORANGE_PIPE, glowingPipe("orange"));
        registerBlock(CopperPipe.WHITE_PIPE, colourPipe("white"));
        registerBlock(CopperPipe.GLOWING_WHITE_PIPE, glowingPipe("white"));

        //FITTINGS
        registerBlock(CopperFitting.COPPER_FITTING, id("copper_fitting"));
        registerBlock(CopperFitting.EXPOSED_FITTING, id("exposed_copper_fitting"));
        registerBlock(CopperFitting.WEATHERED_FITTING, id("weathered_copper_fitting"));
        registerBlock(CopperFitting.OXIDIZED_FITTING, id("oxidized_copper_fitting"));
        //WAXED
        registerBlock(CopperFitting.WAXED_COPPER_FITTING, id("waxed_copper_fitting"));
        registerBlock(CopperFitting.WAXED_EXPOSED_FITTING, id("waxed_exposed_copper_fitting"));
        registerBlock(CopperFitting.WAXED_WEATHERED_FITTING, id("waxed_weathered_copper_fitting"));
        registerBlock(CopperFitting.WAXED_OXIDIZED_FITTING, id("waxed_oxidized_copper_fitting"));

        //CORRODED
        registerBlock(CopperFitting.CORRODED_FITTING, id("corroded_fitting"));

        //COLOURED
        registerBlock(CopperFitting.BLACK_FITTING, colourFitting("black"));
        registerBlock(CopperFitting.GLOWING_BLACK_FITTING, glowingFitting("black"));
        registerBlock(CopperFitting.RED_FITTING, colourFitting("red"));
        registerBlock(CopperFitting.GLOWING_RED_FITTING, glowingFitting("red"));
        registerBlock(CopperFitting.GREEN_FITTING, colourFitting("green"));
        registerBlock(CopperFitting.GLOWING_GREEN_FITTING, glowingFitting("green"));
        registerBlock(CopperFitting.BROWN_FITTING, colourFitting("brown"));
        registerBlock(CopperFitting.GLOWING_BROWN_FITTING, glowingFitting("brown"));
        registerBlock(CopperFitting.BLUE_FITTING, colourFitting("blue"));
        registerBlock(CopperFitting.GLOWING_BLUE_FITTING, glowingFitting("blue"));
        registerBlock(CopperFitting.PURPLE_FITTING, colourFitting("purple"));
        registerBlock(CopperFitting.GLOWING_PURPLE_FITTING, glowingFitting("purple"));
        registerBlock(CopperFitting.CYAN_FITTING, colourFitting("cyan"));
        registerBlock(CopperFitting.GLOWING_CYAN_FITTING, glowingFitting("cyan"));
        registerBlock(CopperFitting.LIGHT_GRAY_FITTING, colourFitting("light_gray"));
        registerBlock(CopperFitting.GLOWING_LIGHT_GRAY_FITTING, glowingFitting("light_gray"));
        registerBlock(CopperFitting.GRAY_FITTING, colourFitting("gray"));
        registerBlock(CopperFitting.GLOWING_GRAY_FITTING, glowingFitting("gray"));
        registerBlock(CopperFitting.PINK_FITTING, colourFitting("pink"));
        registerBlock(CopperFitting.GLOWING_PINK_FITTING, glowingFitting("pink"));
        registerBlock(CopperFitting.LIME_FITTING, colourFitting("lime"));
        registerBlock(CopperFitting.GLOWING_LIME_FITTING, glowingFitting("lime"));
        registerBlock(CopperFitting.YELLOW_FITTING, colourFitting("yellow"));
        registerBlock(CopperFitting.GLOWING_YELLOW_FITTING, glowingFitting("yellow"));
        registerBlock(CopperFitting.LIGHT_BLUE_FITTING, colourFitting("light_blue"));
        registerBlock(CopperFitting.GLOWING_LIGHT_BLUE_FITTING, glowingFitting("light_blue"));
        registerBlock(CopperFitting.MAGENTA_FITTING, colourFitting("magenta"));
        registerBlock(CopperFitting.GLOWING_MAGENTA_FITTING, glowingFitting("magenta"));
        registerBlock(CopperFitting.ORANGE_FITTING, colourFitting("orange"));
        registerBlock(CopperFitting.GLOWING_ORANGE_FITTING, glowingFitting("orange"));
        registerBlock(CopperFitting.WHITE_FITTING, colourFitting("white"));
        registerBlock(CopperFitting.GLOWING_WHITE_FITTING, glowingFitting("white"));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(CopperPipeMain.BLOCK_ID, path);
    }

    public static ResourceLocation colourPipe(String colour) {
        return id(colour + "_pipe");
    }

    public static ResourceLocation glowingPipe(String colour) {
        return id("glowing_" + colour + "_pipe");
    }

    public static ResourceLocation colourFitting(String colour) {
        return id(colour + "_fitting");
    }

    public static ResourceLocation glowingFitting(String colour) {
        return id("glowing_" + colour + "_fitting");
    }

    public static Block registerBlock(Block block, ResourceLocation resourceLocation) {
        var registered = Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        Item item = new BlockItem(block, new FabricItemSettings());
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((entries) -> entries.accept(item));
        return registered;
    }

    public static Block registerBlock(ResourceLocation resourceLocation, Block block) {
        return registerBlock(block, resourceLocation);
    }

    public static Block registerColoured(Block block, ResourceLocation resourceLocation) {
        var registered = Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        Item item = new BlockItem(block, new FabricItemSettings());
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register((entries) -> entries.accept(item));
        return registered;
    }

    public static Block registerColoured(ResourceLocation resourceLocation, Block block) {
        return registerColoured(block, resourceLocation);
    }

}

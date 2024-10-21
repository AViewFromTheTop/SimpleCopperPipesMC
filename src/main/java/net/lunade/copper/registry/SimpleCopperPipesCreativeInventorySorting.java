package net.lunade.copper.registry;

import net.frozenblock.lib.item.api.FrozenCreativeTabs;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class SimpleCopperPipesCreativeInventorySorting {
    
    public static void init() {
        addInBuildingBlocksAfter(Items.COPPER_TRAPDOOR, SimpleCopperPipesBlocks.COPPER_PIPE);
        addInBuildingBlocksAfter(Items.EXPOSED_COPPER_TRAPDOOR, SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE);
        addInBuildingBlocksAfter(Items.WEATHERED_COPPER_TRAPDOOR, SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE);
        addInBuildingBlocksAfter(Items.OXIDIZED_COPPER_TRAPDOOR, SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE);

        addInBuildingBlocksAfter(Items.WAXED_COPPER_TRAPDOOR, SimpleCopperPipesBlocks.WAXED_COPPER_PIPE);
        addInBuildingBlocksAfter(Items.WAXED_EXPOSED_COPPER_TRAPDOOR, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE);
        addInBuildingBlocksAfter(Items.WAXED_WEATHERED_COPPER_TRAPDOOR, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE);
        addInBuildingBlocksAfter(Items.WAXED_OXIDIZED_COPPER_TRAPDOOR, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE);

        addInRedstoneAfter(Items.WAXED_COPPER_BULB, SimpleCopperPipesBlocks.WAXED_COPPER_PIPE);
        addInRedstoneAfter(Items.WAXED_EXPOSED_COPPER_BULB, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE);
        addInRedstoneAfter(Items.WAXED_WEATHERED_COPPER_BULB, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE);
        addInRedstoneAfter(Items.WAXED_OXIDIZED_COPPER_BULB, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE);

        addInBuildingBlocksAfter(SimpleCopperPipesBlocks.COPPER_PIPE, SimpleCopperPipesBlocks.COPPER_FITTING);
        addInBuildingBlocksAfter(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE, SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING);
        addInBuildingBlocksAfter(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE, SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING);
        addInBuildingBlocksAfter(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE, SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING);

        addInBuildingBlocksAfter(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_COPPER_FITTING);
        addInBuildingBlocksAfter(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING);
        addInBuildingBlocksAfter(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING);
        addInBuildingBlocksAfter(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);

        addInRedstoneAfter(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_COPPER_FITTING);
        addInRedstoneAfter(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING);
        addInRedstoneAfter(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING);
        addInRedstoneAfter(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
    }
    
    private static void addInRedstoneAfter(ItemLike comparedItem, ItemLike item) {
        FrozenCreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.REDSTONE_BLOCKS);
    }

    private static void addInBuildingBlocksAfter(ItemLike comparedItem, ItemLike item) {
        FrozenCreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.BUILDING_BLOCKS);
    }
}

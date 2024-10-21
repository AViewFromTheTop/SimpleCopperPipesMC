package net.lunade.copper.registry;

import net.frozenblock.lib.item.api.FrozenCreativeTabs;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;

public class SimpleCopperPipesCreativeInventorySorting {
    
    public static void init() {
        addInRedstone(SimpleCopperPipesBlocks.COPPER_PIPE);
        addInRedstone(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE);
        addInRedstone(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE);
        addInRedstone(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE);

        addInRedstone(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE);
        addInRedstone(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE);
        addInRedstone(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE);
        addInRedstone(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE);

        addInRedstone(SimpleCopperPipesBlocks.COPPER_FITTING);
        addInRedstone(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING);
        addInRedstone(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING);
        addInRedstone(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING);

        addInRedstone(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING);
        addInRedstone(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING);
        addInRedstone(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING);
        addInRedstone(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
    }
    
    private static void addInRedstone(ItemLike item) {
        FrozenCreativeTabs.add(item, CreativeModeTabs.REDSTONE_BLOCKS);
    }
}

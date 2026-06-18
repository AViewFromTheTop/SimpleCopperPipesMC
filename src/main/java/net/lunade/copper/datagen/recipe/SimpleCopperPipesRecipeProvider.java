package net.lunade.copper.datagen.recipe;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lunade.copper.SimpleCopperPipesFeatureFlags;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

public class SimpleCopperPipesRecipeProvider extends FabricRecipeProvider {

	public SimpleCopperPipesRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				this.shaped(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.COPPER_PIPE.weathering().unaffected(), 3)
					.define('#', Items.COPPER_INGOT)
					.pattern("###")
					.pattern("   ")
					.pattern("###")
					.unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
					.save(exporter);

				this.shaped(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.COPPER_FITTING.weathering().unaffected(), 4)
					.define('#', Items.COPPER_INGOT)
					.pattern("###")
					.pattern("# #")
					.pattern("###")
					.unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
					.save(exporter);

				this.waxRecipes(SimpleCopperPipesFeatureFlags.SIMPLER_COPPER_PIPES_FLAG_SET);
			}
		};
	}

	@Override
	public String getName() {
		return "Simple Copper Pipes recipes";
	}
}

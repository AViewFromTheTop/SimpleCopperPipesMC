package net.lunade.copper.datagen.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SimpleCopperPipesRecipeProvider extends FabricRecipeProvider {
	public SimpleCopperPipesRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				this.shaped(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.COPPER_PIPE, 3)
						.define('#', Items.COPPER_INGOT)
						.pattern("###")
						.pattern("   ")
						.pattern("###")
						.unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
						.save(exporter);

				this.shapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_COPPER_PIPE)
						.requires(SimpleCopperPipesBlocks.COPPER_PIPE)
						.requires(Items.HONEYCOMB)
						.unlockedBy(RecipeProvider.getHasName(SimpleCopperPipesBlocks.COPPER_PIPE), this.has(SimpleCopperPipesBlocks.COPPER_PIPE))
						.save(exporter);

				this.shapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE)
						.requires(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE)
						.requires(Items.HONEYCOMB)
						.unlockedBy(RecipeProvider.getHasName(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE), this.has(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE))
						.save(exporter);

				this.shapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE)
						.requires(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE)
						.requires(Items.HONEYCOMB)
						.unlockedBy(RecipeProvider.getHasName(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE), this.has(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE))
						.save(exporter);

				this.shapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE)
						.requires(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE)
						.requires(Items.HONEYCOMB)
						.unlockedBy(RecipeProvider.getHasName(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE), this.has(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE))
						.save(exporter);

				// FITTINGS

				this.shaped(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.COPPER_FITTING, 4)
						.define('#', Items.COPPER_INGOT)
						.pattern("###")
						.pattern("# #")
						.pattern("###")
						.unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
						.save(exporter);

				this.shapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_COPPER_FITTING)
						.requires(SimpleCopperPipesBlocks.COPPER_FITTING)
						.requires(Items.HONEYCOMB)
						.unlockedBy(RecipeProvider.getHasName(SimpleCopperPipesBlocks.COPPER_FITTING), this.has(SimpleCopperPipesBlocks.COPPER_FITTING))
						.save(exporter);

				this.shapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING)
						.requires(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING)
						.requires(Items.HONEYCOMB)
						.unlockedBy(RecipeProvider.getHasName(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING), this.has(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING))
						.save(exporter);

				this.shapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING)
						.requires(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING)
						.requires(Items.HONEYCOMB)
						.unlockedBy(RecipeProvider.getHasName(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING), this.has(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING))
						.save(exporter);

				this.shapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING)
						.requires(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING)
						.requires(Items.HONEYCOMB)
						.unlockedBy(RecipeProvider.getHasName(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING), this.has(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING))
						.save(exporter);
			}
		};
	}

	@Override
	public @NotNull String getName() {
		return "Simple Copper Pipes recipes";
	}
}

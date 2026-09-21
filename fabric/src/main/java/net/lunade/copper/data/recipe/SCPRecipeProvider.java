/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Simple Copper Pipes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

package net.lunade.copper.data.recipe;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.frozenblock.lib.item.api.recipe.RecipeExportNamespaceFix;
import net.lunade.copper.SCPConstants;
import net.lunade.copper.SCPFeatureFlags;
import net.lunade.copper.registry.SCPBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public final class SCPRecipeProvider extends FabricRecipeProvider {

	public SCPRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
		return new RecipeProvider(recipeOutput, advancementOutput) {
			@Override
			public void buildRecipes() {
				RecipeExportNamespaceFix.setCurrentGeneratingModId(SCPConstants.MOD_ID);

				this.waxRecipes(SCPFeatureFlags.SIMPLER_COPPER_PIPES_FLAG_SET);

				this.shaped(RecipeCategory.REDSTONE, SCPBlocks.COPPER_PIPE.weathering().unaffected(), 3)
					.define('#', Items.COPPER_INGOT)
					.pattern("###")
					.pattern("   ")
					.pattern("###")
					.unlockedBy(getHasName(Items.COPPER_INGOT), this.has(Items.COPPER_INGOT))
					.save(this.output);

				this.shaped(RecipeCategory.REDSTONE, SCPBlocks.COPPER_FITTING.weathering().unaffected(), 4)
					.define('#', Items.COPPER_INGOT)
					.pattern("###")
					.pattern("# #")
					.pattern("###")
					.unlockedBy(getHasName(Items.COPPER_INGOT), this.has(Items.COPPER_INGOT))
					.save(this.output);

				RecipeExportNamespaceFix.clearCurrentGeneratingModId();
			}
		};
	}

	@Override
	public String getName() {
		return "Simple Copper Pipes recipes";
	}
}

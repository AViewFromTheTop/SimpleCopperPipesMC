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
import net.lunade.copper.SCPFeatureFlags;
import net.lunade.copper.registry.SCPBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

public class SCPRecipeProvider extends FabricRecipeProvider {

	public SCPRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				this.shaped(RecipeCategory.REDSTONE, SCPBlocks.COPPER_PIPE.weathering().unaffected(), 3)
					.define('#', Items.COPPER_INGOT)
					.pattern("###")
					.pattern("   ")
					.pattern("###")
					.unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
					.save(exporter);

				this.shaped(RecipeCategory.REDSTONE, SCPBlocks.COPPER_FITTING.weathering().unaffected(), 4)
					.define('#', Items.COPPER_INGOT)
					.pattern("###")
					.pattern("# #")
					.pattern("###")
					.unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
					.save(exporter);

				this.waxRecipes(SCPFeatureFlags.SIMPLER_COPPER_PIPES_FLAG_SET);
			}
		};
	}

	@Override
	public String getName() {
		return "Simple Copper Pipes recipes";
	}
}

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

package net.lunade.copper.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.lunade.copper.data.loot.SCPBlockLootProvider;
import net.lunade.copper.data.model.SCPModelProvider;
import net.lunade.copper.data.recipe.SCPRecipeProvider;
import net.lunade.copper.data.tag.SCPBlockTagProvider;
import net.lunade.copper.data.tag.SCPItemTagsProvider;
import net.minecraft.core.RegistrySetBuilder;

public final class SCPDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		final FabricDataGenerator.Pack pack = dataGenerator.createPack();

		// ASSETS
		pack.addProvider(SCPModelProvider::new);

		// DATA
		pack.addProvider(SCPBlockLootProvider::new);
		pack.addProvider(SCPBlockTagProvider::new);
		pack.addProvider(SCPItemTagsProvider::new);
		pack.addProvider(SCPRecipeProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {}
}

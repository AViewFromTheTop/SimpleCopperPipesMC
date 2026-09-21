package net.lunade.copper.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.lunade.copper.datagen.loot.SCPBlockLootProvider;
import net.lunade.copper.datagen.model.SCPModelProvider;
import net.lunade.copper.datagen.recipe.SCPRecipeProvider;
import net.lunade.copper.datagen.tag.SCPBlockTagProvider;
import net.lunade.copper.datagen.tag.SCPItemTagsProvider;
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

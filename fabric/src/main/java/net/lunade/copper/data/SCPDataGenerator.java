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

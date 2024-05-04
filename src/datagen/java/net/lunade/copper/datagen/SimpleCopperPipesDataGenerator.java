package net.lunade.copper.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.lunade.copper.datagen.loot.SimpleCopperPipesBlockLootProvider;
import net.lunade.copper.datagen.recipe.SimpleCopperPipesRecipeProvider;
import net.lunade.copper.datagen.tag.SimpleCopperPipesBlockTagProvider;
import net.lunade.copper.datagen.tag.SimpleCopperPipesItemTagProvider;
import net.minecraft.core.RegistrySetBuilder;
import org.jetbrains.annotations.NotNull;

public final class SimpleCopperPipesDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(@NotNull FabricDataGenerator dataGenerator) {
        final FabricDataGenerator.Pack pack = dataGenerator.createPack();

        // DATA

        pack.addProvider(SimpleCopperPipesBlockLootProvider::new);
        pack.addProvider(SimpleCopperPipesBlockTagProvider::new);
        pack.addProvider(SimpleCopperPipesItemTagProvider::new);
        pack.addProvider(SimpleCopperPipesRecipeProvider::new);
    }

    @Override
    public void buildRegistry(@NotNull RegistrySetBuilder registryBuilder) {
    }

}

package net.lunade.copper.datagen.tag;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.lunade.copper.tag.SimpleCopperPipesItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class SimpleCopperPipesItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public SimpleCopperPipesItemTagProvider(@NotNull FabricDataOutput output, @NotNull CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		this.valueLookupBuilder(SimpleCopperPipesItemTags.COPPER_PIPES)
			.add(SimpleCopperPipesBlocks.COPPER_PIPE.asItem())
			.add(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE.asItem())
			.add(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE.asItem())
			.add(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE.asItem())
			.add(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE.asItem())
			.add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE.asItem())
			.add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE.asItem())
			.add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE.asItem());

		this.valueLookupBuilder(SimpleCopperPipesItemTags.COPPER_FITTINGS)
			.add(SimpleCopperPipesBlocks.COPPER_FITTING.asItem())
			.add(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING.asItem())
			.add(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING.asItem())
			.add(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING.asItem())
			.add(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING.asItem())
			.add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING.asItem())
			.add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING.asItem())
			.add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING.asItem());

		this.builder(SimpleCopperPipesItemTags.IGNORES_COPPER_PIPE_MENU)
			.addOptionalTag(SimpleCopperPipesItemTags.COPPER_PIPES)
			.addOptionalTag(SimpleCopperPipesItemTags.COPPER_FITTINGS)
			.addOptional(ResourceKey.create(Registries.ITEM, ResourceLocation.tryBuild("create", "wrench")));
	}
}

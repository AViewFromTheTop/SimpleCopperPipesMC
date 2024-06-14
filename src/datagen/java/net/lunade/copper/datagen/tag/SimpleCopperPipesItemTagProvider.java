package net.lunade.copper.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lunade.copper.registry.RegisterBlocks;
import net.lunade.copper.tag.SimpleCopperPipesItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class SimpleCopperPipesItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public SimpleCopperPipesItemTagProvider(@NotNull FabricDataOutput output, @NotNull CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		this.getOrCreateTagBuilder(SimpleCopperPipesItemTags.COPPER_PIPES)
				.add(RegisterBlocks.COPPER_PIPE.asItem())
				.add(RegisterBlocks.EXPOSED_COPPER_PIPE.asItem())
				.add(RegisterBlocks.WEATHERED_COPPER_PIPE.asItem())
				.add(RegisterBlocks.OXIDIZED_COPPER_PIPE.asItem())
				.add(RegisterBlocks.WAXED_COPPER_PIPE.asItem())
				.add(RegisterBlocks.WAXED_EXPOSED_COPPER_PIPE.asItem())
				.add(RegisterBlocks.WAXED_WEATHERED_COPPER_PIPE.asItem())
				.add(RegisterBlocks.WAXED_OXIDIZED_COPPER_PIPE.asItem());

		this.getOrCreateTagBuilder(SimpleCopperPipesItemTags.COPPER_FITTINGS)
				.add(RegisterBlocks.COPPER_FITTING.asItem())
				.add(RegisterBlocks.EXPOSED_COPPER_FITTING.asItem())
				.add(RegisterBlocks.WEATHERED_COPPER_FITTING.asItem())
				.add(RegisterBlocks.OXIDIZED_COPPER_FITTING.asItem())
				.add(RegisterBlocks.WAXED_COPPER_FITTING.asItem())
				.add(RegisterBlocks.WAXED_EXPOSED_COPPER_FITTING.asItem())
				.add(RegisterBlocks.WAXED_WEATHERED_COPPER_FITTING.asItem())
				.add(RegisterBlocks.WAXED_OXIDIZED_COPPER_FITTING.asItem());

		this.getOrCreateTagBuilder(SimpleCopperPipesItemTags.IGNORES_COPPER_PIPE_MENU)
				.addOptionalTag(SimpleCopperPipesItemTags.COPPER_PIPES)
				.addOptionalTag(SimpleCopperPipesItemTags.COPPER_FITTINGS)
				.addOptional(ResourceLocation.tryBuild("create", "wrench"));
	}
}

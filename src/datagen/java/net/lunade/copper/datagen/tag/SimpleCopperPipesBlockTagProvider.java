package net.lunade.copper.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lunade.copper.registry.RegisterBlocks;
import net.lunade.copper.tag.SimpleCopperPipesBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class SimpleCopperPipesBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public SimpleCopperPipesBlockTagProvider(@NotNull FabricDataOutput output, @NotNull CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
				.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_PIPES)
				.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_FITTINGS);

		this.getOrCreateTagBuilder(SimpleCopperPipesBlockTags.COPPER_PIPES)
				.add(RegisterBlocks.COPPER_PIPE)
				.add(RegisterBlocks.EXPOSED_COPPER_PIPE)
				.add(RegisterBlocks.WEATHERED_COPPER_PIPE)
				.add(RegisterBlocks.OXIDIZED_COPPER_PIPE)
				.add(RegisterBlocks.WAXED_COPPER_PIPE)
				.add(RegisterBlocks.WAXED_EXPOSED_COPPER_PIPE)
				.add(RegisterBlocks.WAXED_WEATHERED_COPPER_PIPE)
				.add(RegisterBlocks.WAXED_OXIDIZED_COPPER_PIPE);

		this.getOrCreateTagBuilder(SimpleCopperPipesBlockTags.COPPER_FITTINGS)
				.add(RegisterBlocks.COPPER_FITTING)
				.add(RegisterBlocks.EXPOSED_COPPER_FITTING)
				.add(RegisterBlocks.WEATHERED_COPPER_FITTING)
				.add(RegisterBlocks.OXIDIZED_COPPER_FITTING)
				.add(RegisterBlocks.WAXED_COPPER_FITTING)
				.add(RegisterBlocks.WAXED_EXPOSED_COPPER_FITTING)
				.add(RegisterBlocks.WAXED_WEATHERED_COPPER_FITTING)
				.add(RegisterBlocks.WAXED_OXIDIZED_COPPER_FITTING);

		this.getOrCreateTagBuilder(SimpleCopperPipesBlockTags.WAXED)
				.add(RegisterBlocks.WAXED_COPPER_PIPE)
				.add(RegisterBlocks.WAXED_EXPOSED_COPPER_PIPE)
				.add(RegisterBlocks.WAXED_WEATHERED_COPPER_PIPE)
				.add(RegisterBlocks.WAXED_OXIDIZED_COPPER_PIPE)
				.add(RegisterBlocks.WAXED_COPPER_FITTING)
				.add(RegisterBlocks.WAXED_EXPOSED_COPPER_FITTING)
				.add(RegisterBlocks.WAXED_WEATHERED_COPPER_FITTING)
				.add(RegisterBlocks.WAXED_OXIDIZED_COPPER_FITTING);

		this.getOrCreateTagBuilder(SimpleCopperPipesBlockTags.SILENT_PIPES);

		this.getOrCreateTagBuilder(TagKey.create(Registries.BLOCK,  ResourceLocation.tryBuild("create", "wrench_pickup")))
				.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_PIPES)
				.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_FITTINGS);
	}
}

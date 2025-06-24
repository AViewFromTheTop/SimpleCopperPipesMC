package net.lunade.copper.datagen.tag;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.lunade.copper.tag.SimpleCopperPipesBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public final class SimpleCopperPipesBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public SimpleCopperPipesBlockTagProvider(@NotNull FabricDataOutput output, @NotNull CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		this.builder(BlockTags.MINEABLE_WITH_PICKAXE)
			.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_PIPES)
			.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_FITTINGS);

		this.valueLookupBuilder(SimpleCopperPipesBlockTags.COPPER_PIPES)
			.add(SimpleCopperPipesBlocks.COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE);

		this.valueLookupBuilder(SimpleCopperPipesBlockTags.COPPER_FITTINGS)
			.add(SimpleCopperPipesBlocks.COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);

		this.valueLookupBuilder(SimpleCopperPipesBlockTags.WAXED)
			.add(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE)
			.add(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING)
			.add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);

		this.valueLookupBuilder(SimpleCopperPipesBlockTags.SILENT_PIPES);

		this.valueLookupBuilder(TagKey.create(Registries.BLOCK, ResourceLocation.tryBuild("create", "wrench_pickup")))
			.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_PIPES)
			.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_FITTINGS);
	}
}

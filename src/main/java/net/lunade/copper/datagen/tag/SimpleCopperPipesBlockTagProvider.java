package net.lunade.copper.datagen.tag;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.lunade.copper.tag.SimpleCopperPipesBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

public final class SimpleCopperPipesBlockTagProvider extends FabricTagProvider.BlockTagProvider {

	public SimpleCopperPipesBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
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

		this.valueLookupBuilder(SimpleCopperPipesBlockTags.SILENT_COPPER_PIPES);

		this.valueLookupBuilder(SimpleCopperPipesBlockTags.COPPER_PIPE_CHECKS_SUPPORT_SHAPE)
			.addOptionalTag(BlockTags.TRAPDOORS)
			.addOptionalTag(BlockTags.DOORS)
			.addOptionalTag(BlockTags.CLIMBABLE)
			.addOptionalTag(BlockTags.LEAVES)
			.addOptionalTag(ConventionalBlockTags.GLASS_BLOCKS);

		this.valueLookupBuilder(TagKey.create(Registries.BLOCK, Identifier.tryBuild("create", "wrench_pickup")))
			.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_PIPES)
			.addOptionalTag(SimpleCopperPipesBlockTags.COPPER_FITTINGS);
	}
}

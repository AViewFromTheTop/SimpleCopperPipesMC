package net.lunade.copper.datagen.tag;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.lunade.copper.tag.SimpleCopperPipesBlockItemTags;
import net.lunade.copper.tag.SimpleCopperPipesBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

public final class SimpleCopperPipesBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

	public SimpleCopperPipesBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		new SimpleCopperPipesBlockItemTagsProvider(tagId -> BlockItemTagsProvider.wrapForBlocks(this.tag(tagId.block()))).run();

		this.builder(BlockTags.MINEABLE_WITH_PICKAXE)
			.addOptionalTag(SimpleCopperPipesBlockItemTags.COPPER_PIPES.block())
			.addOptionalTag(SimpleCopperPipesBlockItemTags.COPPER_FITTINGS.block());

		this.builder(SimpleCopperPipesBlockTags.SILENT_COPPER_PIPES);

		this.builder(SimpleCopperPipesBlockTags.COPPER_PIPE_CHECKS_SUPPORT_SHAPE)
			.addOptionalTag(BlockTags.TRAPDOORS)
			.addOptionalTag(BlockTags.DOORS)
			.addOptionalTag(BlockTags.CLIMBABLE)
			.addOptionalTag(BlockTags.LEAVES)
			.addOptionalTag(ConventionalBlockTags.GLASS_BLOCKS);

		this.builder(TagKey.create(Registries.BLOCK, Identifier.tryBuild("create", "wrench_pickup")))
			.addOptionalTag(SimpleCopperPipesBlockItemTags.COPPER_PIPES.block())
			.addOptionalTag(SimpleCopperPipesBlockItemTags.COPPER_FITTINGS.block());
	}
}

package net.lunade.copper.datagen.tag;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.lunade.copper.tag.SCPBlockItemTags;
import net.lunade.copper.tag.SCPBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

public final class SCPBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

	public SCPBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		new SCPBlockItemTagsProvider(tagId -> BlockItemTagsProvider.wrapForBlocks(this.tag(tagId.block()))).run();

		this.builder(BlockTags.MINEABLE_WITH_PICKAXE)
			.addOptionalTag(SCPBlockItemTags.COPPER_PIPES.block())
			.addOptionalTag(SCPBlockItemTags.COPPER_FITTINGS.block());

		this.builder(SCPBlockTags.SILENT_COPPER_PIPES);

		this.builder(SCPBlockTags.COPPER_PIPE_CHECKS_SUPPORT_SHAPE)
			.addOptionalTag(BlockTags.TRAPDOORS)
			.addOptionalTag(BlockTags.DOORS)
			.addOptionalTag(BlockTags.CLIMBABLE)
			.addOptionalTag(BlockTags.LEAVES)
			.addOptionalTag(ConventionalBlockTags.GLASS_BLOCKS);

		this.builder(TagKey.create(Registries.BLOCK, Identifier.tryBuild("create", "wrench_pickup")))
			.addOptionalTag(SCPBlockItemTags.COPPER_PIPES.block())
			.addOptionalTag(SCPBlockItemTags.COPPER_FITTINGS.block());
	}
}

package net.lunade.copper.datagen.tag;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.lunade.copper.tag.SimpleCopperPipesBlockItemTags;
import net.lunade.copper.tag.SimpleCopperPipesItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public final class SimpleCopperPipesItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {

	public SimpleCopperPipesItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		new SimpleCopperPipesBlockItemTagsProvider(tagId -> BlockItemTagsProvider.wrapForItems(this.tag(tagId.item()))).run();

		this.builder(SimpleCopperPipesItemTags.IGNORES_COPPER_PIPE_MENU)
			.addOptionalTag(SimpleCopperPipesBlockItemTags.COPPER_PIPES.item())
			.addOptionalTag(SimpleCopperPipesBlockItemTags.COPPER_FITTINGS.item())
			.addOptional(ResourceKey.create(Registries.ITEM, Identifier.tryBuild("thecopperierage", "wrench")))
			.addOptional(ResourceKey.create(Registries.ITEM, Identifier.tryBuild("create", "wrench")));
	}
}

package net.lunade.copper.data.tag;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.lunade.copper.tag.SCPBlockItemTags;
import net.lunade.copper.tag.SCPItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public final class SCPItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {

	public SCPItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		new SCPBlockItemTagsProvider(tagId -> BlockItemTagsProvider.wrapForItems(this.tag(tagId.item()))).run();

		this.builder(SCPItemTags.IGNORES_COPPER_PIPE_MENU)
			.addOptionalTag(SCPBlockItemTags.COPPER_PIPES.item())
			.addOptionalTag(SCPBlockItemTags.COPPER_FITTINGS.item())
			.addOptional(ResourceKey.create(Registries.ITEM, Identifier.tryBuild("thecopperierage", "wrench")))
			.addOptional(ResourceKey.create(Registries.ITEM, Identifier.tryBuild("create", "wrench")));
	}
}

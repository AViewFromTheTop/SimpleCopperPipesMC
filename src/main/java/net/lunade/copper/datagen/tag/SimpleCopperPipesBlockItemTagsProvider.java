package net.lunade.copper.datagen.tag;

import java.util.function.Function;
import net.lunade.copper.references.SimpleCopperPipesBlockItemIDs;
import net.lunade.copper.tag.SimpleCopperPipesBlockItemTags;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.tags.BlockItemTagId;

public final class SimpleCopperPipesBlockItemTagsProvider extends BlockItemTagsProvider {

	SimpleCopperPipesBlockItemTagsProvider(Function<BlockItemTagId, CombinedAppender> tagSupplier) {
		super(tagSupplier);
	}

	@Override
	protected void run() {
		this.tag(SimpleCopperPipesBlockItemTags.COPPER_PIPES)
			.addAll(SimpleCopperPipesBlockItemIDs.COPPER_PIPE.asList());

		this.tag(SimpleCopperPipesBlockItemTags.COPPER_FITTINGS)
			.addAll(SimpleCopperPipesBlockItemIDs.COPPER_PIPE.asList());
	}
}

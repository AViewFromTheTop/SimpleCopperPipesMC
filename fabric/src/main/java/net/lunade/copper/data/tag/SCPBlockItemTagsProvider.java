package net.lunade.copper.data.tag;

import java.util.function.Function;
import net.lunade.copper.references.SCPBlockItemIDs;
import net.lunade.copper.tag.SCPBlockItemTags;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.tags.BlockItemTagId;

public final class SCPBlockItemTagsProvider extends BlockItemTagsProvider {

	SCPBlockItemTagsProvider(Function<BlockItemTagId, CombinedAppender> tagSupplier) {
		super(tagSupplier);
	}

	@Override
	protected void run() {
		this.tag(SCPBlockItemTags.COPPER_PIPES)
			.addAll(SCPBlockItemIDs.COPPER_PIPE.asList());

		this.tag(SCPBlockItemTags.COPPER_FITTINGS)
			.addAll(SCPBlockItemIDs.COPPER_FITTING.asList());
	}
}

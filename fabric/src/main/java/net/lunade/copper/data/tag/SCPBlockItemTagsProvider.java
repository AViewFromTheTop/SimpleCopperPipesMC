/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Simple Copper Pipes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

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

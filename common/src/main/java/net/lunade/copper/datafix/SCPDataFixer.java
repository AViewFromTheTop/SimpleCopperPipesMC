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

package net.lunade.copper.datafix;

import com.mojang.datafixers.schemas.Schema;
import java.util.function.BiConsumer;
import net.fabricmc.frozenblock.datafixer.api.FabricDataFixerBuilder;
import net.fabricmc.frozenblock.datafixer.api.FabricDataFixes;
import net.fabricmc.frozenblock.datafixer.api.SimpleFixes;
import net.lunade.copper.SCPConstants;
import net.lunade.copper.datafix.fix.CanAcceptFieldRenameFix;
import net.lunade.copper.datafix.fix.TransferableDataFieldRenameFix;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringUtil;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public final class SCPDataFixer {
	public static final int DATA_VERSION = 3;

	public static void applyDataFixes(String modId) {
		final FabricDataFixerBuilder builder = new FabricDataFixerBuilder(DATA_VERSION);
		builder.addSchema(0, FabricDataFixes.BASE_SCHEMA);

		final Schema schemaV2 = builder.addSchema(2, NamespacedSchema::new);

		// COLORED BLOCKS
		final BiConsumer<String, String> coloredBlockFixer = (color, block) -> {
			final String blockName = color + "_" + block;
			final String glowingBlockName = "glowing_" + blockName;
			final Identifier targetId = SCPConstants.id("copper_" + block);

			SimpleFixes.addBlockItemRenameFix(
				builder,
				color + " " + block + " -> normal",
				SCPConstants.legacyId(blockName),
				targetId,
				schemaV2
			);

			SimpleFixes.addBlockItemRenameFix(
				builder,
				"glowing " + color + " " + block + " -> normal",
				SCPConstants.legacyId(glowingBlockName),
				targetId,
				schemaV2
			);
		};

		for (String block : new String[]{"pipe", "fitting"}) {
			coloredBlockFixer.accept("black", block);
			coloredBlockFixer.accept("red", block);
			coloredBlockFixer.accept("green", block);
			coloredBlockFixer.accept("brown", block);
			coloredBlockFixer.accept("blue", block);
			coloredBlockFixer.accept("purple", block);
			coloredBlockFixer.accept("cyan", block);
			coloredBlockFixer.accept("light_gray", block);
			coloredBlockFixer.accept("gray", block);
			coloredBlockFixer.accept("pink", block);
			coloredBlockFixer.accept("lime", block);
			coloredBlockFixer.accept("yellow", block);
			coloredBlockFixer.accept("light_blue", block);
			coloredBlockFixer.accept("magenta", block);
			coloredBlockFixer.accept("orange", block);
			coloredBlockFixer.accept("white", block);
		}

		// CORRODED BLOCKS
		SimpleFixes.addBlockItemRenameFix(
			builder,
			"corroded pipe -> normal",
			SCPConstants.legacyId("corroded_pipe"),
			SCPConstants.id("copper_pipe"),
			schemaV2
		);

		SimpleFixes.addBlockItemRenameFix(
			builder,
			"corroded fitting -> normal",
			SCPConstants.legacyId("corroded_fitting"),
			SCPConstants.id("copper_fitting"),
			schemaV2
		);

		// SURVIVING BLOCKS
		final BiConsumer<String, String> survivingBlockFixer = (prefix, block) -> {
			final String blockName = StringUtil.isNullOrEmpty(prefix) ?
				"copper_" + block
				: prefix + "_copper_" + block;
			final String waxedBlockName = "waxed_" + blockName;

			SimpleFixes.addBlockItemRenameFix(
				builder,
				"upgrade " + blockName,
				SCPConstants.legacyId(blockName),
				SCPConstants.id(blockName),
				schemaV2
			);

			SimpleFixes.addBlockItemRenameFix(
				builder,
				"upgrade " + waxedBlockName,
				SCPConstants.legacyId(waxedBlockName),
				SCPConstants.id(waxedBlockName),
				schemaV2
			);
		};

		for (String block : new String[]{"pipe", "fitting"}) {
			for (String prefix : new String[]{"", "exposed", "weathered", "oxidized"}) {
				survivingBlockFixer.accept(prefix, block);
			}
		}

		SimpleFixes.addBlockEntityRenameFix(
			builder,
			"upgrade copper pipe block entity",
			SCPConstants.legacyId("copper_pipe"),
			SCPConstants.id("copper_pipe"),
			schemaV2
		);

		SimpleFixes.addBlockEntityRenameFix(
			builder,
			"upgrade copper pipe fitting entity",
			SCPConstants.legacyId("copper_fitting"),
			SCPConstants.id("copper_fitting"),
			schemaV2
		);

		final Schema schemaV3 = builder.addSchema(3, NamespacedSchema::new);
		builder.addFixer(new TransferableDataFieldRenameFix(SCPConstants.id("copper_pipe").toString(), schemaV3));
		builder.addFixer(new TransferableDataFieldRenameFix(SCPConstants.id("copper_fitting").toString(), schemaV3));
		builder.addFixer(new CanAcceptFieldRenameFix(SCPConstants.id("copper_pipe").toString(), schemaV3));

		FabricDataFixes.buildAndRegisterFixer(modId, builder);
	}

	private SCPDataFixer() {}
}

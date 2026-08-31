package net.lunade.copper.datafix;

import com.mojang.datafixers.schemas.Schema;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.datafix.fix.CanAcceptFieldRenameFix;
import net.lunade.copper.datafix.fix.TransferableDataFieldRenameFix;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.BlockEntityRenameFix;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.fabricmc.frozenblock.datafixer.api.FabricDataFixerBuilder;
import net.fabricmc.frozenblock.datafixer.api.FabricDataFixes;
import net.fabricmc.frozenblock.datafixer.api.SimpleFixes;

public class SimpleCopperPipesDataFixer {
	private static final String COPPER_PIPE = SimpleCopperPipesConstants.id("copper_pipe").toString();
	private static final String COPPER_FITTING = SimpleCopperPipesConstants.id("copper_fitting").toString();
	public static final int DATA_VERSION = 3;

	public static void applyDataFixes(String modId) {
		final FabricDataFixerBuilder builder = new FabricDataFixerBuilder(DATA_VERSION);
		builder.addSchema(0, FabricDataFixes.BASE_SCHEMA);

		final var normalPipe = SimpleCopperPipesConstants.id("copper_pipe");
		final var normalFitting = SimpleCopperPipesConstants.id("copper_fitting");
		final Schema schemaV2 = builder.addSchema(2, NamespacedSchema::new);

		//PIPE

		SimpleFixes.addBlockRenameFix(builder, "black -> normal", SimpleCopperPipesConstants.legacyColoredPipe("black"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "black -> normal", SimpleCopperPipesConstants.legacyColoredPipe("black"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing black -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("black"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing black -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("black"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "red -> normal", SimpleCopperPipesConstants.legacyColoredPipe("red"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "red -> normal", SimpleCopperPipesConstants.legacyColoredPipe("red"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing red -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("red"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing red -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("red"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "green -> normal", SimpleCopperPipesConstants.legacyColoredPipe("green"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "green -> normal", SimpleCopperPipesConstants.legacyColoredPipe("green"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing green -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("green"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing green -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("green"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "brown -> normal", SimpleCopperPipesConstants.legacyColoredPipe("brown"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "brown -> normal", SimpleCopperPipesConstants.legacyColoredPipe("brown"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing brown -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("brown"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing brown -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("brown"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "blue -> normal", SimpleCopperPipesConstants.legacyColoredPipe("blue"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "blue -> normal", SimpleCopperPipesConstants.legacyColoredPipe("blue"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing blue -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("blue"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing blue -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("blue"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "purple -> normal", SimpleCopperPipesConstants.legacyColoredPipe("purple"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "purple -> normal", SimpleCopperPipesConstants.legacyColoredPipe("purple"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing purple -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("purple"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing purple -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("purple"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "cyan -> normal", SimpleCopperPipesConstants.legacyColoredPipe("cyan"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "cyan -> normal", SimpleCopperPipesConstants.legacyColoredPipe("cyan"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing cyan -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("cyan"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing cyan -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("cyan"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "light_gray -> normal", SimpleCopperPipesConstants.legacyColoredPipe("light_gray"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "light_gray -> normal", SimpleCopperPipesConstants.legacyColoredPipe("light_gray"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing light_gray -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("light_gray"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing light_gray -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("light_gray"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "gray -> normal", SimpleCopperPipesConstants.legacyColoredPipe("gray"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "gray -> normal", SimpleCopperPipesConstants.legacyColoredPipe("gray"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing gray -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("gray"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing gray -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("gray"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "pink -> normal", SimpleCopperPipesConstants.legacyColoredPipe("pink"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "pink -> normal", SimpleCopperPipesConstants.legacyColoredPipe("pink"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing pink -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("pink"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing pink -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("pink"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "lime -> normal", SimpleCopperPipesConstants.legacyColoredPipe("lime"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "lime -> normal", SimpleCopperPipesConstants.legacyColoredPipe("lime"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing lime -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("lime"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing lime -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("lime"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "yellow -> normal", SimpleCopperPipesConstants.legacyColoredPipe("yellow"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "yellow -> normal", SimpleCopperPipesConstants.legacyColoredPipe("yellow"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing yellow -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("yellow"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing yellow -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("yellow"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "light_blue -> normal", SimpleCopperPipesConstants.legacyColoredPipe("light_blue"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "light_blue -> normal", SimpleCopperPipesConstants.legacyColoredPipe("light_blue"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing light_blue -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("light_blue"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing light_blue -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("light_blue"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "magenta -> normal", SimpleCopperPipesConstants.legacyColoredPipe("magenta"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "magenta -> normal", SimpleCopperPipesConstants.legacyColoredPipe("magenta"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing magenta -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("magenta"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing magenta -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("magenta"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "orange -> normal", SimpleCopperPipesConstants.legacyColoredPipe("orange"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "orange -> normal", SimpleCopperPipesConstants.legacyColoredPipe("orange"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing orange -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("orange"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing orange -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("orange"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "white -> normal", SimpleCopperPipesConstants.legacyColoredPipe("white"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "white -> normal", SimpleCopperPipesConstants.legacyColoredPipe("white"), normalPipe, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing white -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("white"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing white -> normal", SimpleCopperPipesConstants.legacyGlowingPipe("white"), normalPipe, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "corroded -> normal", SimpleCopperPipesConstants.legacyId("corroded_pipe"), normalPipe, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "corroded -> normal", SimpleCopperPipesConstants.legacyId("corroded_pipe"), normalPipe, schemaV2);

		// FITTING

		SimpleFixes.addBlockRenameFix(builder, "black -> normal", SimpleCopperPipesConstants.legacyColoredFitting("black"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "black -> normal", SimpleCopperPipesConstants.legacyColoredFitting("black"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing black -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("black"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing black -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("black"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "red -> normal", SimpleCopperPipesConstants.legacyColoredFitting("red"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "red -> normal", SimpleCopperPipesConstants.legacyColoredFitting("red"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing red -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("red"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing red -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("red"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "green -> normal", SimpleCopperPipesConstants.legacyColoredFitting("green"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "green -> normal", SimpleCopperPipesConstants.legacyColoredFitting("green"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing green -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("green"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing green -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("green"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "brown -> normal", SimpleCopperPipesConstants.legacyColoredFitting("brown"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "brown -> normal", SimpleCopperPipesConstants.legacyColoredFitting("brown"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing brown -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("brown"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing brown -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("brown"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "blue -> normal", SimpleCopperPipesConstants.legacyColoredFitting("blue"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "blue -> normal", SimpleCopperPipesConstants.legacyColoredFitting("blue"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing blue -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("blue"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing blue -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("blue"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "purple -> normal", SimpleCopperPipesConstants.legacyColoredFitting("purple"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "purple -> normal", SimpleCopperPipesConstants.legacyColoredFitting("purple"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing purple -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("purple"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing purple -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("purple"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "cyan -> normal", SimpleCopperPipesConstants.legacyColoredFitting("cyan"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "cyan -> normal", SimpleCopperPipesConstants.legacyColoredFitting("cyan"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing cyan -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("cyan"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing cyan -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("cyan"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "light_gray -> normal", SimpleCopperPipesConstants.legacyColoredFitting("light_gray"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "light_gray -> normal", SimpleCopperPipesConstants.legacyColoredFitting("light_gray"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing light_gray -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("light_gray"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing light_gray -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("light_gray"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "gray -> normal", SimpleCopperPipesConstants.legacyColoredFitting("gray"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "gray -> normal", SimpleCopperPipesConstants.legacyColoredFitting("gray"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing gray -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("gray"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing gray -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("gray"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "pink -> normal", SimpleCopperPipesConstants.legacyColoredFitting("pink"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "pink -> normal", SimpleCopperPipesConstants.legacyColoredFitting("pink"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing pink -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("pink"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing pink -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("pink"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "lime -> normal", SimpleCopperPipesConstants.legacyColoredFitting("lime"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "lime -> normal", SimpleCopperPipesConstants.legacyColoredFitting("lime"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing lime -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("lime"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing lime -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("lime"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "yellow -> normal", SimpleCopperPipesConstants.legacyColoredFitting("yellow"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "yellow -> normal", SimpleCopperPipesConstants.legacyColoredFitting("yellow"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing yellow -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("yellow"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing yellow -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("yellow"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "light_blue -> normal", SimpleCopperPipesConstants.legacyColoredFitting("light_blue"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "light_blue -> normal", SimpleCopperPipesConstants.legacyColoredFitting("light_blue"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing light_blue -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("light_blue"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing light_blue -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("light_blue"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "magenta -> normal", SimpleCopperPipesConstants.legacyColoredFitting("magenta"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "magenta -> normal", SimpleCopperPipesConstants.legacyColoredFitting("magenta"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing magenta -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("magenta"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing magenta -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("magenta"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "orange -> normal", SimpleCopperPipesConstants.legacyColoredFitting("orange"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "orange -> normal", SimpleCopperPipesConstants.legacyColoredFitting("orange"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing orange -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("orange"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing orange -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("orange"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "white -> normal", SimpleCopperPipesConstants.legacyColoredFitting("white"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "white -> normal", SimpleCopperPipesConstants.legacyColoredFitting("white"), normalFitting, schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "glowing white -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("white"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "glowing white -> normal", SimpleCopperPipesConstants.legacyGlowingFitting("white"), normalFitting, schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "corroded -> normal", SimpleCopperPipesConstants.legacyId("corroded_fitting"), normalFitting, schemaV2);
		SimpleFixes.addItemRenameFix(builder, "corroded -> normal", SimpleCopperPipesConstants.legacyId("corroded_fitting"), normalFitting, schemaV2);

		// SURVIVING BLOCKS
		SimpleFixes.addBlockRenameFix(builder, "upgrade pipe", SimpleCopperPipesConstants.legacyId("copper_pipe"), SimpleCopperPipesConstants.id("copper_pipe"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade pipe", SimpleCopperPipesConstants.legacyId("copper_pipe"), SimpleCopperPipesConstants.id("copper_pipe"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade exposed pipe", SimpleCopperPipesConstants.legacyId("exposed_copper_pipe"), SimpleCopperPipesConstants.id("exposed_copper_pipe"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade exposed pipe", SimpleCopperPipesConstants.legacyId("exposed_copper_pipe"), SimpleCopperPipesConstants.id("exposed_copper_pipe"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade weathered pipe", SimpleCopperPipesConstants.legacyId("weathered_copper_pipe"), SimpleCopperPipesConstants.id("weathered_copper_pipe"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade weathered pipe", SimpleCopperPipesConstants.legacyId("weathered_copper_pipe"), SimpleCopperPipesConstants.id("weathered_copper_pipe"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade oxidized pipe", SimpleCopperPipesConstants.legacyId("oxidized_copper_pipe"), SimpleCopperPipesConstants.id("oxidized_copper_pipe"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade oxidized pipe", SimpleCopperPipesConstants.legacyId("oxidized_copper_pipe"), SimpleCopperPipesConstants.id("oxidized_copper_pipe"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade waxed pipe", SimpleCopperPipesConstants.legacyId("waxed_copper_pipe"), SimpleCopperPipesConstants.id("waxed_copper_pipe"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade waxed pipe", SimpleCopperPipesConstants.legacyId("waxed_copper_pipe"), SimpleCopperPipesConstants.id("waxed_copper_pipe"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade waxed exposed pipe", SimpleCopperPipesConstants.legacyId("waxed_exposed_copper_pipe"), SimpleCopperPipesConstants.id("waxed_exposed_copper_pipe"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade waxed exposed pipe", SimpleCopperPipesConstants.legacyId("waxed_exposed_copper_pipe"), SimpleCopperPipesConstants.id("waxed_exposed_copper_pipe"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade waxed weathered pipe", SimpleCopperPipesConstants.legacyId("waxed_weathered_copper_pipe"), SimpleCopperPipesConstants.id("waxed_weathered_copper_pipe"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade waxed weathered pipe", SimpleCopperPipesConstants.legacyId("waxed_weathered_copper_pipe"), SimpleCopperPipesConstants.id("waxed_weathered_copper_pipe"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade waxed oxidized pipe", SimpleCopperPipesConstants.legacyId("waxed_oxidized_copper_pipe"), SimpleCopperPipesConstants.id("waxed_oxidized_copper_pipe"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade waxed oxidized pipe", SimpleCopperPipesConstants.legacyId("waxed_oxidized_copper_pipe"), SimpleCopperPipesConstants.id("waxed_oxidized_copper_pipe"), schemaV2);

		SimpleFixes.addBlockRenameFix(builder, "upgrade fitting", SimpleCopperPipesConstants.legacyId("copper_fitting"), SimpleCopperPipesConstants.id("copper_fitting"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade fitting", SimpleCopperPipesConstants.legacyId("copper_fitting"), SimpleCopperPipesConstants.id("copper_fitting"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade exposed fitting", SimpleCopperPipesConstants.legacyId("exposed_copper_fitting"), SimpleCopperPipesConstants.id("exposed_copper_fitting"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade exposed fitting", SimpleCopperPipesConstants.legacyId("exposed_copper_fitting"), SimpleCopperPipesConstants.id("exposed_copper_fitting"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade weathered fitting", SimpleCopperPipesConstants.legacyId("weathered_copper_fitting"), SimpleCopperPipesConstants.id("weathered_copper_fitting"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade weathered fitting", SimpleCopperPipesConstants.legacyId("weathered_copper_fitting"), SimpleCopperPipesConstants.id("weathered_copper_fitting"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade oxidized fitting", SimpleCopperPipesConstants.legacyId("oxidized_copper_fitting"), SimpleCopperPipesConstants.id("oxidized_copper_fitting"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade oxidized fitting", SimpleCopperPipesConstants.legacyId("oxidized_copper_fitting"), SimpleCopperPipesConstants.id("oxidized_copper_fitting"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade waxed fitting", SimpleCopperPipesConstants.legacyId("waxed_copper_fitting"), SimpleCopperPipesConstants.id("waxed_copper_fitting"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade waxed fitting", SimpleCopperPipesConstants.legacyId("waxed_copper_fitting"), SimpleCopperPipesConstants.id("waxed_copper_fitting"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade waxed exposed fitting", SimpleCopperPipesConstants.legacyId("waxed_exposed_copper_fitting"), SimpleCopperPipesConstants.id("waxed_exposed_copper_fitting"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade waxed exposed fitting", SimpleCopperPipesConstants.legacyId("waxed_exposed_copper_fitting"), SimpleCopperPipesConstants.id("waxed_exposed_copper_fitting"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade waxed weathered fitting", SimpleCopperPipesConstants.legacyId("waxed_weathered_copper_fitting"), SimpleCopperPipesConstants.id("waxed_weathered_copper_fitting"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade waxed weathered fitting", SimpleCopperPipesConstants.legacyId("waxed_weathered_copper_fitting"), SimpleCopperPipesConstants.id("waxed_weathered_copper_fitting"), schemaV2);
		SimpleFixes.addBlockRenameFix(builder, "upgrade waxed oxidized fitting", SimpleCopperPipesConstants.legacyId("waxed_oxidized_copper_fitting"), SimpleCopperPipesConstants.id("waxed_oxidized_copper_fitting"), schemaV2);
		SimpleFixes.addItemRenameFix(builder, "upgrade waxed oxidized fitting", SimpleCopperPipesConstants.legacyId("waxed_oxidized_copper_fitting"), SimpleCopperPipesConstants.id("waxed_oxidized_copper_fitting"), schemaV2);

		builder.addFixer(
			BlockEntityRenameFix.create(
				schemaV2,
				"upgrade copper pipe block entity",
				DataFixers.createRenamer(
					"lunade:copper_pipe",
					"simple_copper_pipes:copper_pipe"
				)
			)
		);

		builder.addFixer(
			BlockEntityRenameFix.create(
				schemaV2,
				"upgrade copper fitting block entity",
				DataFixers.createRenamer(
					"lunade:copper_fitting",
					"simple_copper_pipes:copper_fitting"
				)
			)
		);

		final Schema schemaV3 = builder.addSchema(3, NamespacedSchema::new);
		builder.addFixer(new TransferableDataFieldRenameFix(COPPER_PIPE, schemaV3));
		builder.addFixer(new TransferableDataFieldRenameFix(COPPER_FITTING, schemaV3));
		builder.addFixer(new CanAcceptFieldRenameFix(COPPER_PIPE, schemaV3));

		FabricDataFixes.buildAndRegisterFixer(modId, builder);
	}
}

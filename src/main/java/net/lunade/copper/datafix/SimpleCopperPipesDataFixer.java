package net.lunade.copper.datafix;

import com.mojang.datafixers.schemas.Schema;
import net.fabricmc.loader.api.ModContainer;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.BlockEntityRenameFix;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes;
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.SimpleFixes;

public class SimpleCopperPipesDataFixer {
    public static final int DATA_VERSION = 2;

    public static void applyDataFixes(final @NotNull ModContainer mod) {
        var builder = new QuiltDataFixerBuilder(DATA_VERSION);
        builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA);

        final var normalPipe = SimpleCopperPipesBlocks.id("copper_pipe");
        final var normalFitting = SimpleCopperPipesBlocks.id("copper_fitting");
        Schema schemaV2 = builder.addSchema(2, NamespacedSchema::new);

        //PIPE

        SimpleFixes.addBlockRenameFix(builder, "black -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("black"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "black -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("black"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing black -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("black"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing black -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("black"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "red -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("red"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "red -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("red"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing red -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("red"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing red -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("red"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "green -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("green"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "green -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("green"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing green -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("green"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing green -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("green"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "brown -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("brown"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "brown -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("brown"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing brown -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("brown"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing brown -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("brown"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "blue -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("blue"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "blue -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("blue"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing blue -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("blue"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing blue -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("blue"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "purple -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("purple"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "purple -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("purple"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing purple -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("purple"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing purple -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("purple"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "cyan -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("cyan"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "cyan -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("cyan"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing cyan -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("cyan"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing cyan -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("cyan"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "light_gray -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("light_gray"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "light_gray -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("light_gray"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing light_gray -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("light_gray"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing light_gray -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("light_gray"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "gray -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("gray"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "gray -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("gray"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing gray -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("gray"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing gray -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("gray"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "pink -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("pink"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "pink -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("pink"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing pink -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("pink"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing pink -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("pink"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "lime -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("lime"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "lime -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("lime"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing lime -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("lime"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing lime -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("lime"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "yellow -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("yellow"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "yellow -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("yellow"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing yellow -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("yellow"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing yellow -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("yellow"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "light_blue -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("light_blue"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "light_blue -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("light_blue"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing light_blue -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("light_blue"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing light_blue -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("light_blue"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "magenta -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("magenta"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "magenta -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("magenta"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing magenta -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("magenta"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing magenta -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("magenta"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "orange -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("orange"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "orange -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("orange"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing orange -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("orange"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing orange -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("orange"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "white -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("white"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "white -> normal", SimpleCopperPipesBlocks.legacyColoredPipe("white"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing white -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("white"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing white -> normal", SimpleCopperPipesBlocks.legacyGlowingPipe("white"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "corroded -> normal", SimpleCopperPipesBlocks.legacyId("corroded_pipe"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "corroded -> normal", SimpleCopperPipesBlocks.legacyId("corroded_pipe"), normalPipe, schemaV2);

        // FITTING

        SimpleFixes.addBlockRenameFix(builder, "black -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("black"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "black -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("black"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing black -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("black"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing black -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("black"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "red -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("red"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "red -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("red"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing red -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("red"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing red -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("red"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "green -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("green"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "green -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("green"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing green -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("green"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing green -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("green"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "brown -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("brown"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "brown -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("brown"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing brown -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("brown"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing brown -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("brown"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "blue -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("blue"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "blue -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("blue"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing blue -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("blue"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing blue -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("blue"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "purple -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("purple"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "purple -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("purple"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing purple -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("purple"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing purple -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("purple"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "cyan -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("cyan"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "cyan -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("cyan"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing cyan -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("cyan"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing cyan -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("cyan"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "light_gray -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("light_gray"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "light_gray -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("light_gray"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing light_gray -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("light_gray"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing light_gray -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("light_gray"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "gray -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("gray"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "gray -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("gray"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing gray -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("gray"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing gray -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("gray"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "pink -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("pink"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "pink -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("pink"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing pink -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("pink"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing pink -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("pink"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "lime -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("lime"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "lime -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("lime"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing lime -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("lime"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing lime -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("lime"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "yellow -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("yellow"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "yellow -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("yellow"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing yellow -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("yellow"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing yellow -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("yellow"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "light_blue -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("light_blue"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "light_blue -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("light_blue"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing light_blue -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("light_blue"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing light_blue -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("light_blue"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "magenta -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("magenta"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "magenta -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("magenta"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing magenta -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("magenta"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing magenta -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("magenta"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "orange -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("orange"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "orange -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("orange"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing orange -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("orange"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing orange -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("orange"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "white -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("white"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "white -> normal", SimpleCopperPipesBlocks.legacyColoredFitting("white"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing white -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("white"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing white -> normal", SimpleCopperPipesBlocks.legacyGlowingFitting("white"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "corroded -> normal", SimpleCopperPipesBlocks.legacyId("corroded_fitting"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "corroded -> normal", SimpleCopperPipesBlocks.legacyId("corroded_fitting"), normalFitting, schemaV2);

        // SURVIVING BLOCKS
        SimpleFixes.addBlockRenameFix(builder, "upgrade pipe", SimpleCopperPipesBlocks.legacyId("copper_pipe"), SimpleCopperPipesBlocks.id("copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade pipe", SimpleCopperPipesBlocks.legacyId("copper_pipe"), SimpleCopperPipesBlocks.id("copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade exposed pipe", SimpleCopperPipesBlocks.legacyId("exposed_copper_pipe"), SimpleCopperPipesBlocks.id("exposed_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade exposed pipe", SimpleCopperPipesBlocks.legacyId("exposed_copper_pipe"), SimpleCopperPipesBlocks.id("exposed_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade weathered pipe", SimpleCopperPipesBlocks.legacyId("weathered_copper_pipe"), SimpleCopperPipesBlocks.id("weathered_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade weathered pipe", SimpleCopperPipesBlocks.legacyId("weathered_copper_pipe"), SimpleCopperPipesBlocks.id("weathered_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade oxidized pipe", SimpleCopperPipesBlocks.legacyId("oxidized_copper_pipe"), SimpleCopperPipesBlocks.id("oxidized_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade oxidized pipe", SimpleCopperPipesBlocks.legacyId("oxidized_copper_pipe"), SimpleCopperPipesBlocks.id("oxidized_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed pipe", SimpleCopperPipesBlocks.legacyId("waxed_copper_pipe"), SimpleCopperPipesBlocks.id("waxed_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed pipe", SimpleCopperPipesBlocks.legacyId("waxed_copper_pipe"), SimpleCopperPipesBlocks.id("waxed_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed exposed pipe", SimpleCopperPipesBlocks.legacyId("waxed_exposed_copper_pipe"), SimpleCopperPipesBlocks.id("waxed_exposed_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed exposed pipe", SimpleCopperPipesBlocks.legacyId("waxed_exposed_copper_pipe"), SimpleCopperPipesBlocks.id("waxed_exposed_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed weathered pipe", SimpleCopperPipesBlocks.legacyId("waxed_weathered_copper_pipe"), SimpleCopperPipesBlocks.id("waxed_weathered_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed weathered pipe", SimpleCopperPipesBlocks.legacyId("waxed_weathered_copper_pipe"), SimpleCopperPipesBlocks.id("waxed_weathered_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed oxidized pipe", SimpleCopperPipesBlocks.legacyId("waxed_oxidized_copper_pipe"), SimpleCopperPipesBlocks.id("waxed_oxidized_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed oxidized pipe", SimpleCopperPipesBlocks.legacyId("waxed_oxidized_copper_pipe"), SimpleCopperPipesBlocks.id("waxed_oxidized_copper_pipe"), schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "upgrade fitting", SimpleCopperPipesBlocks.legacyId("copper_fitting"), SimpleCopperPipesBlocks.id("copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade fitting", SimpleCopperPipesBlocks.legacyId("copper_fitting"), SimpleCopperPipesBlocks.id("copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade exposed fitting", SimpleCopperPipesBlocks.legacyId("exposed_copper_fitting"), SimpleCopperPipesBlocks.id("exposed_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade exposed fitting", SimpleCopperPipesBlocks.legacyId("exposed_copper_fitting"), SimpleCopperPipesBlocks.id("exposed_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade weathered fitting", SimpleCopperPipesBlocks.legacyId("weathered_copper_fitting"), SimpleCopperPipesBlocks.id("weathered_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade weathered fitting", SimpleCopperPipesBlocks.legacyId("weathered_copper_fitting"), SimpleCopperPipesBlocks.id("weathered_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade oxidized fitting", SimpleCopperPipesBlocks.legacyId("oxidized_copper_fitting"), SimpleCopperPipesBlocks.id("oxidized_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade oxidized fitting", SimpleCopperPipesBlocks.legacyId("oxidized_copper_fitting"), SimpleCopperPipesBlocks.id("oxidized_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed fitting", SimpleCopperPipesBlocks.legacyId("waxed_copper_fitting"), SimpleCopperPipesBlocks.id("waxed_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed fitting", SimpleCopperPipesBlocks.legacyId("waxed_copper_fitting"), SimpleCopperPipesBlocks.id("waxed_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed exposed fitting", SimpleCopperPipesBlocks.legacyId("waxed_exposed_copper_fitting"), SimpleCopperPipesBlocks.id("waxed_exposed_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed exposed fitting", SimpleCopperPipesBlocks.legacyId("waxed_exposed_copper_fitting"), SimpleCopperPipesBlocks.id("waxed_exposed_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed weathered fitting", SimpleCopperPipesBlocks.legacyId("waxed_weathered_copper_fitting"), SimpleCopperPipesBlocks.id("waxed_weathered_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed weathered fitting", SimpleCopperPipesBlocks.legacyId("waxed_weathered_copper_fitting"), SimpleCopperPipesBlocks.id("waxed_weathered_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed oxidized fitting", SimpleCopperPipesBlocks.legacyId("waxed_oxidized_copper_fitting"), SimpleCopperPipesBlocks.id("waxed_oxidized_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed oxidized fitting", SimpleCopperPipesBlocks.legacyId("waxed_oxidized_copper_fitting"), SimpleCopperPipesBlocks.id("waxed_oxidized_copper_fitting"), schemaV2);

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

        QuiltDataFixes.buildAndRegisterFixer(mod, builder);
    }
}

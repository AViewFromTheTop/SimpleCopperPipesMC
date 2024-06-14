package net.lunade.copper.datafix;

import com.mojang.datafixers.schemas.Schema;
import net.fabricmc.loader.api.ModContainer;
import net.lunade.copper.registry.RegisterBlocks;
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

        final var normalPipe = RegisterBlocks.id("copper_pipe");
        final var normalFitting = RegisterBlocks.id("copper_fitting");
        Schema schemaV2 = builder.addSchema(2, NamespacedSchema::new);

        //PIPE

        SimpleFixes.addBlockRenameFix(builder, "black -> normal", RegisterBlocks.legacyColoredPipe("black"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "black -> normal", RegisterBlocks.legacyColoredPipe("black"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing black -> normal", RegisterBlocks.legacyGlowingPipe("black"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing black -> normal", RegisterBlocks.legacyGlowingPipe("black"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "red -> normal", RegisterBlocks.legacyColoredPipe("red"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "red -> normal", RegisterBlocks.legacyColoredPipe("red"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing red -> normal", RegisterBlocks.legacyGlowingPipe("red"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing red -> normal", RegisterBlocks.legacyGlowingPipe("red"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "green -> normal", RegisterBlocks.legacyColoredPipe("green"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "green -> normal", RegisterBlocks.legacyColoredPipe("green"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing green -> normal", RegisterBlocks.legacyGlowingPipe("green"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing green -> normal", RegisterBlocks.legacyGlowingPipe("green"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "brown -> normal", RegisterBlocks.legacyColoredPipe("brown"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "brown -> normal", RegisterBlocks.legacyColoredPipe("brown"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing brown -> normal", RegisterBlocks.legacyGlowingPipe("brown"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing brown -> normal", RegisterBlocks.legacyGlowingPipe("brown"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "blue -> normal", RegisterBlocks.legacyColoredPipe("blue"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "blue -> normal", RegisterBlocks.legacyColoredPipe("blue"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing blue -> normal", RegisterBlocks.legacyGlowingPipe("blue"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing blue -> normal", RegisterBlocks.legacyGlowingPipe("blue"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "purple -> normal", RegisterBlocks.legacyColoredPipe("purple"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "purple -> normal", RegisterBlocks.legacyColoredPipe("purple"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing purple -> normal", RegisterBlocks.legacyGlowingPipe("purple"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing purple -> normal", RegisterBlocks.legacyGlowingPipe("purple"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "cyan -> normal", RegisterBlocks.legacyColoredPipe("cyan"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "cyan -> normal", RegisterBlocks.legacyColoredPipe("cyan"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing cyan -> normal", RegisterBlocks.legacyGlowingPipe("cyan"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing cyan -> normal", RegisterBlocks.legacyGlowingPipe("cyan"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "light_gray -> normal", RegisterBlocks.legacyColoredPipe("light_gray"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "light_gray -> normal", RegisterBlocks.legacyColoredPipe("light_gray"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing light_gray -> normal", RegisterBlocks.legacyGlowingPipe("light_gray"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing light_gray -> normal", RegisterBlocks.legacyGlowingPipe("light_gray"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "gray -> normal", RegisterBlocks.legacyColoredPipe("gray"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "gray -> normal", RegisterBlocks.legacyColoredPipe("gray"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing gray -> normal", RegisterBlocks.legacyGlowingPipe("gray"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing gray -> normal", RegisterBlocks.legacyGlowingPipe("gray"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "pink -> normal", RegisterBlocks.legacyColoredPipe("pink"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "pink -> normal", RegisterBlocks.legacyColoredPipe("pink"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing pink -> normal", RegisterBlocks.legacyGlowingPipe("pink"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing pink -> normal", RegisterBlocks.legacyGlowingPipe("pink"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "lime -> normal", RegisterBlocks.legacyColoredPipe("lime"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "lime -> normal", RegisterBlocks.legacyColoredPipe("lime"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing lime -> normal", RegisterBlocks.legacyGlowingPipe("lime"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing lime -> normal", RegisterBlocks.legacyGlowingPipe("lime"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "yellow -> normal", RegisterBlocks.legacyColoredPipe("yellow"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "yellow -> normal", RegisterBlocks.legacyColoredPipe("yellow"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing yellow -> normal", RegisterBlocks.legacyGlowingPipe("yellow"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing yellow -> normal", RegisterBlocks.legacyGlowingPipe("yellow"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "light_blue -> normal", RegisterBlocks.legacyColoredPipe("light_blue"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "light_blue -> normal", RegisterBlocks.legacyColoredPipe("light_blue"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing light_blue -> normal", RegisterBlocks.legacyGlowingPipe("light_blue"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing light_blue -> normal", RegisterBlocks.legacyGlowingPipe("light_blue"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "magenta -> normal", RegisterBlocks.legacyColoredPipe("magenta"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "magenta -> normal", RegisterBlocks.legacyColoredPipe("magenta"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing magenta -> normal", RegisterBlocks.legacyGlowingPipe("magenta"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing magenta -> normal", RegisterBlocks.legacyGlowingPipe("magenta"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "orange -> normal", RegisterBlocks.legacyColoredPipe("orange"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "orange -> normal", RegisterBlocks.legacyColoredPipe("orange"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing orange -> normal", RegisterBlocks.legacyGlowingPipe("orange"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing orange -> normal", RegisterBlocks.legacyGlowingPipe("orange"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "white -> normal", RegisterBlocks.legacyColoredPipe("white"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "white -> normal", RegisterBlocks.legacyColoredPipe("white"), normalPipe, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing white -> normal", RegisterBlocks.legacyGlowingPipe("white"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing white -> normal", RegisterBlocks.legacyGlowingPipe("white"), normalPipe, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "corroded -> normal", RegisterBlocks.legacyId("corroded_pipe"), normalPipe, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "corroded -> normal", RegisterBlocks.legacyId("corroded_pipe"), normalPipe, schemaV2);

        // FITTING

        SimpleFixes.addBlockRenameFix(builder, "black -> normal", RegisterBlocks.legacyColoredFitting("black"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "black -> normal", RegisterBlocks.legacyColoredFitting("black"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing black -> normal", RegisterBlocks.legacyGlowingFitting("black"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing black -> normal", RegisterBlocks.legacyGlowingFitting("black"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "red -> normal", RegisterBlocks.legacyColoredFitting("red"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "red -> normal", RegisterBlocks.legacyColoredFitting("red"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing red -> normal", RegisterBlocks.legacyGlowingFitting("red"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing red -> normal", RegisterBlocks.legacyGlowingFitting("red"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "green -> normal", RegisterBlocks.legacyColoredFitting("green"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "green -> normal", RegisterBlocks.legacyColoredFitting("green"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing green -> normal", RegisterBlocks.legacyGlowingFitting("green"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing green -> normal", RegisterBlocks.legacyGlowingFitting("green"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "brown -> normal", RegisterBlocks.legacyColoredFitting("brown"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "brown -> normal", RegisterBlocks.legacyColoredFitting("brown"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing brown -> normal", RegisterBlocks.legacyGlowingFitting("brown"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing brown -> normal", RegisterBlocks.legacyGlowingFitting("brown"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "blue -> normal", RegisterBlocks.legacyColoredFitting("blue"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "blue -> normal", RegisterBlocks.legacyColoredFitting("blue"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing blue -> normal", RegisterBlocks.legacyGlowingFitting("blue"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing blue -> normal", RegisterBlocks.legacyGlowingFitting("blue"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "purple -> normal", RegisterBlocks.legacyColoredFitting("purple"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "purple -> normal", RegisterBlocks.legacyColoredFitting("purple"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing purple -> normal", RegisterBlocks.legacyGlowingFitting("purple"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing purple -> normal", RegisterBlocks.legacyGlowingFitting("purple"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "cyan -> normal", RegisterBlocks.legacyColoredFitting("cyan"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "cyan -> normal", RegisterBlocks.legacyColoredFitting("cyan"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing cyan -> normal", RegisterBlocks.legacyGlowingFitting("cyan"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing cyan -> normal", RegisterBlocks.legacyGlowingFitting("cyan"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "light_gray -> normal", RegisterBlocks.legacyColoredFitting("light_gray"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "light_gray -> normal", RegisterBlocks.legacyColoredFitting("light_gray"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing light_gray -> normal", RegisterBlocks.legacyGlowingFitting("light_gray"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing light_gray -> normal", RegisterBlocks.legacyGlowingFitting("light_gray"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "gray -> normal", RegisterBlocks.legacyColoredFitting("gray"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "gray -> normal", RegisterBlocks.legacyColoredFitting("gray"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing gray -> normal", RegisterBlocks.legacyGlowingFitting("gray"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing gray -> normal", RegisterBlocks.legacyGlowingFitting("gray"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "pink -> normal", RegisterBlocks.legacyColoredFitting("pink"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "pink -> normal", RegisterBlocks.legacyColoredFitting("pink"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing pink -> normal", RegisterBlocks.legacyGlowingFitting("pink"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing pink -> normal", RegisterBlocks.legacyGlowingFitting("pink"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "lime -> normal", RegisterBlocks.legacyColoredFitting("lime"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "lime -> normal", RegisterBlocks.legacyColoredFitting("lime"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing lime -> normal", RegisterBlocks.legacyGlowingFitting("lime"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing lime -> normal", RegisterBlocks.legacyGlowingFitting("lime"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "yellow -> normal", RegisterBlocks.legacyColoredFitting("yellow"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "yellow -> normal", RegisterBlocks.legacyColoredFitting("yellow"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing yellow -> normal", RegisterBlocks.legacyGlowingFitting("yellow"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing yellow -> normal", RegisterBlocks.legacyGlowingFitting("yellow"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "light_blue -> normal", RegisterBlocks.legacyColoredFitting("light_blue"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "light_blue -> normal", RegisterBlocks.legacyColoredFitting("light_blue"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing light_blue -> normal", RegisterBlocks.legacyGlowingFitting("light_blue"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing light_blue -> normal", RegisterBlocks.legacyGlowingFitting("light_blue"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "magenta -> normal", RegisterBlocks.legacyColoredFitting("magenta"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "magenta -> normal", RegisterBlocks.legacyColoredFitting("magenta"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing magenta -> normal", RegisterBlocks.legacyGlowingFitting("magenta"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing magenta -> normal", RegisterBlocks.legacyGlowingFitting("magenta"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "orange -> normal", RegisterBlocks.legacyColoredFitting("orange"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "orange -> normal", RegisterBlocks.legacyColoredFitting("orange"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing orange -> normal", RegisterBlocks.legacyGlowingFitting("orange"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing orange -> normal", RegisterBlocks.legacyGlowingFitting("orange"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "white -> normal", RegisterBlocks.legacyColoredFitting("white"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "white -> normal", RegisterBlocks.legacyColoredFitting("white"), normalFitting, schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "glowing white -> normal", RegisterBlocks.legacyGlowingFitting("white"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "glowing white -> normal", RegisterBlocks.legacyGlowingFitting("white"), normalFitting, schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "corroded -> normal", RegisterBlocks.legacyId("corroded_fitting"), normalFitting, schemaV2);
        SimpleFixes.addItemRenameFix(builder, "corroded -> normal", RegisterBlocks.legacyId("corroded_fitting"), normalFitting, schemaV2);

        // SURVIVING BLOCKS
        SimpleFixes.addBlockRenameFix(builder, "upgrade pipe", RegisterBlocks.legacyId("copper_pipe"), RegisterBlocks.id("copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade pipe", RegisterBlocks.legacyId("copper_pipe"), RegisterBlocks.id("copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade exposed pipe", RegisterBlocks.legacyId("exposed_copper_pipe"), RegisterBlocks.id("exposed_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade exposed pipe", RegisterBlocks.legacyId("exposed_copper_pipe"), RegisterBlocks.id("exposed_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade weathered pipe", RegisterBlocks.legacyId("weathered_copper_pipe"), RegisterBlocks.id("weathered_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade weathered pipe", RegisterBlocks.legacyId("weathered_copper_pipe"), RegisterBlocks.id("weathered_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade oxidized pipe", RegisterBlocks.legacyId("oxidized_copper_pipe"), RegisterBlocks.id("oxidized_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade oxidized pipe", RegisterBlocks.legacyId("oxidized_copper_pipe"), RegisterBlocks.id("oxidized_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed pipe", RegisterBlocks.legacyId("waxed_copper_pipe"), RegisterBlocks.id("waxed_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed pipe", RegisterBlocks.legacyId("waxed_copper_pipe"), RegisterBlocks.id("waxed_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed exposed pipe", RegisterBlocks.legacyId("waxed_exposed_copper_pipe"), RegisterBlocks.id("waxed_exposed_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed exposed pipe", RegisterBlocks.legacyId("waxed_exposed_copper_pipe"), RegisterBlocks.id("waxed_exposed_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed weathered pipe", RegisterBlocks.legacyId("waxed_weathered_copper_pipe"), RegisterBlocks.id("waxed_weathered_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed weathered pipe", RegisterBlocks.legacyId("waxed_weathered_copper_pipe"), RegisterBlocks.id("waxed_weathered_copper_pipe"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed oxidized pipe", RegisterBlocks.legacyId("waxed_oxidized_copper_pipe"), RegisterBlocks.id("waxed_oxidized_copper_pipe"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed oxidized pipe", RegisterBlocks.legacyId("waxed_oxidized_copper_pipe"), RegisterBlocks.id("waxed_oxidized_copper_pipe"), schemaV2);

        SimpleFixes.addBlockRenameFix(builder, "upgrade fitting", RegisterBlocks.legacyId("copper_fitting"), RegisterBlocks.id("copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade fitting", RegisterBlocks.legacyId("copper_fitting"), RegisterBlocks.id("copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade exposed fitting", RegisterBlocks.legacyId("exposed_copper_fitting"), RegisterBlocks.id("exposed_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade exposed fitting", RegisterBlocks.legacyId("exposed_copper_fitting"), RegisterBlocks.id("exposed_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade weathered fitting", RegisterBlocks.legacyId("weathered_copper_fitting"), RegisterBlocks.id("weathered_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade weathered fitting", RegisterBlocks.legacyId("weathered_copper_fitting"), RegisterBlocks.id("weathered_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade oxidized fitting", RegisterBlocks.legacyId("oxidized_copper_fitting"), RegisterBlocks.id("oxidized_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade oxidized fitting", RegisterBlocks.legacyId("oxidized_copper_fitting"), RegisterBlocks.id("oxidized_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed fitting", RegisterBlocks.legacyId("waxed_copper_fitting"), RegisterBlocks.id("waxed_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed fitting", RegisterBlocks.legacyId("waxed_copper_fitting"), RegisterBlocks.id("waxed_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed exposed fitting", RegisterBlocks.legacyId("waxed_exposed_copper_fitting"), RegisterBlocks.id("waxed_exposed_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed exposed fitting", RegisterBlocks.legacyId("waxed_exposed_copper_fitting"), RegisterBlocks.id("waxed_exposed_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed weathered fitting", RegisterBlocks.legacyId("waxed_weathered_copper_fitting"), RegisterBlocks.id("waxed_weathered_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed weathered fitting", RegisterBlocks.legacyId("waxed_weathered_copper_fitting"), RegisterBlocks.id("waxed_weathered_copper_fitting"), schemaV2);
        SimpleFixes.addBlockRenameFix(builder, "upgrade waxed oxidized fitting", RegisterBlocks.legacyId("waxed_oxidized_copper_fitting"), RegisterBlocks.id("waxed_oxidized_copper_fitting"), schemaV2);
        SimpleFixes.addItemRenameFix(builder, "upgrade waxed oxidized fitting", RegisterBlocks.legacyId("waxed_oxidized_copper_fitting"), RegisterBlocks.id("waxed_oxidized_copper_fitting"), schemaV2);

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

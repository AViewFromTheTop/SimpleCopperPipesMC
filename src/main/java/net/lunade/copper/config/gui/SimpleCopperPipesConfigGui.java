package net.lunade.copper.config.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import net.lunade.copper.SimpleCopperPipes;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;

@Environment(EnvType.CLIENT)
public final class SimpleCopperPipesConfigGui {

	static Screen buildScreen(Screen parent) {
		var configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"));
		var entryBuilder = configBuilder.entryBuilder();

		configBuilder.setSavingRunnable(SimpleCopperPipesConfig.CONFIG::save);

		var main = configBuilder.getOrCreateCategory(text("main"));
		setupEntries(main, entryBuilder);

		return configBuilder.build();
	}

	private static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("openable_fittings"), SimpleCopperPipesConfig.OPENABLE_FITTINGS.get())
					.setTooltip(tooltip("openable_fittings"))
					.setYesNoTextSupplier(bool -> text(bool.toString())),
				SimpleCopperPipesConfig.OPENABLE_FITTINGS
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("dispensing"), SimpleCopperPipesConfig.DISPENSING.get())
					.setTooltip(tooltip("dispensing"))
					.setYesNoTextSupplier(bool -> text(bool.toString())),
				SimpleCopperPipesConfig.DISPENSING
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("dispense_sounds"), SimpleCopperPipesConfig.DISPENSE_SOUNDS.get())
					.setTooltip(tooltip("dispense_sounds"))
					.setYesNoTextSupplier(bool -> text(bool.toString())),
				SimpleCopperPipesConfig.DISPENSE_SOUNDS
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("suction_sounds"), SimpleCopperPipesConfig.SUCTION_SOUNDS.get())
					.setTooltip(tooltip("suction_sounds"))
					.setYesNoTextSupplier(bool -> text(bool.toString())),
				SimpleCopperPipesConfig.SUCTION_SOUNDS
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("sense_game_events"), SimpleCopperPipesConfig.SENSE_GAME_EVENTS.get())
					.setTooltip(tooltip("sense_game_events"))
					.setYesNoTextSupplier(bool -> text(bool.toString())),
				SimpleCopperPipesConfig.SENSE_GAME_EVENTS
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("carry_water"), SimpleCopperPipesConfig.CARRY_WATER.get())
					.setSaveConsumer(newValue -> {
						SimpleCopperPipesConfig.CARRY_WATER.setValue(newValue);
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_water"))
					.setYesNoTextSupplier(bool -> text(bool.toString())),
				SimpleCopperPipesConfig.CARRY_WATER
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("carry_lava"), SimpleCopperPipesConfig.CARRY_LAVA.get())
					.setSaveConsumer(newValue -> {
						SimpleCopperPipesConfig.CARRY_LAVA.setValue(newValue);
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_lava"))
					.setYesNoTextSupplier(bool -> text(bool.toString())),
				SimpleCopperPipesConfig.CARRY_LAVA
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("carry_smoke"), SimpleCopperPipesConfig.CARRY_SMOKE.get())
					.setSaveConsumer(newValue -> {
						SimpleCopperPipesConfig.CARRY_SMOKE.setValue(newValue);
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_smoke"))
					.setYesNoTextSupplier(bool -> text(bool.toString())),
				SimpleCopperPipesConfig.CARRY_SMOKE
			)
		);
	}

	@Contract(value = "_ -> new", pure = true)
	private static Component text(String key) {
		return Component.translatable("option." + SimpleCopperPipesConstants.NAMESPACE + "." + key);
	}

	@Contract(value = "_ -> new", pure = true)
	private static Component tooltip(String key) {
		return Component.translatable("tooltip." + SimpleCopperPipesConstants.NAMESPACE + "." + key);
	}
}

package net.lunade.copper.config.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.api.instance.Config;
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

		configBuilder.setSavingRunnable(SimpleCopperPipesConfig.INSTANCE::save);

		var main = configBuilder.getOrCreateCategory(text("main"));
		setupEntries(main, entryBuilder);

		return configBuilder.build();
	}

	private static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		var config = SimpleCopperPipesConfig.get(true);
		var modifiedConfig = SimpleCopperPipesConfig.getWithSync();
		Class<? extends SimpleCopperPipesConfig> clazz = config.getClass();
		Config<?> configInstance = SimpleCopperPipesConfig.INSTANCE;
		var defaultConfig = SimpleCopperPipesConfig.INSTANCE.defaultInstance();

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("openable_fittings"), modifiedConfig.openableFittings)
					.setDefaultValue(defaultConfig.openableFittings)
					.setSaveConsumer(newValue -> config.openableFittings = newValue)
					.setTooltip(tooltip("openable_fittings"))
					.setYesNoTextSupplier(bool -> text(bool.toString()))
					.build(),
				clazz,
				"openableFittings",
				configInstance
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("dispensing"), modifiedConfig.dispensing)
					.setDefaultValue(defaultConfig.dispensing)
					.setSaveConsumer(newValue -> config.dispensing = newValue)
					.setTooltip(tooltip("dispensing"))
					.setYesNoTextSupplier(bool -> text(bool.toString()))
					.build(),
				clazz,
				"dispensing",
				configInstance
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("dispense_sounds"), modifiedConfig.dispenseSounds)
					.setDefaultValue(defaultConfig.dispenseSounds)
					.setSaveConsumer(newValue -> config.dispenseSounds = newValue)
					.setTooltip(tooltip("dispense_sounds"))
					.setYesNoTextSupplier(bool -> text(bool.toString()))
					.build(),
				clazz,
				"dispenseSounds",
				configInstance
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("suction_sounds"), modifiedConfig.suctionSounds)
					.setDefaultValue(defaultConfig.suctionSounds)
					.setSaveConsumer(newValue -> config.suctionSounds = newValue)
					.setTooltip(tooltip("suction_sounds"))
					.setYesNoTextSupplier(bool -> text(bool.toString()))
					.build(),
				clazz,
				"suctionSounds",
				configInstance
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("sense_game_events"), modifiedConfig.senseGameEvents)
					.setDefaultValue(defaultConfig.senseGameEvents)
					.setSaveConsumer(newValue -> config.senseGameEvents = newValue)
					.setTooltip(tooltip("sense_game_events"))
					.setYesNoTextSupplier(bool -> text(bool.toString()))
					.build(),
				clazz,
				"senseGameEvents",
				configInstance
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("carry_water"), modifiedConfig.carryWater)
					.setDefaultValue(defaultConfig.carryWater)
					.setSaveConsumer(newValue -> {
						config.carryWater = newValue;
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_water"))
					.setYesNoTextSupplier(bool -> text(bool.toString()))
					.build(),
				clazz,
				"carryWater",
				configInstance
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("carry_lava"), modifiedConfig.carryLava)
					.setDefaultValue(defaultConfig.carryLava)
					.setSaveConsumer(newValue -> {
						config.carryLava = newValue;
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_lava"))
					.setYesNoTextSupplier(bool -> text(bool.toString()))
					.build(),
				clazz,
				"carryLava",
				configInstance
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("carry_smoke"), modifiedConfig.carrySmoke)
					.setDefaultValue(defaultConfig.carrySmoke)
					.setSaveConsumer(newValue -> {
						config.carrySmoke = newValue;
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_smoke"))
					.setYesNoTextSupplier(bool -> text(bool.toString()))
					.build(),
				clazz,
				"carrySmoke",
				configInstance
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

package net.lunade.copper.config.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import static net.frozenblock.lib.config.clothconfig.FrozenLibClothConfigGuiHelper.booleanEntry;
import static net.frozenblock.lib.config.clothconfig.FrozenLibClothConfigGuiHelper.syncedEntry;
import net.lunade.copper.SimpleCopperPipes;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@ClientOnly
public final class SimpleCopperPipesConfigGui {

	public static Screen buildScreen(Screen parent) {
		final ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"));
		final ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

		configBuilder.setSavingRunnable(SimpleCopperPipesConfig.CONFIG::save);

		final ConfigCategory main = configBuilder.getOrCreateCategory(text("main"));
		setupEntries(main, entryBuilder);

		return configBuilder.build();
	}

	private static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(booleanEntry(builder, "openable_fittings",  SimpleCopperPipesConfig.OPENABLE_FITTINGS));
		category.addEntry(booleanEntry(builder, "dispensing",  SimpleCopperPipesConfig.DISPENSING));
		category.addEntry(booleanEntry(builder, "dispense_sounds",  SimpleCopperPipesConfig.DISPENSE_SOUNDS));
		category.addEntry(booleanEntry(builder, "suction_sounds",  SimpleCopperPipesConfig.SUCTION_SOUNDS));
		category.addEntry(booleanEntry(builder, "sense_game_events",  SimpleCopperPipesConfig.SENSE_GAME_EVENTS));
		category.addEntry(booleanEntry(builder, "sense_game_events",  SimpleCopperPipesConfig.SENSE_GAME_EVENTS));

		category.addEntry(
			syncedEntry(
				builder.startBooleanToggle(text("carry_water"), SimpleCopperPipesConfig.CARRY_WATER.get())
					.setSaveConsumer(newValue -> {
						SimpleCopperPipesConfig.CARRY_WATER.setValue(newValue);
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_water")),
				SimpleCopperPipesConfig.CARRY_WATER
			)
		);

		category.addEntry(
			syncedEntry(
				builder.startBooleanToggle(text("carry_lava"), SimpleCopperPipesConfig.CARRY_LAVA.get())
					.setSaveConsumer(newValue -> {
						SimpleCopperPipesConfig.CARRY_LAVA.setValue(newValue);
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_lava")),
				SimpleCopperPipesConfig.CARRY_LAVA
			)
		);

		category.addEntry(
			syncedEntry(
				builder.startBooleanToggle(text("carry_smoke"), SimpleCopperPipesConfig.CARRY_SMOKE.get())
					.setSaveConsumer(newValue -> {
						SimpleCopperPipesConfig.CARRY_SMOKE.setValue(newValue);
						SimpleCopperPipes.REFRESH_VALUES = true;
					})
					.setTooltip(tooltip("carry_smoke")),
				SimpleCopperPipesConfig.CARRY_SMOKE
			)
		);
	}

	private static Component text(String key) {
		return Component.translatable("option." + SimpleCopperPipesConstants.NAMESPACE + "." + key);
	}

	private static Component tooltip(String key) {
		return Component.translatable("tooltip." + SimpleCopperPipesConstants.NAMESPACE + "." + key);
	}
}

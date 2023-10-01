package net.lunade.copper.config.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class SimpleCopperPipesConfigGui {
    private SimpleCopperPipesConfigGui() {}

    static Screen buildScreen(Screen parent) {
        var configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"));
        var entryBuilder = configBuilder.entryBuilder();

        configBuilder.setSavingRunnable(SimpleCopperPipesConfig.INSTANCE::save);

        var main = configBuilder.getOrCreateCategory(text("main"));
        setupEntries(main, entryBuilder);

        return configBuilder.build();
    }

    private static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        var config = SimpleCopperPipesConfig.get();
        var defaultConfig = SimpleCopperPipesConfig.INSTANCE.defaultInstance();

        category.addEntry(entryBuilder.startBooleanToggle(text("openable_fittings"), config.openableFittings)
                .setDefaultValue(defaultConfig.openableFittings)
                .setSaveConsumer(newValue -> config.openableFittings = newValue)
                .setTooltip(tooltip("openable_fittings"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );

        category.addEntry(entryBuilder.startBooleanToggle(text("dispensing"), config.dispensing)
                .setDefaultValue(defaultConfig.dispensing)
                .setSaveConsumer(newValue -> config.dispensing = newValue)
                .setTooltip(tooltip("dispensing"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );

        category.addEntry(entryBuilder.startBooleanToggle(text("special_effect_dispensing"), config.specialEffectDispensing)
                .setDefaultValue(defaultConfig.specialEffectDispensing)
                .setSaveConsumer(newValue -> config.specialEffectDispensing = newValue)
                .setTooltip(tooltip("special_effect_dispensing"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );

        category.addEntry(entryBuilder.startBooleanToggle(text("dispense_sounds"), config.dispenseSounds)
                .setDefaultValue(defaultConfig.dispenseSounds)
                .setSaveConsumer(newValue -> config.dispenseSounds = newValue)
                .setTooltip(tooltip("dispense_sounds"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );

        category.addEntry(entryBuilder.startBooleanToggle(text("suction_sounds"), config.suctionSounds)
                .setDefaultValue(defaultConfig.suctionSounds)
                .setSaveConsumer(newValue -> config.suctionSounds = newValue)
                .setTooltip(tooltip("suction_sounds"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );

        category.addEntry(entryBuilder.startBooleanToggle(text("sense_game_events"), config.senseGameEvents)
                .setDefaultValue(defaultConfig.senseGameEvents)
                .setSaveConsumer(newValue -> config.senseGameEvents = newValue)
                .setTooltip(tooltip("sense_game_events"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );

        category.addEntry(entryBuilder.startBooleanToggle(text("carry_water"), config.carryWater)
                .setDefaultValue(defaultConfig.carryWater)
                .setSaveConsumer(newValue -> {
                    config.carryWater = newValue;
                    CopperPipeMain.refreshValues = true;
                })
                .setTooltip(tooltip("carry_water"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );

        category.addEntry(entryBuilder.startBooleanToggle(text("carry_lava"), config.carryLava)
                .setDefaultValue(defaultConfig.carryLava)
                .setSaveConsumer(newValue -> {
                    config.carryLava = newValue;
                    CopperPipeMain.refreshValues = true;
                })
                .setTooltip(tooltip("carry_lava"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );

        category.addEntry(entryBuilder.startBooleanToggle(text("carry_smoke"), config.carrySmoke)
                .setDefaultValue(defaultConfig.carrySmoke)
                .setSaveConsumer(newValue -> {
                    config.carrySmoke = newValue;
                    CopperPipeMain.refreshValues = true;
                })
                .setTooltip(tooltip("carry_smoke"))
                .setYesNoTextSupplier(bool -> text(bool.toString()))
                .build()
        );
    }

    private static Component text(String key) {
        return Component.translatable("option." + CopperPipeMain.NAMESPACE + "." + key);
    }

    private static Component tooltip(String key) {
        return Component.translatable("tooltip." + CopperPipeMain.NAMESPACE + "." + key);
    }
}

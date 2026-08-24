package net.lunade.copper.config;

import net.frozenblock.lib.config.v2.config.ConfigData;
import net.frozenblock.lib.config.v2.config.ConfigSettings;
import net.frozenblock.lib.config.v2.entry.ConfigEntry;
import net.frozenblock.lib.config.v2.entry.EntryType;
import net.frozenblock.lib.config.v2.registry.ID;
import net.lunade.copper.SimpleCopperPipesConstants;

public final class SimpleCopperPipesConfig {
	public static final ConfigData<?> CONFIG = ConfigData.createAndRegister(ID.of(SimpleCopperPipesConstants.id("main")), ConfigSettings.JSON5);

	public static final ConfigEntry<Boolean> OPENABLE_FITTINGS = CONFIG.entry("openableFittings", EntryType.BOOL, false);

	public static final ConfigEntry<Boolean> DISPENSING = CONFIG.entry("dispensing", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> DISPENSE_SOUNDS = CONFIG.entry("dispenseSounds", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> SUCTION_SOUNDS = CONFIG.entry("suctionSounds", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> SENSE_GAME_EVENTS = CONFIG.entry("senseGameEvents", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> CARRY_WATER = CONFIG.entry("carryWater", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> CARRY_LAVA = CONFIG.entry("carryLava", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> CARRY_SMOKE = CONFIG.entry("carrySmoke", EntryType.BOOL, true);
}

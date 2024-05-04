package net.lunade.copper.config;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData;
import net.lunade.copper.SimpleCopperPipesSharedConstants;

public final class SimpleCopperPipesConfig {

    public static final Config<SimpleCopperPipesConfig> INSTANCE = ConfigRegistry.register(
            new JsonConfig<>(
                    SimpleCopperPipesSharedConstants.MOD_ID,
                    SimpleCopperPipesConfig.class,
                    JsonType.JSON5,
                    null,
                    null
            )
    );

    @EntrySyncData("openableFittings")
    public boolean openableFittings = false;

    @EntrySyncData("dispensing")
    public boolean dispensing = true;

    @EntrySyncData("dispenseSounds")
    public boolean dispenseSounds = true;

    @EntrySyncData("suctionSounds")
    public boolean suctionSounds = true;

    @EntrySyncData("senseGameEvents")
    public boolean senseGameEvents = true;

    @EntrySyncData("carryWater")
    public boolean carryWater = true;

    @EntrySyncData("carryLava")
    public boolean carryLava = true;

    @EntrySyncData("carrySmoke")
    public boolean carrySmoke = true;

    public static SimpleCopperPipesConfig get() {
        return INSTANCE.config();
    }

    public static SimpleCopperPipesConfig get(boolean real) {
        if (real)
            return INSTANCE.instance();
        return INSTANCE.config();
    }

    public static SimpleCopperPipesConfig getWithSync() {
        return INSTANCE.configWithSync();
    }
}

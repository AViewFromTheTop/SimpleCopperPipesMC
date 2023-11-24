package net.lunade.copper.config;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.lunade.copper.CopperPipeMain;

public final class SimpleCopperPipesConfig {

    public static final Config<SimpleCopperPipesConfig> INSTANCE = ConfigRegistry.register(
            new JsonConfig<>(
                    CopperPipeMain.MOD_ID,
                    SimpleCopperPipesConfig.class,
                    JsonType.JSON5,
                    null,
                    null
            )
    );

    public boolean openableFittings = false;

    public boolean dispensing = true;

    public boolean specialEffectDispensing = true;

    public boolean dispenseSounds = true;

    public boolean suctionSounds = true;

    public boolean senseGameEvents = true;

    public boolean carryWater = true;

    public boolean carryLava = true;

    public boolean carrySmoke = true;

    public static SimpleCopperPipesConfig get() {
        return INSTANCE.config();
    }
}

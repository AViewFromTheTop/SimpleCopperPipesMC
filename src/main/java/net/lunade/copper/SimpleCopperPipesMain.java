package net.lunade.copper;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.entrypoint.api.FrozenModInitializer;
import net.lunade.copper.blocks.block_entity.leaking_pipes.LeakingPipeDrips;
import net.lunade.copper.blocks.block_entity.leaking_pipes.LeakingPipeManager;
import net.lunade.copper.blocks.properties.CopperPipeProperties;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.datafix.SimpleCopperPipesDataFixer;
import net.lunade.copper.networking.SimpleCopperPipesNetworking;
import net.lunade.copper.registry.PipeMovementRestrictions;
import net.lunade.copper.registry.PoweredPipeDispenses;
import net.lunade.copper.registry.RegisterBlockEntities;
import net.lunade.copper.registry.RegisterBlocks;
import net.lunade.copper.registry.RegisterPipeNbtMethods;
import net.lunade.copper.registry.RegisterSoundEvents;
import net.lunade.copper.registry.RegisterStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCopperPipesMain extends FrozenModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("COPPER_PIPES");
    public static boolean refreshValues = false;

    public SimpleCopperPipesMain() {
        super(SimpleCopperPipesSharedConstants.NAMESPACE);
    }

    public static int getCompatID() {
        return 4;
    }

    @Override
    public void onInitialize(String modId, ModContainer container) {
        SimpleCopperPipesDataFixer.applyDataFixes(container);
        CopperPipeProperties.init();
        SimpleCopperPipesConfig.get();
        RegisterBlocks.register();
        RegisterBlockEntities.init();
        RegisterSoundEvents.init();
        RegisterStats.init();

        RegisterPipeNbtMethods.init();
        PoweredPipeDispenses.init();
        PipeMovementRestrictions.init();
        LeakingPipeDrips.init();

        SimpleCopperPipesNetworking.init();

        ServerLifecycleEvents.SERVER_STOPPED.register((server) -> LeakingPipeManager.clearAll());

        ServerTickEvents.START_SERVER_TICK.register((listener) -> LeakingPipeManager.clearAndSwitch());
        ServerTickEvents.END_SERVER_TICK.register((listener) -> refreshValues = false);

        FabricLoader.getInstance().getEntrypointContainers(SimpleCopperPipesSharedConstants.MOD_ID, SimpleCopperPipesEntrypoint.class).forEach(entrypoint -> {
            try {
                SimpleCopperPipesEntrypoint mainPoint = entrypoint.getEntrypoint();
                mainPoint.init();
                if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                    mainPoint.initDevOnly();
                }
            } catch (Throwable ignored) {

            }
        });
    }

}

package net.lunade.copper;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.entrypoint.api.FrozenModInitializer;
import net.lunade.copper.block.entity.leaking.LeakingPipeDripBehaviors;
import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.datafix.SimpleCopperPipesDataFixer;
import net.lunade.copper.networking.SimpleCopperPipesNetworking;
import net.lunade.copper.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCopperPipes extends FrozenModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Simple Copper Pipes");
    public static boolean REFRESH_VALUES = false;

    public SimpleCopperPipes() {
        super(SimpleCopperPipesConstants.NAMESPACE);
    }

    public static int getCompatID() {
        return 4;
    }

    @Override
    public void onInitialize(String modId, ModContainer container) {
        SimpleCopperPipesDataFixer.applyDataFixes(container);
        SimpleCopperPipesBlockStateProperties.init();
        SimpleCopperPipesConfig.get();
        SimpleCopperPipesBlocks.init();
        SimpleCopperPipesBlockEntityTypes.init();
        SimpleCopperPipesSoundEvents.init();
        SimpleCopperPipesStats.init();

        RegisterPipeNbtMethods.init();
        CopperPipeDispenseBehaviors.init();
        PipeMovementRestrictions.init();
        LeakingPipeDripBehaviors.init();

        SimpleCopperPipesNetworking.init();

        SimpleCopperPipesCreativeInventorySorting.init();

        ServerLifecycleEvents.SERVER_STOPPED.register((server) -> LeakingPipeManager.clearAll());

        ServerTickEvents.START_SERVER_TICK.register((listener) -> LeakingPipeManager.clearAndSwitch());
        ServerTickEvents.END_SERVER_TICK.register((listener) -> REFRESH_VALUES = false);
    }

}

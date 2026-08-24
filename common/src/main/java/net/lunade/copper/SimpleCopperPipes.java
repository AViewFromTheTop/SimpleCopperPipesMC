package net.lunade.copper;

import net.frozenblock.lib.event.api.events.LifecycleEvents;
import net.frozenblock.lib.event.api.events.TickEvents;
import net.frozenblock.lib.feature_flag.api.FeatureFlagApi;
import net.frozenblock.lib.particle.api.VibrationParticleVisibilityApi;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.lunade.copper.block.entity.leaking.LeakingPipeDripBehaviors;
import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.datafix.SimpleCopperPipesDataFixer;
import net.lunade.copper.registry.CopperPipeDispenseBehaviors;
import net.lunade.copper.registry.PipeMovementRestrictions;
import net.lunade.copper.registry.SimpleCopperPipesBlockEntityTypes;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.lunade.copper.registry.SimpleCopperPipesCreativeInventorySorting;
import net.lunade.copper.registry.SimpleCopperPipesItems;
import net.lunade.copper.registry.SimpleCopperPipesSoundEvents;
import net.lunade.copper.registry.SimpleCopperPipesStats;
import net.lunade.copper.registry.TransferablePipeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleCopperPipes {

	public static final Logger LOGGER = LoggerFactory.getLogger("Simple Copper Pipes");
	public static boolean REFRESH_VALUES = false;

	public static void init() {
		SimpleCopperPipesDataFixer.applyDataFixes(SimpleCopperPipesConstants.MOD_ID);
		SimpleCopperPipesFeatureFlags.init();
		FeatureFlagApi.rebuild();

		SimpleCopperPipesBlocks.init();
		SimpleCopperPipesBlockEntityTypes.init();
		SimpleCopperPipesItems.init();
		SimpleCopperPipesSoundEvents.init();
		SimpleCopperPipesStats.init();

		TransferablePipeData.init();
		CopperPipeDispenseBehaviors.init();
		PipeMovementRestrictions.init();
		LeakingPipeDripBehaviors.init();

		SimpleCopperPipesConfig.CONFIG.load(true);

		VibrationParticleVisibilityApi.registerVisibilityTest((data, user) -> !(user instanceof CopperPipeBlockEntity.VibrationUser));

		LifecycleEvents.SERVER_STOPPED.register((server) -> LeakingPipeManager.clearAll());
		TickEvents.START_SERVER_TICK.register((listener) -> LeakingPipeManager.clearAndSwitch());
		TickEvents.END_SERVER_TICK.register((listener) -> SimpleCopperPipes.REFRESH_VALUES = false);
	}

	public static void setup() {
		SimpleCopperPipesCreativeInventorySorting.setup();
		SimpleCopperPipesStats.setup();
	}

	public static int getCompatID() {
		return 4;
	}
}

package net.lunade.copper;

import net.frozenblock.lib.event.api.events.LifecycleEvents;
import net.frozenblock.lib.event.api.events.TickEvents;
import net.frozenblock.lib.feature_flag.api.FeatureFlagApi;
import net.frozenblock.lib.particle.api.VibrationParticleVisibilityApi;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.lunade.copper.block.entity.leaking.LeakingPipeDripBehaviors;
import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.lunade.copper.config.SCPConfig;
import net.lunade.copper.datafix.SCPDataFixer;
import net.lunade.copper.registry.CopperPipeDispenseBehaviors;
import net.lunade.copper.registry.PipeMovementRestrictions;
import net.lunade.copper.registry.SCPBlockEntityTypes;
import net.lunade.copper.registry.SCPBlocks;
import net.lunade.copper.registry.SCPCreativeInventorySorting;
import net.lunade.copper.registry.SCPItems;
import net.lunade.copper.registry.SCPSoundEvents;
import net.lunade.copper.registry.SCPStats;
import net.lunade.copper.registry.TransferablePipeData;

public final class SimpleCopperPipes {
	public static boolean REFRESH_VALUES = false;

	public static void init() {
		SCPDataFixer.applyDataFixes(SCPConstants.MOD_ID);
		SCPFeatureFlags.init();
		FeatureFlagApi.rebuild();

		SCPBlocks.init();
		SCPBlockEntityTypes.init();
		SCPItems.init();
		SCPSoundEvents.init();
		SCPStats.init();

		TransferablePipeData.init();
		CopperPipeDispenseBehaviors.init();
		PipeMovementRestrictions.init();
		LeakingPipeDripBehaviors.init();

		SCPConfig.CONFIG.load(true);

		VibrationParticleVisibilityApi.registerVisibilityTest((data, user) -> !(user instanceof CopperPipeBlockEntity.VibrationUser));

		LifecycleEvents.SERVER_STOPPED.register(server -> LeakingPipeManager.clearAll());
		TickEvents.START_SERVER_TICK.register(listener -> LeakingPipeManager.clearAndSwitch());
		TickEvents.END_SERVER_TICK.register(listener -> SimpleCopperPipes.REFRESH_VALUES = false);
	}

	public static void setup() {
		SCPBlocks.setup();
		SCPCreativeInventorySorting.setup();
		SCPStats.setup();
	}

	public static int getCompatID() {
		return 4;
	}

	private SimpleCopperPipes() {}
}

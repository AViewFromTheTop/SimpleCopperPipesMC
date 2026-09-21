package net.lunade.copper.registry;

import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.lunade.copper.SCPConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public final class SCPStats {
	public static final Identifier INSPECT_PIPE = SCPConstants.id("inspect_copper_pipe");
	public static final Identifier INSPECT_FITTING = SCPConstants.id("inspect_copper_fitting");

	public static void init() {
		var register = DeferredRegister.create(Registries.CUSTOM_STAT, SCPConstants.NAMESPACE);
		register.register(INSPECT_PIPE.getPath(), () -> INSPECT_PIPE);
		register.register(INSPECT_FITTING.getPath(), () -> INSPECT_FITTING);
		register.register();
	}

	public static void setup() {
		Stats.CUSTOM.get(INSPECT_PIPE, StatFormatter.DEFAULT);
		Stats.CUSTOM.get(INSPECT_FITTING, StatFormatter.DEFAULT);
	}

	private SCPStats() {}
}

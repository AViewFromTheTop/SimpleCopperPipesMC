package net.lunade.copper.registry;

import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.frozenblock.lib.platform.api.registry.DeferredSoundEvent;
import net.lunade.copper.SCPConstants;

public final class SCPSoundEvents {
	private static final DeferredRegister.SoundEvents REGISTER = DeferredRegister.createSoundEvents(SCPConstants.MOD_ID);

	public static final DeferredSoundEvent ITEM_IN = REGISTER.register("block.copper_pipe.item_in");
	public static final DeferredSoundEvent ITEM_OUT = REGISTER.register("block.copper_pipe.item_out");
	public static final DeferredSoundEvent LAUNCH = REGISTER.register("block.copper_pipe.launch");

	static {
		REGISTER.register();
	}


	public static void init() {}

	private SCPSoundEvents() {}
}

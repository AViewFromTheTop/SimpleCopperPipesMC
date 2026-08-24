package net.lunade.copper.registry;

import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.frozenblock.lib.platform.api.registry.DeferredHolder;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class SimpleCopperPipesSoundEvents {
	private static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(
		Registries.SOUND_EVENT,
		SimpleCopperPipesConstants.MOD_ID
	);

	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_IN = register("block.copper_pipe.item_in");
	public static final DeferredHolder<SoundEvent, SoundEvent> ITEM_OUT = register("block.copper_pipe.item_out");
	public static final DeferredHolder<SoundEvent, SoundEvent> LAUNCH = register("block.copper_pipe.launch");

	static {
		REGISTER.register();
	}

	public static DeferredHolder<SoundEvent, SoundEvent> register(String id) {
		return REGISTER.register(id, () -> SoundEvent.createVariableRangeEvent(SimpleCopperPipesConstants.id(id)));
	}

	public static void init() {
	}
}

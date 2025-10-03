package net.lunade.copper.registry;

import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class SimpleCopperPipesSoundEvents {
	public static final SoundEvent ITEM_IN = register("block.copper_pipe.item_in");
	public static final SoundEvent ITEM_OUT = register("block.copper_pipe.item_out");
	public static final SoundEvent LAUNCH = register("block.copper_pipe.launch");

	@NotNull
	public static SoundEvent register(@NotNull String path) {
		final ResourceLocation id = SimpleCopperPipesConstants.id(path);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}

	public static void init() {
	}
}

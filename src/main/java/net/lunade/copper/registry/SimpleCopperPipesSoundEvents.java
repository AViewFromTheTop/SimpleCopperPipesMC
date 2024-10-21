package net.lunade.copper.registry;

import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class SimpleCopperPipesSoundEvents {
    public static final SoundEvent ITEM_IN = register("block.copper_pipe.item_in");
    public static final SoundEvent ITEM_OUT = register("block.copper_pipe.item_out");
    public static final SoundEvent LAUNCH = register("block.copper_pipe.launch");
    public static final SoundEvent TURN = register("block.copper_pipe.turn");

    @NotNull
    public static SoundEvent register(@NotNull String path) {
        var id = SimpleCopperPipesConstants.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {
    }
}

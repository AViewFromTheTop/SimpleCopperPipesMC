package net.lunade.copper.registry;

import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class RegisterSoundEvents {
    public static final SoundEvent ITEM_IN = register("block.copper_pipe.item_in");
    public static final SoundEvent ITEM_OUT = register("block.copper_pipe.item_out");
    public static final SoundEvent LAUNCH = register("block.copper_pipe.launch");
    public static final SoundEvent TURN = register("block.copper_pipe.turn");

    @NotNull
    private static Holder.Reference<SoundEvent> registerForHolder(@NotNull ResourceLocation resourceLocation) {
        return registerForHolder(resourceLocation, resourceLocation);
    }

    @NotNull
    public static SoundEvent register(@NotNull String path) {
        var id = SimpleCopperPipesSharedConstants.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    @NotNull
    private static Holder.Reference<SoundEvent> registerForHolder(@NotNull ResourceLocation resourceLocation, @NotNull ResourceLocation resourceLocation2) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation2));
    }

    public static void init() {
    }
}

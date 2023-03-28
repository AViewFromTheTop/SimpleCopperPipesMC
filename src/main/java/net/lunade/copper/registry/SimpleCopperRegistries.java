package net.lunade.copper.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.lunade.copper.PipeMovementRestrictions;
import net.lunade.copper.RegisterPipeNbtMethods;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;

public final class SimpleCopperRegistries {

    public static final MappedRegistry<PipeMovementRestrictions.PipeMovementRestriction> PIPE_MOVEMENT_RESTRICTIONS = FabricRegistryBuilder.createSimple(PipeMovementRestrictions.PipeMovementRestriction.class, new ResourceLocation("copper_pipe", "pipe_movement_restrictions"))
            .buildAndRegister();

    public static final MappedRegistry<RegisterPipeNbtMethods.UniquePipeNbt> UNIQUE_PIPE_NBTS = FabricRegistryBuilder.createSimple(RegisterPipeNbtMethods.UniquePipeNbt.class, new ResourceLocation("copper_pipe", "unique_pipe_nbt"))
            .buildAndRegister();

    public static void initRegistry() {
    }
}

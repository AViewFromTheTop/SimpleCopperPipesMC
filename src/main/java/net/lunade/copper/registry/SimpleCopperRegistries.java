package net.lunade.copper.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.lunade.copper.PipeMovementRestrictions;
import net.lunade.copper.RegisterPipeNbtMethods;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

public final class SimpleCopperRegistries {

    public static final SimpleRegistry<PipeMovementRestrictions.PipeMovementRestriction> PIPE_MOVEMENT_RESTRICTIONS = FabricRegistryBuilder.createSimple(PipeMovementRestrictions.PipeMovementRestriction.class, new Identifier("copper_pipe", "pipe_movement_restrictions"))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static final SimpleRegistry<RegisterPipeNbtMethods.UniquePipeNbt> UNIQUE_PIPE_NBTS = FabricRegistryBuilder.createSimple(RegisterPipeNbtMethods.UniquePipeNbt.class, new Identifier("copper_pipe", "unique_pipe_nbt"))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    public static void initRegistry() {
    }
}

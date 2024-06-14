package net.lunade.copper.blocks.properties;

import java.util.Optional;
import net.lunade.copper.registry.RegisterPipeNbtMethods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PipeFluid implements StringRepresentable {
    NONE("none", Optional.empty()),
    WATER("water", Optional.of(RegisterPipeNbtMethods.WATER)),
    LAVA("lava", Optional.of(RegisterPipeNbtMethods.LAVA)),
    SMOKE("smoke", Optional.of(RegisterPipeNbtMethods.SMOKE));
    public final Optional<ResourceLocation> nbtID;
    private final String name;

    PipeFluid(String name, Optional<ResourceLocation> nbtID) {
        this.name = name;
        this.nbtID = nbtID;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }
}

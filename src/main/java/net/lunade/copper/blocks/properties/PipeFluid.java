package net.lunade.copper.blocks.properties;

import net.lunade.copper.CopperPipeMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum PipeFluid implements StringRepresentable {
    NONE("none", Optional.empty()),
    WATER("water", Optional.of(CopperPipeMain.WATER)),
    LAVA("lava", Optional.of(CopperPipeMain.LAVA)),
    SMOKE("smoke", Optional.of(CopperPipeMain.SMOKE));

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

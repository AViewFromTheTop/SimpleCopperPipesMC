package net.lunade.copper.block.properties;

import java.util.Optional;
import net.lunade.copper.registry.TransferablePipeData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PipeFluid implements StringRepresentable {
	NONE("none", Optional.empty()),
	WATER("water", Optional.of(TransferablePipeData.WATER)),
	LAVA("lava", Optional.of(TransferablePipeData.LAVA)),
	SMOKE("smoke", Optional.of(TransferablePipeData.SMOKE));
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

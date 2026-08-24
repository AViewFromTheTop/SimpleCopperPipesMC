package net.lunade.copper.block.properties;

import java.util.Optional;
import net.lunade.copper.registry.TransferablePipeData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public enum PipeFluid implements StringRepresentable {
	NONE("none", Optional.empty()),
	WATER("water", Optional.of(TransferablePipeData.WATER)),
	LAVA("lava", Optional.of(TransferablePipeData.LAVA)),
	SMOKE("smoke", Optional.of(TransferablePipeData.SMOKE));
	public final Optional<Identifier> nbtID;
	private final String name;

	PipeFluid(String name, Optional<Identifier> nbtID) {
		this.name = name;
		this.nbtID = nbtID;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}

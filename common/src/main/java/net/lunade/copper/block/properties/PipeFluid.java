/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Simple Copper Pipes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

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

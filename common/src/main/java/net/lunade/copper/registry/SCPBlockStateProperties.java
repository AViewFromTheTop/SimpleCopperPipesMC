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

package net.lunade.copper.registry;

import net.lunade.copper.block.properties.PipeFluid;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class SCPBlockStateProperties {
	public static final BooleanProperty FRONT_CONNECTED = BooleanProperty.create("front_connected");
	public static final BooleanProperty BACK_CONNECTED = BooleanProperty.create("back_connected");
	public static final BooleanProperty SMOOTH = BooleanProperty.create("smooth");
	public static final EnumProperty<PipeFluid> FLUID = EnumProperty.create("fluid", PipeFluid.class);
	public static final BooleanProperty HAS_ELECTRICITY = BooleanProperty.create("has_electricity");

	private SCPBlockStateProperties() {}
}

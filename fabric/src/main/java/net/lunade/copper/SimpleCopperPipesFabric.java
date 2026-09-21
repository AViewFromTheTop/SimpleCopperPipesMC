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

package net.lunade.copper;

import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.entrypoint.api.FrozenModInitializer;

public final class SimpleCopperPipesFabric extends FrozenModInitializer {

	public SimpleCopperPipesFabric() {
		super(SCPConstants.MOD_ID);
	}

	@Override
	public void onInitialize(String modId, ModContainer container) {
		SimpleCopperPipes.init();
		SimpleCopperPipes.setup();
	}
}

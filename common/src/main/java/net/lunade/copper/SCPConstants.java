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

import net.minecraft.resources.Identifier;

public final class SCPConstants {
	public static final int CURRENT_FIX_VERSION = 6;
	public static final String MOD_ID = "simple_copper_pipes";
	public static final String LEGACY_NAMESPACE = "lunade";
	public static final String NAMESPACE = MOD_ID;

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(NAMESPACE, path);
	}

	public static Identifier legacyId(String path) {
		return Identifier.fromNamespaceAndPath(LEGACY_NAMESPACE, path);
	}

	private SCPConstants() {}
}

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

import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.frozenblock.lib.platform.api.registry.DeferredSoundEvent;
import net.lunade.copper.SCPConstants;

public final class SCPSoundEvents {
	private static final DeferredRegister.SoundEvents REGISTER = DeferredRegister.createSoundEvents(SCPConstants.MOD_ID);

	public static final DeferredSoundEvent ITEM_IN = REGISTER.register("block.copper_pipe.item_in");
	public static final DeferredSoundEvent ITEM_OUT = REGISTER.register("block.copper_pipe.item_out");
	public static final DeferredSoundEvent LAUNCH = REGISTER.register("block.copper_pipe.launch");

	static {
		REGISTER.register();
	}


	public static void init() {}

	private SCPSoundEvents() {}
}

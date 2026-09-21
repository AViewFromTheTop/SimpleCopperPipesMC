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

import net.frozenblock.lib.FrozenBools;
import net.lunade.copper.config.gui.SCPConfigGui;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SCPConstants.MOD_ID, dist = Dist.CLIENT)
public final class SimpleCopperPipesNeoForgeClient {

	public SimpleCopperPipesNeoForgeClient(IEventBus modBus) {
		SimpleCopperPipesClient.init();

		// AFTER register event
		modBus.addListener(FMLClientSetupEvent.class, event -> {
			SimpleCopperPipesClient.setup();
		});

		if (FrozenBools.HAS_CLOTH_CONFIG) {
			ModLoadingContext.get().registerExtensionPoint(
				IConfigScreenFactory.class,
				() -> (container, parent) -> SCPConfigGui.buildScreen(parent)
			);
		}
	}
}

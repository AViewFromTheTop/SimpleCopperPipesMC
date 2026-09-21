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

package net.lunade.copper.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import net.fabricmc.frozenblock.datafixer.api.DataFixerEntrypoint;
import net.fabricmc.frozenblock.datafixer.api.SchemaRegistry;
import net.lunade.copper.SCPConstants;
import net.minecraft.util.datafix.fixes.References;

public final class SCPDataFixerEntrypoint implements DataFixerEntrypoint {

	@Override
	public void onRegisterBlockEntities(SchemaRegistry registry, Schema schema) {
		registry.register(SCPConstants.legacyId("copper_pipe"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SCPConstants.legacyId("copper_fitting"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SCPConstants.id("copper_pipe"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SCPConstants.id("copper_fitting"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
	}

	@Override
	public void onRegisterEntities(SchemaRegistry registry, Schema schema) {}
}

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

package net.lunade.copper.datafix.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.util.datafix.fixes.References;

public final class CanAcceptFieldRenameFix extends DataFix {
	private final String blockEntity;

	public CanAcceptFieldRenameFix(String blockEntity, Schema outputSchema) {
		super(outputSchema, false);
		this.blockEntity = blockEntity;
	}

	private static Dynamic<?> fixOccupants(Dynamic<?> dynamic) {
		List<Dynamic<?>> oldDynamics = dynamic.get("canAccept").orElseEmptyList().asStream().collect(Collectors.toCollection(ArrayList::new));
		dynamic = dynamic.remove("canAccept");

		return dynamic.set("canAcceptGameEvents", dynamic.createList(oldDynamics.stream()));
	}

	@Override
	protected TypeRewriteRule makeRule() {
		final Type<?> type = this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, this.blockEntity);
		final OpticFinder<?> opticFinder = DSL.namedChoice(this.blockEntity, type);

		return this.fixTypeEverywhereTyped(
			"saveable moveable nbt -> transferable data fix for " + this.blockEntity,
			this.getInputSchema().getType(References.BLOCK_ENTITY),
			this.getOutputSchema().getType(References.BLOCK_ENTITY),
			typed -> typed.updateTyped(opticFinder, this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, this.blockEntity), this::fix)
		);
	}

	private Typed<?> fix(Typed<?> typed) {
		return typed.update(
			DSL.remainderFinder(),
			CanAcceptFieldRenameFix::fixOccupants
		);
	}
}

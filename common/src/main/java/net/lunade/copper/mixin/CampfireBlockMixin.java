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

package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.copper.block.properties.PipeFluid;
import net.lunade.copper.registry.SCPBlockStateProperties;
import net.lunade.copper.tag.SCPBlockItemTags;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

	@WrapOperation(
		method = "isSmokeyPos",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/CampfireBlock;isLitCampfire(Lnet/minecraft/world/level/block/state/BlockState;)Z"
		)
	)
	private static boolean simpleCopperPipes$isSmokeyPos(BlockState blockState, Operation<Boolean> operation) {
		return operation.call(blockState)
			|| (blockState.is(SCPBlockItemTags.COPPER_PIPES.block()) && blockState.getValueOrElse(SCPBlockStateProperties.FLUID, PipeFluid.NONE) == PipeFluid.SMOKE
		);
	}
}

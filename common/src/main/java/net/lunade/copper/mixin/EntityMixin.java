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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

	@Unique
	private boolean simpleCopperPipes$hadWaterPipeNearby;

	@Inject(method = "updateFluidInteraction", at = @At("HEAD"))
	public void simpleCopperPipes$updateInWaterStateAndDoFluidPushing(CallbackInfoReturnable<Boolean> info) {
		if (!this.level().isClientSide()) this.simpleCopperPipes$hadWaterPipeNearby = LeakingPipeManager.isWaterPipeNearby(Entity.class.cast(this), 2);
	}

	@ModifyReturnValue(method = "isInRain", at = @At("RETURN"))
	public boolean simpleCopperPipes$isInRain(boolean original) {
		return original || this.simpleCopperPipes$hadWaterPipeNearby;
	}

	@Shadow
	public Level level() {
		throw new AssertionError("Mixin injection failed - Simple Copper Pipes EntityMixin.");
	}

}

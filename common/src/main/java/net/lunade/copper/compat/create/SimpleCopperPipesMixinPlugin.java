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

package net.lunade.copper.compat.create;

import java.util.List;
import java.util.Set;
import net.frozenblock.lib.platform.ModLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class SimpleCopperPipesMixinPlugin implements IMixinConfigPlugin {
	private static final String IWRENCHABLE = "com/zurrtum/create/content/equipment/wrench/IWrenchable";
	private static final Set<String> CREATE_WRENCHABLE_MIXINS = Set.of(
		"net.lunade.copper.mixin.create.CopperPipeBlockMixin"
	);

	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (CREATE_WRENCHABLE_MIXINS.contains(mixinClassName)) return ModLoader.isNeoForge() || ModLoader.isModLoaded("create");
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if (CREATE_WRENCHABLE_MIXINS.contains(mixinClassName) && !targetClass.interfaces.contains(IWRENCHABLE)) targetClass.interfaces.add(IWRENCHABLE);
	}
}

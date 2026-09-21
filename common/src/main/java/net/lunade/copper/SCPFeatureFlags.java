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

import net.frozenblock.lib.FrozenLibEarlyConstants;
import net.frozenblock.lib.feature_flag.api.FeatureFlagApi;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public final class SCPFeatureFlags {
	public static final FeatureFlag SIMPLE_COPPER_PIPES = FeatureFlagApi.builder.create(SCPConstants.id(SCPConstants.MOD_ID));
	public static final FeatureFlagSet SIMPLER_COPPER_PIPES_FLAG_SET = FeatureFlagSet.of(SIMPLE_COPPER_PIPES);

	public static final FeatureFlag FEATURE_FLAG = FrozenLibEarlyConstants.IS_DATAGEN ? SIMPLE_COPPER_PIPES : FeatureFlags.VANILLA;

	public static void init() {}

	private SCPFeatureFlags() {}
}

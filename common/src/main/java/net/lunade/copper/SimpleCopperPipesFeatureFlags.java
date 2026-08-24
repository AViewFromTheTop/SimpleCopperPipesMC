
package net.lunade.copper;

import net.frozenblock.lib.FrozenBools;
import net.frozenblock.lib.feature_flag.api.FeatureFlagApi;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public final class SimpleCopperPipesFeatureFlags {
	public static final FeatureFlag SIMPLE_COPPER_PIPES = FeatureFlagApi.builder.create(SimpleCopperPipesConstants.id(SimpleCopperPipesConstants.MOD_ID));
	public static final FeatureFlagSet SIMPLER_COPPER_PIPES_FLAG_SET = FeatureFlagSet.of(SIMPLE_COPPER_PIPES);

	public static final FeatureFlag FEATURE_FLAG = FrozenBools.IS_DATAGEN ? SIMPLE_COPPER_PIPES : FeatureFlags.VANILLA;

	public static void init() {}
}

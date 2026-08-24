package net.lunade.copper.registry;

import net.frozenblock.lib.platform.api.registry.DeferredBlock;
import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.SimpleCopperPipesFeatureFlags;
import net.lunade.copper.block.CopperFittingBlock;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.block.WeatheringCopperFittingBlock;
import net.lunade.copper.block.WeatheringCopperPipeBlock;
import net.lunade.copper.references.SimpleCopperPipesBlockItemIDs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class SimpleCopperPipesBlocks {
	private static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(SimpleCopperPipesConstants.MOD_ID);

	public static final WeatheringCopperCollection<DeferredBlock<? extends Block>> COPPER_PIPE = REGISTER.registerWeatheringCopperCollection(
		SimpleCopperPipesBlockItemIDs.COPPER_PIPE,
		(blocks, id, blockFactory, properties) -> blocks.registerBlock(id, blockFactory, properties),
		(state, properties) -> new CopperPipeBlock(properties, CopperPipeBlock.dispenseShotPowerForWeatherState(state)),
		(state, properties) -> new WeatheringCopperPipeBlock(state, properties, CopperPipeBlock.dispenseShotPowerForWeatherState(state)),
		SimpleCopperPipesBlocks::propertiesForState
	);

	public static final WeatheringCopperCollection<DeferredBlock<? extends Block>> COPPER_FITTING = REGISTER.registerWeatheringCopperCollection(
		SimpleCopperPipesBlockItemIDs.COPPER_FITTING,
		(blocks, id, blockFactory, properties) -> blocks.registerBlock(id, blockFactory, properties),
		(state, properties) -> new CopperFittingBlock(properties),
		WeatheringCopperFittingBlock::new,
		SimpleCopperPipesBlocks::propertiesForState
	);

	static {
		REGISTER.register();
	}

	public static void init() {
	}

	private static BlockBehaviour.Properties propertiesForState(WeatheringCopper.WeatherState state) {
		return BlockBehaviour.Properties.of()
			.mapColor(getMapColorForWeatherState(state))
			.requiresCorrectToolForDrops()
			.strength(1.5F, 3F)
			.sound(SoundType.COPPER)
			.requiredFeatures(SimpleCopperPipesFeatureFlags.FEATURE_FLAG);
	}

	public static MapColor getMapColorForWeatherState(WeatheringCopper.WeatherState weatherState) {
		if (weatherState == WeatheringCopper.WeatherState.UNAFFECTED) return MapColor.COLOR_ORANGE;
		if (weatherState == WeatheringCopper.WeatherState.EXPOSED) return MapColor.TERRACOTTA_LIGHT_GRAY;
		if (weatherState == WeatheringCopper.WeatherState.WEATHERED) return MapColor.WARPED_STEM;
		if (weatherState == WeatheringCopper.WeatherState.OXIDIZED) return MapColor.WARPED_NYLIUM;
		return MapColor.NONE;
	}
}

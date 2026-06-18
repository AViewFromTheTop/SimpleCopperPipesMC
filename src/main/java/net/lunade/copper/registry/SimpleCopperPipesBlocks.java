package net.lunade.copper.registry;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.lunade.copper.SimpleCopperPipesFeatureFlags;
import net.lunade.copper.block.CopperFittingBlock;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.block.WeatheringCopperFittingBlock;
import net.lunade.copper.block.WeatheringCopperPipeBlock;
import net.lunade.copper.references.SimpleCopperPipesBlockItemIDs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class SimpleCopperPipesBlocks {
	public static final WeatheringCopperCollection<Block> COPPER_PIPE = WeatheringCopperCollection.registerBlocks(
		SimpleCopperPipesBlockItemIDs.COPPER_PIPE,
		Blocks::register,
		(weatherState, properties) -> new CopperPipeBlock(properties, CopperPipeBlock.dispenseShotPowerForWeatherState(weatherState)),
		(weatherState, properties) -> new WeatheringCopperPipeBlock(weatherState, properties, CopperPipeBlock.dispenseShotPowerForWeatherState(weatherState)),
		weatherState -> BlockBehaviour.Properties.of()
			.mapColor(getMapColorForWeatherState(weatherState))
			.requiresCorrectToolForDrops()
			.strength(1.5F, 3F)
			.sound(SoundType.COPPER)
			.requiredFeatures(SimpleCopperPipesFeatureFlags.FEATURE_FLAG)
	);

	public static final WeatheringCopperCollection<Block> COPPER_FITTING = WeatheringCopperCollection.registerBlocks(
		SimpleCopperPipesBlockItemIDs.COPPER_FITTING,
		Blocks::register,
		(weatherState, properties) -> new CopperFittingBlock(properties),
		WeatheringCopperFittingBlock::new,
		weatherState -> BlockBehaviour.Properties.of()
			.mapColor(getMapColorForWeatherState(weatherState))
			.requiresCorrectToolForDrops()
			.strength(1.5F, 3F)
			.sound(SoundType.COPPER)
			.requiredFeatures(SimpleCopperPipesFeatureFlags.FEATURE_FLAG)
	);

	public static void init() {
		OxidizableBlocksRegistry.registerWeatheringCopperBlocks(COPPER_PIPE);
		OxidizableBlocksRegistry.registerWeatheringCopperBlocks(COPPER_FITTING);
	}

	public static MapColor getMapColorForWeatherState(WeatheringCopper.WeatherState weatherState) {
		if (weatherState == WeatheringCopper.WeatherState.UNAFFECTED) return MapColor.COLOR_ORANGE;
		if (weatherState == WeatheringCopper.WeatherState.EXPOSED) return MapColor.TERRACOTTA_LIGHT_GRAY;
		if (weatherState == WeatheringCopper.WeatherState.WEATHERED) return MapColor.WARPED_STEM;
		if (weatherState == WeatheringCopper.WeatherState.OXIDIZED) return MapColor.WARPED_NYLIUM;
		return MapColor.NONE;
	}
}

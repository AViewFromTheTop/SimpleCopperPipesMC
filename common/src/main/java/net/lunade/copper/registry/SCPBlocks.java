package net.lunade.copper.registry;

import net.frozenblock.lib.block.api.registry.OxidizableBlocksRegistry;
import net.frozenblock.lib.platform.api.registry.DeferredBlock;
import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.lunade.copper.SCPConstants;
import net.lunade.copper.SCPFeatureFlags;
import net.lunade.copper.block.CopperFittingBlock;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.block.WeatheringCopperFittingBlock;
import net.lunade.copper.block.WeatheringCopperPipeBlock;
import net.lunade.copper.references.SCPBlockItemIDs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class SCPBlocks {
	private static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(SCPConstants.MOD_ID)
		.requiredFeatures(SCPFeatureFlags.FEATURE_FLAG);

	public static final WeatheringCopperCollection<DeferredBlock<? extends Block>> COPPER_PIPE = REGISTER.registerWeatheringCopperCollection(
		SCPBlockItemIDs.COPPER_PIPE,
		(blocks, id, blockFactory, properties) -> blocks.registerBlock(id, blockFactory, properties),
		(state, properties) -> new CopperPipeBlock(properties, CopperPipeBlock.dispenseShotPowerForWeatherState(state)),
		(state, properties) -> new WeatheringCopperPipeBlock(state, properties, CopperPipeBlock.dispenseShotPowerForWeatherState(state)),
		SCPBlocks::propertiesForState
	);

	public static final WeatheringCopperCollection<DeferredBlock<? extends Block>> COPPER_FITTING = REGISTER.registerWeatheringCopperCollection(
		SCPBlockItemIDs.COPPER_FITTING,
		(blocks, id, blockFactory, properties) -> blocks.registerBlock(id, blockFactory, properties),
		(state, properties) -> new CopperFittingBlock(properties),
		WeatheringCopperFittingBlock::new,
		SCPBlocks::propertiesForState
	);

	static {
		// Fabric: register all the blocks
		// NeoForge: add the RegisterEvent listener
		REGISTER.register();
	}

	public static void init() {}

	public static void setup() {
		OxidizableBlocksRegistry.registerWeatheringCopperBlocks(asBlocks(COPPER_PIPE));
		OxidizableBlocksRegistry.registerWeatheringCopperBlocks(asBlocks(COPPER_FITTING));
	}

	private static BlockBehaviour.Properties propertiesForState(WeatheringCopper.WeatherState state) {
		return BlockBehaviour.Properties.of()
			.mapColor(getMapColorForWeatherState(state))
			.requiresCorrectToolForDrops()
			.strength(1.5F, 3F)
			.sound(SoundType.COPPER);
	}

	public static MapColor getMapColorForWeatherState(WeatheringCopper.WeatherState weatherState) {
		if (weatherState == WeatheringCopper.WeatherState.UNAFFECTED) return MapColor.COLOR_ORANGE;
		if (weatherState == WeatheringCopper.WeatherState.EXPOSED) return MapColor.TERRACOTTA_LIGHT_GRAY;
		if (weatherState == WeatheringCopper.WeatherState.WEATHERED) return MapColor.WARPED_STEM;
		if (weatherState == WeatheringCopper.WeatherState.OXIDIZED) return MapColor.WARPED_NYLIUM;
		return MapColor.NONE;
	}

	public static WeatheringCopperCollection<Block> asBlocks(WeatheringCopperCollection<DeferredBlock<? extends Block>> collection) {
		return collection.map(DeferredBlock::get);
	}

	private SCPBlocks() {}
}

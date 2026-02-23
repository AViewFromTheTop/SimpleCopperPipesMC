package net.lunade.copper.registry;

import java.util.function.Function;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.block.CopperFittingBlock;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.block.WeatheringCopperFittingBlock;
import net.lunade.copper.block.WeatheringCopperPipeBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class SimpleCopperPipesBlocks {
	public static final Block COPPER_PIPE = register("copper_pipe",
		properties -> new WeatheringCopperPipeBlock(WeatheringCopper.WeatherState.UNAFFECTED, properties, 20),
		BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block EXPOSED_COPPER_PIPE = register("exposed_copper_pipe",
		properties -> new WeatheringCopperPipeBlock(WeatheringCopper.WeatherState.EXPOSED, properties, 18),
		BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block WEATHERED_COPPER_PIPE = register("weathered_copper_pipe",
		properties -> new WeatheringCopperPipeBlock(WeatheringCopper.WeatherState.WEATHERED, properties, 15),
		BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block OXIDIZED_COPPER_PIPE = register("oxidized_copper_pipe",
		properties -> new WeatheringCopperPipeBlock(WeatheringCopper.WeatherState.OXIDIZED, properties, 12),
		BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);

	public static final Block WAXED_COPPER_PIPE = register("waxed_copper_pipe",
		properties -> new CopperPipeBlock(properties, 20),
		BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block WAXED_EXPOSED_COPPER_PIPE = register("waxed_exposed_copper_pipe",
		properties -> new CopperPipeBlock(properties, 18),
		BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block WAXED_WEATHERED_COPPER_PIPE = register("waxed_weathered_copper_pipe",
		properties -> new CopperPipeBlock(properties, 15),
		BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block WAXED_OXIDIZED_COPPER_PIPE = register("waxed_oxidized_copper_pipe",
		properties -> new CopperPipeBlock(properties, 12),
		BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);

	public static final Block COPPER_FITTING = register("copper_fitting",
		properties -> new WeatheringCopperFittingBlock(WeatheringCopper.WeatherState.UNAFFECTED, properties),
		BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block EXPOSED_COPPER_FITTING = register("exposed_copper_fitting",
		properties -> new WeatheringCopperFittingBlock(WeatheringCopper.WeatherState.EXPOSED, properties),
		BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block WEATHERED_COPPER_FITTING = register("weathered_copper_fitting",
		properties -> new WeatheringCopperFittingBlock(WeatheringCopper.WeatherState.WEATHERED, properties),
		BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block OXIDIZED_COPPER_FITTING = register("oxidized_copper_fitting",
		properties -> new WeatheringCopperFittingBlock(WeatheringCopper.WeatherState.OXIDIZED, properties),
		BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);

	public static final Block WAXED_COPPER_FITTING = register("waxed_copper_fitting",
		CopperFittingBlock::new,
		BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block WAXED_EXPOSED_COPPER_FITTING = register("waxed_exposed_copper_fitting",
		CopperFittingBlock::new,
		BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block WAXED_WEATHERED_COPPER_FITTING = register("waxed_weathered_copper_fitting",
		CopperFittingBlock::new,
		BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);
	public static final Block WAXED_OXIDIZED_COPPER_FITTING = register("waxed_oxidized_copper_fitting",
		CopperFittingBlock::new,
		BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3F).sound(SoundType.COPPER)
	);

	public static void init() {
		OxidizableBlocksRegistry.registerNextStage(COPPER_PIPE, EXPOSED_COPPER_PIPE);
		OxidizableBlocksRegistry.registerNextStage(EXPOSED_COPPER_PIPE, WEATHERED_COPPER_PIPE);
		OxidizableBlocksRegistry.registerNextStage(WEATHERED_COPPER_PIPE, OXIDIZED_COPPER_PIPE);

		OxidizableBlocksRegistry.registerNextStage(COPPER_FITTING, EXPOSED_COPPER_FITTING);
		OxidizableBlocksRegistry.registerNextStage(EXPOSED_COPPER_FITTING, WEATHERED_COPPER_FITTING);
		OxidizableBlocksRegistry.registerNextStage(WEATHERED_COPPER_FITTING, OXIDIZED_COPPER_FITTING);

		OxidizableBlocksRegistry.registerWaxable(COPPER_PIPE, WAXED_COPPER_PIPE);
		OxidizableBlocksRegistry.registerWaxable(EXPOSED_COPPER_PIPE, WAXED_EXPOSED_COPPER_PIPE);
		OxidizableBlocksRegistry.registerWaxable(WEATHERED_COPPER_PIPE, WAXED_WEATHERED_COPPER_PIPE);
		OxidizableBlocksRegistry.registerWaxable(OXIDIZED_COPPER_PIPE, WAXED_OXIDIZED_COPPER_PIPE);

		OxidizableBlocksRegistry.registerWaxable(COPPER_FITTING, WAXED_COPPER_FITTING);
		OxidizableBlocksRegistry.registerWaxable(EXPOSED_COPPER_FITTING, WAXED_EXPOSED_COPPER_FITTING);
		OxidizableBlocksRegistry.registerWaxable(WEATHERED_COPPER_FITTING, WAXED_WEATHERED_COPPER_FITTING);
		OxidizableBlocksRegistry.registerWaxable(OXIDIZED_COPPER_FITTING, WAXED_OXIDIZED_COPPER_FITTING);
	}

	public static Identifier id(String path) {
		return Identifier.tryBuild(SimpleCopperPipesConstants.NAMESPACE, path);
	}

	public static Identifier legacyId(String path) {
		return Identifier.tryBuild(SimpleCopperPipesConstants.LEGACY_NAMESPACE, path);
	}

	public static Identifier legacyColoredPipe(String colour) {
		return legacyId(colour + "_pipe");
	}

	public static Identifier legacyGlowingPipe(String colour) {
		return legacyId("glowing_" + colour + "_pipe");
	}

	public static Identifier legacyColoredFitting(String colour) {
		return legacyId(colour + "_fitting");
	}

	public static Identifier legacyGlowingFitting(String colour) {
		return legacyId("glowing_" + colour + "_fitting");
	}

	private static <T extends Block> T registerWithoutItem(String path, Function<BlockBehaviour.Properties, T> block, BlockBehaviour.Properties properties) {
		final Identifier id = SimpleCopperPipesConstants.id(path);
		return doRegister(id, makeBlock(block, properties, id));
	}

	private static <T extends Block> T register(String path, Function<BlockBehaviour.Properties, T> block, BlockBehaviour.Properties properties) {
		final T registered = registerWithoutItem(path, block, properties);
		Items.registerBlock(registered);
		return registered;
	}

	private static <T extends Block> T doRegister(Identifier id, T block) {
		if (BuiltInRegistries.BLOCK.getOptional(id).isEmpty()) return Registry.register(BuiltInRegistries.BLOCK, id, block);
		throw new IllegalArgumentException("Block with id " + id + " is already in the block registry.");
	}

	private static <T extends Block> T makeBlock(Function<BlockBehaviour.Properties, T> function, BlockBehaviour.Properties properties, Identifier id) {
		return function.apply(properties.setId(ResourceKey.create(Registries.BLOCK, id)));
	}
}

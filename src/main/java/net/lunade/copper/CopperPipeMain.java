package net.lunade.copper;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.leaking_pipes.LeakingPipeDrips;
import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.lunade.copper.registry.RegisterCopperBlockEntities;
import net.lunade.copper.registry.RegisterCopperBlocks;
import net.lunade.copper.registry.SimpleCopperRegistries;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopperPipeMain implements ModInitializer {

	public static final int CURRENT_FIX_VERSION = 2;

	public static int getCompatID() {
		return 1;
	}

	public static final String MOD_ID = "simple_copper_pipes";
	public static final String NAMESPACE = "lunade";

	public static final ResourceLocation INSPECT_PIPE = id("inspect_copper_pipe");
	public static final ResourceLocation INSPECT_FITTING = id("inspect_copper_fitting");
	public static final ResourceLocation WATER = id("water");
	public static final ResourceLocation SMOKE = id("smoke");

	public static final TagKey<Block> WAXED = TagKey.create(Registries.BLOCK, id("waxed"));
	public static final TagKey<Block> SILENT_PIPES = TagKey.create(Registries.BLOCK, id("silent_pipes"));

	public static final TagKey<Item> IGNORES_COPPER_PIPE_MENU = TagKey.create(Registries.ITEM, id("ignores_copper_pipe_menu"));

	//SOUNDS
	public static final SoundEvent ITEM_IN = SoundEvent.createVariableRangeEvent(id("block.copper_pipe.item_in"));
	public static final SoundEvent ITEM_OUT = SoundEvent.createVariableRangeEvent(id("block.copper_pipe.item_out"));
	public static final SoundEvent LAUNCH = SoundEvent.createVariableRangeEvent(id("block.copper_pipe.launch"));
	public static final SoundEvent TURN = SoundEvent.createVariableRangeEvent(id("block.copper_pipe.turn"));

	public static final SoundEvent CORRODED_COPPER_PLACE = SoundEvent.createVariableRangeEvent(id("block.corroded_copper.place"));
	public static final SoundEvent CORRODED_COPPER_STEP = SoundEvent.createVariableRangeEvent(id("block.corroded_copper.step"));
	public static final SoundEvent CORRODED_COPPER_BREAK = SoundEvent.createVariableRangeEvent(id("block.corroded_copper.break"));
	public static final SoundEvent CORRODED_COPPER_FALL = SoundEvent.createVariableRangeEvent(id("block.corroded_copper.fall"));
	public static final SoundEvent CORRODED_COPPER_HIT = SoundEvent.createVariableRangeEvent(id("block.corroded_copper.hit"));

	//NOTE BLOCK
	public static final ResourceLocation NOTE_PACKET = id("note_packet");

	//PIPE INK PARTICLES
	public static final SimpleParticleType RED_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType GREEN_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType BROWN_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType BLUE_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType PURPLE_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType CYAN_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType LIGHT_GRAY_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType GRAY_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType PINK_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType LIME_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType YELLOW_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType LIGHT_BLUE_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType MAGENTA_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType ORANGE_INK = FabricParticleTypes.simple();
	public static final SimpleParticleType WHITE_INK = FabricParticleTypes.simple();

	@Override
	public void onInitialize() {
		CopperPipeProperties.init();
		SimpleCopperRegistries.initRegistry();
		// make config file
		SimpleCopperPipesConfig.get();

		RegisterCopperBlocks.register();
		RegisterCopperBlockEntities.init();

		OxidizableBlocksRegistry.registerOxidizableBlockPair(CopperPipe.COPPER_PIPE, CopperPipe.EXPOSED_PIPE);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(CopperPipe.EXPOSED_PIPE, CopperPipe.WEATHERED_PIPE);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(CopperPipe.WEATHERED_PIPE, CopperPipe.OXIDIZED_PIPE);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(CopperPipe.OXIDIZED_PIPE, CopperPipe.CORRODED_PIPE);

		OxidizableBlocksRegistry.registerOxidizableBlockPair(CopperFitting.COPPER_FITTING, CopperFitting.EXPOSED_FITTING);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(CopperFitting.EXPOSED_FITTING, CopperFitting.WEATHERED_FITTING);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(CopperFitting.WEATHERED_FITTING, CopperFitting.OXIDIZED_FITTING);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(CopperFitting.OXIDIZED_FITTING, CopperFitting.CORRODED_FITTING);

		OxidizableBlocksRegistry.registerWaxableBlockPair(CopperPipe.COPPER_PIPE, CopperPipe.WAXED_COPPER_PIPE);
		OxidizableBlocksRegistry.registerWaxableBlockPair(CopperPipe.EXPOSED_PIPE, CopperPipe.WAXED_EXPOSED_PIPE);
		OxidizableBlocksRegistry.registerWaxableBlockPair(CopperPipe.WEATHERED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE);
		OxidizableBlocksRegistry.registerWaxableBlockPair(CopperPipe.OXIDIZED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE);

		OxidizableBlocksRegistry.registerWaxableBlockPair(CopperFitting.COPPER_FITTING, CopperFitting.WAXED_COPPER_FITTING);
		OxidizableBlocksRegistry.registerWaxableBlockPair(CopperFitting.EXPOSED_FITTING, CopperFitting.WAXED_EXPOSED_FITTING);
		OxidizableBlocksRegistry.registerWaxableBlockPair(CopperFitting.WEATHERED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING);
		OxidizableBlocksRegistry.registerWaxableBlockPair(CopperFitting.OXIDIZED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING);

		RegisterCopperBlockEntities.init();

		Registry.register(BuiltInRegistries.CUSTOM_STAT, INSPECT_PIPE, INSPECT_PIPE);
		Registry.register(BuiltInRegistries.CUSTOM_STAT, INSPECT_FITTING, INSPECT_FITTING);
		Stats.CUSTOM.get(INSPECT_PIPE, StatFormatter.DEFAULT);

		//PARTICLE
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("white_ink"), WHITE_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("light_gray_ink"), LIGHT_GRAY_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("gray_ink"), GRAY_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("brown_ink"), BROWN_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("red_ink"), RED_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("orange_ink"), ORANGE_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("yellow_ink"), YELLOW_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("lime_ink"), LIME_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("green_ink"), GREEN_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("cyan_ink"), CYAN_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("light_blue_ink"), LIGHT_BLUE_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("blue_ink"), BLUE_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("purple_ink"), PURPLE_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("magenta_ink"), MAGENTA_INK);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, id("pink_ink"), PINK_INK);

		//SOUND
		Registry.register(BuiltInRegistries.SOUND_EVENT, ITEM_IN.getLocation(), ITEM_IN);
		Registry.register(BuiltInRegistries.SOUND_EVENT, ITEM_OUT.getLocation(), ITEM_OUT);
		Registry.register(BuiltInRegistries.SOUND_EVENT, LAUNCH.getLocation(), LAUNCH);
		Registry.register(BuiltInRegistries.SOUND_EVENT, TURN.getLocation(), TURN);

		Registry.register(BuiltInRegistries.SOUND_EVENT, CORRODED_COPPER_PLACE.getLocation(), CORRODED_COPPER_PLACE);
		Registry.register(BuiltInRegistries.SOUND_EVENT, CORRODED_COPPER_STEP.getLocation(), CORRODED_COPPER_STEP);
		Registry.register(BuiltInRegistries.SOUND_EVENT, CORRODED_COPPER_BREAK.getLocation(), CORRODED_COPPER_BREAK);
		Registry.register(BuiltInRegistries.SOUND_EVENT, CORRODED_COPPER_FALL.getLocation(), CORRODED_COPPER_FALL);
		Registry.register(BuiltInRegistries.SOUND_EVENT, CORRODED_COPPER_HIT.getLocation(), CORRODED_COPPER_HIT);

		RegisterPipeNbtMethods.init();
		PoweredPipeDispenses.init();
		FittingPipeDispenses.init();
		PipeMovementRestrictions.init();
		LeakingPipeDrips.init();

		ServerLifecycleEvents.SERVER_STOPPED.register((server) -> LeakingPipeManager.clearAll());

		ServerTickEvents.START_SERVER_TICK.register((listener) -> LeakingPipeManager.clearAndSwitch());

		FabricLoader.getInstance().getEntrypointContainers("simplecopperpipes", CopperPipeEntrypoint.class).forEach(entrypoint -> {
			try {
				CopperPipeEntrypoint mainPoint = entrypoint.getEntrypoint();
				mainPoint.init();
				if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
					mainPoint.initDevOnly();
				}
			} catch (Throwable ignored) {

			}
		});
	}

	public static final Object2ObjectMap<Block, Block> GLOW_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
		//PIPE
		object2IntOpenHashMap.put(CopperPipe.RED_PIPE, CopperPipe.GLOWING_RED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.ORANGE_PIPE, CopperPipe.GLOWING_ORANGE_PIPE);
		object2IntOpenHashMap.put(CopperPipe.YELLOW_PIPE, CopperPipe.GLOWING_YELLOW_PIPE);
		object2IntOpenHashMap.put(CopperPipe.GREEN_PIPE, CopperPipe.GLOWING_GREEN_PIPE);
		object2IntOpenHashMap.put(CopperPipe.CYAN_PIPE, CopperPipe.GLOWING_CYAN_PIPE);
		object2IntOpenHashMap.put(CopperPipe.LIGHT_BLUE_PIPE, CopperPipe.GLOWING_LIGHT_BLUE_PIPE);
		object2IntOpenHashMap.put(CopperPipe.BLUE_PIPE, CopperPipe.GLOWING_BLUE_PIPE);
		object2IntOpenHashMap.put(CopperPipe.PURPLE_PIPE, CopperPipe.GLOWING_PURPLE_PIPE);
		object2IntOpenHashMap.put(CopperPipe.MAGENTA_PIPE, CopperPipe.GLOWING_MAGENTA_PIPE);
		object2IntOpenHashMap.put(CopperPipe.PINK_PIPE, CopperPipe.GLOWING_PINK_PIPE);
		object2IntOpenHashMap.put(CopperPipe.WHITE_PIPE, CopperPipe.GLOWING_WHITE_PIPE);
		object2IntOpenHashMap.put(CopperPipe.LIGHT_GRAY_PIPE, CopperPipe.GLOWING_LIGHT_GRAY_PIPE);
		object2IntOpenHashMap.put(CopperPipe.GRAY_PIPE, CopperPipe.GLOWING_GRAY_PIPE);
		object2IntOpenHashMap.put(CopperPipe.BLACK_PIPE, CopperPipe.GLOWING_BLACK_PIPE);
		object2IntOpenHashMap.put(CopperPipe.BROWN_PIPE, CopperPipe.GLOWING_BROWN_PIPE);
		//FITTING
		object2IntOpenHashMap.put(CopperFitting.RED_FITTING, CopperFitting.GLOWING_RED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.ORANGE_FITTING, CopperFitting.GLOWING_ORANGE_FITTING);
		object2IntOpenHashMap.put(CopperFitting.YELLOW_FITTING, CopperFitting.GLOWING_YELLOW_FITTING);
		object2IntOpenHashMap.put(CopperFitting.GREEN_FITTING, CopperFitting.GLOWING_GREEN_FITTING);
		object2IntOpenHashMap.put(CopperFitting.CYAN_FITTING, CopperFitting.GLOWING_CYAN_FITTING);
		object2IntOpenHashMap.put(CopperFitting.LIGHT_BLUE_FITTING, CopperFitting.GLOWING_LIGHT_BLUE_FITTING);
		object2IntOpenHashMap.put(CopperFitting.BLUE_FITTING, CopperFitting.GLOWING_BLUE_FITTING);
		object2IntOpenHashMap.put(CopperFitting.PURPLE_FITTING, CopperFitting.GLOWING_PURPLE_FITTING);
		object2IntOpenHashMap.put(CopperFitting.MAGENTA_FITTING, CopperFitting.GLOWING_MAGENTA_FITTING);
		object2IntOpenHashMap.put(CopperFitting.PINK_FITTING, CopperFitting.GLOWING_PINK_FITTING);
		object2IntOpenHashMap.put(CopperFitting.WHITE_FITTING, CopperFitting.GLOWING_WHITE_FITTING);
		object2IntOpenHashMap.put(CopperFitting.LIGHT_GRAY_FITTING, CopperFitting.GLOWING_LIGHT_GRAY_FITTING);
		object2IntOpenHashMap.put(CopperFitting.GRAY_FITTING, CopperFitting.GLOWING_GRAY_FITTING);
		object2IntOpenHashMap.put(CopperFitting.BLACK_FITTING, CopperFitting.GLOWING_BLACK_FITTING);
		object2IntOpenHashMap.put(CopperFitting.BROWN_FITTING, CopperFitting.GLOWING_BROWN_FITTING);
	}));

	public static ResourceLocation id(String path) {
		return new ResourceLocation(NAMESPACE, path);
	}

	public static final Logger LOGGER = LoggerFactory.getLogger("COPPER_PIPES");

}

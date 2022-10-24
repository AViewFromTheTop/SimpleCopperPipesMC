package net.lunade.copper;

import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CopperPipeMain implements ModInitializer {

	public static final int CURRENT_FIX_VERSION = 2;

	public static final String MOD_ID = "copper_pipes";
	public static final String BLOCK_ID = "copper_pipes";

	public static final ResourceLocation INSPECT_PIPE = id("inspect_copper_pipe");
	public static final ResourceLocation WATER = id("water");
	public static final ResourceLocation SMOKE = id("smoke");

	public static BlockEntityType<CopperPipeEntity> COPPER_PIPE_ENTITY;

	public static BlockEntityType<CopperFittingEntity> COPPER_FITTING_ENTITY;
	public static final TagKey<Block> BLOCK_LISTENERS = TagKey.create(Registry.BLOCK_REGISTRY, id("block_event_listeners"));
	public static final TagKey<Block> UNSCRAPEABLE = TagKey.create(Registry.BLOCK_REGISTRY, id("unscrapeable"));
	public static final TagKey<Block> WAXED = TagKey.create(Registry.BLOCK_REGISTRY, id("waxed"));
	public static final TagKey<Block> SILENT_PIPES = TagKey.create(Registry.BLOCK_REGISTRY, id("silent_pipes"));
	public static final TagKey<EntityType<?>> ENTITY_LISTENERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, id("entity_event_listeners"));

	//SOUNDS
	public static final SoundEvent ITEM_IN = new SoundEvent(id("block.copper_pipe.item_in"));
	public static final SoundEvent ITEM_OUT = new SoundEvent(id("block.copper_pipe.item_out"));
	public static final SoundEvent LAUNCH = new SoundEvent(id("block.copper_pipe.launch"));
	public static final SoundEvent TURN = new SoundEvent(id("block.copper_pipe.turn"));

	public static final SoundEvent CORRODED_COPPER_PLACE = new SoundEvent(id("block.corroded_copper.place"));
	public static final SoundEvent CORRODED_COPPER_STEP = new SoundEvent(id("block.corroded_copper.step"));
	public static final SoundEvent CORRODED_COPPER_BREAK = new SoundEvent(id("block.corroded_copper.break"));
	public static final SoundEvent CORRODED_COPPER_FALL = new SoundEvent(id("block.corroded_copper.fall"));
	public static final SoundEvent CORRODED_COPPER_HIT = new SoundEvent(id("block.corroded_copper.hit"));

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

		Registry.register(Registry.CUSTOM_STAT, INSPECT_PIPE, INSPECT_PIPE);
		Stats.CUSTOM.get(INSPECT_PIPE, StatFormatter.DEFAULT);

		//PARTICLE
		Registry.register(Registry.PARTICLE_TYPE, id("red_ink"), RED_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("green_ink"), GREEN_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("brown_ink"), BROWN_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("blue_ink"), BLUE_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("purple_ink"), PURPLE_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("cyan_ink"), CYAN_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("light_gray_ink"), LIGHT_GRAY_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("gray_ink"), GRAY_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("pink_ink"), PINK_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("lime_ink"), LIME_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("yellow_ink"), YELLOW_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("light_blue_ink"), LIGHT_BLUE_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("magenta_ink"), MAGENTA_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("orange_ink"), ORANGE_INK);
		Registry.register(Registry.PARTICLE_TYPE, id("white_ink"), WHITE_INK);

		//PIPE
		registerBlock(CopperPipe.COPPER_PIPE, id("copper_pipe"));
		registerBlock(CopperPipe.EXPOSED_PIPE, id("exposed_copper_pipe"));
		registerBlock(CopperPipe.WEATHERED_PIPE, id("weathered_copper_pipe"));
		registerBlock(CopperPipe.OXIDIZED_PIPE, id("oxidized_copper_pipe"));
		
		//WAXED
		registerBlock(CopperPipe.WAXED_COPPER_PIPE, id("waxed_copper_pipe"));
		registerBlock(CopperPipe.WAXED_EXPOSED_PIPE, id("waxed_exposed_copper_pipe"));
		registerBlock(CopperPipe.WAXED_WEATHERED_PIPE, id("waxed_weathered_copper_pipe"));
		registerBlock(CopperPipe.WAXED_OXIDIZED_PIPE, id("waxed_oxidized_copper_pipe"));

		//CORRODED
		registerBlock(CopperPipe.CORRODED_PIPE, id("corroded_pipe"));

		COPPER_PIPE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "lunade:copper_pipe", FabricBlockEntityTypeBuilder.create(CopperPipeEntity::new, CopperPipe.COPPER_PIPE, CopperPipe.EXPOSED_PIPE, CopperPipe.WEATHERED_PIPE, CopperPipe.OXIDIZED_PIPE, CopperPipe.WAXED_COPPER_PIPE, CopperPipe.WAXED_EXPOSED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE,
		CopperPipe.CORRODED_PIPE).build(null));

		//FITTINGS
		registerBlock(CopperFitting.COPPER_FITTING, id("copper_fitting"));
		registerBlock(CopperFitting.EXPOSED_FITTING, id("exposed_copper_fitting"));
		registerBlock(CopperFitting.WEATHERED_FITTING, id("weathered_copper_fitting"));
		registerBlock(CopperFitting.OXIDIZED_FITTING, id("oxidized_copper_fitting"));
		//WAXED
		registerBlock(CopperFitting.WAXED_COPPER_FITTING, id("waxed_copper_fitting"));
		registerBlock(CopperFitting.WAXED_EXPOSED_FITTING, id("waxed_exposed_copper_fitting"));
		registerBlock(CopperFitting.WAXED_WEATHERED_FITTING, id("waxed_weathered_copper_fitting"));
		registerBlock(CopperFitting.WAXED_OXIDIZED_FITTING, id("waxed_oxidized_copper_fitting"));

		//CORRODED
		registerBlock(CopperFitting.CORRODED_FITTING, id("corroded_fitting"));


		COPPER_FITTING_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "lunade:copper_fitting", FabricBlockEntityTypeBuilder.create(CopperFittingEntity::new, CopperFitting.COPPER_FITTING, CopperFitting.EXPOSED_FITTING, CopperFitting.WEATHERED_FITTING, CopperFitting.OXIDIZED_FITTING, CopperFitting.WAXED_COPPER_FITTING, CopperFitting.WAXED_EXPOSED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING,
		CopperFitting.CORRODED_FITTING).build(null));

		//SOUND
		Registry.register(Registry.SOUND_EVENT, ITEM_IN.getLocation(), ITEM_IN);
		Registry.register(Registry.SOUND_EVENT, ITEM_OUT.getLocation(), ITEM_OUT);
		Registry.register(Registry.SOUND_EVENT, LAUNCH.getLocation(), LAUNCH);
		Registry.register(Registry.SOUND_EVENT, TURN.getLocation(), TURN);

		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_PLACE.getLocation(), CORRODED_COPPER_PLACE);
		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_STEP.getLocation(), CORRODED_COPPER_STEP);
		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_BREAK.getLocation(), CORRODED_COPPER_BREAK);
		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_FALL.getLocation(), CORRODED_COPPER_FALL);
		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_HIT.getLocation(), CORRODED_COPPER_HIT);

		RegisterPipeNbtMethods.init();
		PoweredPipeDispenses.init();
		FittingPipeDispenses.init();
		PipeMovementRestrictions.init();

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

	public static List<Direction> shuffledDirections(RandomSource random) {
		return Util.shuffledCopy(Direction.values(), random);
	}

	public static final Object2ObjectMap<Block, Block> NEXT_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
		//PIPE
		object2IntOpenHashMap.put(CopperPipe.COPPER_PIPE, CopperPipe.EXPOSED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.EXPOSED_PIPE, CopperPipe.WEATHERED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.WEATHERED_PIPE, CopperPipe.OXIDIZED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.OXIDIZED_PIPE, CopperPipe.CORRODED_PIPE);
		//FITTING
		object2IntOpenHashMap.put(CopperFitting.COPPER_FITTING, CopperFitting.EXPOSED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.EXPOSED_FITTING, CopperFitting.WEATHERED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.WEATHERED_FITTING, CopperFitting.OXIDIZED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.OXIDIZED_FITTING, CopperFitting.CORRODED_FITTING);
	}));
	public static final Object2ObjectMap<Block, Block> PREVIOUS_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
		//PIPE
		object2IntOpenHashMap.put(CopperPipe.CORRODED_PIPE, CopperPipe.OXIDIZED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.OXIDIZED_PIPE, CopperPipe.WEATHERED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.WEATHERED_PIPE, CopperPipe.EXPOSED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.EXPOSED_PIPE, CopperPipe.COPPER_PIPE);
		object2IntOpenHashMap.put(CopperPipe.WAXED_COPPER_PIPE, CopperPipe.COPPER_PIPE);
		object2IntOpenHashMap.put(CopperPipe.WAXED_EXPOSED_PIPE, CopperPipe.EXPOSED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.WAXED_WEATHERED_PIPE, CopperPipe.WEATHERED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.WAXED_OXIDIZED_PIPE, CopperPipe.OXIDIZED_PIPE);
		//FITTING
		object2IntOpenHashMap.put(CopperFitting.CORRODED_FITTING, CopperFitting.OXIDIZED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.OXIDIZED_FITTING, CopperFitting.WEATHERED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.WEATHERED_FITTING, CopperFitting.EXPOSED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.EXPOSED_FITTING, CopperFitting.COPPER_FITTING);
		object2IntOpenHashMap.put(CopperFitting.WAXED_COPPER_FITTING, CopperFitting.COPPER_FITTING);
		object2IntOpenHashMap.put(CopperFitting.WAXED_EXPOSED_FITTING, CopperFitting.EXPOSED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.WAXED_WEATHERED_FITTING, CopperFitting.WEATHERED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.WAXED_OXIDIZED_FITTING, CopperFitting.OXIDIZED_FITTING);
	}));
	public static final Object2ObjectMap<Block, Block> WAX_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
		//PIPE
		object2IntOpenHashMap.put(CopperPipe.COPPER_PIPE, CopperPipe.WAXED_COPPER_PIPE);
		object2IntOpenHashMap.put(CopperPipe.EXPOSED_PIPE, CopperPipe.WAXED_EXPOSED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.WEATHERED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE);
		object2IntOpenHashMap.put(CopperPipe.OXIDIZED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE);
		//FITTING
		object2IntOpenHashMap.put(CopperFitting.COPPER_FITTING, CopperFitting.WAXED_COPPER_FITTING);
		object2IntOpenHashMap.put(CopperFitting.EXPOSED_FITTING, CopperFitting.WAXED_EXPOSED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.WEATHERED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING);
		object2IntOpenHashMap.put(CopperFitting.OXIDIZED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING);
	}));
	public static final Object2ObjectMap<Block, Block> GLOW_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {

	}));
	public static final Object2IntMap<Block> OXIDIZATION_INT = Object2IntMaps.unmodifiable(Util.make(new Object2IntOpenHashMap<>(), (object2IntOpenHashMap) -> {
		//PIPE
		object2IntOpenHashMap.put(CopperPipe.COPPER_PIPE, 0);
		object2IntOpenHashMap.put(CopperPipe.EXPOSED_PIPE, 1);
		object2IntOpenHashMap.put(CopperPipe.WEATHERED_PIPE, 2);
		object2IntOpenHashMap.put(CopperPipe.OXIDIZED_PIPE, 3);
		//FITTING
		object2IntOpenHashMap.put(CopperFitting.COPPER_FITTING, 0);
		object2IntOpenHashMap.put(CopperFitting.EXPOSED_FITTING, 1);
		object2IntOpenHashMap.put(CopperFitting.WEATHERED_FITTING, 2);
		object2IntOpenHashMap.put(CopperFitting.OXIDIZED_FITTING, 3);
	}));

	public static ResourceLocation id(String path) {
		return new ResourceLocation(BLOCK_ID, path);
	}

	public static ResourceLocation colourPipe(String colour) {
		return id(colour + "_pipe");
	}

	public static ResourceLocation glowingPipe(String colour) {
		return id("glowing_" + colour + "_pipe");
	}

	public static ResourceLocation colourFitting(String colour) {
		return id(colour + "_fitting");
	}

	public static ResourceLocation glowingFitting(String colour) {
		return id("glowing_" + colour + "_fitting");
	}

	public static final Logger LOGGER = LoggerFactory.getLogger("COPPER_PIPES");

	public static void registerBlock(Block block, ResourceLocation resourceLocation) {
		Registry.register(Registry.BLOCK, resourceLocation, block);
		Registry.register(Registry.ITEM, resourceLocation, new BlockItem(block, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
	}

}

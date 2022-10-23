package net.lunade.copper;

import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main implements ModInitializer {

	public static final int CURRENT_FIX_VERSION = 2;

	public static final ResourceLocation INSPECT_PIPE = new ResourceLocation("lunade", "inspect_copper_pipe");
	public static final ResourceLocation WATER = new ResourceLocation("lunade", "water");
	public static final ResourceLocation SMOKE = new ResourceLocation("lunade", "smoke");

	//COPPER PIPE
	public static final ResourceLocation COPPER_PIPE = new ResourceLocation("lunade", "copper_pipe");
	public static final ResourceLocation EXPOSED_PIPE = new ResourceLocation("lunade", "exposed_copper_pipe");
	public static final ResourceLocation WEATHERED_PIPE = new ResourceLocation("lunade", "weathered_copper_pipe");
	public static final ResourceLocation OXIDIZED_PIPE = new ResourceLocation("lunade", "oxidized_copper_pipe");

	public static final ResourceLocation WAXED_COPPER_PIPE = new ResourceLocation("lunade", "waxed_copper_pipe");
	public static final ResourceLocation WAXED_EXPOSED_PIPE = new ResourceLocation("lunade", "waxed_exposed_copper_pipe");
	public static final ResourceLocation WAXED_WEATHERED_PIPE = new ResourceLocation("lunade", "waxed_weathered_copper_pipe");
	public static final ResourceLocation WAXED_OXIDIZED_PIPE = new ResourceLocation("lunade", "waxed_oxidized_copper_pipe");

	public static final ResourceLocation CORRODED_PIPE = new ResourceLocation("lunade", "corroded_pipe");

	public static BlockEntityType<CopperPipeEntity> COPPER_PIPE_ENTITY;

	//COPPER FITTING
	public static final ResourceLocation COPPER_FITTING = new ResourceLocation("lunade", "copper_fitting");
	public static final ResourceLocation EXPOSED_FITTING = new ResourceLocation("lunade", "exposed_copper_fitting");
	public static final ResourceLocation WEATHERED_FITTING = new ResourceLocation("lunade", "weathered_copper_fitting");
	public static final ResourceLocation OXIDIZED_FITTING = new ResourceLocation("lunade", "oxidized_copper_fitting");

	public static final ResourceLocation WAXED_COPPER_FITTING = new ResourceLocation("lunade", "waxed_copper_fitting");
	public static final ResourceLocation WAXED_EXPOSED_FITTING = new ResourceLocation("lunade", "waxed_exposed_copper_fitting");
	public static final ResourceLocation WAXED_WEATHERED_FITTING = new ResourceLocation("lunade", "waxed_weathered_copper_fitting");
	public static final ResourceLocation WAXED_OXIDIZED_FITTING = new ResourceLocation("lunade", "waxed_oxidized_copper_fitting");

	public static final ResourceLocation CORRODED_FITTING = new ResourceLocation("lunade", "corroded_fitting");

	public static BlockEntityType<CopperFittingEntity> COPPER_FITTING_ENTITY;
	public static final TagKey<Block> BLOCK_LISTENERS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("lunade", "block_event_listeners"));
	public static final TagKey<Block> UNSCRAPEABLE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("lunade", "unscrapeable"));
	public static final TagKey<Block> WAXED = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("lunade", "waxed"));
	public static final TagKey<Block> SILENT_PIPES = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("lunade", "silent_pipes"));
	public static final TagKey<EntityType<?>> ENTITY_LISTENERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("lunade", "entity_event_listeners"));

	//COLORED PIPE
	public static final ResourceLocation BLACK_PIPE = new ResourceLocation("lunade", "black_pipe");
	public static final ResourceLocation RED_PIPE = new ResourceLocation("lunade", "red_pipe");
	public static final ResourceLocation GREEN_PIPE = new ResourceLocation("lunade", "green_pipe");
	public static final ResourceLocation BROWN_PIPE = new ResourceLocation("lunade", "brown_pipe");
	public static final ResourceLocation BLUE_PIPE = new ResourceLocation("lunade", "blue_pipe");
	public static final ResourceLocation PURPLE_PIPE = new ResourceLocation("lunade", "purple_pipe");
	public static final ResourceLocation CYAN_PIPE = new ResourceLocation("lunade", "cyan_pipe");
	public static final ResourceLocation LIGHT_GRAY_PIPE = new ResourceLocation("lunade", "light_gray_pipe");
	public static final ResourceLocation GRAY_PIPE = new ResourceLocation("lunade", "gray_pipe");
	public static final ResourceLocation PINK_PIPE = new ResourceLocation("lunade", "pink_pipe");
	public static final ResourceLocation LIME_PIPE = new ResourceLocation("lunade", "lime_pipe");
	public static final ResourceLocation YELLOW_PIPE = new ResourceLocation("lunade", "yellow_pipe");
	public static final ResourceLocation LIGHT_BLUE_PIPE = new ResourceLocation("lunade", "light_blue_pipe");
	public static final ResourceLocation MAGENTA_PIPE = new ResourceLocation("lunade", "magenta_pipe");
	public static final ResourceLocation ORANGE_PIPE = new ResourceLocation("lunade", "orange_pipe");
	public static final ResourceLocation WHITE_PIPE = new ResourceLocation("lunade", "white_pipe");

	public static final ResourceLocation GLOWING_BLACK_PIPE = new ResourceLocation("lunade", "glowing_black_pipe");
	public static final ResourceLocation GLOWING_RED_PIPE = new ResourceLocation("lunade", "glowing_red_pipe");
	public static final ResourceLocation GLOWING_GREEN_PIPE = new ResourceLocation("lunade", "glowing_green_pipe");
	public static final ResourceLocation GLOWING_BROWN_PIPE = new ResourceLocation("lunade", "glowing_brown_pipe");
	public static final ResourceLocation GLOWING_BLUE_PIPE = new ResourceLocation("lunade", "glowing_blue_pipe");
	public static final ResourceLocation GLOWING_PURPLE_PIPE = new ResourceLocation("lunade", "glowing_purple_pipe");
	public static final ResourceLocation GLOWING_CYAN_PIPE = new ResourceLocation("lunade", "glowing_cyan_pipe");
	public static final ResourceLocation GLOWING_LIGHT_GRAY_PIPE = new ResourceLocation("lunade", "glowing_light_gray_pipe");
	public static final ResourceLocation GLOWING_GRAY_PIPE = new ResourceLocation("lunade", "glowing_gray_pipe");
	public static final ResourceLocation GLOWING_PINK_PIPE = new ResourceLocation("lunade", "glowing_pink_pipe");
	public static final ResourceLocation GLOWING_LIME_PIPE = new ResourceLocation("lunade", "glowing_lime_pipe");
	public static final ResourceLocation GLOWING_YELLOW_PIPE = new ResourceLocation("lunade", "glowing_yellow_pipe");
	public static final ResourceLocation GLOWING_LIGHT_BLUE_PIPE = new ResourceLocation("lunade", "glowing_light_blue_pipe");
	public static final ResourceLocation GLOWING_MAGENTA_PIPE = new ResourceLocation("lunade", "glowing_magenta_pipe");
	public static final ResourceLocation GLOWING_ORANGE_PIPE = new ResourceLocation("lunade", "glowing_orange_pipe");
	public static final ResourceLocation GLOWING_WHITE_PIPE = new ResourceLocation("lunade", "glowing_white_pipe");

	//COLORED FITTING
	public static final ResourceLocation BLACK_FITTING = new ResourceLocation("lunade", "black_fitting");
	public static final ResourceLocation RED_FITTING = new ResourceLocation("lunade", "red_fitting");
	public static final ResourceLocation GREEN_FITTING = new ResourceLocation("lunade", "green_fitting");
	public static final ResourceLocation BROWN_FITTING = new ResourceLocation("lunade", "brown_fitting");
	public static final ResourceLocation BLUE_FITTING = new ResourceLocation("lunade", "blue_fitting");
	public static final ResourceLocation PURPLE_FITTING = new ResourceLocation("lunade", "purple_fitting");
	public static final ResourceLocation CYAN_FITTING = new ResourceLocation("lunade", "cyan_fitting");
	public static final ResourceLocation LIGHT_GRAY_FITTING = new ResourceLocation("lunade", "light_gray_fitting");
	public static final ResourceLocation GRAY_FITTING = new ResourceLocation("lunade", "gray_fitting");
	public static final ResourceLocation PINK_FITTING = new ResourceLocation("lunade", "pink_fitting");
	public static final ResourceLocation LIME_FITTING = new ResourceLocation("lunade", "lime_fitting");
	public static final ResourceLocation YELLOW_FITTING = new ResourceLocation("lunade", "yellow_fitting");
	public static final ResourceLocation LIGHT_BLUE_FITTING = new ResourceLocation("lunade", "light_blue_fitting");
	public static final ResourceLocation MAGENTA_FITTING = new ResourceLocation("lunade", "magenta_fitting");
	public static final ResourceLocation ORANGE_FITTING = new ResourceLocation("lunade", "orange_fitting");
	public static final ResourceLocation WHITE_FITTING = new ResourceLocation("lunade", "white_fitting");

	public static final ResourceLocation GLOWING_BLACK_FITTING = new ResourceLocation("lunade", "glowing_black_fitting");
	public static final ResourceLocation GLOWING_RED_FITTING = new ResourceLocation("lunade", "glowing_red_fitting");
	public static final ResourceLocation GLOWING_GREEN_FITTING = new ResourceLocation("lunade", "glowing_green_fitting");
	public static final ResourceLocation GLOWING_BROWN_FITTING = new ResourceLocation("lunade", "glowing_brown_fitting");
	public static final ResourceLocation GLOWING_BLUE_FITTING = new ResourceLocation("lunade", "glowing_blue_fitting");
	public static final ResourceLocation GLOWING_PURPLE_FITTING = new ResourceLocation("lunade", "glowing_purple_fitting");
	public static final ResourceLocation GLOWING_CYAN_FITTING = new ResourceLocation("lunade", "glowing_cyan_fitting");
	public static final ResourceLocation GLOWING_LIGHT_GRAY_FITTING = new ResourceLocation("lunade", "glowing_light_gray_fitting");
	public static final ResourceLocation GLOWING_GRAY_FITTING = new ResourceLocation("lunade", "glowing_gray_fitting");
	public static final ResourceLocation GLOWING_PINK_FITTING = new ResourceLocation("lunade", "glowing_pink_fitting");
	public static final ResourceLocation GLOWING_LIME_FITTING = new ResourceLocation("lunade", "glowing_lime_fitting");
	public static final ResourceLocation GLOWING_YELLOW_FITTING = new ResourceLocation("lunade", "glowing_yellow_fitting");
	public static final ResourceLocation GLOWING_LIGHT_BLUE_FITTING = new ResourceLocation("lunade", "glowing_light_blue_fitting");
	public static final ResourceLocation GLOWING_MAGENTA_FITTING = new ResourceLocation("lunade", "glowing_magenta_fitting");
	public static final ResourceLocation GLOWING_ORANGE_FITTING = new ResourceLocation("lunade", "glowing_orange_fitting");
	public static final ResourceLocation GLOWING_WHITE_FITTING = new ResourceLocation("lunade", "glowing_white_fitting");

	//SOUNDS
	public static final SoundEvent ITEM_IN = new SoundEvent(new ResourceLocation("lunade", "block.copper_pipe.item_in"));
	public static final SoundEvent ITEM_OUT = new SoundEvent(new ResourceLocation("lunade", "block.copper_pipe.item_out"));
	public static final SoundEvent LAUNCH = new SoundEvent(new ResourceLocation("lunade", "block.copper_pipe.launch"));
	public static final SoundEvent TURN = new SoundEvent(new ResourceLocation("lunade", "block.copper_pipe.turn"));

	public static final SoundEvent CORRODED_COPPER_PLACE = new SoundEvent(new ResourceLocation("lunade", "block.corroded_copper.place"));
	public static final SoundEvent CORRODED_COPPER_STEP = new SoundEvent(new ResourceLocation("lunade", "block.corroded_copper.step"));
	public static final SoundEvent CORRODED_COPPER_BREAK = new SoundEvent(new ResourceLocation("lunade", "block.corroded_copper.break"));
	public static final SoundEvent CORRODED_COPPER_FALL = new SoundEvent(new ResourceLocation("lunade", "block.corroded_copper.fall"));
	public static final SoundEvent CORRODED_COPPER_HIT = new SoundEvent(new ResourceLocation("lunade", "block.corroded_copper.hit"));

	//NOTE BLOCK
	public static final ResourceLocation NOTE_PACKET = new ResourceLocation("lunade","note_packet");

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
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "red_ink"), RED_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "green_ink"), GREEN_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "brown_ink"), BROWN_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "blue_ink"), BLUE_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "purple_ink"), PURPLE_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "cyan_ink"), CYAN_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "light_gray_ink"), LIGHT_GRAY_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "gray_ink"), GRAY_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "pink_ink"), PINK_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "lime_ink"), LIME_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "yellow_ink"), YELLOW_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "light_blue_ink"), LIGHT_BLUE_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "magenta_ink"), MAGENTA_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "orange_ink"), ORANGE_INK);
		Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation("lunade", "white_ink"), WHITE_INK);

		//PIPE
		Registry.register(Registry.BLOCK, COPPER_PIPE, CopperPipe.COPPER_PIPE);
		Registry.register(Registry.ITEM, COPPER_PIPE, new BlockItem(CopperPipe.COPPER_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, EXPOSED_PIPE, CopperPipe.EXPOSED_PIPE);
		Registry.register(Registry.ITEM, EXPOSED_PIPE, new BlockItem(CopperPipe.EXPOSED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, WEATHERED_PIPE, CopperPipe.WEATHERED_PIPE);
		Registry.register(Registry.ITEM, WEATHERED_PIPE, new BlockItem(CopperPipe.WEATHERED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, OXIDIZED_PIPE, CopperPipe.OXIDIZED_PIPE);
		Registry.register(Registry.ITEM, OXIDIZED_PIPE, new BlockItem(CopperPipe.OXIDIZED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		//WAXED
		Registry.register(Registry.BLOCK, WAXED_COPPER_PIPE, CopperPipe.WAXED_COPPER_PIPE);
		Registry.register(Registry.ITEM, WAXED_COPPER_PIPE, new BlockItem(CopperPipe.WAXED_COPPER_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_EXPOSED_PIPE, CopperPipe.WAXED_EXPOSED_PIPE);
		Registry.register(Registry.ITEM, WAXED_EXPOSED_PIPE, new BlockItem(CopperPipe.WAXED_EXPOSED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_WEATHERED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE);
		Registry.register(Registry.ITEM, WAXED_WEATHERED_PIPE, new BlockItem(CopperPipe.WAXED_WEATHERED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_OXIDIZED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE);
		Registry.register(Registry.ITEM, WAXED_OXIDIZED_PIPE, new BlockItem(CopperPipe.WAXED_OXIDIZED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, CORRODED_PIPE, CopperPipe.CORRODED_PIPE);
		Registry.register(Registry.ITEM, CORRODED_PIPE, new BlockItem(CopperPipe.CORRODED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		//COLORED
		Registry.register(Registry.BLOCK, BLACK_PIPE, CopperPipe.BLACK_PIPE);
		Registry.register(Registry.ITEM, BLACK_PIPE, new BlockItem(CopperPipe.BLACK_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, RED_PIPE, CopperPipe.RED_PIPE);
		Registry.register(Registry.ITEM, RED_PIPE, new BlockItem(CopperPipe.RED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GREEN_PIPE, CopperPipe.GREEN_PIPE);
		Registry.register(Registry.ITEM, GREEN_PIPE, new BlockItem(CopperPipe.GREEN_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, BROWN_PIPE, CopperPipe.BROWN_PIPE);
		Registry.register(Registry.ITEM, BROWN_PIPE, new BlockItem(CopperPipe.BROWN_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, BLUE_PIPE, CopperPipe.BLUE_PIPE);
		Registry.register(Registry.ITEM, BLUE_PIPE, new BlockItem(CopperPipe.BLUE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, PURPLE_PIPE, CopperPipe.PURPLE_PIPE);
		Registry.register(Registry.ITEM, PURPLE_PIPE, new BlockItem(CopperPipe.PURPLE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, CYAN_PIPE, CopperPipe.CYAN_PIPE);
		Registry.register(Registry.ITEM, CYAN_PIPE, new BlockItem(CopperPipe.CYAN_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, LIGHT_GRAY_PIPE, CopperPipe.LIGHT_GRAY_PIPE);
		Registry.register(Registry.ITEM, LIGHT_GRAY_PIPE, new BlockItem(CopperPipe.LIGHT_GRAY_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GRAY_PIPE, CopperPipe.GRAY_PIPE);
		Registry.register(Registry.ITEM, GRAY_PIPE, new BlockItem(CopperPipe.GRAY_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, PINK_PIPE, CopperPipe.PINK_PIPE);
		Registry.register(Registry.ITEM, PINK_PIPE, new BlockItem(CopperPipe.PINK_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, LIME_PIPE, CopperPipe.LIME_PIPE);
		Registry.register(Registry.ITEM, LIME_PIPE, new BlockItem(CopperPipe.LIME_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, YELLOW_PIPE, CopperPipe.YELLOW_PIPE);
		Registry.register(Registry.ITEM, YELLOW_PIPE, new BlockItem(CopperPipe.YELLOW_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, LIGHT_BLUE_PIPE, CopperPipe.LIGHT_BLUE_PIPE);
		Registry.register(Registry.ITEM, LIGHT_BLUE_PIPE, new BlockItem(CopperPipe.LIGHT_BLUE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, MAGENTA_PIPE, CopperPipe.MAGENTA_PIPE);
		Registry.register(Registry.ITEM, MAGENTA_PIPE, new BlockItem(CopperPipe.MAGENTA_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, ORANGE_PIPE, CopperPipe.ORANGE_PIPE);
		Registry.register(Registry.ITEM, ORANGE_PIPE, new BlockItem(CopperPipe.ORANGE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, WHITE_PIPE, CopperPipe.WHITE_PIPE);
		Registry.register(Registry.ITEM, WHITE_PIPE, new BlockItem(CopperPipe.WHITE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		//GLOWING
		Registry.register(Registry.BLOCK, GLOWING_BLACK_PIPE, CopperPipe.GLOWING_BLACK_PIPE);
		Registry.register(Registry.ITEM, GLOWING_BLACK_PIPE, new BlockItem(CopperPipe.GLOWING_BLACK_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_RED_PIPE, CopperPipe.GLOWING_RED_PIPE);
		Registry.register(Registry.ITEM, GLOWING_RED_PIPE, new BlockItem(CopperPipe.GLOWING_RED_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_GREEN_PIPE, CopperPipe.GLOWING_GREEN_PIPE);
		Registry.register(Registry.ITEM, GLOWING_GREEN_PIPE, new BlockItem(CopperPipe.GLOWING_GREEN_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_BROWN_PIPE, CopperPipe.GLOWING_BROWN_PIPE);
		Registry.register(Registry.ITEM, GLOWING_BROWN_PIPE, new BlockItem(CopperPipe.GLOWING_BROWN_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_BLUE_PIPE, CopperPipe.GLOWING_BLUE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_BLUE_PIPE, new BlockItem(CopperPipe.GLOWING_BLUE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_PURPLE_PIPE, CopperPipe.GLOWING_PURPLE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_PURPLE_PIPE, new BlockItem(CopperPipe.GLOWING_PURPLE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_CYAN_PIPE, CopperPipe.GLOWING_CYAN_PIPE);
		Registry.register(Registry.ITEM, GLOWING_CYAN_PIPE, new BlockItem(CopperPipe.GLOWING_CYAN_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIGHT_GRAY_PIPE, CopperPipe.GLOWING_LIGHT_GRAY_PIPE);
		Registry.register(Registry.ITEM, GLOWING_LIGHT_GRAY_PIPE, new BlockItem(CopperPipe.GLOWING_LIGHT_GRAY_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_GRAY_PIPE, CopperPipe.GLOWING_GRAY_PIPE);
		Registry.register(Registry.ITEM, GLOWING_GRAY_PIPE, new BlockItem(CopperPipe.GLOWING_GRAY_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_PINK_PIPE, CopperPipe.GLOWING_PINK_PIPE);
		Registry.register(Registry.ITEM, GLOWING_PINK_PIPE, new BlockItem(CopperPipe.GLOWING_PINK_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIME_PIPE, CopperPipe.GLOWING_LIME_PIPE);
		Registry.register(Registry.ITEM, GLOWING_LIME_PIPE, new BlockItem(CopperPipe.GLOWING_LIME_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_YELLOW_PIPE, CopperPipe.GLOWING_YELLOW_PIPE);
		Registry.register(Registry.ITEM, GLOWING_YELLOW_PIPE, new BlockItem(CopperPipe.GLOWING_YELLOW_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIGHT_BLUE_PIPE, CopperPipe.GLOWING_LIGHT_BLUE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_LIGHT_BLUE_PIPE, new BlockItem(CopperPipe.GLOWING_LIGHT_BLUE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_MAGENTA_PIPE, CopperPipe.GLOWING_MAGENTA_PIPE);
		Registry.register(Registry.ITEM, GLOWING_MAGENTA_PIPE, new BlockItem(CopperPipe.GLOWING_MAGENTA_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_ORANGE_PIPE, CopperPipe.GLOWING_ORANGE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_ORANGE_PIPE, new BlockItem(CopperPipe.GLOWING_ORANGE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_WHITE_PIPE, CopperPipe.GLOWING_WHITE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_WHITE_PIPE, new BlockItem(CopperPipe.GLOWING_WHITE_PIPE, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		COPPER_PIPE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "lunade:copper_pipe", FabricBlockEntityTypeBuilder.create(CopperPipeEntity::new, CopperPipe.COPPER_PIPE, CopperPipe.EXPOSED_PIPE, CopperPipe.WEATHERED_PIPE, CopperPipe.OXIDIZED_PIPE, CopperPipe.WAXED_COPPER_PIPE, CopperPipe.WAXED_EXPOSED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE
		,CopperPipe.BLACK_PIPE, CopperPipe.RED_PIPE, CopperPipe.GREEN_PIPE, CopperPipe.BROWN_PIPE, CopperPipe.BLUE_PIPE, CopperPipe.PURPLE_PIPE, CopperPipe.CYAN_PIPE, CopperPipe.LIGHT_GRAY_PIPE
		,CopperPipe.GRAY_PIPE, CopperPipe.PINK_PIPE, CopperPipe.LIME_PIPE, CopperPipe.YELLOW_PIPE, CopperPipe.LIGHT_BLUE_PIPE, CopperPipe.MAGENTA_PIPE, CopperPipe.ORANGE_PIPE, CopperPipe.WHITE_PIPE
				,CopperPipe.GLOWING_BLACK_PIPE, CopperPipe.GLOWING_RED_PIPE, CopperPipe.GLOWING_GREEN_PIPE, CopperPipe.GLOWING_BROWN_PIPE, CopperPipe.GLOWING_BLUE_PIPE, CopperPipe.GLOWING_PURPLE_PIPE, CopperPipe.GLOWING_CYAN_PIPE, CopperPipe.GLOWING_LIGHT_GRAY_PIPE
				,CopperPipe.GLOWING_GRAY_PIPE, CopperPipe.GLOWING_PINK_PIPE, CopperPipe.GLOWING_LIME_PIPE, CopperPipe.GLOWING_YELLOW_PIPE, CopperPipe.GLOWING_LIGHT_BLUE_PIPE, CopperPipe.GLOWING_MAGENTA_PIPE, CopperPipe.GLOWING_ORANGE_PIPE, CopperPipe.GLOWING_WHITE_PIPE, CopperPipe.CORRODED_PIPE).build(null));

		//FITTINGS
		Registry.register(Registry.BLOCK, COPPER_FITTING, CopperFitting.COPPER_FITTING);
		Registry.register(Registry.ITEM, COPPER_FITTING, new BlockItem(CopperFitting.COPPER_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, EXPOSED_FITTING, CopperFitting.EXPOSED_FITTING);
		Registry.register(Registry.ITEM, EXPOSED_FITTING, new BlockItem(CopperFitting.EXPOSED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, WEATHERED_FITTING, CopperFitting.WEATHERED_FITTING);
		Registry.register(Registry.ITEM, WEATHERED_FITTING, new BlockItem(CopperFitting.WEATHERED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, OXIDIZED_FITTING, CopperFitting.OXIDIZED_FITTING);
		Registry.register(Registry.ITEM, OXIDIZED_FITTING, new BlockItem(CopperFitting.OXIDIZED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		//WAXED
		Registry.register(Registry.BLOCK, WAXED_COPPER_FITTING, CopperFitting.WAXED_COPPER_FITTING);
		Registry.register(Registry.ITEM, WAXED_COPPER_FITTING, new BlockItem(CopperFitting.WAXED_COPPER_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_EXPOSED_FITTING, CopperFitting.WAXED_EXPOSED_FITTING);
		Registry.register(Registry.ITEM, WAXED_EXPOSED_FITTING, new BlockItem(CopperFitting.WAXED_EXPOSED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_WEATHERED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING);
		Registry.register(Registry.ITEM, WAXED_WEATHERED_FITTING, new BlockItem(CopperFitting.WAXED_WEATHERED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_OXIDIZED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING);
		Registry.register(Registry.ITEM, WAXED_OXIDIZED_FITTING, new BlockItem(CopperFitting.WAXED_OXIDIZED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		Registry.register(Registry.BLOCK, CORRODED_FITTING, CopperFitting.CORRODED_FITTING);
		Registry.register(Registry.ITEM, CORRODED_FITTING, new BlockItem(CopperFitting.CORRODED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		//COLORED
		Registry.register(Registry.BLOCK, BLACK_FITTING, CopperFitting.BLACK_FITTING);
		Registry.register(Registry.ITEM, BLACK_FITTING, new BlockItem(CopperFitting.BLACK_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, RED_FITTING, CopperFitting.RED_FITTING);
		Registry.register(Registry.ITEM, RED_FITTING, new BlockItem(CopperFitting.RED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GREEN_FITTING, CopperFitting.GREEN_FITTING);
		Registry.register(Registry.ITEM, GREEN_FITTING, new BlockItem(CopperFitting.GREEN_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, BROWN_FITTING, CopperFitting.BROWN_FITTING);
		Registry.register(Registry.ITEM, BROWN_FITTING, new BlockItem(CopperFitting.BROWN_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, BLUE_FITTING, CopperFitting.BLUE_FITTING);
		Registry.register(Registry.ITEM, BLUE_FITTING, new BlockItem(CopperFitting.BLUE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, PURPLE_FITTING, CopperFitting.PURPLE_FITTING);
		Registry.register(Registry.ITEM, PURPLE_FITTING, new BlockItem(CopperFitting.PURPLE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, CYAN_FITTING, CopperFitting.CYAN_FITTING);
		Registry.register(Registry.ITEM, CYAN_FITTING, new BlockItem(CopperFitting.CYAN_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, LIGHT_GRAY_FITTING, CopperFitting.LIGHT_GRAY_FITTING);
		Registry.register(Registry.ITEM, LIGHT_GRAY_FITTING, new BlockItem(CopperFitting.LIGHT_GRAY_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GRAY_FITTING, CopperFitting.GRAY_FITTING);
		Registry.register(Registry.ITEM, GRAY_FITTING, new BlockItem(CopperFitting.GRAY_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, PINK_FITTING, CopperFitting.PINK_FITTING);
		Registry.register(Registry.ITEM, PINK_FITTING, new BlockItem(CopperFitting.PINK_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, LIME_FITTING, CopperFitting.LIME_FITTING);
		Registry.register(Registry.ITEM, LIME_FITTING, new BlockItem(CopperFitting.LIME_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, YELLOW_FITTING, CopperFitting.YELLOW_FITTING);
		Registry.register(Registry.ITEM, YELLOW_FITTING, new BlockItem(CopperFitting.YELLOW_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, LIGHT_BLUE_FITTING, CopperFitting.LIGHT_BLUE_FITTING);
		Registry.register(Registry.ITEM, LIGHT_BLUE_FITTING, new BlockItem(CopperFitting.LIGHT_BLUE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, MAGENTA_FITTING, CopperFitting.MAGENTA_FITTING);
		Registry.register(Registry.ITEM, MAGENTA_FITTING, new BlockItem(CopperFitting.MAGENTA_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, ORANGE_FITTING, CopperFitting.ORANGE_FITTING);
		Registry.register(Registry.ITEM, ORANGE_FITTING, new BlockItem(CopperFitting.ORANGE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, WHITE_FITTING, CopperFitting.WHITE_FITTING);
		Registry.register(Registry.ITEM, WHITE_FITTING, new BlockItem(CopperFitting.WHITE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		//GLOWING
		Registry.register(Registry.BLOCK, GLOWING_BLACK_FITTING, CopperFitting.GLOWING_BLACK_FITTING);
		Registry.register(Registry.ITEM, GLOWING_BLACK_FITTING, new BlockItem(CopperFitting.GLOWING_BLACK_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_RED_FITTING, CopperFitting.GLOWING_RED_FITTING);
		Registry.register(Registry.ITEM, GLOWING_RED_FITTING, new BlockItem(CopperFitting.GLOWING_RED_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_GREEN_FITTING, CopperFitting.GLOWING_GREEN_FITTING);
		Registry.register(Registry.ITEM, GLOWING_GREEN_FITTING, new BlockItem(CopperFitting.GLOWING_GREEN_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_BROWN_FITTING, CopperFitting.GLOWING_BROWN_FITTING);
		Registry.register(Registry.ITEM, GLOWING_BROWN_FITTING, new BlockItem(CopperFitting.GLOWING_BROWN_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_BLUE_FITTING, CopperFitting.GLOWING_BLUE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_BLUE_FITTING, new BlockItem(CopperFitting.GLOWING_BLUE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_PURPLE_FITTING, CopperFitting.GLOWING_PURPLE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_PURPLE_FITTING, new BlockItem(CopperFitting.GLOWING_PURPLE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_CYAN_FITTING, CopperFitting.GLOWING_CYAN_FITTING);
		Registry.register(Registry.ITEM, GLOWING_CYAN_FITTING, new BlockItem(CopperFitting.GLOWING_CYAN_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIGHT_GRAY_FITTING, CopperFitting.GLOWING_LIGHT_GRAY_FITTING);
		Registry.register(Registry.ITEM, GLOWING_LIGHT_GRAY_FITTING, new BlockItem(CopperFitting.GLOWING_LIGHT_GRAY_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_GRAY_FITTING, CopperFitting.GLOWING_GRAY_FITTING);
		Registry.register(Registry.ITEM, GLOWING_GRAY_FITTING, new BlockItem(CopperFitting.GLOWING_GRAY_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_PINK_FITTING, CopperFitting.GLOWING_PINK_FITTING);
		Registry.register(Registry.ITEM, GLOWING_PINK_FITTING, new BlockItem(CopperFitting.GLOWING_PINK_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIME_FITTING, CopperFitting.GLOWING_LIME_FITTING);
		Registry.register(Registry.ITEM, GLOWING_LIME_FITTING, new BlockItem(CopperFitting.GLOWING_LIME_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_YELLOW_FITTING, CopperFitting.GLOWING_YELLOW_FITTING);
		Registry.register(Registry.ITEM, GLOWING_YELLOW_FITTING, new BlockItem(CopperFitting.GLOWING_YELLOW_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIGHT_BLUE_FITTING, CopperFitting.GLOWING_LIGHT_BLUE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_LIGHT_BLUE_FITTING, new BlockItem(CopperFitting.GLOWING_LIGHT_BLUE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_MAGENTA_FITTING, CopperFitting.GLOWING_MAGENTA_FITTING);
		Registry.register(Registry.ITEM, GLOWING_MAGENTA_FITTING, new BlockItem(CopperFitting.GLOWING_MAGENTA_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_ORANGE_FITTING, CopperFitting.GLOWING_ORANGE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_ORANGE_FITTING, new BlockItem(CopperFitting.GLOWING_ORANGE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_WHITE_FITTING, CopperFitting.GLOWING_WHITE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_WHITE_FITTING, new BlockItem(CopperFitting.GLOWING_WHITE_FITTING, new FabricItemSettings().tab(CreativeModeTab.TAB_REDSTONE)));

		COPPER_FITTING_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "lunade:copper_fitting", FabricBlockEntityTypeBuilder.create(CopperFittingEntity::new, CopperFitting.COPPER_FITTING, CopperFitting.EXPOSED_FITTING, CopperFitting.WEATHERED_FITTING, CopperFitting.OXIDIZED_FITTING, CopperFitting.WAXED_COPPER_FITTING, CopperFitting.WAXED_EXPOSED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING
				,CopperFitting.BLACK_FITTING, CopperFitting.RED_FITTING, CopperFitting.GREEN_FITTING, CopperFitting.BROWN_FITTING, CopperFitting.BLUE_FITTING, CopperFitting.PURPLE_FITTING, CopperFitting.CYAN_FITTING, CopperFitting.LIGHT_GRAY_FITTING
				,CopperFitting.GRAY_FITTING, CopperFitting.PINK_FITTING, CopperFitting.LIME_FITTING, CopperFitting.YELLOW_FITTING, CopperFitting.LIGHT_BLUE_FITTING, CopperFitting.MAGENTA_FITTING, CopperFitting.ORANGE_FITTING, CopperFitting.WHITE_FITTING
				,CopperFitting.GLOWING_BLACK_FITTING, CopperFitting.GLOWING_RED_FITTING, CopperFitting.GLOWING_GREEN_FITTING, CopperFitting.GLOWING_BROWN_FITTING, CopperFitting.GLOWING_BLUE_FITTING, CopperFitting.GLOWING_PURPLE_FITTING, CopperFitting.GLOWING_CYAN_FITTING, CopperFitting.GLOWING_LIGHT_GRAY_FITTING
				,CopperFitting.GLOWING_GRAY_FITTING, CopperFitting.GLOWING_PINK_FITTING, CopperFitting.GLOWING_LIME_FITTING, CopperFitting.GLOWING_YELLOW_FITTING, CopperFitting.GLOWING_LIGHT_BLUE_FITTING, CopperFitting.GLOWING_MAGENTA_FITTING, CopperFitting.GLOWING_ORANGE_FITTING, CopperFitting.GLOWING_WHITE_FITTING, CopperFitting.CORRODED_FITTING).build(null));

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

	public static final Logger LOGGER = LoggerFactory.getLogger("COPPER_PIPES");

	public static Map<Object, Long> instantMap = new HashMap<>();

	public static void startMeasuring(Object object) {
		long started = System.nanoTime();
		String name = object.getClass().getName();
		LOGGER.error("Started measuring {}", name.substring(name.lastIndexOf(".") + 1));
		instantMap.put(object, started);
	}

	public static void stopMeasuring(Object object) {
		if (instantMap.containsKey(object)) {
			String name = object.getClass().getName();
			LOGGER.error("{} took {} nanoseconds", name.substring(name.lastIndexOf(".") + 1), System.nanoTime() - instantMap.get(object));
			instantMap.remove(object);
		}
	}

}

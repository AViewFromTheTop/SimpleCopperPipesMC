package net.lunade.copper;

import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main implements ModInitializer {

	public static final Identifier PIPE_INK_PACKET = new Identifier("lunade","seed_particle_packet");

	public static final Identifier INSPECT_PIPE = new Identifier("lunade", "inspect_copper_pipe");

	//COPPER PIPE
	public static final Identifier COPPER_PIPE = new Identifier("lunade", "copper_pipe");
	public static final Identifier EXPOSED_PIPE = new Identifier("lunade", "exposed_copper_pipe");
	public static final Identifier WEATHERED_PIPE = new Identifier("lunade", "weathered_copper_pipe");
	public static final Identifier OXIDIZED_PIPE = new Identifier("lunade", "oxidized_copper_pipe");

	public static final Identifier WAXED_COPPER_PIPE = new Identifier("lunade", "waxed_copper_pipe");
	public static final Identifier WAXED_EXPOSED_PIPE = new Identifier("lunade", "waxed_exposed_copper_pipe");
	public static final Identifier WAXED_WEATHERED_PIPE = new Identifier("lunade", "waxed_weathered_copper_pipe");
	public static final Identifier WAXED_OXIDIZED_PIPE = new Identifier("lunade", "waxed_oxidized_copper_pipe");

	public static final Identifier CORRODED_PIPE = new Identifier("lunade", "corroded_pipe");

	public static BlockEntityType<CopperPipeEntity> COPPER_PIPE_ENTITY;

	//COPPER FITTING
	public static final Identifier COPPER_FITTING = new Identifier("lunade", "copper_fitting");
	public static final Identifier EXPOSED_FITTING = new Identifier("lunade", "exposed_copper_fitting");
	public static final Identifier WEATHERED_FITTING = new Identifier("lunade", "weathered_copper_fitting");
	public static final Identifier OXIDIZED_FITTING = new Identifier("lunade", "oxidized_copper_fitting");

	public static final Identifier WAXED_COPPER_FITTING = new Identifier("lunade", "waxed_copper_fitting");
	public static final Identifier WAXED_EXPOSED_FITTING = new Identifier("lunade", "waxed_exposed_copper_fitting");
	public static final Identifier WAXED_WEATHERED_FITTING = new Identifier("lunade", "waxed_weathered_copper_fitting");
	public static final Identifier WAXED_OXIDIZED_FITTING = new Identifier("lunade", "waxed_oxidized_copper_fitting");

	public static final Identifier CORRODED_FITTING = new Identifier("lunade", "corroded_fitting");

	public static BlockEntityType<CopperFittingEntity> COPPER_FITTING_ENTITY;
	public static final TagKey<Block> BLOCK_LISTENERS = TagKey.of(Registry.BLOCK_KEY, new Identifier("lunade", "block_event_listeners"));
	public static final TagKey<Block> UNSCRAPEABLE = TagKey.of(Registry.BLOCK_KEY, new Identifier("lunade", "unscrapeable"));
	public static final TagKey<Block> WAXED = TagKey.of(Registry.BLOCK_KEY, new Identifier("lunade", "waxed"));
	public static final TagKey<Block> SILENT_PIPES = TagKey.of(Registry.BLOCK_KEY, new Identifier("lunade", "silent_pipes"));
	public static final TagKey<EntityType<?>> ENTITY_LISTENERS = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier("lunade", "entity_event_listeners"));

	//COLORED PIPE
	public static final Identifier BLACK_PIPE = new Identifier("lunade", "black_pipe");
	public static final Identifier RED_PIPE = new Identifier("lunade", "red_pipe");
	public static final Identifier GREEN_PIPE = new Identifier("lunade", "green_pipe");
	public static final Identifier BROWN_PIPE = new Identifier("lunade", "brown_pipe");
	public static final Identifier BLUE_PIPE = new Identifier("lunade", "blue_pipe");
	public static final Identifier PURPLE_PIPE = new Identifier("lunade", "purple_pipe");
	public static final Identifier CYAN_PIPE = new Identifier("lunade", "cyan_pipe");
	public static final Identifier LIGHT_GRAY_PIPE = new Identifier("lunade", "light_gray_pipe");
	public static final Identifier GRAY_PIPE = new Identifier("lunade", "gray_pipe");
	public static final Identifier PINK_PIPE = new Identifier("lunade", "pink_pipe");
	public static final Identifier LIME_PIPE = new Identifier("lunade", "lime_pipe");
	public static final Identifier YELLOW_PIPE = new Identifier("lunade", "yellow_pipe");
	public static final Identifier LIGHT_BLUE_PIPE = new Identifier("lunade", "light_blue_pipe");
	public static final Identifier MAGENTA_PIPE = new Identifier("lunade", "magenta_pipe");
	public static final Identifier ORANGE_PIPE = new Identifier("lunade", "orange_pipe");
	public static final Identifier WHITE_PIPE = new Identifier("lunade", "white_pipe");

	public static final Identifier GLOWING_BLACK_PIPE = new Identifier("lunade", "glowing_black_pipe");
	public static final Identifier GLOWING_RED_PIPE = new Identifier("lunade", "glowing_red_pipe");
	public static final Identifier GLOWING_GREEN_PIPE = new Identifier("lunade", "glowing_green_pipe");
	public static final Identifier GLOWING_BROWN_PIPE = new Identifier("lunade", "glowing_brown_pipe");
	public static final Identifier GLOWING_BLUE_PIPE = new Identifier("lunade", "glowing_blue_pipe");
	public static final Identifier GLOWING_PURPLE_PIPE = new Identifier("lunade", "glowing_purple_pipe");
	public static final Identifier GLOWING_CYAN_PIPE = new Identifier("lunade", "glowing_cyan_pipe");
	public static final Identifier GLOWING_LIGHT_GRAY_PIPE = new Identifier("lunade", "glowing_light_gray_pipe");
	public static final Identifier GLOWING_GRAY_PIPE = new Identifier("lunade", "glowing_gray_pipe");
	public static final Identifier GLOWING_PINK_PIPE = new Identifier("lunade", "glowing_pink_pipe");
	public static final Identifier GLOWING_LIME_PIPE = new Identifier("lunade", "glowing_lime_pipe");
	public static final Identifier GLOWING_YELLOW_PIPE = new Identifier("lunade", "glowing_yellow_pipe");
	public static final Identifier GLOWING_LIGHT_BLUE_PIPE = new Identifier("lunade", "glowing_light_blue_pipe");
	public static final Identifier GLOWING_MAGENTA_PIPE = new Identifier("lunade", "glowing_magenta_pipe");
	public static final Identifier GLOWING_ORANGE_PIPE = new Identifier("lunade", "glowing_orange_pipe");
	public static final Identifier GLOWING_WHITE_PIPE = new Identifier("lunade", "glowing_white_pipe");

	//COLORED FITTING
	public static final Identifier BLACK_FITTING = new Identifier("lunade", "black_fitting");
	public static final Identifier RED_FITTING = new Identifier("lunade", "red_fitting");
	public static final Identifier GREEN_FITTING = new Identifier("lunade", "green_fitting");
	public static final Identifier BROWN_FITTING = new Identifier("lunade", "brown_fitting");
	public static final Identifier BLUE_FITTING = new Identifier("lunade", "blue_fitting");
	public static final Identifier PURPLE_FITTING = new Identifier("lunade", "purple_fitting");
	public static final Identifier CYAN_FITTING = new Identifier("lunade", "cyan_fitting");
	public static final Identifier LIGHT_GRAY_FITTING = new Identifier("lunade", "light_gray_fitting");
	public static final Identifier GRAY_FITTING = new Identifier("lunade", "gray_fitting");
	public static final Identifier PINK_FITTING = new Identifier("lunade", "pink_fitting");
	public static final Identifier LIME_FITTING = new Identifier("lunade", "lime_fitting");
	public static final Identifier YELLOW_FITTING = new Identifier("lunade", "yellow_fitting");
	public static final Identifier LIGHT_BLUE_FITTING = new Identifier("lunade", "light_blue_fitting");
	public static final Identifier MAGENTA_FITTING = new Identifier("lunade", "magenta_fitting");
	public static final Identifier ORANGE_FITTING = new Identifier("lunade", "orange_fitting");
	public static final Identifier WHITE_FITTING = new Identifier("lunade", "white_fitting");

	public static final Identifier GLOWING_BLACK_FITTING = new Identifier("lunade", "glowing_black_fitting");
	public static final Identifier GLOWING_RED_FITTING = new Identifier("lunade", "glowing_red_fitting");
	public static final Identifier GLOWING_GREEN_FITTING = new Identifier("lunade", "glowing_green_fitting");
	public static final Identifier GLOWING_BROWN_FITTING = new Identifier("lunade", "glowing_brown_fitting");
	public static final Identifier GLOWING_BLUE_FITTING = new Identifier("lunade", "glowing_blue_fitting");
	public static final Identifier GLOWING_PURPLE_FITTING = new Identifier("lunade", "glowing_purple_fitting");
	public static final Identifier GLOWING_CYAN_FITTING = new Identifier("lunade", "glowing_cyan_fitting");
	public static final Identifier GLOWING_LIGHT_GRAY_FITTING = new Identifier("lunade", "glowing_light_gray_fitting");
	public static final Identifier GLOWING_GRAY_FITTING = new Identifier("lunade", "glowing_gray_fitting");
	public static final Identifier GLOWING_PINK_FITTING = new Identifier("lunade", "glowing_pink_fitting");
	public static final Identifier GLOWING_LIME_FITTING = new Identifier("lunade", "glowing_lime_fitting");
	public static final Identifier GLOWING_YELLOW_FITTING = new Identifier("lunade", "glowing_yellow_fitting");
	public static final Identifier GLOWING_LIGHT_BLUE_FITTING = new Identifier("lunade", "glowing_light_blue_fitting");
	public static final Identifier GLOWING_MAGENTA_FITTING = new Identifier("lunade", "glowing_magenta_fitting");
	public static final Identifier GLOWING_ORANGE_FITTING = new Identifier("lunade", "glowing_orange_fitting");
	public static final Identifier GLOWING_WHITE_FITTING = new Identifier("lunade", "glowing_white_fitting");

	//SOUNDS
	public static final SoundEvent ITEM_IN = new SoundEvent(new Identifier("lunade", "block.copper_pipe.item_in"));
	public static final SoundEvent ITEM_OUT = new SoundEvent(new Identifier("lunade", "block.copper_pipe.item_out"));
	public static final SoundEvent LAUNCH = new SoundEvent(new Identifier("lunade", "block.copper_pipe.launch"));
	public static final SoundEvent TURN = new SoundEvent(new Identifier("lunade", "block.copper_pipe.turn"));

	public static final SoundEvent CORRODED_COPPER_PLACE = new SoundEvent(new Identifier("lunade", "block.corroded_copper.place"));
	public static final SoundEvent CORRODED_COPPER_STEP = new SoundEvent(new Identifier("lunade", "block.corroded_copper.step"));
	public static final SoundEvent CORRODED_COPPER_BREAK = new SoundEvent(new Identifier("lunade", "block.corroded_copper.break"));
	public static final SoundEvent CORRODED_COPPER_FALL = new SoundEvent(new Identifier("lunade", "block.corroded_copper.fall"));
	public static final SoundEvent CORRODED_COPPER_HIT = new SoundEvent(new Identifier("lunade", "block.corroded_copper.hit"));

	//NOTE BLOCK
	public static final Identifier NOTE_PACKET = new Identifier("lunade","note_packet");

	//PIPE INK PARTICLES
	public static final DefaultParticleType RED_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType GREEN_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType BROWN_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType BLUE_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType PURPLE_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType CYAN_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType LIGHT_GRAY_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType GRAY_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType PINK_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType LIME_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType YELLOW_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType LIGHT_BLUE_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType MAGENTA_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType ORANGE_INK = FabricParticleTypes.simple();
	public static final DefaultParticleType WHITE_INK = FabricParticleTypes.simple();

	public static final GameEvent NOTE_BLOCK_PLAY = new GameEvent("lunade_note_block_play", 16);

	@Override
	public void onInitialize() {
		CopperPipeProperties.init();

		Registry.register(Registry.CUSTOM_STAT, INSPECT_PIPE, INSPECT_PIPE);
		Stats.CUSTOM.getOrCreateStat(INSPECT_PIPE, StatFormatter.DEFAULT);

		//PARTICLE
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "red_ink"), RED_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "green_ink"), GREEN_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "brown_ink"), BROWN_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "blue_ink"), BLUE_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "purple_ink"), PURPLE_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "cyan_ink"), CYAN_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "light_gray_ink"), LIGHT_GRAY_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "gray_ink"), GRAY_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "pink_ink"), PINK_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "lime_ink"), LIME_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "yellow_ink"), YELLOW_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "light_blue_ink"), LIGHT_BLUE_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "magenta_ink"), MAGENTA_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "orange_ink"), ORANGE_INK);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier("lunade", "white_ink"), WHITE_INK);

		//PIPE
		Registry.register(Registry.BLOCK, COPPER_PIPE, CopperPipe.COPPER_PIPE);
		Registry.register(Registry.ITEM, COPPER_PIPE, new BlockItem(CopperPipe.COPPER_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, EXPOSED_PIPE, CopperPipe.EXPOSED_PIPE);
		Registry.register(Registry.ITEM, EXPOSED_PIPE, new BlockItem(CopperPipe.EXPOSED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, WEATHERED_PIPE, CopperPipe.WEATHERED_PIPE);
		Registry.register(Registry.ITEM, WEATHERED_PIPE, new BlockItem(CopperPipe.WEATHERED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, OXIDIZED_PIPE, CopperPipe.OXIDIZED_PIPE);
		Registry.register(Registry.ITEM, OXIDIZED_PIPE, new BlockItem(CopperPipe.OXIDIZED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		//WAXED
		Registry.register(Registry.BLOCK, WAXED_COPPER_PIPE, CopperPipe.WAXED_COPPER_PIPE);
		Registry.register(Registry.ITEM, WAXED_COPPER_PIPE, new BlockItem(CopperPipe.WAXED_COPPER_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_EXPOSED_PIPE, CopperPipe.WAXED_EXPOSED_PIPE);
		Registry.register(Registry.ITEM, WAXED_EXPOSED_PIPE, new BlockItem(CopperPipe.WAXED_EXPOSED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_WEATHERED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE);
		Registry.register(Registry.ITEM, WAXED_WEATHERED_PIPE, new BlockItem(CopperPipe.WAXED_WEATHERED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_OXIDIZED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE);
		Registry.register(Registry.ITEM, WAXED_OXIDIZED_PIPE, new BlockItem(CopperPipe.WAXED_OXIDIZED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, CORRODED_PIPE, CopperPipe.CORRODED_PIPE);
		Registry.register(Registry.ITEM, CORRODED_PIPE, new BlockItem(CopperPipe.CORRODED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		//COLORED
		Registry.register(Registry.BLOCK, BLACK_PIPE, CopperPipe.BLACK_PIPE);
		Registry.register(Registry.ITEM, BLACK_PIPE, new BlockItem(CopperPipe.BLACK_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, RED_PIPE, CopperPipe.RED_PIPE);
		Registry.register(Registry.ITEM, RED_PIPE, new BlockItem(CopperPipe.RED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GREEN_PIPE, CopperPipe.GREEN_PIPE);
		Registry.register(Registry.ITEM, GREEN_PIPE, new BlockItem(CopperPipe.GREEN_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, BROWN_PIPE, CopperPipe.BROWN_PIPE);
		Registry.register(Registry.ITEM, BROWN_PIPE, new BlockItem(CopperPipe.BROWN_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, BLUE_PIPE, CopperPipe.BLUE_PIPE);
		Registry.register(Registry.ITEM, BLUE_PIPE, new BlockItem(CopperPipe.BLUE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, PURPLE_PIPE, CopperPipe.PURPLE_PIPE);
		Registry.register(Registry.ITEM, PURPLE_PIPE, new BlockItem(CopperPipe.PURPLE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, CYAN_PIPE, CopperPipe.CYAN_PIPE);
		Registry.register(Registry.ITEM, CYAN_PIPE, new BlockItem(CopperPipe.CYAN_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, LIGHT_GRAY_PIPE, CopperPipe.LIGHT_GRAY_PIPE);
		Registry.register(Registry.ITEM, LIGHT_GRAY_PIPE, new BlockItem(CopperPipe.LIGHT_GRAY_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GRAY_PIPE, CopperPipe.GRAY_PIPE);
		Registry.register(Registry.ITEM, GRAY_PIPE, new BlockItem(CopperPipe.GRAY_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, PINK_PIPE, CopperPipe.PINK_PIPE);
		Registry.register(Registry.ITEM, PINK_PIPE, new BlockItem(CopperPipe.PINK_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, LIME_PIPE, CopperPipe.LIME_PIPE);
		Registry.register(Registry.ITEM, LIME_PIPE, new BlockItem(CopperPipe.LIME_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, YELLOW_PIPE, CopperPipe.YELLOW_PIPE);
		Registry.register(Registry.ITEM, YELLOW_PIPE, new BlockItem(CopperPipe.YELLOW_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, LIGHT_BLUE_PIPE, CopperPipe.LIGHT_BLUE_PIPE);
		Registry.register(Registry.ITEM, LIGHT_BLUE_PIPE, new BlockItem(CopperPipe.LIGHT_BLUE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, MAGENTA_PIPE, CopperPipe.MAGENTA_PIPE);
		Registry.register(Registry.ITEM, MAGENTA_PIPE, new BlockItem(CopperPipe.MAGENTA_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, ORANGE_PIPE, CopperPipe.ORANGE_PIPE);
		Registry.register(Registry.ITEM, ORANGE_PIPE, new BlockItem(CopperPipe.ORANGE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, WHITE_PIPE, CopperPipe.WHITE_PIPE);
		Registry.register(Registry.ITEM, WHITE_PIPE, new BlockItem(CopperPipe.WHITE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		//GLOWING
		Registry.register(Registry.BLOCK, GLOWING_BLACK_PIPE, CopperPipe.GLOWING_BLACK_PIPE);
		Registry.register(Registry.ITEM, GLOWING_BLACK_PIPE, new BlockItem(CopperPipe.GLOWING_BLACK_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_RED_PIPE, CopperPipe.GLOWING_RED_PIPE);
		Registry.register(Registry.ITEM, GLOWING_RED_PIPE, new BlockItem(CopperPipe.GLOWING_RED_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_GREEN_PIPE, CopperPipe.GLOWING_GREEN_PIPE);
		Registry.register(Registry.ITEM, GLOWING_GREEN_PIPE, new BlockItem(CopperPipe.GLOWING_GREEN_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_BROWN_PIPE, CopperPipe.GLOWING_BROWN_PIPE);
		Registry.register(Registry.ITEM, GLOWING_BROWN_PIPE, new BlockItem(CopperPipe.GLOWING_BROWN_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_BLUE_PIPE, CopperPipe.GLOWING_BLUE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_BLUE_PIPE, new BlockItem(CopperPipe.GLOWING_BLUE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_PURPLE_PIPE, CopperPipe.GLOWING_PURPLE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_PURPLE_PIPE, new BlockItem(CopperPipe.GLOWING_PURPLE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_CYAN_PIPE, CopperPipe.GLOWING_CYAN_PIPE);
		Registry.register(Registry.ITEM, GLOWING_CYAN_PIPE, new BlockItem(CopperPipe.GLOWING_CYAN_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIGHT_GRAY_PIPE, CopperPipe.GLOWING_LIGHT_GRAY_PIPE);
		Registry.register(Registry.ITEM, GLOWING_LIGHT_GRAY_PIPE, new BlockItem(CopperPipe.GLOWING_LIGHT_GRAY_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_GRAY_PIPE, CopperPipe.GLOWING_GRAY_PIPE);
		Registry.register(Registry.ITEM, GLOWING_GRAY_PIPE, new BlockItem(CopperPipe.GLOWING_GRAY_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_PINK_PIPE, CopperPipe.GLOWING_PINK_PIPE);
		Registry.register(Registry.ITEM, GLOWING_PINK_PIPE, new BlockItem(CopperPipe.GLOWING_PINK_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIME_PIPE, CopperPipe.GLOWING_LIME_PIPE);
		Registry.register(Registry.ITEM, GLOWING_LIME_PIPE, new BlockItem(CopperPipe.GLOWING_LIME_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_YELLOW_PIPE, CopperPipe.GLOWING_YELLOW_PIPE);
		Registry.register(Registry.ITEM, GLOWING_YELLOW_PIPE, new BlockItem(CopperPipe.GLOWING_YELLOW_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIGHT_BLUE_PIPE, CopperPipe.GLOWING_LIGHT_BLUE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_LIGHT_BLUE_PIPE, new BlockItem(CopperPipe.GLOWING_LIGHT_BLUE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_MAGENTA_PIPE, CopperPipe.GLOWING_MAGENTA_PIPE);
		Registry.register(Registry.ITEM, GLOWING_MAGENTA_PIPE, new BlockItem(CopperPipe.GLOWING_MAGENTA_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_ORANGE_PIPE, CopperPipe.GLOWING_ORANGE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_ORANGE_PIPE, new BlockItem(CopperPipe.GLOWING_ORANGE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_WHITE_PIPE, CopperPipe.GLOWING_WHITE_PIPE);
		Registry.register(Registry.ITEM, GLOWING_WHITE_PIPE, new BlockItem(CopperPipe.GLOWING_WHITE_PIPE, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		COPPER_PIPE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "lunade:copper_pipe", FabricBlockEntityTypeBuilder.create(CopperPipeEntity::new, CopperPipe.COPPER_PIPE, CopperPipe.EXPOSED_PIPE, CopperPipe.WEATHERED_PIPE, CopperPipe.OXIDIZED_PIPE, CopperPipe.WAXED_COPPER_PIPE, CopperPipe.WAXED_EXPOSED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE
				,CopperPipe.BLACK_PIPE, CopperPipe.RED_PIPE, CopperPipe.GREEN_PIPE, CopperPipe.BROWN_PIPE, CopperPipe.BLUE_PIPE, CopperPipe.PURPLE_PIPE, CopperPipe.CYAN_PIPE, CopperPipe.LIGHT_GRAY_PIPE
				,CopperPipe.GRAY_PIPE, CopperPipe.PINK_PIPE, CopperPipe.LIME_PIPE, CopperPipe.YELLOW_PIPE, CopperPipe.LIGHT_BLUE_PIPE, CopperPipe.MAGENTA_PIPE, CopperPipe.ORANGE_PIPE, CopperPipe.WHITE_PIPE
				,CopperPipe.GLOWING_BLACK_PIPE, CopperPipe.GLOWING_RED_PIPE, CopperPipe.GLOWING_GREEN_PIPE, CopperPipe.GLOWING_BROWN_PIPE, CopperPipe.GLOWING_BLUE_PIPE, CopperPipe.GLOWING_PURPLE_PIPE, CopperPipe.GLOWING_CYAN_PIPE, CopperPipe.GLOWING_LIGHT_GRAY_PIPE
				,CopperPipe.GLOWING_GRAY_PIPE, CopperPipe.GLOWING_PINK_PIPE, CopperPipe.GLOWING_LIME_PIPE, CopperPipe.GLOWING_YELLOW_PIPE, CopperPipe.GLOWING_LIGHT_BLUE_PIPE, CopperPipe.GLOWING_MAGENTA_PIPE, CopperPipe.GLOWING_ORANGE_PIPE, CopperPipe.GLOWING_WHITE_PIPE, CopperPipe.CORRODED_PIPE).build(null));

		//FITTINGS
		Registry.register(Registry.BLOCK, COPPER_FITTING, CopperFitting.COPPER_FITTING);
		Registry.register(Registry.ITEM, COPPER_FITTING, new BlockItem(CopperFitting.COPPER_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, EXPOSED_FITTING, CopperFitting.EXPOSED_FITTING);
		Registry.register(Registry.ITEM, EXPOSED_FITTING, new BlockItem(CopperFitting.EXPOSED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, WEATHERED_FITTING, CopperFitting.WEATHERED_FITTING);
		Registry.register(Registry.ITEM, WEATHERED_FITTING, new BlockItem(CopperFitting.WEATHERED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, OXIDIZED_FITTING, CopperFitting.OXIDIZED_FITTING);
		Registry.register(Registry.ITEM, OXIDIZED_FITTING, new BlockItem(CopperFitting.OXIDIZED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		//WAXED
		Registry.register(Registry.BLOCK, WAXED_COPPER_FITTING, CopperFitting.WAXED_COPPER_FITTING);
		Registry.register(Registry.ITEM, WAXED_COPPER_FITTING, new BlockItem(CopperFitting.WAXED_COPPER_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_EXPOSED_FITTING, CopperFitting.WAXED_EXPOSED_FITTING);
		Registry.register(Registry.ITEM, WAXED_EXPOSED_FITTING, new BlockItem(CopperFitting.WAXED_EXPOSED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_WEATHERED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING);
		Registry.register(Registry.ITEM, WAXED_WEATHERED_FITTING, new BlockItem(CopperFitting.WAXED_WEATHERED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, WAXED_OXIDIZED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING);
		Registry.register(Registry.ITEM, WAXED_OXIDIZED_FITTING, new BlockItem(CopperFitting.WAXED_OXIDIZED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK, CORRODED_FITTING, CopperFitting.CORRODED_FITTING);
		Registry.register(Registry.ITEM, CORRODED_FITTING, new BlockItem(CopperFitting.CORRODED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		//COLORED
		Registry.register(Registry.BLOCK, BLACK_FITTING, CopperFitting.BLACK_FITTING);
		Registry.register(Registry.ITEM, BLACK_FITTING, new BlockItem(CopperFitting.BLACK_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, RED_FITTING, CopperFitting.RED_FITTING);
		Registry.register(Registry.ITEM, RED_FITTING, new BlockItem(CopperFitting.RED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GREEN_FITTING, CopperFitting.GREEN_FITTING);
		Registry.register(Registry.ITEM, GREEN_FITTING, new BlockItem(CopperFitting.GREEN_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, BROWN_FITTING, CopperFitting.BROWN_FITTING);
		Registry.register(Registry.ITEM, BROWN_FITTING, new BlockItem(CopperFitting.BROWN_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, BLUE_FITTING, CopperFitting.BLUE_FITTING);
		Registry.register(Registry.ITEM, BLUE_FITTING, new BlockItem(CopperFitting.BLUE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, PURPLE_FITTING, CopperFitting.PURPLE_FITTING);
		Registry.register(Registry.ITEM, PURPLE_FITTING, new BlockItem(CopperFitting.PURPLE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, CYAN_FITTING, CopperFitting.CYAN_FITTING);
		Registry.register(Registry.ITEM, CYAN_FITTING, new BlockItem(CopperFitting.CYAN_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, LIGHT_GRAY_FITTING, CopperFitting.LIGHT_GRAY_FITTING);
		Registry.register(Registry.ITEM, LIGHT_GRAY_FITTING, new BlockItem(CopperFitting.LIGHT_GRAY_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GRAY_FITTING, CopperFitting.GRAY_FITTING);
		Registry.register(Registry.ITEM, GRAY_FITTING, new BlockItem(CopperFitting.GRAY_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, PINK_FITTING, CopperFitting.PINK_FITTING);
		Registry.register(Registry.ITEM, PINK_FITTING, new BlockItem(CopperFitting.PINK_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, LIME_FITTING, CopperFitting.LIME_FITTING);
		Registry.register(Registry.ITEM, LIME_FITTING, new BlockItem(CopperFitting.LIME_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, YELLOW_FITTING, CopperFitting.YELLOW_FITTING);
		Registry.register(Registry.ITEM, YELLOW_FITTING, new BlockItem(CopperFitting.YELLOW_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, LIGHT_BLUE_FITTING, CopperFitting.LIGHT_BLUE_FITTING);
		Registry.register(Registry.ITEM, LIGHT_BLUE_FITTING, new BlockItem(CopperFitting.LIGHT_BLUE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, MAGENTA_FITTING, CopperFitting.MAGENTA_FITTING);
		Registry.register(Registry.ITEM, MAGENTA_FITTING, new BlockItem(CopperFitting.MAGENTA_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, ORANGE_FITTING, CopperFitting.ORANGE_FITTING);
		Registry.register(Registry.ITEM, ORANGE_FITTING, new BlockItem(CopperFitting.ORANGE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, WHITE_FITTING, CopperFitting.WHITE_FITTING);
		Registry.register(Registry.ITEM, WHITE_FITTING, new BlockItem(CopperFitting.WHITE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		//GLOWING
		Registry.register(Registry.BLOCK, GLOWING_BLACK_FITTING, CopperFitting.GLOWING_BLACK_FITTING);
		Registry.register(Registry.ITEM, GLOWING_BLACK_FITTING, new BlockItem(CopperFitting.GLOWING_BLACK_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_RED_FITTING, CopperFitting.GLOWING_RED_FITTING);
		Registry.register(Registry.ITEM, GLOWING_RED_FITTING, new BlockItem(CopperFitting.GLOWING_RED_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_GREEN_FITTING, CopperFitting.GLOWING_GREEN_FITTING);
		Registry.register(Registry.ITEM, GLOWING_GREEN_FITTING, new BlockItem(CopperFitting.GLOWING_GREEN_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_BROWN_FITTING, CopperFitting.GLOWING_BROWN_FITTING);
		Registry.register(Registry.ITEM, GLOWING_BROWN_FITTING, new BlockItem(CopperFitting.GLOWING_BROWN_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_BLUE_FITTING, CopperFitting.GLOWING_BLUE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_BLUE_FITTING, new BlockItem(CopperFitting.GLOWING_BLUE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_PURPLE_FITTING, CopperFitting.GLOWING_PURPLE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_PURPLE_FITTING, new BlockItem(CopperFitting.GLOWING_PURPLE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_CYAN_FITTING, CopperFitting.GLOWING_CYAN_FITTING);
		Registry.register(Registry.ITEM, GLOWING_CYAN_FITTING, new BlockItem(CopperFitting.GLOWING_CYAN_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIGHT_GRAY_FITTING, CopperFitting.GLOWING_LIGHT_GRAY_FITTING);
		Registry.register(Registry.ITEM, GLOWING_LIGHT_GRAY_FITTING, new BlockItem(CopperFitting.GLOWING_LIGHT_GRAY_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_GRAY_FITTING, CopperFitting.GLOWING_GRAY_FITTING);
		Registry.register(Registry.ITEM, GLOWING_GRAY_FITTING, new BlockItem(CopperFitting.GLOWING_GRAY_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_PINK_FITTING, CopperFitting.GLOWING_PINK_FITTING);
		Registry.register(Registry.ITEM, GLOWING_PINK_FITTING, new BlockItem(CopperFitting.GLOWING_PINK_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIME_FITTING, CopperFitting.GLOWING_LIME_FITTING);
		Registry.register(Registry.ITEM, GLOWING_LIME_FITTING, new BlockItem(CopperFitting.GLOWING_LIME_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_YELLOW_FITTING, CopperFitting.GLOWING_YELLOW_FITTING);
		Registry.register(Registry.ITEM, GLOWING_YELLOW_FITTING, new BlockItem(CopperFitting.GLOWING_YELLOW_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_LIGHT_BLUE_FITTING, CopperFitting.GLOWING_LIGHT_BLUE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_LIGHT_BLUE_FITTING, new BlockItem(CopperFitting.GLOWING_LIGHT_BLUE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_MAGENTA_FITTING, CopperFitting.GLOWING_MAGENTA_FITTING);
		Registry.register(Registry.ITEM, GLOWING_MAGENTA_FITTING, new BlockItem(CopperFitting.GLOWING_MAGENTA_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_ORANGE_FITTING, CopperFitting.GLOWING_ORANGE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_ORANGE_FITTING, new BlockItem(CopperFitting.GLOWING_ORANGE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.BLOCK, GLOWING_WHITE_FITTING, CopperFitting.GLOWING_WHITE_FITTING);
		Registry.register(Registry.ITEM, GLOWING_WHITE_FITTING, new BlockItem(CopperFitting.GLOWING_WHITE_FITTING, new FabricItemSettings().group(ItemGroup.REDSTONE)));

		COPPER_FITTING_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "lunade:copper_fitting", FabricBlockEntityTypeBuilder.create(CopperFittingEntity::new, CopperFitting.COPPER_FITTING, CopperFitting.EXPOSED_FITTING, CopperFitting.WEATHERED_FITTING, CopperFitting.OXIDIZED_FITTING, CopperFitting.WAXED_COPPER_FITTING, CopperFitting.WAXED_EXPOSED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING
				,CopperFitting.BLACK_FITTING, CopperFitting.RED_FITTING, CopperFitting.GREEN_FITTING, CopperFitting.BROWN_FITTING, CopperFitting.BLUE_FITTING, CopperFitting.PURPLE_FITTING, CopperFitting.CYAN_FITTING, CopperFitting.LIGHT_GRAY_FITTING
				,CopperFitting.GRAY_FITTING, CopperFitting.PINK_FITTING, CopperFitting.LIME_FITTING, CopperFitting.YELLOW_FITTING, CopperFitting.LIGHT_BLUE_FITTING, CopperFitting.MAGENTA_FITTING, CopperFitting.ORANGE_FITTING, CopperFitting.WHITE_FITTING
				,CopperFitting.GLOWING_BLACK_FITTING, CopperFitting.GLOWING_RED_FITTING, CopperFitting.GLOWING_GREEN_FITTING, CopperFitting.GLOWING_BROWN_FITTING, CopperFitting.GLOWING_BLUE_FITTING, CopperFitting.GLOWING_PURPLE_FITTING, CopperFitting.GLOWING_CYAN_FITTING, CopperFitting.GLOWING_LIGHT_GRAY_FITTING
				,CopperFitting.GLOWING_GRAY_FITTING, CopperFitting.GLOWING_PINK_FITTING, CopperFitting.GLOWING_LIME_FITTING, CopperFitting.GLOWING_YELLOW_FITTING, CopperFitting.GLOWING_LIGHT_BLUE_FITTING, CopperFitting.GLOWING_MAGENTA_FITTING, CopperFitting.GLOWING_ORANGE_FITTING, CopperFitting.GLOWING_WHITE_FITTING, CopperFitting.CORRODED_FITTING).build(null));

		//SOUND
		Registry.register(Registry.SOUND_EVENT, ITEM_IN.getId(), ITEM_IN);
		Registry.register(Registry.SOUND_EVENT, ITEM_OUT.getId(), ITEM_OUT);
		Registry.register(Registry.SOUND_EVENT, LAUNCH.getId(), LAUNCH);
		Registry.register(Registry.SOUND_EVENT, TURN.getId(), TURN);

		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_PLACE.getId(), CORRODED_COPPER_PLACE);
		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_STEP.getId(), CORRODED_COPPER_STEP);
		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_BREAK.getId(), CORRODED_COPPER_BREAK);
		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_FALL.getId(), CORRODED_COPPER_FALL);
		Registry.register(Registry.SOUND_EVENT, CORRODED_COPPER_HIT.getId(), CORRODED_COPPER_HIT);

		Registry.register(Registry.GAME_EVENT, new Identifier("lunade", "note_block_play"), NOTE_BLOCK_PLAY);

		RegisterPipeNbtMethods.init();
	}

	public static List<Direction> shuffledDirections(Random random) {
		ArrayList<Direction> chosenDirs = new ArrayList<>();
		ArrayList<Direction> dirs = new ArrayList<>() {{ addAll(List.of(Direction.values())); }};
		while (!dirs.isEmpty()) {
			Direction direction = Util.getRandom(dirs, random);
			chosenDirs.add(direction);
			dirs.remove(direction);
		}
		return chosenDirs;
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

}
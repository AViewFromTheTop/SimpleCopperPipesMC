package net.lunade.copper.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

// block entities need to be in here because class initialization is weird
// you know what im saying?
public final class RegisterCopperBlockEntities {
    private RegisterCopperBlockEntities() {
    }

    public static final BlockEntityType<CopperPipeEntity> COPPER_PIPE_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "lunade:copper_pipe", FabricBlockEntityTypeBuilder.create(CopperPipeEntity::new, CopperPipe.COPPER_PIPE, CopperPipe.EXPOSED_PIPE, CopperPipe.WEATHERED_PIPE, CopperPipe.OXIDIZED_PIPE, CopperPipe.WAXED_COPPER_PIPE, CopperPipe.WAXED_EXPOSED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE
            ,CopperPipe.BLACK_PIPE, CopperPipe.RED_PIPE, CopperPipe.GREEN_PIPE, CopperPipe.BROWN_PIPE, CopperPipe.BLUE_PIPE, CopperPipe.PURPLE_PIPE, CopperPipe.CYAN_PIPE, CopperPipe.LIGHT_GRAY_PIPE
            ,CopperPipe.GRAY_PIPE, CopperPipe.PINK_PIPE, CopperPipe.LIME_PIPE, CopperPipe.YELLOW_PIPE, CopperPipe.LIGHT_BLUE_PIPE, CopperPipe.MAGENTA_PIPE, CopperPipe.ORANGE_PIPE, CopperPipe.WHITE_PIPE
            ,CopperPipe.GLOWING_BLACK_PIPE, CopperPipe.GLOWING_RED_PIPE, CopperPipe.GLOWING_GREEN_PIPE, CopperPipe.GLOWING_BROWN_PIPE, CopperPipe.GLOWING_BLUE_PIPE, CopperPipe.GLOWING_PURPLE_PIPE, CopperPipe.GLOWING_CYAN_PIPE, CopperPipe.GLOWING_LIGHT_GRAY_PIPE
            ,CopperPipe.GLOWING_GRAY_PIPE, CopperPipe.GLOWING_PINK_PIPE, CopperPipe.GLOWING_LIME_PIPE, CopperPipe.GLOWING_YELLOW_PIPE, CopperPipe.GLOWING_LIGHT_BLUE_PIPE, CopperPipe.GLOWING_MAGENTA_PIPE, CopperPipe.GLOWING_ORANGE_PIPE, CopperPipe.GLOWING_WHITE_PIPE, CopperPipe.CORRODED_PIPE).build(null)
    );

    public static final BlockEntityType<CopperFittingEntity> COPPER_FITTING_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "lunade:copper_fitting", FabricBlockEntityTypeBuilder.create(CopperFittingEntity::new, CopperFitting.COPPER_FITTING, CopperFitting.EXPOSED_FITTING, CopperFitting.WEATHERED_FITTING, CopperFitting.OXIDIZED_FITTING, CopperFitting.WAXED_COPPER_FITTING, CopperFitting.WAXED_EXPOSED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING
            ,CopperFitting.BLACK_FITTING, CopperFitting.RED_FITTING, CopperFitting.GREEN_FITTING, CopperFitting.BROWN_FITTING, CopperFitting.BLUE_FITTING, CopperFitting.PURPLE_FITTING, CopperFitting.CYAN_FITTING, CopperFitting.LIGHT_GRAY_FITTING
            ,CopperFitting.GRAY_FITTING, CopperFitting.PINK_FITTING, CopperFitting.LIME_FITTING, CopperFitting.YELLOW_FITTING, CopperFitting.LIGHT_BLUE_FITTING, CopperFitting.MAGENTA_FITTING, CopperFitting.ORANGE_FITTING, CopperFitting.WHITE_FITTING
            ,CopperFitting.GLOWING_BLACK_FITTING, CopperFitting.GLOWING_RED_FITTING, CopperFitting.GLOWING_GREEN_FITTING, CopperFitting.GLOWING_BROWN_FITTING, CopperFitting.GLOWING_BLUE_FITTING, CopperFitting.GLOWING_PURPLE_FITTING, CopperFitting.GLOWING_CYAN_FITTING, CopperFitting.GLOWING_LIGHT_GRAY_FITTING
            ,CopperFitting.GLOWING_GRAY_FITTING, CopperFitting.GLOWING_PINK_FITTING, CopperFitting.GLOWING_LIME_FITTING, CopperFitting.GLOWING_YELLOW_FITTING, CopperFitting.GLOWING_LIGHT_BLUE_FITTING, CopperFitting.GLOWING_MAGENTA_FITTING, CopperFitting.GLOWING_ORANGE_FITTING, CopperFitting.GLOWING_WHITE_FITTING, CopperFitting.CORRODED_FITTING).build(null)
    );

    public static void init() {}
}

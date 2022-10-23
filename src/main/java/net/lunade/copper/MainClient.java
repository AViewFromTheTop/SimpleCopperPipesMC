package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.particle.PipeInkParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

public class MainClient implements ClientModInitializer {

    public static final ResourceLocation NOTE_PACKET = new ResourceLocation("lunade","note_packet");

    @Override
    public void onInitializeClient() {
        //CrowdinTranslate.downloadTranslations("simple-copper-pipes", "lunade");
        ParticleFactoryRegistry.getInstance().register(Main.RED_INK, PipeInkParticle.RedFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.GREEN_INK, PipeInkParticle.GreenFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.BROWN_INK, PipeInkParticle.BrownFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.BLUE_INK, PipeInkParticle.BlueFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.PURPLE_INK, PipeInkParticle.PurpleFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.CYAN_INK, PipeInkParticle.CyanFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.LIGHT_GRAY_INK, PipeInkParticle.LightGrayFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.GRAY_INK, PipeInkParticle.GrayFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.PINK_INK, PipeInkParticle.PinkFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.LIME_INK, PipeInkParticle.LimeFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.YELLOW_INK, PipeInkParticle.YellowFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.LIGHT_BLUE_INK, PipeInkParticle.LightBlueFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.MAGENTA_INK, PipeInkParticle.MagentaFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.ORANGE_INK, PipeInkParticle.OrangeFactory::new);
        ParticleFactoryRegistry.getInstance().register(Main.WHITE_INK, PipeInkParticle.WhiteFactory::new);

        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.COPPER_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.EXPOSED_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WEATHERED_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.OXIDIZED_PIPE, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WAXED_COPPER_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WAXED_EXPOSED_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WAXED_WEATHERED_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WAXED_OXIDIZED_PIPE, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.BLACK_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.RED_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GREEN_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.BROWN_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.BLUE_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.PURPLE_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.CYAN_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.LIGHT_GRAY_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GRAY_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.PINK_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.LIME_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.YELLOW_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.LIGHT_BLUE_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.MAGENTA_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.ORANGE_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WHITE_PIPE, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_BLACK_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_RED_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_GREEN_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_BROWN_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_BLUE_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_PURPLE_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_CYAN_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_LIGHT_GRAY_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_GRAY_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_PINK_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_LIME_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_YELLOW_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_LIGHT_BLUE_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_MAGENTA_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_ORANGE_PIPE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_WHITE_PIPE, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.COPPER_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.EXPOSED_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WEATHERED_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.OXIDIZED_FITTING, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WAXED_COPPER_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WAXED_EXPOSED_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WAXED_WEATHERED_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WAXED_OXIDIZED_FITTING, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.BLACK_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.RED_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GREEN_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.BROWN_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.BLUE_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.PURPLE_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.CYAN_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.LIGHT_GRAY_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GRAY_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.PINK_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.LIME_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.YELLOW_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.LIGHT_BLUE_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.MAGENTA_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.ORANGE_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WHITE_FITTING, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_BLACK_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_RED_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_GREEN_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_BROWN_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_BLUE_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_PURPLE_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_CYAN_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_LIGHT_GRAY_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_GRAY_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_PINK_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_LIME_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_YELLOW_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_LIGHT_BLUE_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_MAGENTA_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_ORANGE_FITTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_WHITE_FITTING, RenderType.cutout());

        ClientPlayNetworking.registerGlobalReceiver(NOTE_PACKET, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int k = buf.readInt();
            int i = buf.readInt();
            Direction direction = getDirection(i);
            client.execute(() -> {
                assert client.level != null;
                double x = direction.getStepX()*0.6;
                double y = direction.getStepY()*0.6;
                double z = direction.getStepZ()*0.6;
                client.level.addParticle(ParticleTypes.NOTE, (double)pos.getX() + 0.5D + x, (double)pos.getY() + 0.5D + y, (double)pos.getZ() + 0.5D + z, (double)k / 24.0D, 0.0D, 0.0D);
            });
        });
    }

    public static Direction getDirection(int i) {
        if (i==1) {return Direction.UP;}
        if (i==2) {return Direction.DOWN;}
        if (i==3) {return Direction.NORTH;}
        if (i==4) {return Direction.SOUTH;}
        if (i==5) {return Direction.EAST;}
        if (i==6) {return Direction.WEST;}
        return Direction.NORTH;
    }
}

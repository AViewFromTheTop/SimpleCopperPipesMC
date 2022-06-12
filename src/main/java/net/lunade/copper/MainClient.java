package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.particle.PipeInkParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class MainClient implements ClientModInitializer {

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

    @Override
    public void onInitializeClient() {

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

        ParticleFactoryRegistry.getInstance().register(RED_INK, PipeInkParticle.RedFactory::new);
        ParticleFactoryRegistry.getInstance().register(GREEN_INK, PipeInkParticle.GreenFactory::new);
        ParticleFactoryRegistry.getInstance().register(BROWN_INK, PipeInkParticle.BrownFactory::new);
        ParticleFactoryRegistry.getInstance().register(BLUE_INK, PipeInkParticle.BlueFactory::new);
        ParticleFactoryRegistry.getInstance().register(PURPLE_INK, PipeInkParticle.PurpleFactory::new);
        ParticleFactoryRegistry.getInstance().register(CYAN_INK, PipeInkParticle.CyanFactory::new);
        ParticleFactoryRegistry.getInstance().register(LIGHT_GRAY_INK, PipeInkParticle.LightGrayFactory::new);
        ParticleFactoryRegistry.getInstance().register(GRAY_INK, PipeInkParticle.GrayFactory::new);
        ParticleFactoryRegistry.getInstance().register(PINK_INK, PipeInkParticle.PinkFactory::new);
        ParticleFactoryRegistry.getInstance().register(LIME_INK, PipeInkParticle.LimeFactory::new);
        ParticleFactoryRegistry.getInstance().register(YELLOW_INK, PipeInkParticle.YellowFactory::new);
        ParticleFactoryRegistry.getInstance().register(LIGHT_BLUE_INK, PipeInkParticle.LightBlueFactory::new);
        ParticleFactoryRegistry.getInstance().register(MAGENTA_INK, PipeInkParticle.MagentaFactory::new);
        ParticleFactoryRegistry.getInstance().register(ORANGE_INK, PipeInkParticle.OrangeFactory::new);
        ParticleFactoryRegistry.getInstance().register(WHITE_INK, PipeInkParticle.WhiteFactory::new);

        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.COPPER_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.EXPOSED_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WEATHERED_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.OXIDIZED_PIPE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WAXED_COPPER_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WAXED_EXPOSED_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WAXED_WEATHERED_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WAXED_OXIDIZED_PIPE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.BLACK_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.RED_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GREEN_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.BROWN_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.BLUE_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.PURPLE_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.CYAN_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.LIGHT_GRAY_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GRAY_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.PINK_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.LIME_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.YELLOW_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.LIGHT_BLUE_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.MAGENTA_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.ORANGE_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.WHITE_PIPE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_BLACK_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_RED_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_GREEN_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_BROWN_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_BLUE_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_PURPLE_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_CYAN_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_LIGHT_GRAY_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_GRAY_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_PINK_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_LIME_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_YELLOW_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_LIGHT_BLUE_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_MAGENTA_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_ORANGE_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperPipe.GLOWING_WHITE_PIPE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.COPPER_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.EXPOSED_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WEATHERED_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.OXIDIZED_FITTING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WAXED_COPPER_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WAXED_EXPOSED_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WAXED_WEATHERED_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WAXED_OXIDIZED_FITTING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.BLACK_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.RED_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GREEN_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.BROWN_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.BLUE_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.PURPLE_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.CYAN_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.LIGHT_GRAY_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GRAY_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.PINK_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.LIME_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.YELLOW_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.LIGHT_BLUE_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.MAGENTA_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.ORANGE_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.WHITE_FITTING, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_BLACK_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_RED_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_GREEN_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_BROWN_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_BLUE_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_PURPLE_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_CYAN_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_LIGHT_GRAY_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_GRAY_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_PINK_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_LIME_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_YELLOW_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_LIGHT_BLUE_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_MAGENTA_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_ORANGE_FITTING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CopperFitting.GLOWING_WHITE_FITTING, RenderLayer.getCutout());

        ClientPlayNetworking.registerGlobalReceiver(NOTE_PACKET, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int k = buf.readInt();
            int i = buf.readInt();
            Direction direction = getDirection(i);
            client.execute(() -> {
                assert client.world != null;
                double x = direction.getOffsetX()*0.6;
                double y = direction.getOffsetY()*0.6;
                double z = direction.getOffsetZ()*0.6;
                client.world.addParticle(ParticleTypes.NOTE, (double)pos.getX() + 0.5D + x, (double)pos.getY() + 0.5D + y, (double)pos.getZ() + 0.5D + z, (double)k / 24.0D, 0.0D, 0.0D);
            });
        });

        receiveEasyPipeInkPacket();
    }

    public void receiveEasyPipeInkPacket() {
        ClientPlayNetworking.registerGlobalReceiver(Main.PIPE_INK_PACKET, (ctx, handler, byteBuf, responseSender) -> {
            Vec3d pos = new Vec3d(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
            double xVel = byteBuf.readDouble();
            double yVel = byteBuf.readDouble();
            double zVel = byteBuf.readDouble();
            int count = byteBuf.readVarInt();
            int color = byteBuf.readVarInt();
            ctx.execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("why is your world null");
                for (int i=0; i<count; i++) {
                    MinecraftClient.getInstance().world.addParticle(intToParticle(color), pos.x, pos.y, pos.z, xVel, yVel, zVel);
                }
            });
        });
    }

    public static ParticleEffect intToParticle(int i) {
        return switch (i) {
            case 1 -> ParticleTypes.GLOW_SQUID_INK;
            case 2 -> RED_INK;
            case 3 -> GREEN_INK;
            case 4 -> BROWN_INK;
            case 5 -> BLUE_INK;
            case 6 -> PURPLE_INK;
            case 7 -> CYAN_INK;
            case 8 -> LIGHT_GRAY_INK;
            case 9 -> GRAY_INK;
            case 10 -> PINK_INK;
            case 11 -> LIME_INK;
            case 12 -> YELLOW_INK;
            case 13 -> LIGHT_BLUE_INK;
            case 14 -> MAGENTA_INK;
            case 15 -> ORANGE_INK;
            case 16 -> WHITE_INK;
            default -> ParticleTypes.SQUID_INK;
        };
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

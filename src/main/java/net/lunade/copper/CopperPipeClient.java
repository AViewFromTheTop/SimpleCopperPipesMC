package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.lunade.copper.particle.PipeInkParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;

public class CopperPipeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.RED_INK, PipeInkParticle.RedFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.GREEN_INK, PipeInkParticle.GreenFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.BROWN_INK, PipeInkParticle.BrownFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.BLUE_INK, PipeInkParticle.BlueFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.PURPLE_INK, PipeInkParticle.PurpleFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.CYAN_INK, PipeInkParticle.CyanFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.LIGHT_GRAY_INK, PipeInkParticle.LightGrayFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.GRAY_INK, PipeInkParticle.GrayFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.PINK_INK, PipeInkParticle.PinkFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.LIME_INK, PipeInkParticle.LimeFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.YELLOW_INK, PipeInkParticle.YellowFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.LIGHT_BLUE_INK, PipeInkParticle.LightBlueFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.MAGENTA_INK, PipeInkParticle.MagentaFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.ORANGE_INK, PipeInkParticle.OrangeFactory::new);
        ParticleFactoryRegistry.getInstance().register(CopperPipeMain.WHITE_INK, PipeInkParticle.WhiteFactory::new);

        ClientPlayNetworking.registerGlobalReceiver(CopperPipeMain.NOTE_PACKET, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int k = buf.readInt();
            int i = buf.readInt();
            Direction direction = getDirection(i);
            client.execute(() -> {
                assert client.level != null;
                double x = direction.getStepX() * 0.6;
                double y = direction.getStepY() * 0.6;
                double z = direction.getStepZ() * 0.6;
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

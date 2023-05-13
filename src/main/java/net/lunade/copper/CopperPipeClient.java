package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.lunade.copper.particle.PipeInkParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

public class CopperPipeClient implements ClientModInitializer {

    public static final ResourceLocation NOTE_PACKET = new ResourceLocation("lunade","note_packet");

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry particleFactoryRegistry = ParticleFactoryRegistry.getInstance();
        particleFactoryRegistry.register(CopperPipeMain.RED_INK, PipeInkParticle.RedFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.GREEN_INK, PipeInkParticle.GreenFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.BROWN_INK, PipeInkParticle.BrownFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.BLUE_INK, PipeInkParticle.BlueFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.PURPLE_INK, PipeInkParticle.PurpleFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.CYAN_INK, PipeInkParticle.CyanFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.LIGHT_GRAY_INK, PipeInkParticle.LightGrayFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.GRAY_INK, PipeInkParticle.GrayFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.PINK_INK, PipeInkParticle.PinkFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.LIME_INK, PipeInkParticle.LimeFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.YELLOW_INK, PipeInkParticle.YellowFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.LIGHT_BLUE_INK, PipeInkParticle.LightBlueFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.MAGENTA_INK, PipeInkParticle.MagentaFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.ORANGE_INK, PipeInkParticle.OrangeFactory::new);
        particleFactoryRegistry.register(CopperPipeMain.WHITE_INK, PipeInkParticle.WhiteFactory::new);

        ClientPlayNetworking.registerGlobalReceiver(NOTE_PACKET, (client, handler, buf, responseSender) -> {
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
        if (i == 1) {
            return Direction.UP;
        }
        if (i == 2) {
            return Direction.DOWN;
        }
        if (i == 3) {
            return Direction.NORTH;
        }
        if (i == 4) {
            return Direction.SOUTH;
        }
        if (i == 5) {
            return Direction.EAST;
        }
        if ( i== 6) {
            return Direction.WEST;
        }
        return Direction.NORTH;
    }
}

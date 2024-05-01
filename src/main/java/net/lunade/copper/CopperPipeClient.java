package net.lunade.copper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.lunade.copper.networking.CopperPipeClientNetworking;
import net.lunade.copper.particle.PipeInkParticle;

public class CopperPipeClient implements ClientModInitializer {

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

        CopperPipeClientNetworking.registerPacketReceivers();
    }
}

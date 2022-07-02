package net.lunade.copper.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;

public class PipeInkParticle extends AnimatedParticle {
    PipeInkParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, int j, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, spriteProvider, 0.0F);
        this.velocityMultiplier = 0.92F;
        this.scale = 0.5F;
        this.setAlpha(1.0F);
        this.setColor((float) ColorHelper.Argb.getRed(j), (float) ColorHelper.Argb.getGreen(j), (float) ColorHelper.Argb.getBlue(j));
        this.maxAge = (int)((double)(this.scale * 12.0F) / (Math.random() * 0.800000011920929D + 0.20000000298023224D));
        this.setSpriteForAge(spriteProvider);
        this.collidesWithWorld = false;
        this.velocityX = g;
        this.velocityY = h;
        this.velocityZ = i;
    }

    public void tick() {
        super.tick();
        if (!this.dead) {
            this.setSpriteForAge(this.spriteProvider);
            if (this.age > this.maxAge / 2) {
                this.setAlpha(1.0F - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);
            }

            if (this.world.getBlockState(new BlockPos(this.x, this.y, this.z)).isAir()) {
                this.velocityY -= 0.007400000002235174D;
            }
        }

    }

    @Environment(EnvType.CLIENT)
    public static class RedFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public RedFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 93, 216, 221), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class OrangeFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public OrangeFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 10, 134, 232), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class YellowFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public YellowFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 4, 52, 212), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class LimeFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public LimeFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 139, 67, 231), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class GreenFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public GreenFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 168, 142, 230), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class CyanFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public CyanFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 234, 113, 118), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class LightBlueFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public LightBlueFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 129, 70, 33), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class BlueFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public BlueFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 200, 197, 95), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class PurpleFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public PurpleFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 129, 212, 81), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class MagentaFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public MagentaFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 62, 183, 72), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class PinkFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public PinkFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 13, 119, 87), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class WhiteFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public WhiteFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 5, 4, 4), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class LightGrayFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public LightGrayFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 109, 109, 116), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class GrayFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public GrayFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 184, 176, 173), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class BrownFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public BrownFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, ColorHelper.Argb.getArgb(255, 140, 183, 214), this.spriteProvider);
        }
    }
}

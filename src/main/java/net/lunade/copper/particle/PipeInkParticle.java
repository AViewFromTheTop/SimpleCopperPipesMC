package net.lunade.copper.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

public class PipeInkParticle extends SimpleAnimatedParticle {
    PipeInkParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, int j, SpriteSet spriteProvider) {
        super(clientWorld, d, e, f, spriteProvider, 0.0F);
        this.friction = 0.92F;
        this.quadSize = 0.5F;
        this.setAlpha(1.0F);
        this.setColor((float) FastColor.ARGB32.red(j), (float) FastColor.ARGB32.green(j), (float) FastColor.ARGB32.blue(j));
        this.lifetime = (int)((double)(this.quadSize * 12.0F) / (Math.random() * 0.800000011920929D + 0.20000000298023224D));
        this.setSpriteFromAge(spriteProvider);
        this.hasPhysics = false;
        this.xd = g;
        this.yd = h;
        this.zd = i;
    }

    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSpriteFromAge(this.sprites);
            if (this.age > this.lifetime / 2) {
                this.setAlpha(1.0F - ((float)this.age - (float)(this.lifetime / 2)) / (float)this.lifetime);
            }

            if (this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
                this.yd -= 0.007400000002235174D;
            }
        }

    }

    @Environment(EnvType.CLIENT)
    public static class RedFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public RedFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 93, 216, 221), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class OrangeFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public OrangeFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 10, 134, 232), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class YellowFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public YellowFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 4, 52, 212), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class LimeFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public LimeFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 139, 67, 231), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class GreenFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public GreenFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 168, 142, 230), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class CyanFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public CyanFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 234, 113, 118), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class LightBlueFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public LightBlueFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 129, 70, 33), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class BlueFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public BlueFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 200, 197, 95), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class PurpleFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public PurpleFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 129, 212, 81), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class MagentaFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public MagentaFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 62, 183, 72), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class PinkFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public PinkFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 13, 119, 87), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class WhiteFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public WhiteFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 5, 4, 4), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class LightGrayFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public LightGrayFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 109, 109, 116), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class GrayFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public GrayFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 184, 176, 173), this.spriteProvider);
        }
    }
    @Environment(EnvType.CLIENT)
    public static class BrownFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        public BrownFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PipeInkParticle(clientWorld, d, e, f, g, h, i, FastColor.ARGB32.color(255, 140, 183, 214), this.spriteProvider);
        }
    }
}

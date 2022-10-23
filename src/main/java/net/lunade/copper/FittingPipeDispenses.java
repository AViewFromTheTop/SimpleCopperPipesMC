package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FittingPipeDispenses {

    private static final ArrayList<Item> items = new ArrayList<>();
    private static final ArrayList<FittingDispense<?>> dispenses = new ArrayList<>();

    public static void register(Item item, FittingDispense<Item> dispense) {
        if (!items.contains(item)) {
            items.add(item);
            dispenses.add(dispense);
        } else {
            dispenses.set(items.indexOf(item), dispense);
        }
    }

    @Nullable
    public static FittingDispense<?> getDispense(Item item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            return dispenses.get(index);
        }
        return null;
    }

    @FunctionalInterface
    public interface FittingDispense<Item> {
        void dispense(ServerLevel world, ItemStack itemStack, int i, Direction direction, Position position, BlockState state, boolean corroded, BlockPos pos, CopperPipeEntity pipe);
    }


    public static void init() {
        register(Items.INK_SAC, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*7) - 3.5;
            double random2 = (random.nextDouble()*7) - 3.5;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            double velX = axis == Direction.Axis.X ? (i * offX) * 2 : (axis==Direction.Axis.Z ? corroded ? random2 : random2*0.1 : corroded ? random1 : random1*0.1);
            double velY = axis == Direction.Axis.Y ? (i * offY) * 2 : corroded ? random1 : random1*0.1;
            double velZ = axis == Direction.Axis.Z ? (i * offZ) * 2 : corroded ? random2 : random2*0.1;
            UniformInt ran1 = UniformInt.of(-3,3);
            UniformInt ran2 = UniformInt.of(-1,1);
            UniformInt ran3 = UniformInt.of(-3,3);
            if (state.getBlock() instanceof CopperPipe copperPipe) {
                ParticleOptions ink = copperPipe.ink;
                if (world.getBlockState(pos.relative(direction.getOpposite())).getBlock() instanceof CopperFitting fitting) {
                    if (ink == ParticleTypes.SQUID_INK) { ink = fitting.ink; }
                    for (int o=0; o<30; o++) {
                        world.sendParticles(ink, d + ran1.sample(world.random) * 0.1, e + ran2.sample(world.random) * 0.1, f + ran3.sample(world.random) * 0.1, 0, velX, velY, velZ, 0.10000000149011612D);
                    }
                }
            }
        });
        register(Items.GLOW_INK_SAC, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*7) - 3.5;
            double random2 = (random.nextDouble()*7) - 3.5;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            double velX = axis == Direction.Axis.X ? (i * offX) * 2 : (axis==Direction.Axis.Z ? corroded ? random2 : random2*0.1 : corroded ? random1 : random1*0.1);
            double velY = axis == Direction.Axis.Y ? (i * offY) * 2 : corroded ? random1 : random1*0.1;
            double velZ = axis == Direction.Axis.Z ? (i * offZ) * 2 : corroded ? random2 : random2*0.1;
            UniformInt ran1 = UniformInt.of(-3,3);
            UniformInt ran2 = UniformInt.of(-1,1);
            UniformInt ran3 = UniformInt.of(-3,3);
            for (int o=0; o<30; o++) {
                world.sendParticles(ParticleTypes.GLOW_SQUID_INK, d + ran1.sample(world.random) * 0.1, e + ran2.sample(world.random) * 0.1, f + ran3.sample(world.random) * 0.1, 0, velX, velY, velZ, 0.10000000149011612D);
            }
        });
        register(Items.SCULK_SENSOR, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            RandomSource random = world.random;
            Direction.Axis axis = direction.getAxis();
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            double vibX=position.x();
            double vibY=position.y();
            double vibZ=position.z();
            double random1 = (random.nextDouble()*6) - 3;
            double random2 = (random.nextDouble()*6) - 3;
            vibX = axis == Direction.Axis.X ? vibX+(10 * offX) : corroded ? (axis==Direction.Axis.Z ? vibX+random2 : vibX+random1) : vibX;
            vibY = axis == Direction.Axis.Y ? vibY+(10 * offY) : corroded ? vibY+random1 : vibY;
            vibZ = axis == Direction.Axis.Z ? vibZ+(10 * offZ) * 2 : corroded ? vibZ+random2 : vibZ;
            BlockPositionSource blockSource = new BlockPositionSource(new BlockPos(vibX, vibY, vibZ));
            world.sendParticles(new VibrationParticleOption(blockSource, 32), position.x(), position.y(), position.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
        });
        }

}

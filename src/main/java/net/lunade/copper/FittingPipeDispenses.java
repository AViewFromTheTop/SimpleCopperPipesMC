package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.event.BlockPositionSource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

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
        void dispense(ServerWorld world, ItemStack itemStack, int i, Direction direction, Position position, BlockState state, boolean corroded, BlockPos pos, CopperPipeEntity pipe);
    }


    public static void init() {
        register(Items.INK_SAC, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = (random.nextDouble()*7) - 3.5;
            double random2 = (random.nextDouble()*7) - 3.5;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getOffsetX();
            int offY = direction.getOffsetY();
            int offZ = direction.getOffsetZ();
            double velX = axis == Direction.Axis.X ? (i * offX) * 2 : (axis==Direction.Axis.Z ? corroded ? random2 : random2*0.1 : corroded ? random1 : random1*0.1);
            double velY = axis == Direction.Axis.Y ? (i * offY) * 2 : corroded ? random1 : random1*0.1;
            double velZ = axis == Direction.Axis.Z ? (i * offZ) * 2 : corroded ? random2 : random2*0.1;
            UniformIntProvider ran1 = UniformIntProvider.create(-3,3);
            UniformIntProvider ran2 = UniformIntProvider.create(-1,1);
            UniformIntProvider ran3 = UniformIntProvider.create(-3,3);
            if (state.getBlock() instanceof CopperPipe copperPipe) {
                ParticleEffect ink = copperPipe.ink;
                if (world.getBlockState(pos.offset(direction.getOpposite())).getBlock() instanceof CopperFitting fitting) {
                    if (ink == ParticleTypes.SQUID_INK) { ink = fitting.ink; }
                    for (int o=0; o<30; o++) {
                        world.spawnParticles(ink, d + ran1.get(world.random) * 0.1, e + ran2.get(world.random) * 0.1, f + ran3.get(world.random) * 0.1, 0, velX, velY, velZ, 0.10000000149011612D);
                    }
                }
            }
        });
        register(Items.GLOW_INK_SAC, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = (random.nextDouble()*7) - 3.5;
            double random2 = (random.nextDouble()*7) - 3.5;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getOffsetX();
            int offY = direction.getOffsetY();
            int offZ = direction.getOffsetZ();
            double velX = axis == Direction.Axis.X ? (i * offX) * 2 : (axis==Direction.Axis.Z ? corroded ? random2 : random2*0.1 : corroded ? random1 : random1*0.1);
            double velY = axis == Direction.Axis.Y ? (i * offY) * 2 : corroded ? random1 : random1*0.1;
            double velZ = axis == Direction.Axis.Z ? (i * offZ) * 2 : corroded ? random2 : random2*0.1;
            UniformIntProvider ran1 = UniformIntProvider.create(-3,3);
            UniformIntProvider ran2 = UniformIntProvider.create(-1,1);
            UniformIntProvider ran3 = UniformIntProvider.create(-3,3);
            for (int o=0; o<30; o++) {
                world.spawnParticles(ParticleTypes.GLOW_SQUID_INK, d + ran1.get(world.random) * 0.1, e + ran2.get(world.random) * 0.1, f + ran3.get(world.random) * 0.1, 0, velX, velY, velZ, 0.10000000149011612D);
            }
        });
        register(Items.SCULK_SENSOR, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            Random random = world.random;
            Direction.Axis axis = direction.getAxis();
            int offX = direction.getOffsetX();
            int offY = direction.getOffsetY();
            int offZ = direction.getOffsetZ();
            double vibX=position.getX();
            double vibY=position.getY();
            double vibZ=position.getZ();
            double random1 = (random.nextDouble()*6) - 3;
            double random2 = (random.nextDouble()*6) - 3;
            vibX = axis == Direction.Axis.X ? vibX+(10 * offX) : corroded ? (axis==Direction.Axis.Z ? vibX+random2 : vibX+random1) : vibX;
            vibY = axis == Direction.Axis.Y ? vibY+(10 * offY) : corroded ? vibY+random1 : vibY;
            vibZ = axis == Direction.Axis.Z ? vibZ+(10 * offZ) * 2 : corroded ? vibZ+random2 : vibZ;
            BlockPositionSource blockSource = new BlockPositionSource(new BlockPos(vibX, vibY, vibZ));
            RegisterPipeNbtMethods.spawnDelayedVibration(world, pos, new BlockPos(vibX, vibY, vibZ), 32);
        });
        }

}

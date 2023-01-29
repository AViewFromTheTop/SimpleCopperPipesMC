package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PoweredPipeDispenses {

    private static final ArrayList<ItemLike> items = new ArrayList<>();
    private static final ArrayList<PoweredDispense> dispenses = new ArrayList<>();

    public static void register(ItemLike item, PoweredDispense dispense) {
        if (!items.contains(item)) {
            items.add(item);
            dispenses.add(dispense);
        } else {
            dispenses.set(items.indexOf(item), dispense);
        }
    }

    @Nullable
    public static PoweredDispense getDispense(ItemLike item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            return dispenses.get(index);
        }
        return null;
    }

    @FunctionalInterface
    public interface PoweredDispense {
        void dispense(ServerLevel world, ItemStack itemStack, int i, Direction direction, Position position, BlockState state, boolean corroded, BlockPos pos, CopperPipeEntity pipe);
    }

    public static double getYOffset(Direction.Axis axis, double e) {
        if (axis == Direction.Axis.Y) {
            return e - 0.125D;
        } else {
            return e - 0.15625D;
        }
    }
    
    public static double getRandom(RandomSource random) {
        return (random.nextDouble() * 0.6) - 0.3;
    }
    
    public static double getVelX(Direction.Axis axis, int offX, int i, boolean corroded, double random1, double random2) {
        return axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : 0;
    }

    public static double getVelY(Direction.Axis axis, int offY, int i, boolean corroded, double random1) {
        return axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : 0;
    }

    public static double getVelZ(Direction.Axis axis, int offZ, int i, boolean corroded, double random2) {
        return axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : 0;
    }

    public static void init() {
        register(Items.ARROW, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            Arrow shotEntity = new Arrow(world,d,e,f);
            shotEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.SPECTRAL_ARROW, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            SpectralArrow shotEntity = new SpectralArrow(world,d,e,f);
            shotEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.TIPPED_ARROW, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            Arrow shotEntity = new Arrow(world,d,e,f);
            shotEntity.setEffectsFromItem(stack);
            shotEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.SNOWBALL, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) {
                e -= 0.125D;
            } else {
                e -= 0.15625D;
            }
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            Snowball shotEntity = new Snowball(world,d,e,f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.EGG, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            ThrownEgg shotEntity = new ThrownEgg(world,d,e,f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.EXPERIENCE_BOTTLE, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            ThrownExperienceBottle shotEntity = new ThrownExperienceBottle(world,d,e,f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.SPLASH_POTION, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            ThrownPotion shotEntity = Util.make(new ThrownPotion(world, d, e, f), (potionEntity) -> potionEntity.setItem(stack));
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.LINGERING_POTION, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            ThrownPotion shotEntity = Util.make(new ThrownPotion(world, d, e, f), (potionEntity) -> potionEntity.setItem(stack));
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.FIRE_CHARGE, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            RandomSource random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getStepY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getStepZ(), i, corroded, random2);
            SmallFireball smallFireballEntity = new SmallFireball(world, d, e, f, velX, velY, velZ);
            world.addFreshEntity(Util.make(smallFireballEntity, (smallFireballEntityx) -> smallFireballEntityx.setItem(stack)));
            smallFireballEntity.setDeltaMovement(velX, velY, velZ);
        });
    }

}

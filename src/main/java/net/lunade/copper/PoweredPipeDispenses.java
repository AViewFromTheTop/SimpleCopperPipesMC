package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class PoweredPipeDispenses {

    private static final ArrayList<ItemConvertible> items = new ArrayList<>();
    private static final ArrayList<PoweredDispense> dispenses = new ArrayList<>();

    public static void register(ItemConvertible item, PoweredDispense dispense) {
        if (!items.contains(item)) {
            items.add(item);
            dispenses.add(dispense);
        } else {
            dispenses.set(items.indexOf(item), dispense);
        }
    }

    @Nullable
    public static PoweredDispense getDispense(ItemConvertible item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            return dispenses.get(index);
        }
        return null;
    }

    @FunctionalInterface
    public interface PoweredDispense {
        void dispense(ServerWorld world, ItemStack itemStack, int i, Direction direction, Position position, BlockState state, boolean corroded, BlockPos pos, CopperPipeEntity pipe);
    }

    public static double getYOffset(Direction.Axis axis, double e) {
        if (axis == Direction.Axis.Y) {
            return e - 0.125D;
        } else {
            return e - 0.15625D;
        }
    }
    
    public static double getRandom(Random random) {
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
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            ArrowEntity shotEntity = new ArrowEntity(world,d,e,f);
            shotEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            shotEntity.setVelocity(velX, velY, velZ);
            world.spawnEntity(shotEntity);
        });
        register(Items.SPECTRAL_ARROW, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            SpectralArrowEntity shotEntity = new SpectralArrowEntity(world,d,e,f);
            shotEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            shotEntity.setVelocity(velX, velY, velZ);
            world.spawnEntity(shotEntity);
        });
        register(Items.TIPPED_ARROW, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            ArrowEntity shotEntity = new ArrowEntity(world,d,e,f);
            shotEntity.initFromStack(stack);
            shotEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            shotEntity.setVelocity(velX, velY, velZ);
            world.spawnEntity(shotEntity);
        });
        register(Items.SNOWBALL, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) {
                e -= 0.125D;
            } else {
                e -= 0.15625D;
            }
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            SnowballEntity shotEntity = new SnowballEntity(world,d,e,f);
            shotEntity.setVelocity(velX, velY, velZ);
            world.spawnEntity(shotEntity);
        });
        register(Items.EGG, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            EggEntity shotEntity = new EggEntity(world,d,e,f);
            shotEntity.setVelocity(velX, velY, velZ);
            world.spawnEntity(shotEntity);
        });
        register(Items.EXPERIENCE_BOTTLE, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            ExperienceBottleEntity shotEntity = new ExperienceBottleEntity(world,d,e,f);
            shotEntity.setVelocity(velX, velY, velZ);
            world.spawnEntity(shotEntity);
        });
        register(Items.SPLASH_POTION, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            PotionEntity shotEntity = Util.make(new PotionEntity(world, d, e, f), (potionEntity) -> potionEntity.setItem(stack));
            shotEntity.setVelocity(velX, velY, velZ);
            world.spawnEntity(shotEntity);
        });
        register(Items.LINGERING_POTION, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            PotionEntity shotEntity = Util.make(new PotionEntity(world, d, e, f), (potionEntity) -> potionEntity.setItem(stack));
            shotEntity.setVelocity(velX, velY, velZ);
            world.spawnEntity(shotEntity);
        });
        register(Items.FIRE_CHARGE, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.getX();
            double e = position.getY();
            double f = position.getZ();
            Random random = world.random;
            double random1 = getRandom(random);
            double random2 = getRandom(random);
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getOffsetX(), i, corroded, random1, random2);
            double velY = getVelY(axis, direction.getOffsetY(), i, corroded, random1);
            double velZ = getVelZ(axis, direction.getOffsetZ(), i, corroded, random2);
            SmallFireballEntity smallFireballEntity = new SmallFireballEntity(world, d, e, f, velX, velY, velZ);
            world.spawnEntity(Util.make(smallFireballEntity, (smallFireballEntityx) -> smallFireballEntityx.setItem(stack)));
            smallFireballEntity.addVelocity(velX, velY, velZ);
        });
    }

}

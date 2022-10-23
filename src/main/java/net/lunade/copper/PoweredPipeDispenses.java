package net.lunade.copper;

import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PoweredPipeDispenses {

    private static final ArrayList<Item> items = new ArrayList<>();
    private static final ArrayList<PoweredDispense<?>> dispenses = new ArrayList<>();

    public static void register(Item item, PoweredDispense<Item> dispense) {
        if (!items.contains(item)) {
            items.add(item);
            dispenses.add(dispense);
        } else {
            dispenses.set(items.indexOf(item), dispense);
        }
    }

    @Nullable
    public static PoweredDispense<?> getDispense(Item item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            return dispenses.get(index);
        }
        return null;
    }

    @FunctionalInterface
    public interface PoweredDispense<Item> {
        void dispense(ServerLevel world, ItemStack itemStack, int i, Direction direction, Position position, BlockState state, boolean corroded, BlockPos pos, CopperPipeEntity pipe);
    }


    public static void init() {
        register(Items.ARROW, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
            Arrow shotEntity = new Arrow(world,d,e,f);
            shotEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.SPECTRAL_ARROW, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
            SpectralArrow shotEntity = new SpectralArrow(world,d,e,f);
            shotEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.TIPPED_ARROW, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
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
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
            Snowball shotEntity = new Snowball(world,d,e,f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.EGG, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
            ThrownEgg shotEntity = new ThrownEgg(world,d,e,f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.EXPERIENCE_BOTTLE, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
            ThrownExperienceBottle shotEntity = new ThrownExperienceBottle(world,d,e,f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.SPLASH_POTION, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
            ThrownPotion shotEntity = Util.make(new ThrownPotion(world, d, e, f), (potionEntity) -> potionEntity.setItem(stack));
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.LINGERING_POTION, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
            ThrownPotion shotEntity = Util.make(new ThrownPotion(world, d, e, f), (potionEntity) -> potionEntity.setItem(stack));
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.FIRE_CHARGE, (world, stack, i, direction, position, state, corroded, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            double velX = 0;
            double velY = 0;
            double velZ = 0;
            RandomSource random = world.random;
            double random1 = (random.nextDouble()*0.6) - 0.3;
            double random2 = (random.nextDouble()*0.6) - 0.3;
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) { e -= 0.125D;} else { e -= 0.15625D; }
            int offX = direction.getStepX();
            int offY = direction.getStepY();
            int offZ = direction.getStepZ();
            velX = axis == Direction.Axis.X ? (i * offX) * 0.1 : corroded ? (axis == Direction.Axis.Z ? random2 : random1) : velX;
            velY = axis == Direction.Axis.Y ? (i * offY) * 0.1 : corroded ? random1 : velY;
            velZ = axis == Direction.Axis.Z ? (i * offZ) * 0.1 : corroded ? random2 : velZ;
            SmallFireball smallFireballEntity = new SmallFireball(world, d, e, f, velX, velY, velZ);
            world.addFreshEntity(Util.make(smallFireballEntity, (smallFireballEntityx) -> smallFireballEntityx.setItem(stack)));
            smallFireballEntity.setDeltaMovement(velX, velY, velZ);
        });
        }

}

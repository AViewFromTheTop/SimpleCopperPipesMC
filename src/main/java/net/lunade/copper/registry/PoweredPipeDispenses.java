package net.lunade.copper.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Map;
import net.lunade.copper.blocks.block_entity.CopperPipeEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PoweredPipeDispenses {

    private static final Map<ItemLike, PoweredDispense> ITEMS_TO_DISPENSES = new Object2ObjectLinkedOpenHashMap<>();

    public static void register(ItemLike item, PoweredDispense dispense) {
        ITEMS_TO_DISPENSES.put(item, dispense);
    }

    @Nullable
    public static PoweredDispense getDispense(ItemLike item) {
        if (ITEMS_TO_DISPENSES.containsKey(item)) {
            return ITEMS_TO_DISPENSES.get(item);
        }
        return null;
    }

    public static double getYOffset(Direction.Axis axis, double e) {
        if (axis == Direction.Axis.Y) {
            return e - 0.125D;
        } else {
            return e - 0.15625D;
        }
    }

    public static double getRandom(@NotNull RandomSource random) {
        return (random.nextDouble() * 0.6D) - 0.3D;
    }

    public static double getVelX(Direction.Axis axis, int offX, int i) {
        return axis == Direction.Axis.X ? (i * offX) * 0.1D : 0D;
    }

    public static double getVelY(Direction.Axis axis, int offY, int i) {
        return axis == Direction.Axis.Y ? (i * offY) * 0.1D : 0D;
    }

    public static double getVelZ(Direction.Axis axis, int offZ, int i) {
        return axis == Direction.Axis.Z ? (i * offZ) * 0.1D : 0D;
    }

    public static void init() {
        register(Items.ARROW, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            Arrow shotEntity = new Arrow(world, d, e, f, stack, null);
            shotEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.SPECTRAL_ARROW, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            SpectralArrow shotEntity = new SpectralArrow(world, d, e, f, stack, null);
            shotEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.TIPPED_ARROW, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            Arrow shotEntity = new Arrow(world, d, e, f, stack, null);
            shotEntity.setPotionContents(stack.getComponents().get(DataComponents.POTION_CONTENTS));
            shotEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.SNOWBALL, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            if (axis == Direction.Axis.Y) {
                e -= 0.125D;
            } else {
                e -= 0.15625D;
            }
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            Snowball shotEntity = new Snowball(world, d, e, f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.EGG, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            ThrownEgg shotEntity = new ThrownEgg(world, d, e, f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.EXPERIENCE_BOTTLE, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            ThrownExperienceBottle shotEntity = new ThrownExperienceBottle(world, d, e, f);
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.SPLASH_POTION, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            ThrownPotion shotEntity = Util.make(new ThrownPotion(world, d, e, f), (potionEntity) -> potionEntity.setItem(stack));
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.LINGERING_POTION, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            ThrownPotion shotEntity = Util.make(new ThrownPotion(world, d, e, f), (potionEntity) -> potionEntity.setItem(stack));
            shotEntity.setDeltaMovement(velX, velY, velZ);
            world.addFreshEntity(shotEntity);
        });
        register(Items.FIRE_CHARGE, (world, stack, i, direction, position, state, pos, pipe) -> {
            double d = position.x();
            double e = position.y();
            double f = position.z();
            Direction.Axis axis = direction.getAxis();
            e = getYOffset(axis, e);
            double velX = getVelX(axis, direction.getStepX(), i);
            double velY = getVelY(axis, direction.getStepY(), i);
            double velZ = getVelZ(axis, direction.getStepZ(), i);
            SmallFireball smallFireballEntity = new SmallFireball(world, d, e, f, new Vec3(velX, velY, velZ));
            world.addFreshEntity(Util.make(smallFireballEntity, (smallFireballEntityx) -> smallFireballEntityx.setItem(stack)));
            smallFireballEntity.setDeltaMovement(velX, velY, velZ);
        });
    }

    @FunctionalInterface
    public interface PoweredDispense {
        void dispense(ServerLevel world, ItemStack itemStack, int i, Direction direction, Position position, BlockState state, BlockPos pos, CopperPipeEntity pipe);
    }

}

package net.lunade.copper.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Map;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopperPipeDispenseBehaviors {
	private static final Map<ItemLike, PoweredDispense> ITEMS_TO_DISPENSES = new Object2ObjectLinkedOpenHashMap<>();

	public static void register(ItemLike item, PoweredDispense dispense) {
		ITEMS_TO_DISPENSES.put(item, dispense);
	}

	private static final PoweredDispense PROJECTILE_ITEM_DISPENSE = (level, stack, i, direction, position, state, pos, pipe) -> {
		if (!(stack.getItem() instanceof ProjectileItem projectileItem)) return;
		final Direction.Axis axis = direction.getAxis();
		final double x = position.x();
		final double y = getYOffset(axis, position.y());
		final double z = position.z();

		final ProjectileItem.DispenseConfig dispenseConfig = projectileItem.createDispenseConfig();
		Projectile.spawnProjectileUsingShoot(
			projectileItem.asProjectile(level, new Vec3(x, y, z), stack, direction),
			level,
			stack,
			direction.getStepX(),
			direction.getStepY(),
			direction.getStepZ(),
			dispenseConfig.power() * 2F,
			dispenseConfig.uncertainty() * 2F
		);
	};

	@Nullable
	public static PoweredDispense getDispense(ItemLike item) {
		if (ITEMS_TO_DISPENSES.containsKey(item)) return ITEMS_TO_DISPENSES.get(item);
		if (item instanceof ProjectileItem) return PROJECTILE_ITEM_DISPENSE;
		return null;
	}

	public static double getYOffset(Direction.Axis axis, double y) {
		if (axis == Direction.Axis.Y) return y - 0.125D;
		return y - 0.15625D;
	}

	public static double getRandom(@NotNull RandomSource random) {
		return (random.nextDouble() * 0.6D) - 0.3D;
	}

	public static double getVelX(Direction.Axis axis, int offX, int offset) {
		return axis == Direction.Axis.X ? (offset * offX) * 0.1D : 0D;
	}

	public static double getVelY(Direction.Axis axis, int offY, int offset) {
		return axis == Direction.Axis.Y ? (offset * offY) * 0.1D : 0D;
	}

	public static double getVelZ(Direction.Axis axis, int offZ, int offset) {
		return axis == Direction.Axis.Z ? (offset * offZ) * 0.1D : 0D;
	}

	public static void init() {
	}

	@FunctionalInterface
	public interface PoweredDispense {
		void dispense(ServerLevel level, ItemStack stack, int shotPower, Direction direction, Position position, BlockState state, BlockPos pos, CopperPipeBlockEntity pipe);
	}

}

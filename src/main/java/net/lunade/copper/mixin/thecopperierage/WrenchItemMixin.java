package net.lunade.copper.mixin.thecopperierage;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.thecopperierage.item.WrenchItem;
import net.lunade.copper.tag.SimpleCopperPipesBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WrenchItem.class)
public class WrenchItemMixin {

	@WrapOperation(
		method = "changeIntoState",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
		)
	)
	private static boolean simpleCopperPipes$useUpdateIfBlockIsPipe(Level instance, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
		return original.call(instance, pos, state, state.is(SimpleCopperPipesBlockTags.COPPER_PIPES) ? Block.UPDATE_ALL : i);
	}

}

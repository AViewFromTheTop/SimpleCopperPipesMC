package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.copper.block.CopperPipe;
import net.lunade.copper.block.properties.PipeFluid;
import net.lunade.copper.registry.SimpleCopperPipesBlockStateProperties;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @WrapOperation(
            method = "isSmokeyPos",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/CampfireBlock;isLitCampfire(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            )
    )
    private static boolean simpleCopperPipes$isSmokeyPos(BlockState blockState, Operation<Boolean> operation) {
        return operation.call(blockState)
                || (blockState.getBlock() instanceof CopperPipe && blockState.getValue(SimpleCopperPipesBlockStateProperties.FLUID) == PipeFluid.SMOKE);
    }

}

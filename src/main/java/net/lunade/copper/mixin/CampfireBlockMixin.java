package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.properties.CopperPipeProperties;
import net.lunade.copper.blocks.properties.PipeFluid;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CampfireBlock;isLitCampfire(Lnet/minecraft/world/level/block/state/BlockState;)Z"), method = "isSmokeyPos")
    private static boolean simpleCopperPipes$isSmokeyPos(BlockState blockState, Operation<Boolean> operation) {
        return operation.call(blockState)
                || (blockState.getBlock() instanceof CopperPipe && blockState.getValue(CopperPipeProperties.FLUID) == PipeFluid.SMOKE);
    }

}

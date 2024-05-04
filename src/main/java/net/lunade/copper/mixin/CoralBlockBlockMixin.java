package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.copper.blocks.block_entity.leaking_pipes.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CoralBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CoralBlock.class)
public class CoralBlockBlockMixin {

    @ModifyReturnValue(at = @At("TAIL"), method = "scanForWater")
    protected boolean simpleCopperPipes$isInWater(boolean original, BlockGetter blockView, BlockPos blockPos) {
        return original || LeakingPipeManager.isWaterPipeNearbyBlockGetter(blockView, blockPos, 2);
    }

}

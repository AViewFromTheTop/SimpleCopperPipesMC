package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.copper.blocks.block_entity.leaking_pipes.LeakingPipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin {

    @ModifyReturnValue(at = @At("RETURN"), method = "isNearWater")
    private static boolean simpleCopperPipes$isNearWater(boolean original, LevelReader worldView, BlockPos blockPos) {
        return original || LeakingPipeManager.isWaterPipeNearbyBlockGetter(worldView, blockPos, 6);
    }

}

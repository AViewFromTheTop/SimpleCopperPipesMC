package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.lunade.copper.blocks.CopperPipeProperties;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CampfireBlock;isLitCampfire(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0), method = "isSmokeyPos")
    private static boolean simpleCopperPipes$isSmokeyPosA(BlockState state) {
        return CampfireBlock.isLitCampfire(state) || (state.getBlock() instanceof CopperPipe && state.getValue(CopperPipeProperties.HAS_SMOKE));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CampfireBlock;isLitCampfire(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 1), method = "isSmokeyPos")
    private static boolean simpleCopperPipes$isSmokeyPosB(BlockState state) {
        return CampfireBlock.isLitCampfire(state) || (state.getBlock() instanceof CopperPipe && state.getValue(CopperPipeProperties.HAS_SMOKE));
    }

}

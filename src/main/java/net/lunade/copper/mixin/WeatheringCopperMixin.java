package net.lunade.copper.mixin;

import com.google.common.collect.BiMap;
import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Run before Quilt's block content registry.
// Quilt's priority is 500
@Mixin(value = WeatheringCopper.class, priority = 400)
public interface WeatheringCopperMixin {

    @Inject(method = "method_34740", at = @At("RETURN"), cancellable = true)
    private static void addSimpleCopperPipes(CallbackInfoReturnable<BiMap<Block, Block>> cir) {
        CopperPipeMain.OXIDIZATION.putAll(cir.getReturnValue());
        cir.setReturnValue(CopperPipeMain.OXIDIZATION);
    }

    @Inject(method = "method_34739", at = @At("RETURN"), cancellable = true)
    private static void addSimpleCopperPipes2(CallbackInfoReturnable<BiMap<Block, Block>> cir) {
        CopperPipeMain.UNOXIDIZATION.putAll(cir.getReturnValue());
        cir.setReturnValue(CopperPipeMain.UNOXIDIZATION);
    }
}

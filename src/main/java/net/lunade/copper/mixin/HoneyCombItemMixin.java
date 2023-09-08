package net.lunade.copper.mixin;

import com.google.common.collect.BiMap;
import net.lunade.copper.CopperPipeMain;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Run before Quilt's block content registry.
// Quilt's priority is 500
@Mixin(value = HoneycombItem.class, priority = 400)
public class HoneyCombItemMixin {

    @Inject(method = "method_34723", at = @At("RETURN"), cancellable = true)
    private static void addSimpleCopperPipes(CallbackInfoReturnable<BiMap<Block, Block>> cir) {
        CopperPipeMain.WAXING.putAll(cir.getReturnValue());
        cir.setReturnValue(CopperPipeMain.WAXING);
    }

    @Inject(method = "method_34722", at = @At("RETURN"), cancellable = true)
    private static void addSimpleCopperPipes2(CallbackInfoReturnable<BiMap<Block, Block>> cir) {
        CopperPipeMain.UNWAXING.putAll(cir.getReturnValue());
        cir.setReturnValue(CopperPipeMain.UNWAXING);
    }
}

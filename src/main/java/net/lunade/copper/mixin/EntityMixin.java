package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(at = @At("TAIL"), method = "isWet", cancellable = true)
    public void isWet(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        if (CopperPipe.isWaterPipeNearby(entity.world, entity.getBlockPos(), 2)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "isInsideWaterOrBubbleColumn", cancellable = true)
    public void isInsideWaterOrBubbleColumn(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        if (CopperPipe.isWaterPipeNearby(entity.world, entity.getBlockPos(), 2)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}

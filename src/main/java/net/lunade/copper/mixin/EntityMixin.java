package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(at = @At("TAIL"), method = "isInWaterRainOrBubble", cancellable = true)
    public void isInWaterRainOrBubble(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        if (CopperPipe.isWaterPipeNearby(entity.level, entity.blockPosition(), 2)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "isInWaterOrBubble", cancellable = true)
    public void isInWaterOrBubble(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        if (CopperPipe.isWaterPipeNearby(entity.level, entity.blockPosition(), 2)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}

package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    public boolean hadWaterPipeNearby;

    @Inject(at = @At("TAIL"), method = "updateInWaterStateAndDoWaterCurrentPushing")
    public void updateInWaterStateAndDoWaterCurrentPushing(CallbackInfo info) {
        Entity entity = Entity.class.cast(this);
        this.hadWaterPipeNearby = LeakingPipeManager.isWaterPipeNearby(entity.level, entity.blockPosition(), 2);
    }

    @Inject(at = @At("TAIL"), method = "isInWaterRainOrBubble", cancellable = true)
    public void isInWaterRainOrBubble(CallbackInfoReturnable<Boolean> info) {
        if (this.hadWaterPipeNearby) {
            info.setReturnValue(true);
        }
    }

    @Inject(at = @At("TAIL"), method = "isInWaterOrBubble", cancellable = true)
    public void isInWaterOrBubble(CallbackInfoReturnable<Boolean> info) {
        if (this.hadWaterPipeNearby) {
            info.setReturnValue(true);
        }
    }
}

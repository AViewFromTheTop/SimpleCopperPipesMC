package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Unique
    private boolean hadWaterPipeNearby;

    @Inject(at = @At("HEAD"), method = "updateWaterState")
    public void updateWaterState(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        if (!entity.world.isClient) {
            this.hadWaterPipeNearby = LeakingPipeManager.isWaterPipeNearby(entity.world, entity.getBlockPos(), 2);
        }
    }

    @Inject(at = @At("HEAD"), method = "isTouchingWater", cancellable = true)
    public void isTouchingWater(CallbackInfoReturnable<Boolean> info) {
        if (this.hadWaterPipeNearby) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}

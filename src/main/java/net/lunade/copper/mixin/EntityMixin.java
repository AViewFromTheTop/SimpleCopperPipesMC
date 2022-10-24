package net.lunade.copper.mixin;

import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Unique
    private boolean hadWaterPipeNearby;

    @Inject(at = @At("HEAD"), method = "updateInWaterStateAndDoFluidPushing")
    public void updateInWaterStateAndDoFluidPushing(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        if (!entity.level.isClientSide) {
            this.hadWaterPipeNearby = LeakingPipeManager.isWaterPipeNearby(entity.level, entity.blockPosition(), 2);
        }
    }

    @Inject(at = @At("HEAD"), method = "isInWater", cancellable = true)
    public void isInWater(CallbackInfoReturnable<Boolean> info) {
        if (this.hadWaterPipeNearby) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}
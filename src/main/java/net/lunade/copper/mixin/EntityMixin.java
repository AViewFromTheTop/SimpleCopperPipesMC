package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.copper.leaking_pipes.LeakingPipeManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Unique
    private boolean simpleCopperPipes$hadWaterPipeNearby;

    @Inject(at = @At("HEAD"), method = "updateInWaterStateAndDoFluidPushing")
    public void simpleCopperPipes$updateInWaterStateAndDoFluidPushing(CallbackInfoReturnable<Boolean> info) {
        if (!this.level().isClientSide) {
            this.simpleCopperPipes$hadWaterPipeNearby = LeakingPipeManager.isWaterPipeNearby(Entity.class.cast(this), 2);
        }
    }

    @Inject(at = @At("HEAD"), method = "isInRain", cancellable = true)
    public void simpleCopperPipes$isInRain(CallbackInfoReturnable<Boolean> info) {
        if (this.simpleCopperPipes$hadWaterPipeNearby) {
            info.setReturnValue(true);
        }
    }

    @Shadow
    public Level level() {
        throw new AssertionError("Mixin injection failed - Simple Copper Pipes EntityMixin.");
    }

}
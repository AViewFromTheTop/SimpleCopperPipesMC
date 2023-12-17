package net.lunade.copper.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
    private boolean simpleCopperPipes$hadWaterPipeNearby;

    @Inject(at = @At("HEAD"), method = "updateInWaterStateAndDoFluidPushing")
    public void simpleCopperPipes$updateInWaterStateAndDoFluidPushing(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        if (!entity.level().isClientSide) {
            this.simpleCopperPipes$hadWaterPipeNearby = LeakingPipeManager.isWaterPipeNearby(entity, 2);
        }
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "isInRain")
    public boolean simpleCopperPipes$isInRain(boolean original) {
        return original || this.simpleCopperPipes$hadWaterPipeNearby;
    }

}
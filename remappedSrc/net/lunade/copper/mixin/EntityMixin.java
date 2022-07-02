package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(at = @At("TAIL"), method = "isWet", cancellable = true)
    public void isWet(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        info.setReturnValue(entity.isTouchingWater() || this.isBeingRainedOn(entity) || this.isInsideBubbleColumn(entity) || CopperPipe.isWaterPipeNearby(entity.world, entity.getBlockPos(), 2));
        info.cancel();
    }

    private boolean isBeingRainedOn(Entity entity) {
        BlockPos blockPos = entity.getBlockPos();
        return entity.world.hasRain(blockPos) || entity.world.hasRain(new BlockPos(blockPos.getX(), entity.getBoundingBox().maxY, blockPos.getZ()));
    }

    private boolean isInsideBubbleColumn(Entity entity) {
        return entity.world.getBlockState(entity.getBlockPos()).isOf(Blocks.BUBBLE_COLUMN);
    }

    @Inject(at = @At("TAIL"), method = "isInsideWaterOrBubbleColumn", cancellable = true)
    public void isInsideWaterOrBubbleColumn(CallbackInfoReturnable<Boolean> info) {
        Entity entity = Entity.class.cast(this);
        info.setReturnValue(entity.isTouchingWater() || this.isInsideBubbleColumn(entity) || CopperPipe.isWaterPipeNearby(entity.world, entity.getBlockPos(), 2));
        info.cancel();
    }
}

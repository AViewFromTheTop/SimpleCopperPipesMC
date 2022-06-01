package net.lunade.copper.mixin;

import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Entity.class)
public class EntityMixin {
    /** Check if leaking pipe is nearby
     * @author Mojang (original code) Lunade (added leaking pipe check)
     * @reason actually make pipes work */
    @Overwrite
    public boolean isWet() {
        Entity entity = Entity.class.cast(this);
        return entity.isTouchingWater() || this.isBeingRainedOn(entity) || this.isInsideBubbleColumn(entity) || CopperPipe.isWaterPipeNearby(entity.world, entity.getBlockPos(), 2);
    }
    private boolean isBeingRainedOn(Entity entity) {
        BlockPos blockPos = entity.getBlockPos();
        return entity.world.hasRain(blockPos) || entity.world.hasRain(new BlockPos(blockPos.getX(), entity.getBoundingBox().maxY, blockPos.getZ()));
    }
    private boolean isInsideBubbleColumn(Entity entity) {
        return entity.world.getBlockState(entity.getBlockPos()).isOf(Blocks.BUBBLE_COLUMN);
    }
    /** Make Fish Live Out Of Water
     * @author Mojang (original code) Lunade (added leaking pipe check)
     * @reason actually make pipes work */
    @Overwrite
    public boolean isInsideWaterOrBubbleColumn() {
        Entity entity = Entity.class.cast(this);
        return entity.isTouchingWater() || this.isInsideBubbleColumn(entity) || CopperPipe.isWaterPipeNearby(entity.world, entity.getBlockPos(), 2);
    }
}

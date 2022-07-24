package net.lunade.copper.block_entity;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.Vibration;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CopperPipeListener implements GameEventListener {
    protected final PositionSource positionSource;
    protected final int range;
    protected final Callback callback;
    protected Optional<GameEvent> event = Optional.empty();
    protected int distance;
    protected int delay = 0;

    public CopperPipeListener(PositionSource positionSource, int i, Callback callback) {
        this.positionSource = positionSource;
        this.range = i;
        this.callback = callback;
    }

    public void tick(World world) {
        if (this.event.isPresent()) {
            --this.delay;
            if (this.delay <= 0) {
                this.delay = 0;
                this.callback.accept(world, this, this.event.get(), this.distance);
                this.event = Optional.empty();
            }
        }

    }

    public PositionSource getPositionSource() {
        return this.positionSource;
    }

    public int getRange() {
        return this.range;
    }

    public boolean listen(World world, GameEvent gameEvent, @Nullable Entity entity, BlockPos blockPos) {
        if (this.event.isPresent()) {
            return false;
        } else if (!this.callback.canAccept(gameEvent, entity)) {
            return false;
        } else {
            Optional<BlockPos> optional = this.positionSource.getPos(world);
            if (!optional.isPresent()) {
                return false;
            } else {
                BlockPos blockPos2 = optional.get();
                if (!this.callback.accepts(world, this, blockPos, gameEvent, entity)) {
                    return false;
                } else if (this.isOccluded(world, blockPos, blockPos2)) {
                    return false;
                } else {
                    this.listen(world, gameEvent, blockPos, blockPos2);
                    return true;
                }
            }
        }
    }


    private void listen(World world, GameEvent gameEvent, BlockPos blockPos, BlockPos blockPos2) {
        this.event = Optional.of(gameEvent);
        if (world instanceof ServerWorld) {
            this.distance = MathHelper.floor(Math.sqrt(blockPos.getSquaredDistance(blockPos2)));
            this.delay = this.distance;
            ((ServerWorld)world).sendVibrationPacket(new Vibration(blockPos, this.positionSource, this.delay));
        }

    }

    private boolean isOccluded(World world, BlockPos blockPos, BlockPos blockPos2) {
        return world.raycast(new BlockStateRaycastContext(Vec3d.ofCenter(blockPos), Vec3d.ofCenter(blockPos2), (blockState) -> {
            return blockState.isIn(BlockTags.OCCLUDES_VIBRATION_SIGNALS);
        })).getType() == HitResult.Type.BLOCK;
    }

    public interface Callback {
        boolean accepts(World world, GameEventListener gameEventListener, BlockPos blockPos, GameEvent gameEvent, @Nullable Entity entity);

        void accept(World world, GameEventListener gameEventListener, GameEvent gameEvent, int i);

        boolean canAccept(GameEvent gameEvent, @Nullable Entity entity);
    }
}


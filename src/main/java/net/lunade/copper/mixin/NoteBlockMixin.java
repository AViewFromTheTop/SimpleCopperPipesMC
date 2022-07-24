package net.lunade.copper.mixin;

import net.lunade.copper.Main;
import net.minecraft.block.NoteBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {

    @Inject(at = @At("TAIL"), method = "playNote")
    private void playNote(World world, BlockPos blockPos, CallbackInfo info) {
        if (world.getBlockState(blockPos.up()).isAir()) {
            world.emitGameEvent(Main.NOTE_BLOCK_PLAY, blockPos);
        }
    }

}

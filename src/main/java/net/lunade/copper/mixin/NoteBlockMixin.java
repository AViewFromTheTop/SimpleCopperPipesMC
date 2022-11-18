package net.lunade.copper.mixin;

import net.lunade.copper.CopperPipeMain;
import net.minecraft.block.NoteBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addSyncedBlockEvent(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;II)V"), method = "playNote")
    private void playNote(World world, BlockPos blockPos, CallbackInfo info) {
        world.emitGameEvent(CopperPipeMain.NOTE_BLOCK_PLAY, blockPos);
    }

}

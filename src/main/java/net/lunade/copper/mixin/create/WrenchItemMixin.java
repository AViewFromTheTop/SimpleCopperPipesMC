package net.lunade.copper.mixin.create;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WrenchItem.class)
public class WrenchItemMixin {

    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/equipment/wrench/WrenchItem;onItemUseOnOther(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void simpleCopperPipes$wrenchPickup(UseOnContext context, CallbackInfoReturnable<InteractionResult> info, Player player, BlockState state, Block block) {
        this.simpleCopperPipes$rotateCopperPipes(context, info);
    }

    @Unique
    private void simpleCopperPipes$rotateCopperPipes(@NotNull UseOnContext context, CallbackInfoReturnable<InteractionResult> info) {
        Player player = context.getPlayer();
        if (player == null || player.isShiftKeyDown()) {
            return;
        }
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if (block instanceof CopperFitting) {
            info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide)); // Don't do anything if the player isn't shifting.
        }
        if (block instanceof CopperPipe) {
            IWrenchable wrenchable = new IWrenchable() {
            };
            BlockState rotated = wrenchable.getRotatedBlockState(blockState, context.getClickedFace());

            BlockState state = rotated
                    .setValue(CopperPipe.BACK_CONNECTED, CopperPipe.canConnectBack(world, blockPos, rotated.getValue(CopperPipe.FACING)))
                    .setValue(CopperPipe.FRONT_CONNECTED, CopperPipe.canConnectFront(world, blockPos, rotated.getValue(CopperPipe.FACING)))
                    .setValue(CopperPipe.SMOOTH, CopperPipe.isSmooth(world, blockPos, rotated.getValue(CopperPipe.FACING)));

            world.setBlockAndUpdate(blockPos, state);
            AllSoundEvents.WRENCH_ROTATE.playOnServer(world, blockPos, 1, context.getLevel().getRandom().nextFloat() + 0.5F);
            info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }
}

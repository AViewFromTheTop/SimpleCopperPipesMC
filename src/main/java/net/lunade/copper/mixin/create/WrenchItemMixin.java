package net.lunade.copper.mixin.create;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WrenchItem.class)
public abstract class WrenchItemMixin {

    @Shadow
    private static InteractionResult onItemUseOnOther(UseOnContext context) {
        return null;
    }

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/equipment/wrench/WrenchItem;onItemUseOnOther(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void wrenchPickup(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, Player player, BlockState state, Block block) {
        rotateCopperPipes(context, cir);
    }

    @Unique
    private void rotateCopperPipes(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        if (player.isShiftKeyDown()) {
            return;
        }
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (blockState.getBlock() instanceof CopperPipe pipe) {
            IWrenchable wrenchable = new IWrenchable() {};
            BlockState rotated = wrenchable.getRotatedBlockState(blockState, context.getClickedFace());

            BlockState state = rotated
                    .setValue(CopperPipe.BACK_CONNECTED, CopperPipe.canConnectBack(world, blockPos, rotated.getValue(CopperPipe.FACING)))
                    .setValue(CopperPipe.FRONT_CONNECTED, CopperPipe.canConnectFront(world, blockPos, rotated.getValue(CopperPipe.FACING)))
                    .setValue(CopperPipe.SMOOTH, CopperPipe.isSmooth(world, blockPos, rotated.getValue(CopperPipe.FACING)));

            world.setBlockAndUpdate(blockPos, state);
            AllSoundEvents.WRENCH_ROTATE.playOnServer(world, blockPos, 1, Create.RANDOM.nextFloat() + 0.5F);
            cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
        }
    }
}

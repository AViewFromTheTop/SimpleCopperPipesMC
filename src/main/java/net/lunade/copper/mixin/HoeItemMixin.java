package net.lunade.copper.mixin;

import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext itemUsageContext, CallbackInfoReturnable<ActionResult> info) {
        World world = itemUsageContext.getWorld();
        BlockPos blockPos = itemUsageContext.getBlockPos();
        PlayerEntity playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getStack();

        if (blockState != null && blockState.getBlock() instanceof CopperPipe) {
            if (playerEntity instanceof ServerPlayerEntity player) {
                Criteria.ITEM_USED_ON_BLOCK.trigger(player, blockPos, itemStack);
            }

            Direction face = itemUsageContext.getSide();
            if (face != blockState.get(CopperPipe.FACING)) {
                BlockState state = blockState.with(CopperPipe.FACING, face)
                        .with(CopperPipe.BACK_CONNECTED, CopperPipe.canConnectBack(world, blockPos, face))
                        .with(CopperPipe.FRONT_CONNECTED, CopperPipe.canConnectFront(world, blockPos, face))
                        .with(CopperPipe.SMOOTH, CopperPipe.isSmooth(world, blockPos, face));

                world.setBlockState(blockPos, state);
                world.playSound(null, blockPos, CopperPipeMain.TURN, SoundCategory.BLOCKS, 0.5F, 1F);
                if (playerEntity != null) {
                    itemStack.damage(1, playerEntity, (playerEntityx) -> playerEntityx.sendToolBreakStatus(itemUsageContext.getHand()));
                }
            }
            info.setReturnValue(ActionResult.success(world.isClient));
        }
    }

}
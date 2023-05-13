package net.lunade.copper.mixin;

import net.lunade.copper.CopperPipeMain;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(at = @At("TAIL"), method = "useOn", cancellable = true)
    public void simpleCopperPipes$useOn(UseOnContext itemUsageContext, CallbackInfoReturnable<InteractionResult> info) {
        Level world = itemUsageContext.getLevel();
        BlockPos blockPos = itemUsageContext.getClickedPos();
        Player playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getItemInHand();

        if (itemStack.is(Items.GLOW_INK_SAC)) {
            Block block = blockState.getBlock();
            if (CopperPipeMain.GLOW_STAGE.containsKey(block)) {
                world.playSound(playerEntity, blockPos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.levelEvent(playerEntity, 3005, blockPos, 0);
                if (playerEntity instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) playerEntity, blockPos, itemStack);
                }

                if (CopperPipeMain.GLOW_STAGE.containsKey(block)) {
                    world.setBlockAndUpdate(blockPos, CopperPipeMain.GLOW_STAGE.get(block).withPropertiesOf(blockState));
                }
                if (playerEntity != null) {
                    itemStack.shrink(1);
                }

                info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
            }
        }
    }

}
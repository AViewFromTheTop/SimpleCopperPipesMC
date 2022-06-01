package net.lunade.copper.mixin;

import net.lunade.copper.Main;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
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

    @Inject(at = @At("HEAD"), method = "useOnBlock")
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext, CallbackInfoReturnable info) {
        World world = itemUsageContext.getWorld();
        BlockPos blockPos = itemUsageContext.getBlockPos();
        PlayerEntity playerEntity = itemUsageContext.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = itemUsageContext.getStack();

        boolean pipe = false;
        boolean fitting = false;

        if (blockState!=null && blockState.getBlock() instanceof CopperPipe) {pipe = true;}
        //if (blockState!=null && blockState.getBlock() instanceof CopperFitting && playerEntity != null) {fitting=true;}

        if (pipe) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            Block block = blockState.getBlock();
            if (block instanceof CopperPipe) {
                assert playerEntity != null;
                Direction face = itemUsageContext.getSide();
                if (face!=blockState.get(CopperPipe.FACING)) {
                    BlockState state = blockState.with(CopperPipe.FACING, face);
                    state = state.with(CopperPipe.BACK_CONNECTED, CopperPipe.canConnectBack(world, blockPos, face))
                            .with(CopperPipe.FRONT_CONNECTED, CopperPipe.canConnectFront(world, blockPos, face))
                            .with(CopperPipe.SMOOTH, CopperPipe.isSmooth(world, blockPos, face));
                    world.setBlockState(blockPos, state);
                    //TODO: ADD SOUNDEVENT FOR PIPE TURNING
                    world.playSound(null, blockPos, Main.TURN, SoundCategory.BLOCKS, 0.5F, 1F);
                    itemStack.damage(1, playerEntity, (playerEntityx) -> {
                        playerEntityx.sendToolBreakStatus(itemUsageContext.getHand());
                    });
                }
            }

            return ActionResult.success(world.isClient);
        } /*else if (fitting) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            Block block = blockState.getBlock();
            if (block instanceof CopperFitting) {
                if (CopperFitting.getPreviousStage(world, blockPos)!=null) {
                    CopperFitting.makeCopyOf(blockState, world, blockPos, CopperFitting.getPreviousStage(world, blockPos));
                }
            }
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, (playerEntityx) -> {
                    playerEntityx.sendToolBreakStatus(itemUsageContext.getHand());
                });
            }

            return ActionResult.success(world.isClient);
        }*/ else {
            return ActionResult.PASS;
        }
    }

}

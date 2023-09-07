package net.lunade.copper.mixin;

import com.google.common.collect.ImmutableBiMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Run before Quilt's block content registry.
// Quilt's priority is 500
@Mixin(value = HoneycombItem.class, priority = 400)
public class HoneyCombItemMixin {

    @WrapOperation(method = "method_34723", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableBiMap$Builder;build()Lcom/google/common/collect/ImmutableBiMap;"))
    private static ImmutableBiMap<Block, Block> addSimpleCopperPipes(ImmutableBiMap.Builder<Block, Block> registry, Operation<ImmutableBiMap<Block, Block>> original) {
        // PIPE
        registry.put(CopperPipe.COPPER_PIPE, CopperPipe.WAXED_COPPER_PIPE);
        registry.put(CopperPipe.EXPOSED_PIPE, CopperPipe.WAXED_EXPOSED_PIPE);
        registry.put(CopperPipe.WEATHERED_PIPE, CopperPipe.WAXED_WEATHERED_PIPE);
        registry.put(CopperPipe.OXIDIZED_PIPE, CopperPipe.WAXED_OXIDIZED_PIPE);
        // FITTING
        registry.put(CopperFitting.COPPER_FITTING, CopperFitting.WAXED_COPPER_FITTING);
        registry.put(CopperFitting.EXPOSED_FITTING, CopperFitting.WAXED_EXPOSED_FITTING);
        registry.put(CopperFitting.WEATHERED_FITTING, CopperFitting.WAXED_WEATHERED_FITTING);
        registry.put(CopperFitting.OXIDIZED_FITTING, CopperFitting.WAXED_OXIDIZED_FITTING);

        return original.call(registry);
    }
}

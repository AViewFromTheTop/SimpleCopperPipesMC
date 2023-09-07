package net.lunade.copper.mixin;

import com.google.common.collect.ImmutableBiMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.copper.blocks.CopperFitting;
import net.lunade.copper.blocks.CopperPipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WeatheringCopper.class)
public interface WeatheringCopperMixin {

    @WrapOperation(method = "method_34740", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableBiMap$Builder;build()Lcom/google/common/collect/ImmutableBiMap;"))
    private static ImmutableBiMap<Block, Block> addSimpleCopperPipes(ImmutableBiMap.Builder<Block, Block> registry, Operation<ImmutableBiMap<Block, Block>> original) {
        // PIPE
        registry.put(CopperPipe.COPPER_PIPE, CopperPipe.EXPOSED_PIPE);
        registry.put(CopperPipe.EXPOSED_PIPE, CopperPipe.WEATHERED_PIPE);
        registry.put(CopperPipe.WEATHERED_PIPE, CopperPipe.OXIDIZED_PIPE);
        registry.put(CopperPipe.OXIDIZED_PIPE, CopperPipe.CORRODED_PIPE);
        // FITTING
        registry.put(CopperFitting.COPPER_FITTING, CopperFitting.EXPOSED_FITTING);
        registry.put(CopperFitting.EXPOSED_FITTING, CopperFitting.WEATHERED_FITTING);
        registry.put(CopperFitting.WEATHERED_FITTING, CopperFitting.OXIDIZED_FITTING);
        registry.put(CopperFitting.OXIDIZED_FITTING, CopperFitting.CORRODED_FITTING);

        return original.call(registry);
    }
}

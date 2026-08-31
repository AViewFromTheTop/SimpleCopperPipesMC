package net.lunade.copper.datagen.loot;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.frozenblock.lib.platform.api.registry.DeferredBlock;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.minecraft.core.HolderLookup;

public final class SimpleCopperPipesBlockLootProvider extends FabricBlockLootSubProvider {

	public SimpleCopperPipesBlockLootProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(dataOutput, registries);
	}

	@Override
	public void generate() {
		SimpleCopperPipesBlocks.COPPER_PIPE.map(DeferredBlock::get).forEach(this::dropSelf);
		SimpleCopperPipesBlocks.COPPER_FITTING.map(DeferredBlock::get).forEach(this::dropSelf);
	}
}

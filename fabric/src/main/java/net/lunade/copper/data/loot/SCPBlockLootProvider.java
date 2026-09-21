package net.lunade.copper.data.loot;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.frozenblock.lib.platform.api.registry.DeferredBlock;
import net.lunade.copper.registry.SCPBlocks;
import net.minecraft.core.HolderLookup;

public final class SCPBlockLootProvider extends FabricBlockLootSubProvider {

	public SCPBlockLootProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(dataOutput, registries);
	}

	@Override
	public void generate() {
		SCPBlocks.COPPER_PIPE.map(DeferredBlock::get).forEach(this::dropSelf);
		SCPBlocks.COPPER_FITTING.map(DeferredBlock::get).forEach(this::dropSelf);
	}
}

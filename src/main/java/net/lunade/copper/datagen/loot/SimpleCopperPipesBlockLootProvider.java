package net.lunade.copper.datagen.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lunade.copper.registry.SimpleCopperPipesBlocks;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class SimpleCopperPipesBlockLootProvider extends FabricBlockLootTableProvider {

	public SimpleCopperPipesBlockLootProvider(@NotNull FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(dataOutput, registries);
	}

	@Override
	public void generate() {
		this.dropSelf(SimpleCopperPipesBlocks.COPPER_PIPE);
		this.dropSelf(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE);
		this.dropSelf(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE);
		this.dropSelf(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE);

		this.dropSelf(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE);
		this.dropSelf(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE);
		this.dropSelf(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE);
		this.dropSelf(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE);

		this.dropSelf(SimpleCopperPipesBlocks.COPPER_FITTING);
		this.dropSelf(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING);
		this.dropSelf(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING);
		this.dropSelf(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING);

		this.dropSelf(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING);
		this.dropSelf(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING);
		this.dropSelf(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING);
		this.dropSelf(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
	}

}

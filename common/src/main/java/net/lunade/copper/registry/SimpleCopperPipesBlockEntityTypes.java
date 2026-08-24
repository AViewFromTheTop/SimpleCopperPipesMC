package net.lunade.copper.registry;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.frozenblock.lib.platform.RegistryHelper;
import net.frozenblock.lib.platform.api.registry.DeferredBlock;
import net.frozenblock.lib.platform.api.registry.DeferredHolder;
import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.frozenblock.lib.transfer.api.TransferApi;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.lunade.copper.block.entity.CopperFittingBlockEntity;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.lunade.copper.references.SimpleCopperPipesBlockEntityTypeIDs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class SimpleCopperPipesBlockEntityTypes {
	private static final DeferredRegister<BlockEntityType<?>> REGISTER = RegistryHelper.createDeferredRegister(
		Registries.BLOCK_ENTITY_TYPE,
		SimpleCopperPipesConstants.NAMESPACE
	);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperPipeBlockEntity>> COPPER_PIPE = register(
		SimpleCopperPipesBlockEntityTypeIDs.COPPER_PIPE,
		CopperPipeBlockEntity::new,
		type -> TransferApi.registerItemHandler(type, ((blockEntity, direction) -> blockEntity)),
		SimpleCopperPipesBlocks.COPPER_PIPE.asList()
	);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperFittingBlockEntity>> COPPER_FITTING = register(
		SimpleCopperPipesBlockEntityTypeIDs.COPPER_FITTING,
		CopperFittingBlockEntity::new,
		type -> TransferApi.registerItemHandler(type, ((blockEntity, direction) -> blockEntity)),
		SimpleCopperPipesBlocks.COPPER_FITTING.asList()
	);

	static {
		REGISTER.register();
	}

	public static void init() {}

	@SafeVarargs
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(ResourceKey<BlockEntityType<?>> id, BlockEntityType.BlockEntitySupplier<T> builder, Supplier<Block>... blocks) {
		return register(id, builder, null, blocks);
	}

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(ResourceKey<BlockEntityType<?>> id, BlockEntityType.BlockEntitySupplier<T> builder, Collection<DeferredBlock<? extends Block>> blocks) {
		return register(id, builder, null, blocks);
	}

	@SafeVarargs
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(ResourceKey<BlockEntityType<?>> id, BlockEntityType.BlockEntitySupplier<T> builder, Consumer<BlockEntityType<T>> also, Supplier<Block>... blocks) {
		return REGISTER.register(id, () -> new BlockEntityType<>(builder, Arrays.stream(blocks).map(Supplier::get).collect(Collectors.toSet())), also);
	}

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(ResourceKey<BlockEntityType<?>> id, BlockEntityType.BlockEntitySupplier<T> builder, Consumer<BlockEntityType<T>> also, Collection<DeferredBlock<? extends Block>> blocks) {
		return REGISTER.register(id, () -> new BlockEntityType<>(builder, blocks.stream().map(Supplier::get).collect(Collectors.toSet())), also);
	}
}

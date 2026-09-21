/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Simple Copper Pipes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

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
import net.lunade.copper.SCPConstants;
import net.lunade.copper.block.entity.CopperFittingBlockEntity;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.lunade.copper.references.SCPBlockEntityTypeIDs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class SCPBlockEntityTypes {
	private static final DeferredRegister<BlockEntityType<?>> REGISTER = RegistryHelper.createDeferredRegister(
		Registries.BLOCK_ENTITY_TYPE,
		SCPConstants.NAMESPACE
	);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperPipeBlockEntity>> COPPER_PIPE = register(
		SCPBlockEntityTypeIDs.COPPER_PIPE,
		CopperPipeBlockEntity::new,
		type -> TransferApi.registerItemHandler(type, ((blockEntity, direction) -> blockEntity)),
		SCPBlocks.COPPER_PIPE.asList()
	);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperFittingBlockEntity>> COPPER_FITTING = register(
		SCPBlockEntityTypeIDs.COPPER_FITTING,
		CopperFittingBlockEntity::new,
		type -> TransferApi.registerItemHandler(type, ((blockEntity, direction) -> blockEntity)),
		SCPBlocks.COPPER_FITTING.asList()
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

	private SCPBlockEntityTypes() {}
}

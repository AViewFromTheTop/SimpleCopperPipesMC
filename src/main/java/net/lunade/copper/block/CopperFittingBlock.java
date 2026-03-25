package net.lunade.copper.block;

import com.mojang.serialization.MapCodec;
import net.lunade.copper.block.entity.CopperFittingBlockEntity;
import net.lunade.copper.block.properties.PipeFluid;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.SimpleCopperPipesBlockEntityTypes;
import net.lunade.copper.registry.SimpleCopperPipesBlockStateProperties;
import net.lunade.copper.registry.SimpleCopperPipesStats;
import net.lunade.copper.tag.SimpleCopperPipesBlockTags;
import net.lunade.copper.tag.SimpleCopperPipesItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CopperFittingBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final MapCodec<CopperFittingBlock> CODEC = simpleCodec(CopperFittingBlock::new);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final EnumProperty<PipeFluid> FLUID = SimpleCopperPipesBlockStateProperties.FLUID;
	public static final BooleanProperty HAS_ELECTRICITY = SimpleCopperPipesBlockStateProperties.HAS_ELECTRICITY;
	private static final VoxelShape FITTING_SHAPE = Block.box(3D, 3D, 3D, 13D, 13D, 13D);

	public CopperFittingBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(POWERED, false)
			.setValue(WATERLOGGED, false)
			.setValue(FLUID, PipeFluid.NONE)
			.setValue(HAS_ELECTRICITY, false)
		);
	}

	public static void updateBlockEntityValues(Level level, BlockPos pos, BlockState state) {
		if (!(state.getBlock() instanceof CopperFittingBlock)) return;
		if (!(level.getBlockEntity(pos) instanceof CopperFittingBlockEntity fitting)) return;
		fitting.canWater = state.getValue(BlockStateProperties.WATERLOGGED) && SimpleCopperPipesConfig.CARRY_WATER.get();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return FITTING_SHAPE;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return FITTING_SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState()
			.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
			.setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	protected BlockState updateShape(
		BlockState state,
		LevelReader level,
		ScheduledTickAccess tickAccess,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		RandomSource random
	) {
		if (state.getValue(WATERLOGGED)) tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		boolean electricity = state.getValue(HAS_ELECTRICITY);
		if (neighborState.getBlock() instanceof LightningRodBlock && neighborState.getValue(POWERED)) electricity = true;
		return state.setValue(HAS_ELECTRICITY, electricity);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean bl) {
		level.setBlockAndUpdate(blockPos, state.setValue(CopperFittingBlock.POWERED, level.hasNeighborSignal(blockPos)));
		updateBlockEntityValues(level, blockPos, state);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CopperFittingBlockEntity(pos, state);
	}

	@Override
	protected boolean propagatesSkylightDown(BlockState state) {
		return state.getFluidState().isEmpty();
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (level.isClientSide()) return null;
		return createTickerHelper(
			blockEntityType,
			SimpleCopperPipesBlockEntityTypes.COPPER_FITTING,
			(level1, pos1, state1, blockEntity) -> blockEntity.serverTick(level1, pos1, state1)
		);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		super.setPlacedBy(level, pos, state, entity, stack);
		updateBlockEntityValues(level, pos, state);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		if (state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
		return super.getFluidState(state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!SimpleCopperPipesConfig.OPENABLE_FITTINGS.get()) return super.useWithoutItem(state, level, pos, player, hitResult);
		if (!(level.getBlockEntity(pos) instanceof CopperFittingBlockEntity fittingEntity)) return InteractionResult.PASS;
		player.openMenu(fittingEntity);
		player.awardStat(Stats.CUSTOM.get(SimpleCopperPipesStats.INSPECT_FITTING));
		return InteractionResult.SUCCESS;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.is(SimpleCopperPipesItemTags.IGNORES_COPPER_PIPE_MENU)) return InteractionResult.PASS;
		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	public int getCooldown() {
		return 0;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, POWERED, FLUID, HAS_ELECTRICITY);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return WeatheringCopper.getNext(state.getBlock()).isPresent();
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean bl) {
		updateBlockEntityValues(level, pos, state);
		Containers.updateNeighboursAfterDestroy(state, level, pos);
	}

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
		if (blockState.getValue(HAS_ELECTRICITY)) ParticleUtils.spawnParticlesAlongAxis(Direction.UP.getAxis(), level, blockPos, 0.55D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
	}

	@Override
	public boolean shouldChangedStateKeepBlockEntity(BlockState state) {
		return state.is(SimpleCopperPipesBlockTags.COPPER_FITTINGS);
	}

	@Override
	protected MapCodec<? extends CopperFittingBlock> codec() {
		return CODEC;
	}
}

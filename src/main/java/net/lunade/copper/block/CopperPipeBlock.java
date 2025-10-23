package net.lunade.copper.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.lunade.copper.block.entity.CopperPipeBlockEntity;
import net.lunade.copper.block.entity.leaking.LeakingPipeDripBehaviors;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopperPipeBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	private static final VoxelShape SHAPE = Shapes.or(Block.box(4D, 4D, 2D, 12D, 12D, 16D), Block.box(3D, 3D, 0.D, 13D, 13D, 2D));
	private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(SHAPE);
	private static final VoxelShape FRONT_SHAPE = Shapes.or(Block.box(4D, 4D, -2D, 12D, 12D, 16D), Block.box(3D, 3D, -4D, 13D, 13D, -2D));
	private static final Map<Direction, VoxelShape> FRONT_SHAPES = Shapes.rotateAll(FRONT_SHAPE);
	private static final VoxelShape BACK_SHAPE = Shapes.or(Block.box(4D, 4D, 2D, 12D, 12D, 20D), Block.box(3D, 3D, 0.D, 13D, 13D, 2D));
	private static final Map<Direction, VoxelShape> BACK_SHAPES = Shapes.rotateAll(BACK_SHAPE);
	private static final VoxelShape DOUBLE_SHAPE = Block.box(4D, 4D, -4D, 12D, 12D, 20D);
	private static final Map<Direction.Axis, VoxelShape> DOUBLE_SHAPES = Shapes.rotateAllAxis(DOUBLE_SHAPE);
	private static final VoxelShape SMOOTH_SHAPE = Block.box(4D, 4D, 0D, 12D, 12D, 16D);
	private static final Map<Direction.Axis, VoxelShape> SMOOTH_SHAPES = Shapes.rotateAllAxis(SMOOTH_SHAPE);
	private static final VoxelShape BACK_SMOOTH_SHAPE = Block.box(4D, 4D, 0D, 12D, 12D, 20D);
	private static final Map<Direction, VoxelShape> BACK_SMOOTH_SHAPES = Shapes.rotateAll(BACK_SMOOTH_SHAPE);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final BooleanProperty FRONT_CONNECTED = SimpleCopperPipesBlockStateProperties.FRONT_CONNECTED;
	public static final BooleanProperty BACK_CONNECTED = SimpleCopperPipesBlockStateProperties.BACK_CONNECTED;
	public static final BooleanProperty SMOOTH = SimpleCopperPipesBlockStateProperties.SMOOTH;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<PipeFluid> FLUID = SimpleCopperPipesBlockStateProperties.FLUID;
	public static final BooleanProperty HAS_ELECTRICITY = SimpleCopperPipesBlockStateProperties.HAS_ELECTRICITY;
	public static final MapCodec<CopperPipeBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
		propertiesCodec(),
		Codec.INT.fieldOf("dispense_shot_power").forGetter((copperPipe) -> copperPipe.dispenseShotPower)
	).apply(instance, CopperPipeBlock::new));
	public final int dispenseShotPower;

	public CopperPipeBlock(Properties settings, int dispenseShotPower) {
		super(settings);
		this.dispenseShotPower = dispenseShotPower;
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(FACING, Direction.DOWN)
			.setValue(SMOOTH, false)
			.setValue(WATERLOGGED, false)
			.setValue(FLUID, PipeFluid.NONE)
			.setValue(HAS_ELECTRICITY, false)
			.setValue(POWERED, false)
		);
	}

	public static void updateBlockEntityValues(@NotNull LevelReader level, BlockPos pos, @NotNull BlockState state) {
		if (level.getBlockEntity(pos) instanceof CopperPipeBlockEntity pipe) pipe.updateBlockEntityValues(level, pos, state, SimpleCopperPipesConfig.get());
	}

	public static boolean canConnectFront(@NotNull LevelReader level, @NotNull BlockPos pos, Direction direction) {
		final BlockState state = level.getBlockState(pos.relative(direction));
		if (state.getBlock() instanceof CopperPipeBlock) return state.getValue(CopperPipeBlock.FACING) != direction.getOpposite() && state.getValue(CopperPipeBlock.FACING) != direction;
		return state.getBlock() instanceof CopperFittingBlock;
	}

	public static boolean canConnectBack(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Direction direction) {
		final BlockState state = level.getBlockState(pos.relative(direction.getOpposite()));
		if (state.getBlock() instanceof CopperPipeBlock) return state.getValue(CopperPipeBlock.FACING) != direction.getOpposite() && state.getValue(CopperPipeBlock.FACING) != direction;
		return state.getBlock() instanceof CopperFittingBlock;
	}

	public static boolean isSmooth(@NotNull LevelReader level, @NotNull BlockPos pos, Direction direction) {
		final BlockState state = level.getBlockState(pos.relative(direction));
		if (state.getBlock() instanceof CopperPipeBlock) return state.getValue(CopperPipeBlock.FACING) == direction && !canConnectFront(level, pos, direction);
		return false;
	}

	public static boolean canConnectFront(@NotNull LevelAccessor level, @NotNull BlockPos pos, Direction direction) {
		final BlockState state = level.getBlockState(pos.relative(direction));
		if (state.getBlock() instanceof CopperPipeBlock) return state.getValue(CopperPipeBlock.FACING) != direction.getOpposite() && state.getValue(CopperPipeBlock.FACING) != direction;
		return state.getBlock() instanceof CopperFittingBlock;
	}

	public static boolean canConnectBack(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull Direction direction) {
		final BlockState state = level.getBlockState(pos.relative(direction.getOpposite()));
		if (state.getBlock() instanceof CopperPipeBlock) return state.getValue(CopperPipeBlock.FACING) != direction.getOpposite() && state.getValue(CopperPipeBlock.FACING) != direction;
		return state.getBlock() instanceof CopperFittingBlock;
	}

	public static boolean isSmooth(@NotNull LevelAccessor level, @NotNull BlockPos pos, Direction direction) {
		final BlockState state = level.getBlockState(pos.relative(direction));
		if (state.getBlock() instanceof CopperPipeBlock) return state.getValue(CopperPipeBlock.FACING) == direction && !canConnectFront(level, pos, direction);
		return false;
	}

	public VoxelShape getPipeShape(@NotNull BlockState state) {
		final boolean front = state.getValue(FRONT_CONNECTED);
		final boolean back = state.getValue(BACK_CONNECTED);
		final boolean smooth = state.getValue(SMOOTH);
		final Direction facing = state.getValue(FACING);
		if (smooth && back) return BACK_SMOOTH_SHAPES.get(facing);
		if (smooth) return SMOOTH_SHAPES.get(facing.getAxis());
		if (front && back) return DOUBLE_SHAPES.get(facing.getAxis());
		if (front) return FRONT_SHAPES.get(facing);
		if (back) return BACK_SHAPES.get(facing);
		return SHAPES.get(facing);
	}

	@Override
	@NotNull
	public VoxelShape getShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos, CollisionContext shapeContext) {
		return getPipeShape(blockState);
	}

	@Override
	@NotNull
	public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
		return getPipeShape(blockState);
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext itemPlacementContext) {
		Direction direction = itemPlacementContext.getClickedFace();
		BlockPos blockPos = itemPlacementContext.getClickedPos();
		return this.defaultBlockState()
			.setValue(FACING, direction)
			.setValue(FRONT_CONNECTED, canConnectFront(itemPlacementContext.getLevel(), blockPos, direction))
			.setValue(BACK_CONNECTED, canConnectBack(itemPlacementContext.getLevel(), blockPos, direction))
			.setValue(SMOOTH, isSmooth(itemPlacementContext.getLevel(), blockPos, direction))
			.setValue(WATERLOGGED, itemPlacementContext.getLevel().getFluidState(blockPos).getType() == Fluids.WATER);
	}

	@Override
	protected @NotNull BlockState updateShape(
		@NotNull BlockState state,
		LevelReader level,
		ScheduledTickAccess scheduledTickAccess,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		RandomSource random
	) {
		if (state.getValue(WATERLOGGED)) scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		boolean electricity = state.getValue(HAS_ELECTRICITY);
		if (neighborState.getBlock() instanceof LightningRodBlock && neighborState.getValue(POWERED)) electricity = true;

		final Direction facing = state.getValue(FACING);
		final BlockState finalState = state
			.setValue(FRONT_CONNECTED, canConnectFront(level, pos, facing))
			.setValue(BACK_CONNECTED, canConnectBack(level, pos, facing))
			.setValue(SMOOTH, isSmooth(level, pos, facing))
			.setValue(HAS_ELECTRICITY, electricity);
		if (direction.getAxis() == facing.getAxis()) updateBlockEntityValues(level, pos, finalState);
		return finalState;
	}

	@Override
	protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean bl) {
		final boolean powered = level.hasNeighborSignal(pos);
		if (powered != state.getValue(POWERED)) level.setBlockAndUpdate(pos, state.setValue(POWERED, powered));
		updateBlockEntityValues(level, pos, state);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new CopperPipeBlockEntity(blockPos, blockState);
	}

	@Override
	protected boolean propagatesSkylightDown(@NotNull BlockState blockState) {
		return blockState.getFluidState().isEmpty();
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (level.isClientSide()) return null;
		return createTickerHelper(
			blockEntityType,
			SimpleCopperPipesBlockEntityTypes.COPPER_PIPE,
			(level1, pos1, state1, blockEntity) -> blockEntity.serverTick(level1, pos1, state1, SimpleCopperPipesConfig.get())
		);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity) {
		if (blockEntity instanceof CopperPipeBlockEntity pipeEntity) return pipeEntity.getListener();
		return null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity livingEntity, @NotNull ItemStack stack) {
		super.setPlacedBy(level, pos, state, livingEntity, stack);
		updateBlockEntityValues(level, pos, state);
	}

	@Override
	@NotNull
	public FluidState getFluidState(@NotNull BlockState state) {
		if (state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
		return super.getFluidState(state);
	}

	@Override
	protected @NotNull InteractionResult useWithoutItem(BlockState state, @NotNull Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!(level.getBlockEntity(pos) instanceof CopperPipeBlockEntity copperPipeBlockEntity)) return InteractionResult.PASS;
		player.openMenu(copperPipeBlockEntity);
		player.awardStat(Stats.CUSTOM.get(SimpleCopperPipesStats.INSPECT_PIPE));
		return InteractionResult.SUCCESS;
	}

	@Override
	protected @NotNull InteractionResult useItemOn(
		@NotNull ItemStack stack,
		BlockState state,
		Level level,
		BlockPos pos,
		Player player,
		InteractionHand hand,
		BlockHitResult hitResult
	) {
		if (stack.is(SimpleCopperPipesItemTags.IGNORES_COPPER_PIPE_MENU)) return InteractionResult.PASS;
		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	public int getCooldown() {
		return 1;
	}

	@Override
	@NotNull
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState blockState) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, @NotNull Level level, BlockPos blockPos, Direction direction) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
	}

	@Override
	@NotNull
	public BlockState rotate(@NotNull BlockState blockState, @NotNull Rotation blockRotation) {
		return blockState.setValue(FACING, blockRotation.rotate(blockState.getValue(FACING)));
	}

	@Override
	@NotNull
	public BlockState mirror(@NotNull BlockState blockState, @NotNull Mirror blockMirror) {
		return blockState.rotate(blockMirror.getRotation(blockState.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, FRONT_CONNECTED, BACK_CONNECTED, SMOOTH, WATERLOGGED, FLUID, HAS_ELECTRICITY, POWERED);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

	@Override
	public void randomTick(@NotNull BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
		final Direction direction = blockState.getValue(FACING);
		final boolean isLava = blockState.getValue(FLUID) == PipeFluid.LAVA;
		if (blockState.getValue(FLUID) == PipeFluid.WATER || isLava && direction != Direction.UP) {
			if (random.nextFloat() > (isLava ? 0.05859375F : 0.17578125F) * 2) return;

			final BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
			boolean hasOffset = false;
			for (int i = 0; i < 12; i++) { //Searches for 12 blocks
				if (direction != Direction.DOWN && !hasOffset) {
					mutableBlockPos.move(direction);
					hasOffset = true;
				}
				mutableBlockPos.move(Direction.DOWN);
				final BlockState state = level.getBlockState(mutableBlockPos);
				if (level.getFluidState(mutableBlockPos).isEmpty()) {
					final LeakingPipeDripBehaviors.DripOn dripOn = LeakingPipeDripBehaviors.getDrip(state.getBlock());
					if (dripOn != null) {
						dripOn.dripOn(isLava, level, mutableBlockPos, state);
						break;
					}
					if (state.getCollisionShape(level, mutableBlockPos) != Shapes.empty()) break;
				} else {
					break;
				}
			}
		}
	}

	@Override
	public boolean isRandomlyTicking(@NotNull BlockState blockState) {
		return blockState.getValue(FLUID) == PipeFluid.WATER || blockState.getValue(FLUID) == PipeFluid.LAVA;
	}

	@Override
	public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, RandomSource random) {
		final Direction facing = blockState.getValue(FACING);
		final BlockPos facingPos = blockPos.relative(facing);
		final BlockState facingState = level.getBlockState(facingPos);
		final FluidState fluidState = facingState.getFluidState();
		boolean canWater = blockState.getValue(FLUID) == PipeFluid.WATER && facing != Direction.UP;
		boolean canLava = blockState.getValue(FLUID) == PipeFluid.LAVA && random.nextInt(2) == 0 && facing != Direction.UP;
		boolean canSmoke = blockState.getValue(FLUID) == PipeFluid.SMOKE && random.nextInt(5) == 0;
		boolean canWaterOrLava = canWater || canLava;
		boolean hasSmokeOrWaterOrLava = canWaterOrLava || canSmoke;
		if (hasSmokeOrWaterOrLava) {
			final double outX = blockPos.getX() + getDripX(facing, random);
			final double outY = blockPos.getY() + getDripY(facing, random);
			final double outZ = blockPos.getZ() + getDripZ(facing, random);
			if (canWaterOrLava && (fluidState.isEmpty() || ((fluidState.getHeight(level, facingPos)) + (double) facingPos.getY()) < outY)) {
				level.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA, outX, outY, outZ, 0, 0, 0);
			}
			if (canSmoke) level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, outX, outY, outZ, 0, 0.07D, 0);
			if ((!facingState.isAir() && fluidState.isEmpty())) {
				final double x = blockPos.getX() + getDripX(facing, random);
				final double y = blockPos.getY() + getDripY(facing, random);
				final double z = blockPos.getZ() + getDripZ(facing, random);
				if (canWaterOrLava && facing == Direction.DOWN) {
					level.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA, x, outY, z, 0, 0, 0);
				}
				if (canSmoke && facing == Direction.UP) level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.07D, 0);
			}
		}

		if (blockState.getValue(HAS_ELECTRICITY)) {
			ParticleUtils.spawnParticlesAlongAxis(facing.getAxis(), level, blockPos, 0.4D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
		}
		if (fluidState.is(FluidTags.WATER) && (random.nextFloat() <= 0.1F || facingState.getCollisionShape(level, facingPos).isEmpty())) {
			level.addParticle(
				ParticleTypes.BUBBLE,
				blockPos.getX() + getDripX(facing, random),
				blockPos.getY() + getDripY(facing, random),
				blockPos.getZ() + getDripZ(facing, random),
				facing.getStepX() * 0.7D,
				facing.getStepY() * 0.7D,
				facing.getStepZ() * 0.7D
			);
			if ((canLava || canSmoke) && random.nextInt(2) == 0) {
				level.addParticle(
					ParticleTypes.SMOKE,
					blockPos.getX() + getDripX(facing, random),
					blockPos.getY() + getDripY(facing, random),
					blockPos.getZ() + getDripZ(facing, random),
					facing.getStepX() * 0.05D,
					facing.getStepY() * 0.05D,
					facing.getStepZ() * 0.05D
				);
			}
		}
	}

	public double getRan(RandomSource random) {
		return UniformInt.of(-25, 25).sample(random) * 0.01;
	}

	public double getDripX(@NotNull Direction direction, RandomSource random) {
		return switch (direction) {
			case DOWN, SOUTH, NORTH -> 0.5 + getRan(random);
			case UP -> 0.5;
			case EAST -> 1.05;
			case WEST -> -0.05;
		};
	}

	public double getDripY(@NotNull Direction direction, RandomSource random) {
		return switch (direction) {
			case DOWN -> -0.05;
			case UP -> 1.05;
			case NORTH, WEST, EAST, SOUTH -> 0.4375 + Mth.clamp(getRan(random), -2, 0.625);
		};
	}

	public double getDripZ(@NotNull Direction direction, RandomSource random) {
		return switch (direction) {
			case DOWN, EAST, WEST -> 0.5 + getRan(random);
			case UP -> 0.5;
			case NORTH -> -0.05;
			case SOUTH -> 1.05;
		};
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean bl) {
		updateBlockEntityValues(level, pos, state);
		Containers.updateNeighboursAfterDestroy(state, level, pos);
	}

	@Override
	public boolean shouldChangedStateKeepBlockEntity(BlockState state) {
		return state.is(SimpleCopperPipesBlockTags.COPPER_PIPES);
	}

	@Override
	protected @NotNull MapCodec<? extends CopperPipeBlock> codec() {
		return CODEC;
	}
}

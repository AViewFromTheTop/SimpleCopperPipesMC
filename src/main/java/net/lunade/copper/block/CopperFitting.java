package net.lunade.copper.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lunade.copper.block.entity.CopperFittingEntity;
import net.lunade.copper.block.properties.PipeFluid;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.SimpleCopperPipesBlockEntityTypes;
import net.lunade.copper.registry.SimpleCopperPipesBlockStateProperties;
import net.lunade.copper.registry.SimpleCopperPipesStats;
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
import net.minecraft.world.level.block.*;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopperFitting extends BaseEntityBlock implements SimpleWaterloggedBlock, WeatheringCopper {
    public static final MapCodec<CopperFitting> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            WeatherState.CODEC.fieldOf("weather_state").forGetter((copperFitting -> copperFitting.weatherState)),
            propertiesCodec(),
            Codec.INT.fieldOf("cooldown").forGetter((copperFitting) -> copperFitting.cooldown)
    ).apply(instance, CopperFitting::new));
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<PipeFluid> FLUID = SimpleCopperPipesBlockStateProperties.FLUID;
    public static final BooleanProperty HAS_ELECTRICITY = SimpleCopperPipesBlockStateProperties.HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM = SimpleCopperPipesBlockStateProperties.HAS_ITEM;
    private static final VoxelShape FITTING_SHAPE = Block.box(2.5D, 2.5D, 2.5D, 13.5D, 13.5D, 13.5D);
    public final int cooldown;
    private final WeatherState weatherState;

    public CopperFitting(WeatherState weatherState, Properties settings, int cooldown) {
        super(settings);
        this.weatherState = weatherState;
        this.cooldown = cooldown;
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(WATERLOGGED, false).setValue(FLUID, PipeFluid.NONE).setValue(HAS_ELECTRICITY, false).setValue(HAS_ITEM, false));
    }

    public CopperFitting(Properties settings, int cooldown) {
        this(WeatherState.UNAFFECTED, settings, cooldown);
    }

    public static void updateBlockEntityValues(Level level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof CopperFittingEntity fitting) {
                fitting.canWater = state.getValue(BlockStateProperties.WATERLOGGED) && SimpleCopperPipesConfig.get().carryWater;
            }
        }
    }

    @Override
    @NotNull
    public VoxelShape getShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos, CollisionContext shapeContext) {
        return FITTING_SHAPE;
    }

    @Override
    @NotNull
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        return FITTING_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext itemPlacementContext) {
        return this.defaultBlockState()
                .setValue(WATERLOGGED, itemPlacementContext.getLevel().getFluidState(itemPlacementContext.getClickedPos()).getType() == Fluids.WATER)
                .setValue(POWERED, itemPlacementContext.getLevel().hasNeighborSignal(itemPlacementContext.getClickedPos()));
    }

    @Override
    protected @NotNull BlockState updateShape(
            @NotNull BlockState blockState,
            LevelReader levelReader,
            ScheduledTickAccess scheduledTickAccess,
            BlockPos blockPos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource randomSource
    ) {
        if (blockState.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelReader));
        }
        boolean electricity = blockState.getValue(HAS_ELECTRICITY);
        if (neighborState.getBlock() instanceof LightningRodBlock) {
            if (neighborState.getValue(POWERED)) {
                electricity = true;
            }
        }
        return blockState.setValue(HAS_ELECTRICITY, electricity);
    }

    @Override
    protected void neighborChanged(BlockState blockState, @NotNull Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean bl) {
        if (level.hasNeighborSignal(blockPos)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(CopperFitting.POWERED, true));
        } else {
            level.setBlockAndUpdate(blockPos, blockState.setValue(CopperFitting.POWERED, false));
        }
        updateBlockEntityValues(level, blockPos, blockState);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CopperFittingEntity(blockPos, blockState);
    }

    @Override
    protected boolean propagatesSkylightDown(@NotNull BlockState blockState) {
        return blockState.getFluidState().isEmpty();
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return createTickerHelper(blockEntityType, SimpleCopperPipesBlockEntityTypes.COPPER_FITTING_ENTITY, (level1, blockPos, blockState1, copperFittingEntity) ->
                    copperFittingEntity.serverTick(level1, blockPos, blockState1)
            );
        }
        return null;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        updateBlockEntityValues(level, blockPos, blockState);
    }

    @Override
    @NotNull
    public FluidState getFluidState(@NotNull BlockState blockState) {
        if (blockState.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(blockState);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!SimpleCopperPipesConfig.get().openableFittings)
            return super.useWithoutItem(state, level, pos, player, hitResult);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CopperFittingEntity fittingEntity) {
            player.openMenu(fittingEntity);
            player.awardStat(Stats.CUSTOM.get(SimpleCopperPipesStats.INSPECT_FITTING));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
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
        if (stack.is(SimpleCopperPipesItemTags.IGNORES_COPPER_PIPE_MENU)) {
            return InteractionResult.PASS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
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
    public int getAnalogOutputSignal(BlockState blockState, @NotNull Level level, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, POWERED, FLUID, HAS_ELECTRICITY, HAS_ITEM);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        this.changeOverTime(blockState, serverLevel, blockPos, random);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState blockState) {
        return WeatheringCopper.getNext(blockState.getBlock()).isPresent();
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateBlockEntityValues(level, blockPos, blockState);
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperFitting)) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperFittingEntity) {
                Containers.dropContents(level, blockPos, (CopperFittingEntity) blockEntity);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
            level.removeBlockEntity(blockPos);
        }
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        if (blockState.getValue(HAS_ELECTRICITY)) {
            ParticleUtils.spawnParticlesAlongAxis(Direction.UP.getAxis(), level, blockPos, 0.55D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
        }
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    protected @NotNull MapCodec<? extends CopperFitting> codec() {
        return CODEC;
    }
}

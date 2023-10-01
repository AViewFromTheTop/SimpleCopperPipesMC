package net.lunade.copper.blocks;

import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;
import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.lunade.copper.blocks.properties.CopperPipeProperties;
import net.lunade.copper.blocks.properties.PipeFluid;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.RegisterCopperBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopperFitting extends BaseEntityBlock implements SimpleWaterloggedBlock, WeatheringCopper {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<PipeFluid> FLUID = CopperPipeProperties.FLUID;
    public static final BooleanProperty HAS_ELECTRICITY = CopperPipeProperties.HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM = CopperPipeProperties.HAS_ITEM;
    private static final VoxelShape FITTING_SHAPE = Block.box(2.5D, 2.5D, 2.5D, 13.5D, 13.5D, 13.5D);

    private final WeatherState weatherState;
    public final int cooldown;
    public final ParticleOptions ink;

    public CopperFitting(WeatherState weatherState, Properties settings, int cooldown, ParticleOptions ink) {
        super(settings);
        this.weatherState = weatherState;
        this.cooldown = cooldown;
        this.ink = ink;
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(WATERLOGGED, false).setValue(FLUID, PipeFluid.NONE).setValue(HAS_ELECTRICITY, false).setValue(HAS_ITEM, false));
    }

    public CopperFitting(Properties settings, int cooldown, ParticleOptions ink) {
        this(WeatherState.UNAFFECTED, settings, cooldown, ink);
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
    @NotNull
    public BlockState updateShape(@NotNull BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        boolean electricity = blockState.getValue(HAS_ELECTRICITY);
        if (levelAccessor.getBlockState(blockPos2).getBlock() instanceof LightningRodBlock) {
            if (levelAccessor.getBlockState(blockPos2).getValue(POWERED)) {
                electricity = true;
            }
        } return blockState.setValue(HAS_ELECTRICITY, electricity);
    }

    @Override
    public void neighborChanged(BlockState blockState, @NotNull Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
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
    public boolean propagatesSkylightDown(@NotNull BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        return blockState.getFluidState().isEmpty();
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return createTickerHelper(blockEntityType, RegisterCopperBlockEntities.COPPER_FITTING_ENTITY, (level1, blockPos, blockState1, copperFittingEntity) ->
                    copperFittingEntity.serverTick(level1, blockPos, blockState1)
            );
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            if (level.getBlockEntity(blockPos) instanceof CopperFittingEntity copperFittingEntity) {
                copperFittingEntity.setCustomName(itemStack.getHoverName());
            }
        }
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
    @NotNull
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, @NotNull Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!SimpleCopperPipesConfig.get().openableFittings) return super.use(blockState, level, blockPos, player, hand, blockHitResult);

        Item item = player.getItemInHand(hand).getItem();
        if (TagUtil.isIn(CopperPipeMain.IGNORES_COPPER_PIPE_MENU, item)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof CopperFittingEntity fittingEntity) {
            player.openMenu(fittingEntity);
            player.awardStat(Stats.CUSTOM.get(CopperPipeMain.INSPECT_FITTING));
        }
        return InteractionResult.CONSUME;
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
    protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, POWERED, FLUID, HAS_ELECTRICITY, HAS_ITEM);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockView, BlockPos blockPos, PathComputationType navigationType) {
        return false;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        this.onRandomTick(blockState, serverLevel, blockPos, random);
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
    public boolean isRandomlyTicking(@NotNull BlockState blockState) {
        Block block = blockState.getBlock();
        return block == CopperFitting.COPPER_FITTING || block == CopperFitting.EXPOSED_FITTING || block == CopperFitting.WEATHERED_FITTING;
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
    public WeatherState getAge() {
        return this.weatherState;
    }

    public static final Block COPPER_FITTING = new CopperFitting(WeatherState.UNAFFECTED, Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block EXPOSED_FITTING = new CopperFitting(WeatherState.EXPOSED, Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block WEATHERED_FITTING = new CopperFitting(WeatherState.WEATHERED, Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block OXIDIZED_FITTING = new CopperFitting(WeatherState.OXIDIZED, Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK);

    public static final Block WAXED_COPPER_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_EXPOSED_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_WEATHERED_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_OXIDIZED_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK);

    public static final Block CORRODED_FITTING = new CopperFitting(WeatherState.OXIDIZED, Properties
            .of().mapColor(MapColor.QUARTZ)
            .requiresCorrectToolForDrops()
            .strength(2F, 3.5F)
            .sound(new SoundType(1.0f, 1.25f,
                    CopperPipeMain.CORRODED_COPPER_PLACE,
                    CopperPipeMain.CORRODED_COPPER_STEP,
                    CopperPipeMain.CORRODED_COPPER_BREAK,
                    CopperPipeMain.CORRODED_COPPER_FALL,
                    CopperPipeMain.CORRODED_COPPER_HIT
    )), 4, ParticleTypes.SQUID_INK);

    public static final Block BLACK_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, ParticleTypes.SQUID_INK);
    public static final Block RED_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.RED_INK);
    public static final Block GREEN_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.GREEN_INK);
    public static final Block BROWN_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.BROWN_INK);
    public static final Block BLUE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.BLUE_INK);
    public static final Block PURPLE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.PURPLE_INK);
    public static final Block CYAN_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.CYAN_INK);
    public static final Block LIGHT_GRAY_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.LIGHT_GRAY_INK);
    public static final Block GRAY_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.GRAY_INK);
    public static final Block PINK_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.PINK_INK);
    public static final Block LIME_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.LIME_INK);
    public static final Block YELLOW_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.YELLOW_INK);
    public static final Block LIGHT_BLUE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.LIGHT_BLUE_INK);
    public static final Block MAGENTA_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.MAGENTA_INK);
    public static final Block ORANGE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.ORANGE_INK);
    public static final Block WHITE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.WHITE_INK);

    public static final Block GLOWING_BLACK_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, ParticleTypes.SQUID_INK);
    public static final Block GLOWING_RED_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.RED_INK);
    public static final Block GLOWING_GREEN_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.GREEN_INK);
    public static final Block GLOWING_BROWN_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.BROWN_INK);
    public static final Block GLOWING_BLUE_FITTING =  new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.BLUE_INK);
    public static final Block GLOWING_PURPLE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.PURPLE_INK);
    public static final Block GLOWING_CYAN_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.CYAN_INK);
    public static final Block GLOWING_LIGHT_GRAY_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.LIGHT_GRAY_INK);
    public static final Block GLOWING_GRAY_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.GRAY_INK);
    public static final Block GLOWING_PINK_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.PINK_INK);
    public static final Block GLOWING_LIME_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.LIME_INK);
    public static final Block GLOWING_YELLOW_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.YELLOW_INK);
    public static final Block GLOWING_LIGHT_BLUE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.LIGHT_BLUE_INK);
    public static final Block GLOWING_MAGENTA_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.MAGENTA_INK);
    public static final Block GLOWING_ORANGE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.ORANGE_INK);
    public static final Block GLOWING_WHITE_FITTING = new CopperFitting(Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.WHITE_INK);
}

package net.lunade.copper.blocks;

import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.block_entity.CopperBlockEntities;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.lunade.copper.CopperPipeMain.*;

public class CopperFitting extends BaseEntityBlock implements SimpleWaterloggedBlock, WeatheringCopper {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty HAS_WATER = CopperPipeProperties.HAS_WATER;
    public static final BooleanProperty HAS_SMOKE = CopperPipeProperties.HAS_SMOKE;
    public static final BooleanProperty HAS_ELECTRICITY = CopperPipeProperties.HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM = CopperPipeProperties.HAS_ITEM;
    private static final VoxelShape FITTING_SHAPE = Block.box(2.5D, 2.5D, 2.5D, 13.5D, 13.5D, 13.5D);

    private final WeatherState weatherState;
    public ParticleOptions ink;
    public int cooldown;

    public CopperFitting(WeatherState weatherState, Properties settings, int cooldown, ParticleOptions ink) {
        super(settings);
        this.weatherState = weatherState;
        this.cooldown = cooldown;
        this.ink = ink;
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(WATERLOGGED, false).setValue(HAS_WATER, false).setValue(HAS_SMOKE, false).setValue(HAS_ELECTRICITY, false).setValue(HAS_ITEM, false));
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
    public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
        return this.defaultBlockState()
                .setValue(WATERLOGGED, itemPlacementContext.getLevel().getFluidState(itemPlacementContext.getClickedPos()).getType() == Fluids.WATER)
                .setValue(POWERED, itemPlacementContext.getLevel().hasNeighborSignal(itemPlacementContext.getClickedPos()));
    }

    @Override
    @NotNull
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor worldAccess, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED)) {
            worldAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldAccess));
        }
        boolean electricity = blockState.getValue(HAS_ELECTRICITY);
        if (worldAccess.getBlockState(blockPos2).getBlock() instanceof LightningRodBlock) {
            if (worldAccess.getBlockState(blockPos2).getValue(POWERED)) {
                electricity = true;
            }
        } return blockState.setValue(HAS_ELECTRICITY, electricity);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (world.hasNeighborSignal(blockPos)) {
            world.setBlockAndUpdate(blockPos, blockState.setValue(CopperFitting.POWERED, true));
        } else {
            world.setBlockAndUpdate(blockPos, blockState.setValue(CopperFitting.POWERED, false));
        }
        updateBlockEntityValues(world, blockPos, blockState);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CopperFittingEntity(blockPos, blockState);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        return blockState.getFluidState().isEmpty();
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!world.isClientSide) {
            return createTickerHelper(blockEntityType, CopperBlockEntities.COPPER_FITTING_ENTITY, (world1, blockPos, blockState1, copperFittingEntity) ->
                    copperFittingEntity.serverTick(world1, blockPos, blockState1)
            );
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            if (world.getBlockEntity(blockPos) instanceof CopperFittingEntity copperFittingEntity) {
                copperFittingEntity.setCustomName(itemStack.getHoverName());
            }
        }
        updateBlockEntityValues(world, blockPos, blockState);
    }

    @Override
    @NotNull
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(blockState);
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
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(blockPos));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED).add(POWERED).add(HAS_WATER).add(HAS_SMOKE).add(HAS_ELECTRICITY).add(HAS_ITEM);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockView, BlockPos blockPos, PathComputationType navigationType) {
        return false;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverWorld, BlockPos blockPos, RandomSource random) {
        this.onRandomTick(blockState, serverWorld, blockPos, random);
    }

    public static void updateBlockEntityValues(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CopperFittingEntity fitting) {
                fitting.canWater = state.getValue(BlockStateProperties.WATERLOGGED);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        Block block = blockState.getBlock();
        return block == CopperFitting.COPPER_FITTING || block == CopperFitting.EXPOSED_FITTING || block == CopperFitting.WEATHERED_FITTING;
    }

    @Override
    public void onRemove(BlockState blockState, Level world, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateBlockEntityValues(world, blockPos, blockState);
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperFitting)) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperFittingEntity) {
                Containers.dropContents(world, blockPos, (CopperFittingEntity) blockEntity);
                world.updateNeighbourForOutputSignal(blockPos, this);
            }
            world.removeBlockEntity(blockPos);
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos blockPos, RandomSource random) {
        if (blockState.getValue(HAS_ELECTRICITY)) {
            ParticleUtils.spawnParticlesAlongAxis(Direction.UP.getAxis(), world, blockPos, 0.55D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
        }
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    public static final Block COPPER_FITTING = registerBlock(id("copper_fitting"), new CopperFitting(WeatherState.UNAFFECTED, Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK));
    public static final Block EXPOSED_FITTING = registerBlock(id("exposed_copper_fitting"), new CopperFitting(WeatherState.EXPOSED, Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK));
    public static final Block WEATHERED_FITTING = registerBlock(id("weathered_copper_fitting"), new CopperFitting(WeatherState.WEATHERED, Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK));
    public static final Block OXIDIZED_FITTING = registerBlock(id("oxidized_copper_fitting"), new CopperFitting(WeatherState.OXIDIZED, Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK));

    public static final Block WAXED_COPPER_FITTING = registerBlock(id("waxed_copper_fitting"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK));
    public static final Block WAXED_EXPOSED_FITTING = registerBlock(id("waxed_exposed_copper_fitting"), new CopperFitting(Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK));
    public static final Block WAXED_WEATHERED_FITTING = registerBlock(id("waxed_weathered_copper_fitting"), new CopperFitting(Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK));
    public static final Block WAXED_OXIDIZED_FITTING = registerBlock(id("waxed_oxidized_copper_fitting"), new CopperFitting(Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK));

    public static final Block CORRODED_FITTING = registerBlock(id("corroded_fitting"), new CopperFitting(WeatherState.OXIDIZED, Properties
            .of().mapColor(MapColor.QUARTZ)
            .requiresCorrectToolForDrops()
            .strength(2F, 3.5F)
            .sound(new SoundType(1.0f, 1.25f,
                    CopperPipeMain.CORRODED_COPPER_PLACE,
                    CopperPipeMain.CORRODED_COPPER_STEP,
                    CopperPipeMain.CORRODED_COPPER_BREAK,
                    CopperPipeMain.CORRODED_COPPER_FALL,
                    CopperPipeMain.CORRODED_COPPER_HIT
    )), 4, ParticleTypes.SQUID_INK));

    public static final Block BLACK_FITTING = registerColoured(colourFitting("black"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, ParticleTypes.SQUID_INK));
    public static final Block RED_FITTING = registerColoured(colourFitting("red"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.RED_INK));
    public static final Block GREEN_FITTING = registerColoured(colourFitting("green"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.GREEN_INK));
    public static final Block BROWN_FITTING = registerColoured(colourFitting("brown"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.BROWN_INK));
    public static final Block BLUE_FITTING = registerColoured(colourFitting("blue"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.BLUE_INK));
    public static final Block PURPLE_FITTING = registerColoured(colourFitting("purple"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.PURPLE_INK));
    public static final Block CYAN_FITTING = registerColoured(colourFitting("cyan"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.CYAN_INK));
    public static final Block LIGHT_GRAY_FITTING = registerColoured(colourFitting("light_gray"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.LIGHT_GRAY_INK));
    public static final Block GRAY_FITTING = registerColoured(colourFitting("gray"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.GRAY_INK));
    public static final Block PINK_FITTING = registerColoured(colourFitting("pink"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.PINK_INK));
    public static final Block LIME_FITTING = registerColoured(colourFitting("lime"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.LIME_INK));
    public static final Block YELLOW_FITTING = registerColoured(colourFitting("yellow"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.YELLOW_INK));
    public static final Block LIGHT_BLUE_FITTING = registerColoured(colourFitting("light_blue"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.LIGHT_BLUE_INK));
    public static final Block MAGENTA_FITTING = registerColoured(colourFitting("magenta"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.MAGENTA_INK));
    public static final Block ORANGE_FITTING = registerColoured(colourFitting("orange"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.ORANGE_INK));
    public static final Block WHITE_FITTING = registerColoured(colourFitting("white"), new CopperFitting(Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, CopperPipeMain.WHITE_INK));

    public static final Block GLOWING_BLACK_FITTING = registerColoured(glowingFitting("black"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, ParticleTypes.SQUID_INK));
    public static final Block GLOWING_RED_FITTING = registerColoured(glowingFitting("red"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.RED_INK));
    public static final Block GLOWING_GREEN_FITTING = registerColoured(glowingFitting("green"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.GREEN_INK));
    public static final Block GLOWING_BROWN_FITTING = registerColoured(glowingFitting("brown"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.BROWN_INK));
    public static final Block GLOWING_BLUE_FITTING = registerColoured(glowingFitting("blue"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.BLUE_INK));
    public static final Block GLOWING_PURPLE_FITTING = registerColoured(glowingFitting("purple"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.PURPLE_INK));
    public static final Block GLOWING_CYAN_FITTING = registerColoured(glowingFitting("cyan"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.CYAN_INK));
    public static final Block GLOWING_LIGHT_GRAY_FITTING = registerColoured(glowingFitting("light_gray"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.LIGHT_GRAY_INK));
    public static final Block GLOWING_GRAY_FITTING = registerColoured(glowingFitting("gray"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.GRAY_INK));
    public static final Block GLOWING_PINK_FITTING = registerColoured(glowingFitting("pink"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.PINK_INK));
    public static final Block GLOWING_LIME_FITTING = registerColoured(glowingFitting("lime"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)), 2, CopperPipeMain.LIME_INK));
    public static final Block GLOWING_YELLOW_FITTING = registerColoured(glowingFitting("yellow"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.YELLOW_INK));
    public static final Block GLOWING_LIGHT_BLUE_FITTING = registerColoured(glowingFitting("light_blue"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.LIGHT_BLUE_INK));
    public static final Block GLOWING_MAGENTA_FITTING = registerColoured(glowingFitting("magenta"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.MAGENTA_INK));
    public static final Block GLOWING_ORANGE_FITTING = registerColoured(glowingFitting("orange"), new CopperFitting(Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.ORANGE_INK));
    public static final Block GLOWING_WHITE_FITTING = registerColoured(glowingFitting("white"), new CopperFitting(Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.shouldGlow(state)),2, CopperPipeMain.WHITE_INK));
}

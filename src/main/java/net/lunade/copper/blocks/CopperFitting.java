package net.lunade.copper.blocks;

import net.lunade.copper.CopperPipeMain;
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
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CopperFitting extends BaseEntityBlock implements SimpleWaterloggedBlock, Copyable {

    public ParticleOptions ink;
    public int cooldown;

    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty HAS_WATER;
    public static final BooleanProperty HAS_SMOKE;
    public static final BooleanProperty HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM;
    private static final VoxelShape FITTING_SHAPE;

    public CopperFitting(Properties settings, int cooldown, ParticleOptions ink) {
        super(settings);
        this.cooldown = cooldown;
        this.ink = ink;
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(WATERLOGGED, false).setValue(HAS_WATER, false).setValue(HAS_SMOKE, false).setValue(HAS_ELECTRICITY, false).setValue(HAS_ITEM, false));
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos, CollisionContext shapeContext) { return FITTING_SHAPE; }

    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos) { return FITTING_SHAPE; }

    public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
        BlockPos blockPos = itemPlacementContext.getClickedPos();
        FluidState fluidState = itemPlacementContext.getLevel().getFluidState(blockPos);
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor worldAccess, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED)) { worldAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldAccess)); }
        boolean electricity = blockState.getValue(HAS_ELECTRICITY);
        if (worldAccess.getBlockState(blockPos2).getBlock() instanceof LightningRodBlock) {
            if (worldAccess.getBlockState(blockPos2).getValue(POWERED)) { electricity = true; }
        } return blockState.setValue(HAS_ELECTRICITY, electricity);
    }

    public void neighborChanged(BlockState blockState, Level world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (world.hasNeighborSignal(blockPos)) { world.setBlockAndUpdate(blockPos, blockState.setValue(CopperFitting.POWERED, true));}
        else { world.setBlockAndUpdate(blockPos, blockState.setValue(CopperFitting.POWERED, false)); }
        updateBlockEntityValues(world, blockPos, blockState);
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) { return new CopperFittingEntity(blockPos, blockState); }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockView, BlockPos blockPos) { return blockState.getFluidState().isEmpty(); }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!world.isClientSide) {
            return createTickerHelper(blockEntityType, CopperPipeMain.COPPER_FITTING_ENTITY, (world1, blockPos, blockState1, copperFittingEntity) -> copperFittingEntity.serverTick(world1, blockPos, blockState1));
        } return null;
    }

    public void setPlacedBy(Level world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperFittingEntity) { ((CopperFittingEntity) blockEntity).setCustomName(itemStack.getHoverName()); }
        }
        updateBlockEntityValues(world, blockPos, blockState);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED)) { return Fluids.WATER.getSource(false); }return super.getFluidState(blockState);
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos blockPos) { return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(blockPos)); }

    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) { builder.add(WATERLOGGED).add(POWERED).add(HAS_WATER).add(HAS_SMOKE).add(HAS_ELECTRICITY).add(HAS_ITEM); }

    public boolean isPathfindable(BlockState blockState, BlockGetter blockView, BlockPos blockPos, PathComputationType navigationType) { return false; }

    public void randomTick(BlockState blockState, ServerLevel serverWorld, BlockPos blockPos, RandomSource random) {
        if (random.nextFloat() < 0.05688889F) {
            this.tryDegrade(blockState, serverWorld, blockPos, random);
        }
    }

    public void tryDegrade(BlockState blockState, ServerLevel serverWorld, BlockPos blockPos, RandomSource random) {
        Block first = blockState.getBlock();
        if (CopperPipeMain.OXIDIZATION_INT.containsKey(first)) {
            int i = CopperPipeMain.OXIDIZATION_INT.getInt(first);
            int j = 0;
            int k = 0;
            float degradationChance = i == 0 ? 0.75F : 1.0F;
            for (BlockPos blockPos2 : BlockPos.withinManhattan(blockPos, 4, 4, 4)) {
                int l = blockPos2.distManhattan(blockPos);
                if (l > 4) { break; }

                if (!blockPos2.equals(blockPos)) {
                    BlockState blockState2 = serverWorld.getBlockState(blockPos2);
                    Block block = blockState2.getBlock();
                    if (block instanceof ChangeOverTimeBlock) {
                        Enum<?> enum_ = ((ChangeOverTimeBlock<?>) block).getAge();
                        if (enum_.getClass() == WeatheringCopper.WeatherState.class) {
                            int m = enum_.ordinal();
                            if (m < i) { return; }
                            if (m > i) { ++k;} else { ++j; }
                        }
                    } else if (CopperPipeMain.OXIDIZATION_INT.containsKey(block)) {
                        int m = CopperPipeMain.OXIDIZATION_INT.getInt(block);
                        if (m < i) { return; }
                        if (m > i) { ++k; } else { ++j; }
                    }
                }
            }
            float f = (float) (k + 1) / (float) (k + j + 1);
            float g = f * f * degradationChance;
            if (random.nextFloat() < g) {
                if (CopperPipeMain.NEXT_STAGE.containsKey(first)) {
                    serverWorld.setBlockAndUpdate(blockPos, makeCopyOf(blockState, CopperPipeMain.NEXT_STAGE.get(first)));
                }
            }
        }
    }

    public static void updateBlockEntityValues(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CopperFittingEntity fitting) {
                fitting.canWater = state.getValue(BlockStateProperties.WATERLOGGED);
            }
        }
    }

    public boolean isRandomlyTicking(BlockState blockState) {
        Block block = blockState.getBlock();
        return block == CopperFitting.COPPER_FITTING || block == CopperFitting.EXPOSED_FITTING || block == CopperFitting.WEATHERED_FITTING;
    }

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

    public void makeCopyOf(BlockState state, Level world, BlockPos blockPos, Block block) {
        if (block instanceof CopperFitting) {
            world.setBlockAndUpdate(blockPos, block.defaultBlockState().setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(POWERED, state.getValue(POWERED))
                    .setValue(HAS_WATER, state.getValue(HAS_WATER)).setValue(HAS_ITEM, state.getValue(HAS_ITEM)).setValue(HAS_SMOKE, state.getValue(HAS_SMOKE)).setValue(HAS_ELECTRICITY, state.getValue(HAS_ELECTRICITY)));
        }
    }

    public BlockState makeCopyOf(BlockState state, Block block) {
        if (block instanceof CopperFitting) {
            return block.defaultBlockState().setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(POWERED, state.getValue(POWERED))
                    .setValue(HAS_WATER, state.getValue(HAS_WATER)).setValue(HAS_ITEM, state.getValue(HAS_ITEM)).setValue(HAS_SMOKE, state.getValue(HAS_SMOKE)).setValue(HAS_ELECTRICITY, state.getValue(HAS_ELECTRICITY));
        } else return null;
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        POWERED = BlockStateProperties.POWERED;
        HAS_WATER = CopperPipeProperties.HAS_WATER;
        HAS_SMOKE = CopperPipeProperties.HAS_SMOKE;
        HAS_ELECTRICITY = CopperPipeProperties.HAS_ELECTRICITY;
        HAS_ITEM = CopperPipeProperties.HAS_ITEM;
        FITTING_SHAPE = Block.box(2.5D, 2.5D, 2.5D, 13.5D, 13.5D, 13.5D);
    }

    public static final Block OXIDIZED_FITTING = new CopperFitting(Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block WEATHERED_FITTING = new CopperFitting(Properties.of(Material.METAL, MaterialColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block EXPOSED_FITTING = new CopperFitting(Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block COPPER_FITTING = new CopperFitting(Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1, ParticleTypes.SQUID_INK);

    public static final Block WAXED_OXIDIZED_FITTING = new CopperFitting(Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_WEATHERED_FITTING = new CopperFitting(Properties.of(Material.METAL, MaterialColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_EXPOSED_FITTING = new CopperFitting(Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_COPPER_FITTING = new CopperFitting(Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 0, ParticleTypes.SQUID_INK);

    public static final Block CORRODED_FITTING = new CopperFitting(Properties
            .of(Material.METAL, MaterialColor.QUARTZ)
            .requiresCorrectToolForDrops()
            .strength(2F, 3.5F)
            .sound(new SoundType(1.0f, 1.25f,
                    CopperPipeMain.CORRODED_COPPER_PLACE,
                    CopperPipeMain.CORRODED_COPPER_STEP,
                    CopperPipeMain.CORRODED_COPPER_BREAK,
                    CopperPipeMain.CORRODED_COPPER_FALL,
                    CopperPipeMain.CORRODED_COPPER_HIT
    )), 4, ParticleTypes.SQUID_INK);

}

package net.lunade.copper.blocks;

import net.lunade.copper.Main;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CopperFitting extends BlockWithEntity implements Waterloggable, Copyable {

    public ParticleEffect ink;
    public int cooldown;

    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty HAS_WATER;
    public static final BooleanProperty HAS_SMOKE;
    public static final BooleanProperty HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM;
    private static final VoxelShape FITTING_SHAPE;

    public CopperFitting(Settings settings, int cooldown, ParticleEffect ink) {
        super(settings);
        this.cooldown = cooldown;
        this.ink = ink;
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false).with(WATERLOGGED, false).with(HAS_WATER, false).with(HAS_SMOKE, false).with(HAS_ELECTRICITY, false).with(HAS_ITEM, false));
    }

    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) { return FITTING_SHAPE; }

    public VoxelShape getRaycastShape(BlockState blockState, BlockView blockView, BlockPos blockPos) { return FITTING_SHAPE; }

    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        BlockPos blockPos = itemPlacementContext.getBlockPos();
        FluidState fluidState = itemPlacementContext.getWorld().getFluidState(blockPos);
        return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState blockState2, WorldAccess worldAccess, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.get(WATERLOGGED)) { worldAccess.createAndScheduleFluidTick(blockPos, Fluids.WATER, Fluids.WATER.getTickRate(worldAccess)); }
        boolean electricity = blockState.get(HAS_ELECTRICITY);
        if (worldAccess.getBlockState(blockPos2).getBlock() instanceof LightningRodBlock) {
            if (worldAccess.getBlockState(blockPos2).get(POWERED)) { electricity = true; }
        } return blockState.with(HAS_ELECTRICITY, electricity);
    }

    public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (world.isReceivingRedstonePower(blockPos)) { world.setBlockState(blockPos, blockState.with(CopperFitting.POWERED, true));}
        else { world.setBlockState(blockPos, blockState.with(CopperFitting.POWERED, false)); }
        updateBlockEntityValues(world, blockPos, blockState);
    }

    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) { return new CopperFittingEntity(blockPos, blockState); }

    @Override
    public boolean isTranslucent(BlockState blockState, BlockView blockView, BlockPos blockPos) { return blockState.getFluidState().isEmpty(); }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!world.isClient) {
            return checkType(blockEntityType, Main.COPPER_FITTING_ENTITY, (world1, blockPos, blockState1, copperFittingEntity) -> copperFittingEntity.serverTick(world1, blockPos, blockState1));
        } return null;
    }

    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperFittingEntity) { ((CopperFittingEntity) blockEntity).setCustomName(itemStack.getName()); }
        }
        updateBlockEntityValues(world, blockPos, blockState);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.get(WATERLOGGED)) { return Fluids.WATER.getStill(false); }return super.getFluidState(blockState);
    }

    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    public boolean hasComparatorOutput(BlockState blockState) {
        return true;
    }

    public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) { return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(blockPos)); }

    protected void appendProperties(Builder<Block, BlockState> builder) { builder.add(WATERLOGGED).add(POWERED).add(HAS_WATER).add(HAS_SMOKE).add(HAS_ELECTRICITY).add(HAS_ITEM); }

    public boolean canPathfindThrough(BlockState blockState, BlockView blockView, BlockPos blockPos, NavigationType navigationType) { return false; }

    public void randomTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        if (random.nextFloat() < 0.05688889F) {
            this.tryDegrade(blockState, serverWorld, blockPos, random);
        }
    }

    public void tryDegrade(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        Block first = blockState.getBlock();
        if (Main.OXIDIZATION_INT.containsKey(first)) {
            int i = Main.OXIDIZATION_INT.getInt(first);
            int j = 0;
            int k = 0;
            float degradationChance = i == 0 ? 0.75F : 1.0F;
            for (BlockPos blockPos2 : BlockPos.iterateOutwards(blockPos, 4, 4, 4)) {
                int l = blockPos2.getManhattanDistance(blockPos);
                if (l > 4) { break; }

                if (!blockPos2.equals(blockPos)) {
                    BlockState blockState2 = serverWorld.getBlockState(blockPos2);
                    Block block = blockState2.getBlock();
                    if (block instanceof Degradable) {
                        Enum<?> enum_ = ((Degradable<?>) block).getDegradationLevel();
                        if (enum_.getClass() == Oxidizable.OxidationLevel.class) {
                            int m = enum_.ordinal();
                            if (m < i) { return; }
                            if (m > i) { ++k;} else { ++j; }
                        }
                    } else if (Main.OXIDIZATION_INT.containsKey(block)) {
                        int m = Main.OXIDIZATION_INT.getInt(block);
                        if (m < i) { return; }
                        if (m > i) { ++k; } else { ++j; }
                    }
                }
            }
            float f = (float) (k + 1) / (float) (k + j + 1);
            float g = f * f * degradationChance;
            if (random.nextFloat() < g) {
                if (Main.NEXT_STAGE.containsKey(first)) {
                    serverWorld.setBlockState(blockPos, makeCopyOf(blockState, Main.NEXT_STAGE.get(first)));
                }
            }
        }
    }

    public boolean hasRandomTicks(BlockState blockState) {
        Block block = blockState.getBlock();
        return block == CopperFitting.COPPER_FITTING || block == CopperFitting.EXPOSED_FITTING || block == CopperFitting.WEATHERED_FITTING;
    }

    public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateBlockEntityValues(world, blockPos, blockState);
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperFitting)) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperFittingEntity) {
                ItemScatterer.spawn(world, blockPos, (CopperFittingEntity) blockEntity);
                world.updateComparators(blockPos, this);
            }
            world.removeBlockEntity(blockPos);
        }
    }

    public static void updateBlockEntityValues(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CopperFittingEntity fitting) {
                fitting.canWater = state.get(Properties.WATERLOGGED);
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        if (blockState.get(HAS_ELECTRICITY)) {
            ParticleUtil.spawnParticle(Direction.UP.getAxis(), world, blockPos, 0.55D, ParticleTypes.ELECTRIC_SPARK, UniformIntProvider.create(1, 2));
        }
    }

    public void makeCopyOf(BlockState state, World world, BlockPos blockPos, Block block) {
        if (block instanceof CopperFitting) {
            world.setBlockState(blockPos, block.getDefaultState().with(WATERLOGGED, state.get(WATERLOGGED)).with(POWERED, state.get(POWERED))
                    .with(HAS_WATER, state.get(HAS_WATER)).with(HAS_ITEM, state.get(HAS_ITEM)).with(HAS_SMOKE, state.get(HAS_SMOKE)).with(HAS_ELECTRICITY, state.get(HAS_ELECTRICITY)));
        }
    }

    public BlockState makeCopyOf(BlockState state, Block block) {
        if (block instanceof CopperFitting) {
            return block.getDefaultState().with(WATERLOGGED, state.get(WATERLOGGED)).with(POWERED, state.get(POWERED))
                    .with(HAS_WATER, state.get(HAS_WATER)).with(HAS_ITEM, state.get(HAS_ITEM)).with(HAS_SMOKE, state.get(HAS_SMOKE)).with(HAS_ELECTRICITY, state.get(HAS_ELECTRICITY));
        } else return null;
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
        POWERED = Properties.POWERED;
        HAS_WATER = CopperPipeProperties.HAS_WATER;
        HAS_SMOKE = CopperPipeProperties.HAS_SMOKE;
        HAS_ELECTRICITY = CopperPipeProperties.HAS_ELECTRICITY;
        HAS_ITEM = CopperPipeProperties.HAS_ITEM;
        FITTING_SHAPE = Block.createCuboidShape(2.5D, 2.5D, 2.5D, 13.5D, 13.5D, 13.5D);
    }

    public static final Block OXIDIZED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.TEAL).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block WEATHERED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.DARK_AQUA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block EXPOSED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1, ParticleTypes.SQUID_INK);
    public static final Block COPPER_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1, ParticleTypes.SQUID_INK);

    public static final Block WAXED_OXIDIZED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.TEAL).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_WEATHERED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.DARK_AQUA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_EXPOSED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 0, ParticleTypes.SQUID_INK);
    public static final Block WAXED_COPPER_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 0, ParticleTypes.SQUID_INK);

    public static final Block CORRODED_FITTING = new CopperFitting(Settings
            .of(Material.METAL, MapColor.OFF_WHITE)
            .requiresTool()
            .strength(2F, 3.5F)
            .sounds(new BlockSoundGroup(1.0f, 1.25f,
                    Main.CORRODED_COPPER_PLACE,
                    Main.CORRODED_COPPER_STEP,
                    Main.CORRODED_COPPER_BREAK,
                    Main.CORRODED_COPPER_FALL,
                    Main.CORRODED_COPPER_HIT
            )), 4, ParticleTypes.SQUID_INK);

    public static final Block BLACK_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, ParticleTypes.SQUID_INK);
    public static final Block RED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.RED).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.RED_INK);
    public static final Block GREEN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.GREEN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.GREEN_INK);
    public static final Block BROWN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BROWN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.BROWN_INK);
    public static final Block BLUE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.BLUE_INK);
    public static final Block PURPLE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.PURPLE_INK);
    public static final Block CYAN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.CYAN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.CYAN_INK);
    public static final Block LIGHT_GRAY_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.LIGHT_GRAY_INK);
    public static final Block GRAY_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.GRAY_INK);
    public static final Block PINK_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.PINK_INK);
    public static final Block LIME_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIME).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.LIME_INK);
    public static final Block YELLOW_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.YELLOW_INK);
    public static final Block LIGHT_BLUE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.LIGHT_BLUE_INK);
    public static final Block MAGENTA_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.MAGENTA_INK);
    public static final Block ORANGE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.ORANGE_INK);
    public static final Block WHITE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2, Main.WHITE_INK);

    public static final Block GLOWING_BLACK_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, ParticleTypes.SQUID_INK);
    public static final Block GLOWING_RED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.RED).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.RED_INK);
    public static final Block GLOWING_GREEN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.GREEN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.GREEN_INK);
    public static final Block GLOWING_BROWN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BROWN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.BROWN_INK);
    public static final Block GLOWING_BLUE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.BLUE_INK);
    public static final Block GLOWING_PURPLE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.PURPLE_INK);
    public static final Block GLOWING_CYAN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.CYAN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.CYAN_INK);
    public static final Block GLOWING_LIGHT_GRAY_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)),2, Main.LIGHT_GRAY_INK);
    public static final Block GLOWING_GRAY_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.GRAY_INK);
    public static final Block GLOWING_PINK_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.PINK_INK);
    public static final Block GLOWING_LIME_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIME).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 2, Main.LIME_INK);
    public static final Block GLOWING_YELLOW_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)),2, Main.YELLOW_INK);
    public static final Block GLOWING_LIGHT_BLUE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)),2, Main.LIGHT_BLUE_INK);
    public static final Block GLOWING_MAGENTA_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)),2, Main.MAGENTA_INK);
    public static final Block GLOWING_ORANGE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)),2, Main.ORANGE_INK);
    public static final Block GLOWING_WHITE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)),2, Main.WHITE_INK);

}
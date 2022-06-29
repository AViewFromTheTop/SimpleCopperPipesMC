package net.lunade.copper.blocks;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.*;
import net.lunade.copper.Main;
import net.lunade.copper.block_entity.CopperFittingEntity;
import net.lunade.copper.block_entity.CopperPipeEntity;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static net.lunade.copper.blocks.CopperPipe.FACING;

public class CopperFitting extends BlockWithEntity implements Waterloggable {

    public boolean waxed;
    public int inkInt;
    public int cooldown;

    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty HAS_WATER;
    public static final BooleanProperty HAS_SMOKE;
    public static final BooleanProperty HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM;
    private static final VoxelShape FITTING_SHAPE;

    public CopperFitting(Settings settings, boolean waxed, int cooldown, int ink) {
        super(settings);
        this.waxed = waxed;
        this.cooldown = cooldown;
        this.inkInt = ink;
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
    }

    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) { return new CopperFittingEntity(blockPos, blockState); }

    @Override
    public boolean isTranslucent(BlockState blockState, BlockView blockView, BlockPos blockPos) { return blockState.getFluidState().isEmpty(); }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) { return world.isClient ? null : checkType(blockEntityType, Main.COPPER_FITTING_ENTITY, CopperFittingEntity::serverTick); }

    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperFittingEntity) { ((CopperFittingEntity) blockEntity).setCustomName(itemStack.getName()); }
        }
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
        if (OXIDIZATION_INT.containsKey(first)) {
            int i = OXIDIZATION_INT.getInt(first);
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
                    } else if (block instanceof CopperPipe) {
                        if (CopperPipe.OXIDIZATION_INT.containsKey(block)) {
                            int m = CopperPipe.OXIDIZATION_INT.getInt(block);
                            if (m < i) { return; }
                            if (m > i) { ++k; } else { ++j; }
                        }
                    } else if (block instanceof CopperFitting) {
                        if (OXIDIZATION_INT.containsKey(block)) {
                            int m = OXIDIZATION_INT.getInt(block);
                            if (m < i) { return; }
                            if (m > i) { ++k; } else { ++j; }
                        }
                    }
                }
            }
            float f = (float) (k + 1) / (float) (k + j + 1);
            float g = f * f * degradationChance;
            if (random.nextFloat() < g) {
                if (NEXT_STAGE.containsKey(first)) {
                    serverWorld.setBlockState(blockPos, makeCopyOf(blockState, NEXT_STAGE.get(first)));
                }
            }
        }
    }

    public static void sendElectricity(World world, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            BlockPos pos = blockPos.offset(direction);
            if (world.isChunkLoaded(pos)) {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof CopperPipe) {
                    BlockEntity entity = world.getBlockEntity(pos);
                    if (entity instanceof CopperPipeEntity pipe) {
                        if (pipe.electricityCooldown == -1) {
                            state = state.with(CopperPipe.HAS_ELECTRICITY, true);
                            world.syncWorldEvent(3002, pos, state.get(FACING).getAxis().ordinal());
                            world.setBlockState(pos, state);
                        }
                    }
                }
                if (state.getBlock() instanceof CopperFitting) {
                    BlockEntity entity = world.getBlockEntity(pos);
                    if (entity instanceof CopperFittingEntity fitting) {
                        if (fitting.electricityCooldown == -1) {
                            state = state.with(CopperFitting.HAS_ELECTRICITY, true);
                            world.syncWorldEvent(3002, pos, direction.getAxis().ordinal());
                            world.setBlockState(pos, state);
                        }
                    }
                }
            }
        }
    }

    public static int canWater(World world, BlockPos blockPos) {
        BlockPos p;
        IntArrayList nums = new IntArrayList();
        for (Direction direction : Direction.values()) {
            if (world.getBlockState(blockPos.offset(direction)).getBlock() instanceof CopperPipe) {
                p = blockPos.offset(direction);
                if (world.getBlockState(p).get(FACING) == direction.getOpposite()) {
                    if (!world.isChunkLoaded(p)) { return 0; }
                    nums.add(CopperPipe.canWaterFitting(world, p, world.getBlockState(p)));
                }
            }
        }
        int a = getHighest(nums);
        if (a > 0) { return 12; }
        return 0;
    }

    public static int canSmoke(World world, BlockPos blockPos) {
        BlockPos p;
        IntArrayList nums = new IntArrayList();
        for (Direction direction : Direction.values()) {
            if (world.getBlockState(blockPos.offset(direction)).getBlock() instanceof CopperPipe) {
                p = blockPos.offset(direction);
                if (world.getBlockState(p).get(FACING) == direction.getOpposite()) {
                    if (!world.isChunkLoaded(p)) { return 0; }
                    nums.add(CopperPipe.canSmokeFitting(world, p, world.getBlockState(blockPos.offset(direction))));
                }
            }
        }
        int a = getHighest(nums);
        if (a > 0) { return 12; }
        return 0;
    }

    public static int getHighest(IntArrayList list) {
        int highest = 0;
        for (int i : list.elements()) {
            highest = Math.max(i, highest);
        } return highest;
    }

    public boolean hasRandomTicks(BlockState blockState) {
        Block block = blockState.getBlock();
        return block == CopperFitting.COPPER_FITTING || block == CopperFitting.EXPOSED_FITTING || block == CopperFitting.WEATHERED_FITTING;
    }

    public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperFitting)) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperFittingEntity) {
                ItemScatterer.spawn(world, blockPos, (CopperFittingEntity) blockEntity);
                world.updateComparators(blockPos, this);
            }
            world.removeBlockEntity(blockPos);
        }
    }

    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        if (blockState.get(HAS_ELECTRICITY)) {
            ParticleUtil.spawnParticle(Direction.UP.getAxis(), world, blockPos, 0.55D, ParticleTypes.ELECTRIC_SPARK, UniformIntProvider.create(1, 2));
        }
    }

    public static void makeCopyOf(BlockState state, World world, BlockPos blockPos, Block block) {
        if (block instanceof CopperFitting) {
            world.setBlockState(blockPos, block.getDefaultState().with(WATERLOGGED, state.get(WATERLOGGED)).with(POWERED, state.get(POWERED))
                    .with(HAS_WATER, state.get(HAS_WATER)).with(HAS_ITEM, state.get(HAS_ITEM)).with(HAS_SMOKE, state.get(HAS_SMOKE)).with(HAS_ELECTRICITY, state.get(HAS_ELECTRICITY)));
        }
    }

    public static BlockState makeCopyOf(BlockState state, Block block) {
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

    public static final Block OXIDIZED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.TEAL).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,1, 0);
    public static final Block WEATHERED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.DARK_AQUA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,1, 0);
    public static final Block EXPOSED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,1, 0);
    public static final Block COPPER_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,1, 0);

    public static final Block WAXED_OXIDIZED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.TEAL).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), true,0, 0);
    public static final Block WAXED_WEATHERED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.DARK_AQUA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), true,0, 0);
    public static final Block WAXED_EXPOSED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), true,0, 0);
    public static final Block WAXED_COPPER_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), true,0, 0);

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
    )), false,4, 0);

    public static final Block BLACK_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, 0);
    public static final Block RED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.RED).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("red"));
    public static final Block GREEN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.GREEN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("green"));
    public static final Block BROWN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BROWN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("brown"));
    public static final Block BLUE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("blue"));
    public static final Block PURPLE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("purple"));
    public static final Block CYAN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.CYAN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("cyan"));
    public static final Block LIGHT_GRAY_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("light_gray"));
    public static final Block GRAY_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("gray"));
    public static final Block PINK_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("pink"));
    public static final Block LIME_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIME).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("lime"));
    public static final Block YELLOW_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("yellow"));
    public static final Block LIGHT_BLUE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("light_blue"));
    public static final Block MAGENTA_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("magenta"));
    public static final Block ORANGE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("orange"));
    public static final Block WHITE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), false,2, Main.colorToInt("white"));

    public static final Block GLOWING_BLACK_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, 0);
    public static final Block GLOWING_RED_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.RED).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("red"));
    public static final Block GLOWING_GREEN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.GREEN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("green"));
    public static final Block GLOWING_BROWN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BROWN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("brown"));
    public static final Block GLOWING_BLUE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("blue"));
    public static final Block GLOWING_PURPLE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("purple"));
    public static final Block GLOWING_CYAN_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.CYAN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("cyan"));
    public static final Block GLOWING_LIGHT_GRAY_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("light_gray"));
    public static final Block GLOWING_GRAY_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("gray"));
    public static final Block GLOWING_PINK_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("pink"));
    public static final Block GLOWING_LIME_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIME).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("lime"));
    public static final Block GLOWING_YELLOW_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("yellow"));
    public static final Block GLOWING_LIGHT_BLUE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("light_blue"));
    public static final Block GLOWING_MAGENTA_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("magenta"));
    public static final Block GLOWING_ORANGE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("orange"));
    public static final Block GLOWING_WHITE_FITTING = new CopperFitting(Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), false,2, Main.colorToInt("white"));

    public static final Object2ObjectMap<Block, Block> NEXT_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(COPPER_FITTING, EXPOSED_FITTING);
        object2IntOpenHashMap.put(EXPOSED_FITTING, WEATHERED_FITTING);
        object2IntOpenHashMap.put(WEATHERED_FITTING, OXIDIZED_FITTING);
        object2IntOpenHashMap.put(OXIDIZED_FITTING, CORRODED_FITTING);
    }));
    public static final Object2ObjectMap<Block, Block> PREVIOUS_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(CORRODED_FITTING, OXIDIZED_FITTING);
        object2IntOpenHashMap.put(OXIDIZED_FITTING, WEATHERED_FITTING);
        object2IntOpenHashMap.put(WEATHERED_FITTING, EXPOSED_FITTING);
        object2IntOpenHashMap.put(EXPOSED_FITTING, COPPER_FITTING);
        object2IntOpenHashMap.put(WAXED_COPPER_FITTING, COPPER_FITTING);
        object2IntOpenHashMap.put(WAXED_EXPOSED_FITTING, EXPOSED_FITTING);
        object2IntOpenHashMap.put(WAXED_WEATHERED_FITTING, WEATHERED_FITTING);
        object2IntOpenHashMap.put(WAXED_OXIDIZED_FITTING, OXIDIZED_FITTING);
    }));
    public static final Object2ObjectMap<Block, Block> WAX_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(COPPER_FITTING, WAXED_COPPER_FITTING);
        object2IntOpenHashMap.put(EXPOSED_FITTING, WAXED_EXPOSED_FITTING);
        object2IntOpenHashMap.put(WEATHERED_FITTING, WAXED_WEATHERED_FITTING);
        object2IntOpenHashMap.put(OXIDIZED_FITTING, WAXED_OXIDIZED_FITTING);
    }));
    public static final Object2ObjectMap<Block, Block> GLOW_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(RED_FITTING, GLOWING_RED_FITTING);
        object2IntOpenHashMap.put(ORANGE_FITTING, GLOWING_ORANGE_FITTING);
        object2IntOpenHashMap.put(YELLOW_FITTING, GLOWING_YELLOW_FITTING);
        object2IntOpenHashMap.put(GREEN_FITTING, GLOWING_GREEN_FITTING);
        object2IntOpenHashMap.put(CYAN_FITTING, GLOWING_CYAN_FITTING);
        object2IntOpenHashMap.put(LIGHT_BLUE_FITTING, GLOWING_LIGHT_BLUE_FITTING);
        object2IntOpenHashMap.put(BLUE_FITTING, GLOWING_BLUE_FITTING);
        object2IntOpenHashMap.put(PURPLE_FITTING, GLOWING_PURPLE_FITTING);
        object2IntOpenHashMap.put(MAGENTA_FITTING, GLOWING_MAGENTA_FITTING);
        object2IntOpenHashMap.put(PINK_FITTING, GLOWING_PINK_FITTING);
        object2IntOpenHashMap.put(WHITE_FITTING, GLOWING_WHITE_FITTING);
        object2IntOpenHashMap.put(LIGHT_GRAY_FITTING, GLOWING_LIGHT_GRAY_FITTING);
        object2IntOpenHashMap.put(GRAY_FITTING, GLOWING_GRAY_FITTING);
        object2IntOpenHashMap.put(BLACK_FITTING, GLOWING_BLACK_FITTING);
        object2IntOpenHashMap.put(BROWN_FITTING, GLOWING_BROWN_FITTING);
    }));
    public static final Object2IntMap<Block> OXIDIZATION_INT = Object2IntMaps.unmodifiable(Util.make(new Object2IntOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(COPPER_FITTING, 0);
        object2IntOpenHashMap.put(EXPOSED_FITTING, 1);
        object2IntOpenHashMap.put(WEATHERED_FITTING, 2);
        object2IntOpenHashMap.put(OXIDIZED_FITTING, 3);
    }));

}

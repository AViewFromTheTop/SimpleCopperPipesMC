package net.lunade.copper.blocks;

import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;
import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.lunade.copper.leaking_pipes.LeakingPipeDrips;
import net.lunade.copper.registry.RegisterCopperBlockEntities;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.lunade.copper.CopperPipeMain.INSPECT_PIPE;

public class CopperPipe extends BaseEntityBlock implements SimpleWaterloggedBlock, WeatheringCopper {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty FRONT_CONNECTED = CopperPipeProperties.FRONT_CONNECTED;
    public static final BooleanProperty BACK_CONNECTED = CopperPipeProperties.BACK_CONNECTED;
    public static final BooleanProperty SMOOTH = CopperPipeProperties.SMOOTH;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HAS_WATER = CopperPipeProperties.HAS_WATER;
    public static final BooleanProperty HAS_SMOKE = CopperPipeProperties.HAS_SMOKE;
    public static final BooleanProperty HAS_ELECTRICITY = CopperPipeProperties.HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM = CopperPipeProperties.HAS_ITEM;

    private static final VoxelShape UP_SHAPE = Shapes.or(Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D),Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D));
    private static final VoxelShape DOWN_SHAPE = Shapes.or(Block.box(4.0D, 2.0D, 4.0D, 12.0D, 16.0D, 12.0D),Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D));
    private static final VoxelShape NORTH_SHAPE = Shapes.or(Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 16.0D),Block.box(3.0D, 3.0D, 0.D, 13.0D, 13.0D, 2.0D));
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 14.0D),Block.box(3.0D, 3.0D, 14.D, 13.0D, 13.0D, 16.0D));
    private static final VoxelShape EAST_SHAPE = Shapes.or(Block.box(0.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D),Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
    private static final VoxelShape WEST_SHAPE = Shapes.or(Block.box(2.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D),Block.box(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D));

    private static final VoxelShape DOWN_FRONT = Shapes.or(Block.box(4.0D, -2.0D, 4.0D, 12.0D, 16.0D, 12.0D),Block.box(3.0D, -4.0D, 3.0D, 13.0D, -2.0D, 13.0D));
    private static final VoxelShape EAST_FRONT = Shapes.or(Block.box(0.0D, 4.0D, 4.0D, 18.0D, 12.0D, 12.0D),Block.box(18.0D, 3.0D, 3.0D, 20.0D, 13.0D, 13.0D));
    private static final VoxelShape NORTH_FRONT = Shapes.or(Block.box(4.0D, 4.0D, -2.0D, 12.0D, 12.0D, 16.0D),Block.box(3.0D, 3.0D, -4.0D, 13.0D, 13.0D, -2.0D));
    private static final VoxelShape SOUTH_FRONT = Shapes.or(Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 18.0D),Block.box(3.0D, 3.0D, 18.D, 13.0D, 13.0D, 20.0D));
    private static final VoxelShape WEST_FRONT = Shapes.or(Block.box(-2.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D),Block.box(-4.0D, 3.0D, 3.0D, -2.0D, 13.0D, 13.0D));
    private static final VoxelShape UP_FRONT = Shapes.or(Block.box(4.0D, 0.0D, 4.0D, 12.0D, 18.0D, 12.0D),Block.box(3.0D, 18.0D, 3.0D, 13.0D, 20.0D, 13.0D));

    private static final VoxelShape DOWN_BACK = Shapes.or(Block.box(4.0D, 2.0D, 4.0D, 12.0D, 20.0D, 12.0D),Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D));
    private static final VoxelShape EAST_BACK = Shapes.or(Block.box(-4.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D),Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
    private static final VoxelShape NORTH_BACK = Shapes.or(Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 20.0D),Block.box(3.0D, 3.0D, 0.D, 13.0D, 13.0D, 2.0D));
    private static final VoxelShape SOUTH_BACK = Shapes.or(Block.box(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 14.0D),Block.box(3.0D, 3.0D, 14.D, 13.0D, 13.0D, 16.0D));
    private static final VoxelShape WEST_BACK = Shapes.or(Block.box(2.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D),Block.box(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D));
    private static final VoxelShape UP_BACK = Shapes.or(Block.box(4.0D, -4.0D, 4.0D, 12.0D, 14.0D, 12.0D),Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D));

    private static final VoxelShape DOWN_DOUBLE = Block.box(4.0D, -4.0D, 4.0D, 12.0D, 20.0D, 12.0D);
    private static final VoxelShape NORTH_DOUBLE = Block.box(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 20.0D);
    private static final VoxelShape EAST_DOUBLE = Block.box(-4.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D);

    private static final VoxelShape DOWN_SMOOTH = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape NORTH_SMOOTH = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 16.0D);
    private static final VoxelShape EAST_SMOOTH = Block.box(0.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);

    private static final VoxelShape DOWN_BACK_SMOOTH = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 20.0D, 12.0D);
    private static final VoxelShape NORTH_BACK_SMOOTH = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 20.0D);
    private static final VoxelShape SOUTH_BACK_SMOOTH = Block.box(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 16.0D);
    private static final VoxelShape EAST_BACK_SMOOTH = Block.box(-4.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
    private static final VoxelShape WEST_BACK_SMOOTH = Block.box(0.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D);
    private static final VoxelShape UP_BACK_SMOOTH = Block.box(4.0D, -4.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    private final WeatherState weatherState;
    public int cooldown;
    public int dispenserShotLength;
    public ParticleOptions ink;

    public CopperPipe(WeatherState weatherState, Properties settings, int cooldown, int dispenserShotLength, ParticleOptions ink) {
        super(settings);
        this.weatherState = weatherState;
        this.cooldown = cooldown;
        this.dispenserShotLength = dispenserShotLength;
        this.ink = ink;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(SMOOTH, false).setValue(WATERLOGGED, false).setValue(HAS_WATER, false).setValue(HAS_SMOKE, false).setValue(HAS_ELECTRICITY, false).setValue(HAS_ITEM, false).setValue(POWERED, false));
    }

    public CopperPipe(Properties settings, int cooldown, int dispenserShotLength, ParticleOptions ink) {
        this(WeatherState.UNAFFECTED, settings, cooldown, dispenserShotLength, ink);
    }

    public VoxelShape getPipeShape(BlockState blockState) {
        boolean front = blockState.getValue(FRONT_CONNECTED);
        boolean back = blockState.getValue(BACK_CONNECTED);
        boolean smooth = blockState.getValue(SMOOTH);
        if (smooth && back) {
            return switch (blockState.getValue(FACING)) {
                case DOWN -> DOWN_BACK_SMOOTH;
                case UP -> UP_BACK_SMOOTH;
                case NORTH -> NORTH_BACK_SMOOTH;
                case SOUTH -> SOUTH_BACK_SMOOTH;
                case EAST -> EAST_BACK_SMOOTH;
                case WEST -> WEST_BACK_SMOOTH;
            };
        }
        if (smooth) {
            return switch (blockState.getValue(FACING)) {
                case DOWN, UP -> DOWN_SMOOTH;
                case NORTH, SOUTH -> NORTH_SMOOTH;
                case EAST, WEST -> EAST_SMOOTH;
            };
        }
        if (front && back) {
            return switch (blockState.getValue(FACING)) {
                case DOWN, UP -> DOWN_DOUBLE;
                case NORTH, SOUTH -> NORTH_DOUBLE;
                case EAST, WEST -> EAST_DOUBLE;
            };
        }
        if (front) {
            return switch (blockState.getValue(FACING)) {
                case DOWN -> DOWN_FRONT;
                case UP -> UP_FRONT;
                case NORTH -> NORTH_FRONT;
                case SOUTH -> SOUTH_FRONT;
                case EAST -> EAST_FRONT;
                case WEST -> WEST_FRONT;
            };
        }
        if (back) {
            return switch (blockState.getValue(FACING)) {
                case DOWN -> DOWN_BACK;
                case UP -> UP_BACK;
                case NORTH -> NORTH_BACK;
                case SOUTH -> SOUTH_BACK;
                case EAST -> EAST_BACK;
                case WEST -> WEST_BACK;
            };
        }
        return switch (blockState.getValue(FACING)) {
            case DOWN -> DOWN_SHAPE;
            case UP -> UP_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
        };
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
        }
        Direction facing = blockState.getValue(FACING);
        return blockState
                .setValue(FRONT_CONNECTED, canConnectFront(levelAccessor, blockPos, facing))
                .setValue(BACK_CONNECTED, canConnectBack(levelAccessor, blockPos, facing))
                .setValue(SMOOTH, isSmooth(levelAccessor, blockPos, facing))
                .setValue(HAS_ELECTRICITY, electricity);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        boolean powered = isReceivingRedstonePower(blockPos, level);
        if (powered != blockState.getValue(POWERED)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, powered));
        }
        updateBlockEntityValues(level, blockPos, blockState);
    }

    public static void updateBlockEntityValues(Level level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            BlockState dirState = level.getBlockState(pos.relative(direction));
            BlockState oppState = level.getBlockState(pos.relative(direction.getOpposite()));
            Block oppBlock = oppState.getBlock();
            if (level.getBlockEntity(pos) instanceof CopperPipeEntity pipe) {
                pipe.canDispense = (dirState.isAir() || dirState.getBlock() == Blocks.WATER) && (!oppState.isAir() && oppBlock != Blocks.WATER);
                pipe.corroded = oppBlock == CopperFitting.CORRODED_FITTING || state.getBlock() == CopperPipe.CORRODED_PIPE;
                pipe.shootsControlled = oppBlock == Blocks.DROPPER;
                pipe.shootsSpecial = oppBlock == Blocks.DISPENSER;
                pipe.canAccept = !(oppBlock instanceof CopperPipe) && !(oppBlock instanceof CopperFitting) && !oppState.isRedstoneConductor(level, pos);
                pipe.canSmoke = oppBlock instanceof CampfireBlock ? oppState.getValue(BlockStateProperties.LIT) : false;
                pipe.canWater = oppBlock == Blocks.WATER || state.getValue(BlockStateProperties.WATERLOGGED) || (oppState.hasProperty(BlockStateProperties.WATERLOGGED) ? oppState.getValue(BlockStateProperties.WATERLOGGED) : false);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CopperPipeEntity(blockPos, blockState);
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        return blockState.getFluidState().isEmpty();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return createTickerHelper(blockEntityType, RegisterCopperBlockEntities.COPPER_PIPE_ENTITY, (level1, blockPos, blockState1, copperPipeEntity) ->
                    copperPipeEntity.serverTick(level1, blockPos, blockState1)
            );
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel serverLevel, T blockEntity) {
        if (blockEntity instanceof CopperPipeEntity pipeEntity) {
            return pipeEntity.getListener();
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        updateBlockEntityValues(level, blockPos, blockState);
        if (itemStack.hasCustomHoverName()) {
            if (level.getBlockEntity(blockPos) instanceof CopperPipeEntity copperPipeEntity) {
                copperPipeEntity.setCustomName(itemStack.getHoverName());
            }
        }
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
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, @NotNull Player playerEntity, InteractionHand hand, BlockHitResult blockHitResult) {
        Item item = playerEntity.getItemInHand(hand).getItem();
        if (TagUtil.isIn(CopperPipeMain.IGNORES_COPPER_PIPE_MENU, item)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity copperPipeEntity) {
                playerEntity.openMenu(copperPipeEntity);
                playerEntity.awardStat(Stats.CUSTOM.get(INSPECT_PIPE));
            }
            return InteractionResult.CONSUME;
        }
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
    protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, FRONT_CONNECTED, BACK_CONNECTED, SMOOTH, WATERLOGGED, HAS_WATER, HAS_SMOKE, HAS_ELECTRICITY, HAS_ITEM, POWERED);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockView, BlockPos blockPos, PathComputationType navigationType) {
        return false;
    }

    public static boolean canConnectFront(@NotNull Level level, @NotNull BlockPos blockPos, Direction direction) {
        BlockState state = level.getBlockState(blockPos.relative(direction));
        if (state.getBlock() instanceof CopperPipe) {
            return state.getValue(CopperPipe.FACING) != direction.getOpposite() && state.getValue(CopperPipe.FACING) != direction;
        }
        return state.getBlock() instanceof CopperFitting;
    }

    public static boolean canConnectBack(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        BlockState state = level.getBlockState(blockPos.relative(direction.getOpposite()));
        if (state.getBlock() instanceof CopperPipe) {
            return state.getValue(CopperPipe.FACING) != direction.getOpposite() && state.getValue(CopperPipe.FACING) != direction;
        }
        return state.getBlock() instanceof CopperFitting;
    }

    public static boolean isSmooth(@NotNull Level level, @NotNull BlockPos blockPos, Direction direction) {
        BlockState state = level.getBlockState(blockPos.relative(direction));
        if (state.getBlock() instanceof CopperPipe) {
            return state.getValue(CopperPipe.FACING) == direction && !canConnectFront(level, blockPos, direction);
        }
        return false;
    }

    public static boolean canConnectFront(@NotNull LevelAccessor level, @NotNull BlockPos blockPos, Direction direction) {
        BlockState state = level.getBlockState(blockPos.relative(direction));
        if (state.getBlock() instanceof CopperPipe) {
            return state.getValue(CopperPipe.FACING) != direction.getOpposite() && state.getValue(CopperPipe.FACING) != direction;
        }
        return state.getBlock() instanceof CopperFitting;
    }

    public static boolean canConnectBack(@NotNull LevelAccessor level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        BlockState state = level.getBlockState(blockPos.relative(direction.getOpposite()));
        if (state.getBlock() instanceof CopperPipe) {
            return state.getValue(CopperPipe.FACING) != direction.getOpposite() && state.getValue(CopperPipe.FACING) != direction;
        }
        return state.getBlock() instanceof CopperFitting;
    }

    public static boolean isSmooth(@NotNull LevelAccessor level, @NotNull BlockPos blockPos, Direction direction) {
        BlockState state = level.getBlockState(blockPos.relative(direction));
        if (state.getBlock() instanceof CopperPipe) {
            return state.getValue(CopperPipe.FACING) == direction && !canConnectFront(level, blockPos, direction);
        }
        return false;
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        Direction direction = blockState.getValue(FACING);
        if (blockState.getValue(HAS_WATER) && direction != Direction.UP) {
            BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
            boolean hasOffset = false;
            for (int i = 0; i < 12; i++) { //Searches for 12 blocks
                if (direction != Direction.DOWN && !hasOffset) {
                    mutableBlockPos.move(direction);
                    hasOffset = true;
                }
                mutableBlockPos.move(Direction.DOWN);
                BlockState state = serverLevel.getBlockState(mutableBlockPos);
                if (serverLevel.getFluidState(mutableBlockPos).isEmpty()) {
                    LeakingPipeDrips.DripOn dripOn = LeakingPipeDrips.getDrip(state.getBlock());
                    if (dripOn != null) {
                        dripOn.dripOn(serverLevel, mutableBlockPos, state);
                        break;
                    }
                    if (state.getCollisionShape(serverLevel, mutableBlockPos) != Shapes.empty()) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        this.onRandomTick(blockState, serverLevel, blockPos, random);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState blockState) {
        Block block = blockState.getBlock();
        return block == CopperPipe.COPPER_PIPE || block == CopperPipe.EXPOSED_PIPE || block == CopperPipe.WEATHERED_PIPE || blockState.getValue(HAS_WATER);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, RandomSource random) {
        Direction direction = blockState.getValue(FACING);
        BlockState offsetState = level.getBlockState(blockPos.relative(direction));
        FluidState fluidState = offsetState.getFluidState();
        boolean canWaterOrSmokeExtra = ((!offsetState.isAir() && fluidState.isEmpty()) || direction == Direction.DOWN);
        boolean canWater = blockState.getValue(HAS_WATER) && direction != Direction.UP;
        boolean canSmoke = blockState.getValue(HAS_SMOKE) && random.nextInt(5) == 0;
        boolean hasSmokeOrWater = canWater || canSmoke;
        if (hasSmokeOrWater) {
            double outX = blockPos.getX() + getDripX(direction);
            double outY = blockPos.getY() + getDripY(direction);
            double outZ = blockPos.getZ() + getDripZ(direction);
            if (canWater) {
                level.addParticle(ParticleTypes.DRIPPING_WATER, outX, outY, outZ, 0, 0, 0);
            }
            if (canSmoke) {
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, outX, outY, outZ, 0, 0.07D, 0);
            }
            if (canWaterOrSmokeExtra) {
                double x = blockPos.getX() + getDripX(direction, random);
                double y = blockPos.getY() + getDripY(direction, random);
                double z = blockPos.getZ() + getDripZ(direction, random);
                if (canWater) {
                    level.addParticle(ParticleTypes.DRIPPING_WATER, x, outY, z, 0, 0, 0);
                }
                if (canSmoke) {
                    level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.07D, 0);
                }
            }
        }
        if (blockState.getValue(HAS_ELECTRICITY)) {
            ParticleUtils.spawnParticlesAlongAxis(direction.getAxis(), level, blockPos, 0.4D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
        }
        if (fluidState.is(FluidTags.WATER)) {
            level.addParticle(ParticleTypes.BUBBLE,
                    blockPos.getX() + getDripX(direction, random),
                    blockPos.getY() + getDripY(direction, random),
                    blockPos.getZ() + getDripZ(direction, random),
                    direction.getStepX() * 0.7D,
                    direction.getStepY() * 0.7D,
                    direction.getStepZ() * 0.7D
            );
        }
    }

    public double getRan(RandomSource random) { return UniformInt.of(-25,25).sample(random) * 0.01; }

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

    public double getDripX(@NotNull Direction direction) {
        return switch (direction) {
            case DOWN, SOUTH, NORTH, UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }

    public double getDripY(@NotNull Direction direction) {
        return switch (direction) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, SOUTH, EAST, WEST -> 0.4375;
        };
    }

    public double getDripZ(@NotNull Direction direction) {
        return switch (direction) {
            case DOWN, WEST, EAST, UP -> 0.5;
            case NORTH -> -0.05;
            case SOUTH -> 1.05;
        };
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    public static Position getOutputLocation(@NotNull BlockSource blockPointer, @NotNull Direction facing) {
        return new PositionImpl(
                blockPointer.x() + 0.7D * (double)facing.getStepX(),
                blockPointer.y() + 0.7D * (double)facing.getStepY(),
                blockPointer.z() + 0.7D * (double)facing.getStepZ()
        );
    }

    public static boolean shouldGlow(@NotNull BlockState state) {
        if (state.getBlock() instanceof CopperPipe || state.getBlock() instanceof CopperFitting) {
            return state.getValue(HAS_ITEM) || state.getValue(HAS_ELECTRICITY);
        }
        return false;
    }

    public static int getLuminance(@NotNull BlockState state) {
        if (state.getBlock() instanceof CopperPipe || state.getBlock() instanceof CopperFitting) {
            if (state.getValue(HAS_ELECTRICITY)) {
                return 5;
            }
            if (state.getValue(HAS_ITEM)) {
                return 3;
            }
        }
        return 1;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateBlockEntityValues(level, blockPos, blockState);
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperPipe)) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) {
                Containers.dropContents(level, blockPos, (CopperPipeEntity)blockEntity);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
            level.removeBlockEntity(blockPos);
        }
    }

    public static boolean isReceivingRedstonePower(BlockPos blockPos, Level level) {
        for (Direction direction : Direction.values()) {
            if (level.getSignal(blockPos.relative(direction), direction) > 0) {
                return true;
            }
        }
        return false;
    }

    public static final Block COPPER_PIPE = new CopperPipe(WeatherState.UNAFFECTED, Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, 20, ParticleTypes.SQUID_INK);
    public static final Block EXPOSED_PIPE = new CopperPipe(WeatherState.EXPOSED, Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2,18, ParticleTypes.SQUID_INK);
    public static final Block WEATHERED_PIPE = new CopperPipe(WeatherState.WEATHERED, Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2,15, ParticleTypes.SQUID_INK);
    public static final Block OXIDIZED_PIPE = new CopperPipe(WeatherState.OXIDIZED, Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2,12, ParticleTypes.SQUID_INK);

    public static final Block WAXED_COPPER_PIPE = new CopperPipe(WeatherState.UNAFFECTED, Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1,20, ParticleTypes.SQUID_INK);
    public static final Block WAXED_EXPOSED_PIPE = new CopperPipe(WeatherState.EXPOSED, Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1,18, ParticleTypes.SQUID_INK);
    public static final Block WAXED_WEATHERED_PIPE = new CopperPipe(WeatherState.WEATHERED, Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1,15, ParticleTypes.SQUID_INK);
    public static final Block WAXED_OXIDIZED_PIPE = new CopperPipe(WeatherState.OXIDIZED, Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1,12, ParticleTypes.SQUID_INK);

    public static final Block CORRODED_PIPE = new CopperPipe(WeatherState.OXIDIZED, Properties
            .of().mapColor(MapColor.TERRACOTTA_ORANGE)
            .requiresCorrectToolForDrops()
            .strength(2F, 3.5F)
            .sound(new SoundType(1.0f, 1.25f,
                    CopperPipeMain.CORRODED_COPPER_PLACE,
                    CopperPipeMain.CORRODED_COPPER_STEP,
                    CopperPipeMain.CORRODED_COPPER_BREAK,
                    CopperPipeMain.CORRODED_COPPER_FALL,
                    CopperPipeMain.CORRODED_COPPER_HIT
            )), 7,7, ParticleTypes.SQUID_INK);

    public static final Block BLACK_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,8, ParticleTypes.SQUID_INK);
    public static final Block RED_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,9, CopperPipeMain.RED_INK);
    public static final Block ORANGE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,10, CopperPipeMain.ORANGE_INK);
    public static final Block YELLOW_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,11, CopperPipeMain.YELLOW_INK);
    public static final Block LIME_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,12, CopperPipeMain.LIME_INK);
    public static final Block GREEN_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,13, CopperPipeMain.GREEN_INK);
    public static final Block CYAN_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,14, CopperPipeMain.CYAN_INK);
    public static final Block LIGHT_BLUE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,15, CopperPipeMain.LIGHT_BLUE_INK);
    public static final Block BLUE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,16, CopperPipeMain.BLUE_INK);
    public static final Block PURPLE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,17, CopperPipeMain.PURPLE_INK);
    public static final Block MAGENTA_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,18, CopperPipeMain.MAGENTA_INK);
    public static final Block PINK_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,19, CopperPipeMain.PINK_INK);
    public static final Block WHITE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,20, CopperPipeMain.WHITE_INK);
    public static final Block LIGHT_GRAY_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,21, CopperPipeMain.LIGHT_GRAY_INK);
    public static final Block GRAY_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,22, CopperPipeMain.GRAY_INK);
    public static final Block BROWN_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,23, CopperPipeMain.BROWN_INK);

    public static final Block GLOWING_BLACK_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,7, ParticleTypes.SQUID_INK);
    public static final Block GLOWING_RED_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,8, CopperPipeMain.RED_INK);
    public static final Block GLOWING_ORANGE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,9, CopperPipeMain.ORANGE_INK);
    public static final Block GLOWING_YELLOW_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,10, CopperPipeMain.YELLOW_INK);
    public static final Block GLOWING_LIME_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,11, CopperPipeMain.LIME_INK);
    public static final Block GLOWING_GREEN_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,12, CopperPipeMain.GREEN_INK);
    public static final Block GLOWING_CYAN_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,13, CopperPipeMain.CYAN_INK);
    public static final Block GLOWING_LIGHT_BLUE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4, 14, CopperPipeMain.LIGHT_BLUE_INK);
    public static final Block GLOWING_BLUE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,15, CopperPipeMain.BLUE_INK);
    public static final Block GLOWING_PURPLE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,16, CopperPipeMain.PURPLE_INK);
    public static final Block GLOWING_MAGENTA_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,17, CopperPipeMain.MAGENTA_INK);
    public static final Block GLOWING_PINK_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,18, CopperPipeMain.PINK_INK);
    public static final Block GLOWING_WHITE_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,19, CopperPipeMain.WHITE_INK);
    public static final Block GLOWING_LIGHT_GRAY_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,20, CopperPipeMain.LIGHT_GRAY_INK);
    public static final Block GLOWING_GRAY_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,21, CopperPipeMain.GRAY_INK);
    public static final Block GLOWING_BROWN_PIPE = new CopperPipe(Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, level, pos) -> CopperPipe.shouldGlow(state)), 4,22, CopperPipeMain.BROWN_INK);
}

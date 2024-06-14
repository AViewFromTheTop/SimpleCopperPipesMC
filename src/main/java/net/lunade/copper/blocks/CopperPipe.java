package net.lunade.copper.blocks;

import com.mojang.serialization.MapCodec;
import net.lunade.copper.blocks.block_entity.CopperPipeEntity;
import net.lunade.copper.blocks.block_entity.leaking_pipes.LeakingPipeDrips;
import net.lunade.copper.blocks.properties.CopperPipeProperties;
import net.lunade.copper.blocks.properties.PipeFluid;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.RegisterBlockEntities;
import net.lunade.copper.registry.RegisterStats;
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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopperPipe extends BaseEntityBlock implements SimpleWaterloggedBlock, WeatheringCopper {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty FRONT_CONNECTED = CopperPipeProperties.FRONT_CONNECTED;
    public static final BooleanProperty BACK_CONNECTED = CopperPipeProperties.BACK_CONNECTED;
    public static final BooleanProperty SMOOTH = CopperPipeProperties.SMOOTH;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<PipeFluid> FLUID = CopperPipeProperties.FLUID;
    public static final BooleanProperty HAS_ELECTRICITY = CopperPipeProperties.HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM = CopperPipeProperties.HAS_ITEM;
    private static final VoxelShape UP_SHAPE = Shapes.or(Block.box(4D, 0D, 4D, 12D, 14D, 12D), Block.box(3D, 14D, 3D, 13D, 16D, 13D));
    private static final VoxelShape DOWN_SHAPE = Shapes.or(Block.box(4D, 2D, 4D, 12D, 16D, 12D), Block.box(3D, 0D, 3D, 13D, 2D, 13D));
    private static final VoxelShape NORTH_SHAPE = Shapes.or(Block.box(4D, 4D, 2D, 12D, 12D, 16D), Block.box(3D, 3D, 0.D, 13D, 13D, 2D));
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(Block.box(4D, 4D, 0D, 12D, 12D, 14D), Block.box(3D, 3D, 14.D, 13D, 13D, 16D));
    private static final VoxelShape EAST_SHAPE = Shapes.or(Block.box(0D, 4D, 4D, 14D, 12D, 12D), Block.box(14D, 3D, 3D, 16D, 13D, 13D));
    private static final VoxelShape WEST_SHAPE = Shapes.or(Block.box(2D, 4D, 4D, 16D, 12D, 12D), Block.box(0D, 3D, 3D, 2D, 13D, 13D));
    private static final VoxelShape DOWN_FRONT = Shapes.or(Block.box(4D, -2D, 4D, 12D, 16D, 12D), Block.box(3D, -4D, 3D, 13D, -2D, 13D));
    private static final VoxelShape EAST_FRONT = Shapes.or(Block.box(0D, 4D, 4D, 18D, 12D, 12D), Block.box(18D, 3D, 3D, 20D, 13D, 13D));
    private static final VoxelShape NORTH_FRONT = Shapes.or(Block.box(4D, 4D, -2D, 12D, 12D, 16D), Block.box(3D, 3D, -4D, 13D, 13D, -2D));
    private static final VoxelShape SOUTH_FRONT = Shapes.or(Block.box(4D, 4D, 0D, 12D, 12D, 18D), Block.box(3D, 3D, 18.D, 13D, 13D, 20D));
    private static final VoxelShape WEST_FRONT = Shapes.or(Block.box(-2D, 4D, 4D, 16D, 12D, 12D), Block.box(-4D, 3D, 3D, -2D, 13D, 13D));
    private static final VoxelShape UP_FRONT = Shapes.or(Block.box(4D, 0D, 4D, 12D, 18D, 12D), Block.box(3D, 18D, 3D, 13D, 20D, 13D));
    private static final VoxelShape DOWN_BACK = Shapes.or(Block.box(4D, 2D, 4D, 12D, 20D, 12D), Block.box(3D, 0D, 3D, 13D, 2D, 13D));
    private static final VoxelShape EAST_BACK = Shapes.or(Block.box(-4D, 4D, 4D, 14D, 12D, 12D), Block.box(14D, 3D, 3D, 16D, 13D, 13D));
    private static final VoxelShape NORTH_BACK = Shapes.or(Block.box(4D, 4D, 2D, 12D, 12D, 20D), Block.box(3D, 3D, 0.D, 13D, 13D, 2D));
    private static final VoxelShape SOUTH_BACK = Shapes.or(Block.box(4D, 4D, -4D, 12D, 12D, 14D), Block.box(3D, 3D, 14.D, 13D, 13D, 16D));
    private static final VoxelShape WEST_BACK = Shapes.or(Block.box(2D, 4D, 4D, 20D, 12D, 12D), Block.box(0D, 3D, 3D, 2D, 13D, 13D));
    private static final VoxelShape UP_BACK = Shapes.or(Block.box(4D, -4D, 4D, 12D, 14D, 12D), Block.box(3D, 14D, 3D, 13D, 16D, 13D));
    private static final VoxelShape DOWN_DOUBLE = Block.box(4D, -4D, 4D, 12D, 20D, 12D);
    private static final VoxelShape NORTH_DOUBLE = Block.box(4D, 4D, -4D, 12D, 12D, 20D);
    private static final VoxelShape EAST_DOUBLE = Block.box(-4D, 4D, 4D, 20D, 12D, 12D);
    private static final VoxelShape DOWN_SMOOTH = Block.box(4D, 0D, 4D, 12D, 16D, 12D);
    private static final VoxelShape NORTH_SMOOTH = Block.box(4D, 4D, 0D, 12D, 12D, 16D);
    private static final VoxelShape EAST_SMOOTH = Block.box(0D, 4D, 4D, 16D, 12D, 12D);
    private static final VoxelShape DOWN_BACK_SMOOTH = Block.box(4D, 0D, 4D, 12D, 20D, 12D);
    private static final VoxelShape NORTH_BACK_SMOOTH = Block.box(4D, 4D, 0D, 12D, 12D, 20D);
    private static final VoxelShape SOUTH_BACK_SMOOTH = Block.box(4D, 4D, -4D, 12D, 12D, 16D);
    private static final VoxelShape EAST_BACK_SMOOTH = Block.box(-4D, 4D, 4D, 16D, 12D, 12D);
    private static final VoxelShape WEST_BACK_SMOOTH = Block.box(0D, 4D, 4D, 20D, 12D, 12D);
    private static final VoxelShape UP_BACK_SMOOTH = Block.box(4D, -4D, 4D, 12D, 16D, 12D);
    public final int cooldown;
    public final int dispenserShotLength;
    private final WeatherState weatherState;

    public CopperPipe(WeatherState weatherState, Properties settings, int cooldown, int dispenserShotLength) {
        super(settings);
        this.weatherState = weatherState;
        this.cooldown = cooldown;
        this.dispenserShotLength = dispenserShotLength;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(SMOOTH, false).setValue(WATERLOGGED, false).setValue(FLUID, PipeFluid.NONE).setValue(HAS_ELECTRICITY, false).setValue(HAS_ITEM, false).setValue(POWERED, false));
    }

    public CopperPipe(Properties settings, int cooldown, int dispenserShotLength) {
        this(WeatherState.UNAFFECTED, settings, cooldown, dispenserShotLength);
    }

    public static void updateBlockEntityValues(Level level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            BlockState dirState = level.getBlockState(pos.relative(direction));
            BlockState oppState = level.getBlockState(pos.relative(direction.getOpposite()));
            Block oppBlock = oppState.getBlock();
            if (level.getBlockEntity(pos) instanceof CopperPipeEntity pipe) {
                pipe.canDispense = (dirState.isAir() || dirState.getBlock() == Blocks.WATER) && (!oppState.isAir() && oppBlock != Blocks.WATER);
                pipe.shootsControlled = oppBlock == Blocks.DROPPER;
                pipe.shootsSpecial = oppBlock == Blocks.DISPENSER;
                pipe.canAccept = !(oppBlock instanceof CopperPipe) && !(oppBlock instanceof CopperFitting) && !oppState.isRedstoneConductor(level, pos);
                pipe.canWater = (oppBlock == Blocks.WATER || state.getValue(BlockStateProperties.WATERLOGGED) || (oppState.hasProperty(BlockStateProperties.WATERLOGGED) ? oppState.getValue(BlockStateProperties.WATERLOGGED) : false)) && SimpleCopperPipesConfig.get().carryWater;
                pipe.canLava = oppBlock == Blocks.LAVA && SimpleCopperPipesConfig.get().carryLava;
                boolean canWaterAndLava = pipe.canWater && pipe.canLava;
                pipe.canSmoke = (oppBlock instanceof CampfireBlock && !pipe.canWater && !pipe.canLava ? oppState.getValue(BlockStateProperties.LIT) : canWaterAndLava) && SimpleCopperPipesConfig.get().carrySmoke;
                if (canWaterAndLava) {
                    pipe.canWater = false;
                    pipe.canLava = false;
                }
            }
        }
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

    public static Vec3 getOutputLocation(@NotNull BlockPos pos, @NotNull Direction facing) {
        return new Vec3(
                ((double) pos.getX() + 0.5D) + 0.7D * (double) facing.getStepX(),
                ((double) pos.getY() + 0.5D) + 0.7D * (double) facing.getStepY(),
                ((double) pos.getZ() + 0.5D) + 0.7D * (double) facing.getStepZ()
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

    public static boolean isReceivingRedstonePower(BlockPos blockPos, Level level) {
        for (Direction direction : Direction.values()) {
            if (level.getSignal(blockPos.relative(direction), direction) > 0) {
                return true;
            }
        }
        return false;
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
            return createTickerHelper(blockEntityType, RegisterBlockEntities.COPPER_PIPE_ENTITY, (level1, blockPos, blockState1, copperPipeEntity) ->
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
    protected InteractionResult useWithoutItem(BlockState state, @NotNull Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CopperPipeEntity copperPipeEntity) {
            player.openMenu(copperPipeEntity);
            player.awardStat(Stats.CUSTOM.get(RegisterStats.INSPECT_PIPE));
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(@NotNull ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(SimpleCopperPipesItemTags.IGNORES_COPPER_PIPE_MENU)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FRONT_CONNECTED, BACK_CONNECTED, SMOOTH, WATERLOGGED, FLUID, HAS_ELECTRICITY, HAS_ITEM, POWERED);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        Direction direction = blockState.getValue(FACING);
        boolean isLava = blockState.getValue(FLUID) == PipeFluid.LAVA;
        if (blockState.getValue(FLUID) == PipeFluid.WATER || isLava && direction != Direction.UP) {
            if (random.nextFloat() <= (isLava ? 0.05859375F : 0.17578125F) * 2) {
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
                            dripOn.dripOn(isLava, serverLevel, mutableBlockPos, state);
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
        }
        this.changeOverTime(blockState, serverLevel, blockPos, random);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState blockState) {
        return WeatheringCopper.getNext(blockState.getBlock()).isPresent() || blockState.getValue(FLUID) == PipeFluid.WATER || blockState.getValue(FLUID) == PipeFluid.LAVA;
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, RandomSource random) {
        Direction direction = blockState.getValue(FACING);
        BlockPos offsetPos = blockPos.relative(direction);
        BlockState offsetState = level.getBlockState(offsetPos);
        FluidState fluidState = offsetState.getFluidState();
        boolean canWater = blockState.getValue(FLUID) == PipeFluid.WATER && direction != Direction.UP;
        boolean canLava = blockState.getValue(FLUID) == PipeFluid.LAVA && random.nextInt(2) == 0 && direction != Direction.UP;
        boolean canSmoke = blockState.getValue(FLUID) == PipeFluid.SMOKE && random.nextInt(5) == 0;
        boolean canWaterOrLava = canWater || canLava;
        boolean hasSmokeOrWaterOrLava = canWaterOrLava || canSmoke;
        if (hasSmokeOrWaterOrLava) {
            double outX = blockPos.getX() + getDripX(direction, random);
            double outY = blockPos.getY() + getDripY(direction, random);
            double outZ = blockPos.getZ() + getDripZ(direction, random);
            if (canWaterOrLava && (fluidState.isEmpty() || ((fluidState.getHeight(level, offsetPos)) + (double) offsetPos.getY()) < outY)) {
                level.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA, outX, outY, outZ, 0, 0, 0);
            }
            if (canSmoke) {
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, outX, outY, outZ, 0, 0.07D, 0);
            }
            if ((!offsetState.isAir() && fluidState.isEmpty())) {
                double x = blockPos.getX() + getDripX(direction, random);
                double y = blockPos.getY() + getDripY(direction, random);
                double z = blockPos.getZ() + getDripZ(direction, random);
                if (canWaterOrLava && direction == Direction.DOWN) {
                    level.addParticle(canWater ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA, x, outY, z, 0, 0, 0);
                }
                if (canSmoke && direction == Direction.UP) {
                    level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.07D, 0);
                }
            }
        }
        if (blockState.getValue(HAS_ELECTRICITY)) {
            ParticleUtils.spawnParticlesAlongAxis(direction.getAxis(), level, blockPos, 0.4D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
        }
        if (fluidState.is(FluidTags.WATER) && (random.nextFloat() <= 0.1F || offsetState.getCollisionShape(level, offsetPos).isEmpty())) {
            level.addParticle(ParticleTypes.BUBBLE,
                    blockPos.getX() + getDripX(direction, random),
                    blockPos.getY() + getDripY(direction, random),
                    blockPos.getZ() + getDripZ(direction, random),
                    direction.getStepX() * 0.7D,
                    direction.getStepY() * 0.7D,
                    direction.getStepZ() * 0.7D
            );
            if ((canLava || canSmoke) && random.nextInt(2) == 0) {
                level.addParticle(ParticleTypes.SMOKE,
                        blockPos.getX() + getDripX(direction, random),
                        blockPos.getY() + getDripY(direction, random),
                        blockPos.getZ() + getDripZ(direction, random),
                        direction.getStepX() * 0.05D,
                        direction.getStepY() * 0.05D,
                        direction.getStepZ() * 0.05D
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

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateBlockEntityValues(level, blockPos, blockState);
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperPipe)) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) {
                Containers.dropContents(level, blockPos, (CopperPipeEntity) blockEntity);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
            level.removeBlockEntity(blockPos);
        }
    }

    @Override
    protected MapCodec<? extends CopperPipe> codec() {
        return null;
    }
}

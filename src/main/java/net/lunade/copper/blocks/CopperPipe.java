package net.lunade.copper.blocks;

import net.lunade.copper.CopperPipeMain;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.PositionImpl;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

import static net.lunade.copper.CopperPipeMain.INSPECT_PIPE;

public class CopperPipe extends BaseEntityBlock implements SimpleWaterloggedBlock, Copyable {

    public int cooldown;
    public int dispenserShotLength;
    public ParticleOptions ink;

    public static final DirectionProperty FACING;
    public static final BooleanProperty FRONT_CONNECTED;
    public static final BooleanProperty BACK_CONNECTED;
    public static final BooleanProperty SMOOTH;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty HAS_WATER;
    public static final BooleanProperty HAS_SMOKE;
    public static final BooleanProperty HAS_ELECTRICITY;
    public static final BooleanProperty HAS_ITEM;

    private static final VoxelShape DOWN_SHAPE;
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape NORTH_SHAPE;
    private static final VoxelShape SOUTH_SHAPE;
    private static final VoxelShape WEST_SHAPE;
    private static final VoxelShape UP_SHAPE;

    private static final VoxelShape DOWN_FRONT;
    private static final VoxelShape EAST_FRONT;
    private static final VoxelShape NORTH_FRONT;
    private static final VoxelShape SOUTH_FRONT;
    private static final VoxelShape WEST_FRONT;
    private static final VoxelShape UP_FRONT;

    private static final VoxelShape DOWN_BACK;
    private static final VoxelShape EAST_BACK;
    private static final VoxelShape NORTH_BACK;
    private static final VoxelShape SOUTH_BACK;
    private static final VoxelShape WEST_BACK;
    private static final VoxelShape UP_BACK;

    private static final VoxelShape DOWN_DOUBLE;
    private static final VoxelShape NORTH_DOUBLE;
    private static final VoxelShape EAST_DOUBLE;

    private static final VoxelShape DOWN_SMOOTH;
    private static final VoxelShape NORTH_SMOOTH;
    private static final VoxelShape EAST_SMOOTH;

    private static final VoxelShape DOWN_BACK_SMOOTH;
    private static final VoxelShape NORTH_BACK_SMOOTH;
    private static final VoxelShape SOUTH_BACK_SMOOTH;
    private static final VoxelShape EAST_BACK_SMOOTH;
    private static final VoxelShape WEST_BACK_SMOOTH;
    private static final VoxelShape UP_BACK_SMOOTH;

    public CopperPipe(Properties settings, int cooldown, int dispenserShotLength, ParticleOptions ink) {
        super(settings);
        this.cooldown=cooldown;
        this.dispenserShotLength=dispenserShotLength;
        this.ink=ink;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(SMOOTH, false).setValue(WATERLOGGED, false).setValue(HAS_WATER, false).setValue(HAS_SMOKE, false).setValue(HAS_ELECTRICITY, false).setValue(HAS_ITEM, false).setValue(POWERED, false));
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

    public VoxelShape getShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos, CollisionContext shapeContext) { return getPipeShape(blockState);}
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos) { return getPipeShape(blockState); }

    public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
        Direction direction = itemPlacementContext.getClickedFace().getOpposite();
        BlockPos blockPos = itemPlacementContext.getClickedPos();
        boolean front = canConnectFront(itemPlacementContext.getLevel(), blockPos, direction.getOpposite());
        boolean back = canConnectBack(itemPlacementContext.getLevel(), blockPos, direction.getOpposite());
        boolean smooth = isSmooth(itemPlacementContext.getLevel(), blockPos, direction.getOpposite());
        FluidState fluidState = itemPlacementContext.getLevel().getFluidState(blockPos);
        return this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(FRONT_CONNECTED, front).setValue(BACK_CONNECTED, back).setValue(SMOOTH, smooth).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor worldAccess, BlockPos blockPos, BlockPos blockPos2) {
        boolean front = canConnectFront(worldAccess, blockPos, blockState.getValue(FACING));
        boolean back = canConnectBack(worldAccess, blockPos, blockState.getValue(FACING));
        boolean smooth = isSmooth(worldAccess, blockPos, blockState.getValue(FACING));
        if (blockState.getValue(WATERLOGGED)) {worldAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldAccess));}
        boolean electricity = blockState.getValue(HAS_ELECTRICITY);
        if (worldAccess.getBlockState(blockPos2).getBlock() instanceof LightningRodBlock) { if (worldAccess.getBlockState(blockPos2).getValue(POWERED)) {electricity=true;} }
        return blockState.setValue(FRONT_CONNECTED, front).setValue(BACK_CONNECTED, back).setValue(SMOOTH, smooth).setValue(HAS_ELECTRICITY, electricity);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, world, blockPos, block, blockPos2, bl);
        boolean powered = isReceivingRedstonePower(blockPos, world);
        if (powered!=blockState.getValue(POWERED)) {world.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, powered));}
        updateBlockEntityValues(world, blockPos, blockState);
    }

    public static void updateBlockEntityValues(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            Direction directionOpp = direction.getOpposite();
            Block dirBlock = world.getBlockState(pos.relative(direction)).getBlock();
            BlockState oppState = world.getBlockState(pos.relative(directionOpp));
            Block oppBlock = oppState.getBlock();
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CopperPipeEntity pipe) {
                pipe.canDispense = (dirBlock == Blocks.AIR || dirBlock == Blocks.WATER) && (oppBlock != Blocks.AIR && oppBlock != Blocks.WATER);
                pipe.corroded = oppBlock == CopperFitting.CORRODED_FITTING || state.getBlock() == CopperPipe.CORRODED_PIPE;
                pipe.shootsControlled = oppBlock == Blocks.DROPPER;
                pipe.shootsSpecial = oppBlock == Blocks.DISPENSER;
                pipe.canAccept = !(oppBlock instanceof CopperPipe) && !(oppBlock instanceof CopperFitting) && !oppState.isRedstoneConductor(world, pos);
                pipe.canSmoke = oppBlock instanceof CampfireBlock ? oppState.getValue(BlockStateProperties.LIT) : false;
                pipe.canWater = oppBlock == Blocks.WATER || state.getValue(BlockStateProperties.WATERLOGGED) || (oppState.hasProperty(BlockStateProperties.WATERLOGGED) ? oppState.getValue(BlockStateProperties.WATERLOGGED) : false);
            }
        }
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CopperPipeEntity(blockPos, blockState);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockView, BlockPos blockPos) { return blockState.getFluidState().isEmpty();}

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!world.isClientSide) {
            return createTickerHelper(blockEntityType, CopperPipeMain.COPPER_PIPE_ENTITY, (world1, blockPos, blockState1, copperPipeEntity) -> copperPipeEntity.serverTick(world1, blockPos, blockState1));
        } return null;
    }

    @Nullable
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel world, T blockEntity) {
        if (blockEntity instanceof CopperPipeEntity pipeEntity) {
            return pipeEntity.getGameEventListener();
        } return null;
    }

    public void setPlacedBy(Level world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        updateBlockEntityValues(world, blockPos, blockState);
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) { ((CopperPipeEntity)blockEntity).setCustomName(itemStack.getHoverName()); }
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        } return super.getFluidState(blockState);
    }

    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player playerEntity, InteractionHand hand, BlockHitResult blockHitResult) {
        if (playerEntity.getItemInHand(hand).getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof CopperPipe || blockItem.getBlock() instanceof CopperFitting) {
                return  InteractionResult.PASS;
            }
        }
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) {
                playerEntity.openMenu((CopperPipeEntity) blockEntity);
                playerEntity.awardStat(Stats.CUSTOM.get(INSPECT_PIPE));
            } return InteractionResult.CONSUME;
        }
    }

    public RenderShape getRenderShape(BlockState blockState) { return RenderShape.MODEL; }

    public boolean hasAnalogOutputSignal(BlockState blockState) { return true; }

    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos blockPos) { return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(blockPos)); }

    public BlockState rotate(BlockState blockState, Rotation blockRotation) { return blockState.setValue(FACING, blockRotation.rotate(blockState.getValue(FACING))); }

    public BlockState mirror(BlockState blockState, Mirror blockMirror) { return blockState.rotate(blockMirror.getRotation(blockState.getValue(FACING))); }

    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) { builder.add(FACING).add(FRONT_CONNECTED).add(BACK_CONNECTED).add(SMOOTH).add(WATERLOGGED).add(HAS_WATER).add(HAS_SMOKE).add(HAS_ELECTRICITY).add(HAS_ITEM).add(POWERED); }

    public boolean isPathfindable(BlockState blockState, BlockGetter blockView, BlockPos blockPos, PathComputationType navigationType) { return false; }

    public static boolean canConnectFront(Level world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.relative(direction));
        if (isPipe(state)) {
            return state.getValue(CopperPipe.FACING) != direction.getOpposite() && state.getValue(CopperPipe.FACING) != direction;
        } return isFitting(state);
    }

    public static boolean canConnectBack(Level world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.relative(direction.getOpposite()));
        if (isPipe(state)) {
            return state.getValue(CopperPipe.FACING) != direction.getOpposite() && state.getValue(CopperPipe.FACING) != direction;
        } return isFitting(state);
    }

    public static boolean isSmooth(Level world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.relative(direction));
        if (isPipe(state)) {
            return state.getValue(CopperPipe.FACING) == direction && !canConnectFront(world,blockPos,direction);
        } return false;
    }

    public static boolean canConnectFront(LevelAccessor world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.relative(direction));
        if (isPipe(state)) {
            return state.getValue(CopperPipe.FACING) != direction.getOpposite() && state.getValue(CopperPipe.FACING) != direction;
        } return isFitting(state);
    }

    public static boolean canConnectBack(LevelAccessor world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.relative(direction.getOpposite()));
        if (isPipe(state)) {
            return state.getValue(CopperPipe.FACING) != direction.getOpposite() && state.getValue(CopperPipe.FACING) != direction;
        } return isFitting(state);
    }

    public static boolean isSmooth(LevelAccessor world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.relative(direction));
        if (isPipe(state)) {
            return state.getValue(CopperPipe.FACING) == direction && !canConnectFront(world,blockPos,direction);
        } return false;
    }

    public static boolean isPipe(BlockState state) {return state.getBlock() instanceof CopperPipe;}

    public static boolean isFitting(BlockState state) {return state.getBlock() instanceof CopperFitting;}

    public void randomTick(BlockState blockState, ServerLevel serverWorld, BlockPos blockPos, RandomSource random) {
        Direction direction = blockState.getValue(FACING);
        if (blockState.getValue(HAS_WATER) && direction!=Direction.UP) {
            BlockPos pos = blockPos;
            boolean hasOffset = false;
            for (int i=0; i<12; i++) { //Searches for 12 blocks
                if (direction != Direction.DOWN && !hasOffset) {
                    pos = pos.relative(direction);
                    hasOffset = true;
                }
                pos = pos.below();
                BlockState state = serverWorld.getBlockState(pos);
                if (serverWorld.getFluidState(pos).isEmpty()) {
                    if (state.getBlock() == Blocks.CAULDRON) {
                        i = 99; //Stop loop if viable Cauldron is found
                        serverWorld.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 1));
                    }
                    if (state.getBlock() == Blocks.WATER_CAULDRON) {
                        if (state.getValue(BlockStateProperties.LEVEL_CAULDRON) != 3) { //Ignores filled Cauldrons
                            i = 99; //Stop loop if viable Cauldron is found
                            serverWorld.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, state.getValue(BlockStateProperties.LEVEL_CAULDRON) + 1));
                        }
                    }
                    if (state.getBlock() == Blocks.DIRT) {
                        i = 99; //Stop loop if viable Block is found
                        serverWorld.setBlockAndUpdate(pos, Blocks.MUD.defaultBlockState());
                    }
                    if (state.getBlock() == Blocks.FIRE) {
                        serverWorld.destroyBlock(pos, false);
                    }
                    if (state.isRedstoneConductor(serverWorld, pos)) {
                        i = 99;
                    } //Water will "pass through" all non-full blocks (I.E. ladders, stairs). This also allows for water to "overflow" from Cauldrons down into one below if they're full or have Lava.
                }
            }
        }

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

    public boolean isRandomlyTicking(BlockState blockState) {
        Block block = blockState.getBlock();
        return block==CopperPipe.COPPER_PIPE || block==CopperPipe.EXPOSED_PIPE || block==CopperPipe.WEATHERED_PIPE || blockState.getValue(HAS_WATER);
    }

    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos blockPos, RandomSource random) {
        Direction direction = blockState.getValue(FACING);
        BlockState offsetState = world.getBlockState(blockPos.relative(direction));
        boolean waterInFront = offsetState.getBlock()==Blocks.WATER;
        boolean canWaterOrSmokeExtra = ((offsetState.getBlock()!=Blocks.AIR && !waterInFront) || direction==Direction.DOWN);
        if (blockState.getValue(HAS_WATER) && direction!=Direction.UP) {
            world.addParticle(ParticleTypes.DRIPPING_WATER, blockPos.getX()+getDripX(direction), blockPos.getY()+getDripY(direction), blockPos.getZ()+getDripZ(direction),0,0,0);
            if (canWaterOrSmokeExtra) {
                double x = blockPos.getX()+getDripX(direction, random);
                double y = blockPos.getY()+getDripY(direction, random);
                double z = blockPos.getZ()+getDripZ(direction, random);
                world.addParticle(ParticleTypes.DRIPPING_WATER, x,y,z,0,0,0);
            }
        }
        if (random.nextInt(5) == 0) {
            if (blockState.getValue(HAS_SMOKE)) {
                CampfireBlock.makeParticles(world, blockPos.relative(direction), false, false);
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, blockPos.getX()+getDripX(direction), blockPos.getY()+getDripY(direction), blockPos.getZ()+getDripZ(direction),0.0D, 0.07D, 0.0D);
                if (canWaterOrSmokeExtra) {
                    double x = blockPos.getX()+getDripX(direction, random);
                    double y = blockPos.getY()+getDripY(direction, random);
                    double z = blockPos.getZ()+getDripZ(direction, random);
                    world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x,y,z,0.0D, 0.07D, 0.0D);
                }
            }
        }
        if (blockState.getValue(HAS_ELECTRICITY)) {
            ParticleUtils.spawnParticlesAlongAxis(direction.getAxis(), world, blockPos, 0.4D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
        }
        if (waterInFront) {
            double x = blockPos.getX()+getDripX(direction, random);
            double y = blockPos.getY()+getDripY(direction, random);
            double z = blockPos.getZ()+getDripZ(direction, random);
            world.addParticle(ParticleTypes.BUBBLE, x,y,z,direction.getStepX()*0.7D, direction.getStepY()*0.7D, direction.getStepZ()*0.7D);
        }
    }

    public double getRan(RandomSource random) { return UniformInt.of(-25,25).sample(random) * 0.01; }

    public double getDripX(Direction direction, RandomSource random) {
        return switch (direction) {
            case DOWN, SOUTH, NORTH -> 0.5 + getRan(random);
            case UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }
    public double getDripY(Direction direction, RandomSource random) {
        return switch (direction) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, WEST, EAST, SOUTH -> 0.5 + getRan(random);
        };
    }
    public double getDripZ(Direction direction, RandomSource random) {
        return switch (direction) {
            case DOWN, EAST, WEST -> 0.5 + getRan(random);
            case UP -> 0.5;
            case NORTH -> -0.05;
            case SOUTH -> 1.05;
        };
    }
    public double getDripX(Direction direction) {
        return switch (direction) {
            case DOWN, SOUTH, NORTH, UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }
    public double getDripY(Direction direction) {
        return switch (direction) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, SOUTH, EAST, WEST -> 0.5;
        };
    }
    public double getDripZ(Direction direction) {
        return switch (direction) {
            case DOWN, WEST, EAST, UP -> 0.5;
            case NORTH -> -0.05;
            case SOUTH -> 1.05;
        };
    }

    public static Position getOutputLocation(BlockSource blockPointer, Direction facing) {
        return new PositionImpl(
                blockPointer.x() + 0.7D * (double)facing.getStepX(),
                blockPointer.y() + 0.7D * (double)facing.getStepY(),
                blockPointer.z() + 0.7D * (double)facing.getStepZ());
    }

    public static boolean hasItem(BlockState state) {
        if (state.getBlock() instanceof CopperPipe || state.getBlock() instanceof CopperFitting) {
            return state.getValue(HAS_ITEM) || state.getValue(HAS_ELECTRICITY);
        } return false;
    }

    public static int getLuminance(BlockState state) {
        if (state.getBlock() instanceof CopperPipe || state.getBlock() instanceof CopperFitting) {
            if (state.getValue(HAS_ELECTRICITY)) {return 5;}
            if (state.getValue(HAS_ITEM)) {return 3;}
        } return 1;
    }

    public void onRemove(BlockState blockState, Level world, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateBlockEntityValues(world, blockPos, blockState);
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperPipe)) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) {
                Containers.dropContents(world, blockPos, (CopperPipeEntity)blockEntity);
                world.updateNeighbourForOutputSignal(blockPos, this);
            } world.removeBlockEntity(blockPos);
        }
    }

    public static boolean isReceivingRedstonePower(BlockPos blockPos, Level world) {
        for (Direction direction : Direction.values()) {
            if (world.getSignal(blockPos.relative(direction), direction) > 0) {return true;}
        } return false;
    }

    public void makeCopyOf(BlockState state, Level world, BlockPos blockPos, Block block) {
        if (block instanceof CopperPipe) {
            world.setBlockAndUpdate(blockPos, block.defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(FRONT_CONNECTED, state.getValue(FRONT_CONNECTED))
                    .setValue(BACK_CONNECTED, state.getValue(BACK_CONNECTED)).setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                    .setValue(HAS_WATER, state.getValue(HAS_WATER)).setValue(SMOOTH, state.getValue(SMOOTH))
                    .setValue(HAS_ITEM, state.getValue(HAS_ITEM)).setValue(HAS_SMOKE, state.getValue(HAS_SMOKE)).setValue(HAS_ELECTRICITY, state.getValue(HAS_ELECTRICITY)).setValue(POWERED, state.getValue(POWERED)));
        }
    }
    public BlockState makeCopyOf(BlockState state, Block block) {
        if (block instanceof CopperPipe) {
            return block.defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(FRONT_CONNECTED, state.getValue(FRONT_CONNECTED))
                    .setValue(BACK_CONNECTED, state.getValue(BACK_CONNECTED)).setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                    .setValue(HAS_WATER, state.getValue(HAS_WATER)).setValue(SMOOTH, state.getValue(SMOOTH))
                    .setValue(HAS_ITEM, state.getValue(HAS_ITEM)).setValue(HAS_SMOKE, state.getValue(HAS_SMOKE)).setValue(HAS_ELECTRICITY, state.getValue(HAS_ELECTRICITY)).setValue(POWERED, state.getValue(POWERED));
        } else return null;
    }

    static {
        FACING = BlockStateProperties.FACING;
        FRONT_CONNECTED = CopperPipeProperties.FRONT_CONNECTED;
        BACK_CONNECTED = CopperPipeProperties.BACK_CONNECTED;
        SMOOTH = CopperPipeProperties.SMOOTH;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        HAS_WATER = CopperPipeProperties.HAS_WATER;
        HAS_SMOKE = CopperPipeProperties.HAS_SMOKE;
        HAS_ELECTRICITY = CopperPipeProperties.HAS_ELECTRICITY;
        HAS_ITEM = CopperPipeProperties.HAS_ITEM;
        POWERED = BlockStateProperties.POWERED;

        UP_SHAPE = Shapes.or(Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D),Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D));
        DOWN_SHAPE = Shapes.or(Block.box(4.0D, 2.0D, 4.0D, 12.0D, 16.0D, 12.0D),Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D));
        EAST_SHAPE = Shapes.or(Block.box(0.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D),Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
        WEST_SHAPE = Shapes.or(Block.box(2.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D),Block.box(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D));
        NORTH_SHAPE = Shapes.or(Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 16.0D),Block.box(3.0D, 3.0D, 0.D, 13.0D, 13.0D, 2.0D));
        SOUTH_SHAPE = Shapes.or(Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 14.0D),Block.box(3.0D, 3.0D, 14.D, 13.0D, 13.0D, 16.0D));

        UP_BACK = Shapes.or(Block.box(4.0D, -4.0D, 4.0D, 12.0D, 14.0D, 12.0D),Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D));
        DOWN_BACK = Shapes.or(Block.box(4.0D, 2.0D, 4.0D, 12.0D, 20.0D, 12.0D),Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D));
        EAST_BACK = Shapes.or(Block.box(-4.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D),Block.box(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
        WEST_BACK = Shapes.or(Block.box(2.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D),Block.box(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D));
        NORTH_BACK = Shapes.or(Block.box(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 20.0D),Block.box(3.0D, 3.0D, 0.D, 13.0D, 13.0D, 2.0D));
        SOUTH_BACK = Shapes.or(Block.box(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 14.0D),Block.box(3.0D, 3.0D, 14.D, 13.0D, 13.0D, 16.0D));

        UP_BACK_SMOOTH = Block.box(4.0D, -4.0D, 4.0D, 12.0D, 16.0D, 12.0D);
        DOWN_BACK_SMOOTH = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 20.0D, 12.0D);
        EAST_BACK_SMOOTH = Block.box(-4.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
        WEST_BACK_SMOOTH = Block.box(0.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D);
        NORTH_BACK_SMOOTH = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 20.0D);
        SOUTH_BACK_SMOOTH = Block.box(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 16.0D);

        UP_FRONT = Shapes.or(Block.box(4.0D, 0.0D, 4.0D, 12.0D, 18.0D, 12.0D),Block.box(3.0D, 18.0D, 3.0D, 13.0D, 20.0D, 13.0D));
        DOWN_FRONT = Shapes.or(Block.box(4.0D, -2.0D, 4.0D, 12.0D, 16.0D, 12.0D),Block.box(3.0D, -4.0D, 3.0D, 13.0D, -2.0D, 13.0D));
        EAST_FRONT = Shapes.or(Block.box(0.0D, 4.0D, 4.0D, 18.0D, 12.0D, 12.0D),Block.box(18.0D, 3.0D, 3.0D, 20.0D, 13.0D, 13.0D));
        WEST_FRONT = Shapes.or(Block.box(-2.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D),Block.box(-4.0D, 3.0D, 3.0D, -2.0D, 13.0D, 13.0D));
        NORTH_FRONT = Shapes.or(Block.box(4.0D, 4.0D, -2.0D, 12.0D, 12.0D, 16.0D),Block.box(3.0D, 3.0D, -4.0D, 13.0D, 13.0D, -2.0D));
        SOUTH_FRONT = Shapes.or(Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 18.0D),Block.box(3.0D, 3.0D, 18.D, 13.0D, 13.0D, 20.0D));

        EAST_DOUBLE = Block.box(-4.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D);
        NORTH_DOUBLE = Block.box(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 20.0D);
        DOWN_DOUBLE = Block.box(4.0D, -4.0D, 4.0D, 12.0D, 20.0D, 12.0D);

        EAST_SMOOTH = Block.box(0.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
        NORTH_SMOOTH = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 16.0D);
        DOWN_SMOOTH = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    }

    public static final Block OXIDIZED_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2,12, ParticleTypes.SQUID_INK);
    public static final Block WEATHERED_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2,15, ParticleTypes.SQUID_INK);
    public static final Block EXPOSED_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2,18, ParticleTypes.SQUID_INK);
    public static final Block COPPER_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 2, 20, ParticleTypes.SQUID_INK);

    public static final Block WAXED_OXIDIZED_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1,12, ParticleTypes.SQUID_INK);
    public static final Block WAXED_WEATHERED_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.WARPED_STEM).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1,15, ParticleTypes.SQUID_INK);
    public static final Block WAXED_EXPOSED_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1,18, ParticleTypes.SQUID_INK);
    public static final Block WAXED_COPPER_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 1,20, ParticleTypes.SQUID_INK);

    public static final Block CORRODED_PIPE = new CopperPipe(Properties
            .of(Material.METAL, MaterialColor.TERRACOTTA_ORANGE)
            .requiresCorrectToolForDrops()
            .strength(2F, 3.5F)
            .sound(new SoundType(1.0f, 1.25f,
                    CopperPipeMain.CORRODED_COPPER_PLACE,
                    CopperPipeMain.CORRODED_COPPER_STEP,
                    CopperPipeMain.CORRODED_COPPER_BREAK,
                    CopperPipeMain.CORRODED_COPPER_FALL,
                    CopperPipeMain.CORRODED_COPPER_HIT
            )), 7,7, ParticleTypes.SQUID_INK);

    public static final Block BLACK_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,8, ParticleTypes.SQUID_INK);
    public static final Block RED_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,9, CopperPipeMain.RED_INK);
    public static final Block ORANGE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,10, CopperPipeMain.ORANGE_INK);
    public static final Block YELLOW_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,11, CopperPipeMain.YELLOW_INK);
    public static final Block LIME_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,12, CopperPipeMain.LIME_INK);
    public static final Block GREEN_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,13, CopperPipeMain.GREEN_INK);
    public static final Block CYAN_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,14, CopperPipeMain.CYAN_INK);
    public static final Block LIGHT_BLUE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,15, CopperPipeMain.LIGHT_BLUE_INK);
    public static final Block BLUE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,16, CopperPipeMain.BLUE_INK);
    public static final Block PURPLE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,17, CopperPipeMain.PURPLE_INK);
    public static final Block MAGENTA_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,18, CopperPipeMain.MAGENTA_INK);
    public static final Block PINK_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,19, CopperPipeMain.PINK_INK);
    public static final Block WHITE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,20, CopperPipeMain.WHITE_INK);
    public static final Block LIGHT_GRAY_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,21, CopperPipeMain.LIGHT_GRAY_INK);
    public static final Block GRAY_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,22, CopperPipeMain.GRAY_INK);
    public static final Block BROWN_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER), 4,23, CopperPipeMain.BROWN_INK);

    public static final Block GLOWING_BLACK_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,7, ParticleTypes.SQUID_INK);
    public static final Block GLOWING_RED_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,8, CopperPipeMain.RED_INK);
    public static final Block GLOWING_ORANGE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,9, CopperPipeMain.ORANGE_INK);
    public static final Block GLOWING_YELLOW_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,10, CopperPipeMain.YELLOW_INK);
    public static final Block GLOWING_LIME_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,11, CopperPipeMain.LIME_INK);
    public static final Block GLOWING_GREEN_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,12, CopperPipeMain.GREEN_INK);
    public static final Block GLOWING_CYAN_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,13, CopperPipeMain.CYAN_INK);
    public static final Block GLOWING_LIGHT_BLUE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4, 14, CopperPipeMain.LIGHT_BLUE_INK);
    public static final Block GLOWING_BLUE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,15, CopperPipeMain.BLUE_INK);
    public static final Block GLOWING_PURPLE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,16, CopperPipeMain.PURPLE_INK);
    public static final Block GLOWING_MAGENTA_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,17, CopperPipeMain.MAGENTA_INK);
    public static final Block GLOWING_PINK_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,18, CopperPipeMain.PINK_INK);
    public static final Block GLOWING_WHITE_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,19, CopperPipeMain.WHITE_INK);
    public static final Block GLOWING_LIGHT_GRAY_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,20, CopperPipeMain.LIGHT_GRAY_INK);
    public static final Block GLOWING_GRAY_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,21, CopperPipeMain.GRAY_INK);
    public static final Block GLOWING_BROWN_PIPE = new CopperPipe(Properties.of(Material.METAL, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 3.0F).sound(SoundType.COPPER).lightLevel(CopperPipe::getLuminance).emissiveRendering((state, world, pos) -> CopperPipe.hasItem(state)), 4,22, CopperPipeMain.BROWN_INK);

}

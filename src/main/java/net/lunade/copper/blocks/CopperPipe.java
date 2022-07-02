package net.lunade.copper.blocks;

import it.unimi.dsi.fastutil.objects.*;
import net.lunade.copper.Main;
import net.lunade.copper.block_entity.CopperPipeEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

import static net.lunade.copper.Main.INSPECT_PIPE;

public class CopperPipe extends BlockWithEntity implements Waterloggable {

    public int cooldown;
    public boolean waxed;
    public int dispenserShotLength;
    public ParticleEffect ink;

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

    public CopperPipe(Settings settings, int cooldown, boolean waxed, int dispenserShotLength, ParticleEffect ink) {
        super(settings);
        this.cooldown=cooldown;
        this.waxed=waxed;
        this.dispenserShotLength=dispenserShotLength;
        this.ink=ink;
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.DOWN).with(SMOOTH, false).with(WATERLOGGED, false).with(HAS_WATER, false).with(HAS_SMOKE, false).with(HAS_ELECTRICITY, false).with(HAS_ITEM, false).with(POWERED, false));
    }

    public VoxelShape getPipeShape(BlockState blockState) {
        boolean front = blockState.get(FRONT_CONNECTED);
        boolean back = blockState.get(BACK_CONNECTED);
        boolean smooth = blockState.get(SMOOTH);
        if (smooth && back) {
            return switch (blockState.get(FACING)) {
                case DOWN -> DOWN_BACK_SMOOTH;
                case UP -> UP_BACK_SMOOTH;
                case NORTH -> NORTH_BACK_SMOOTH;
                case SOUTH -> SOUTH_BACK_SMOOTH;
                case EAST -> EAST_BACK_SMOOTH;
                case WEST -> WEST_BACK_SMOOTH;
            };
        }
        if (smooth) {
            return switch (blockState.get(FACING)) {
                case DOWN, UP -> DOWN_SMOOTH;
                case NORTH, SOUTH -> NORTH_SMOOTH;
                case EAST, WEST -> EAST_SMOOTH;
            };
        }
        if (front && back) {
            return switch (blockState.get(FACING)) {
                case DOWN, UP -> DOWN_DOUBLE;
                case NORTH, SOUTH -> NORTH_DOUBLE;
                case EAST, WEST -> EAST_DOUBLE;
            };
        }
        if (front) {
            return switch (blockState.get(FACING)) {
                case DOWN -> DOWN_FRONT;
                case UP -> UP_FRONT;
                case NORTH -> NORTH_FRONT;
                case SOUTH -> SOUTH_FRONT;
                case EAST -> EAST_FRONT;
                case WEST -> WEST_FRONT;
            };
        }
        if (back) {
            return switch (blockState.get(FACING)) {
                case DOWN -> DOWN_BACK;
                case UP -> UP_BACK;
                case NORTH -> NORTH_BACK;
                case SOUTH -> SOUTH_BACK;
                case EAST -> EAST_BACK;
                case WEST -> WEST_BACK;
            };
        }
        return switch (blockState.get(FACING)) {
            case DOWN -> DOWN_SHAPE;
            case UP -> UP_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) { return getPipeShape(blockState);}
    public VoxelShape getRaycastShape(BlockState blockState, BlockView blockView, BlockPos blockPos) { return getPipeShape(blockState); }

    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        Direction direction = itemPlacementContext.getSide().getOpposite();
        BlockPos blockPos = itemPlacementContext.getBlockPos();
        boolean front = canConnectFront(itemPlacementContext.getWorld(), blockPos, direction.getOpposite());
        boolean back = canConnectBack(itemPlacementContext.getWorld(), blockPos, direction.getOpposite());
        boolean smooth = isSmooth(itemPlacementContext.getWorld(), blockPos, direction.getOpposite());
        FluidState fluidState = itemPlacementContext.getWorld().getFluidState(blockPos);
        return this.getDefaultState().with(FACING, direction.getOpposite()).with(FRONT_CONNECTED, front).with(BACK_CONNECTED, back).with(SMOOTH, smooth).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState blockState2, WorldAccess worldAccess, BlockPos blockPos, BlockPos blockPos2) {
        boolean front = canConnectFront(worldAccess, blockPos, blockState.get(FACING));
        boolean back = canConnectBack(worldAccess, blockPos, blockState.get(FACING));
        boolean smooth = isSmooth(worldAccess, blockPos, blockState.get(FACING));
        if (blockState.get(WATERLOGGED)) {worldAccess.createAndScheduleFluidTick(blockPos, Fluids.WATER, Fluids.WATER.getTickRate(worldAccess));}
        boolean electricity = blockState.get(HAS_ELECTRICITY);
        if (worldAccess.getBlockState(blockPos2).getBlock() instanceof LightningRodBlock) { if (worldAccess.getBlockState(blockPos2).get(POWERED)) {electricity=true;} }
        return blockState.with(FRONT_CONNECTED, front).with(BACK_CONNECTED, back).with(SMOOTH, smooth).with(HAS_ELECTRICITY, electricity);
    }

    @Override
    public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborUpdate(blockState, world, blockPos, block, blockPos2, bl);
        boolean powered = isReceivingRedstonePower(blockPos, world);
        if (powered!=blockState.get(POWERED)) {world.setBlockState(blockPos, blockState.with(POWERED, powered));}
    }

    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) { return new CopperPipeEntity(blockPos, blockState); }

    @Override
    public boolean isTranslucent(BlockState blockState, BlockView blockView, BlockPos blockPos) { return blockState.getFluidState().isEmpty();}

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!world.isClient) {
            return CopperPipe.checkType(blockEntityType, Main.COPPER_PIPE_ENTITY, (world1, blockPos, blockState1, copperPipeEntity) -> copperPipeEntity.serverTick(world1, blockPos, blockState1));
        } return null;
    }

    @Nullable
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        if (blockEntity instanceof CopperPipeEntity pipeEntity) {
            return pipeEntity.getGameEventListener();
        } return null;
    }

    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) { ((CopperPipeEntity)blockEntity).setCustomName(itemStack.getName()); }
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        } return super.getFluidState(blockState);
    }

    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        if (playerEntity.getStackInHand(hand).getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof CopperPipe || blockItem.getBlock() instanceof CopperFitting) {
                return  ActionResult.PASS;
            }
        }
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) {
                playerEntity.openHandledScreen((CopperPipeEntity) blockEntity);
                playerEntity.incrementStat(Stats.CUSTOM.getOrCreateStat(INSPECT_PIPE));
            } return ActionResult.CONSUME;
        }
    }

    public BlockRenderType getRenderType(BlockState blockState) { return BlockRenderType.MODEL; }

    public boolean hasComparatorOutput(BlockState blockState) { return true; }

    public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) { return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(blockPos)); }

    public BlockState rotate(BlockState blockState, BlockRotation blockRotation) { return blockState.with(FACING, blockRotation.rotate(blockState.get(FACING))); }

    public BlockState mirror(BlockState blockState, BlockMirror blockMirror) { return blockState.rotate(blockMirror.getRotation(blockState.get(FACING))); }

    protected void appendProperties(Builder<Block, BlockState> builder) { builder.add(FACING).add(FRONT_CONNECTED).add(BACK_CONNECTED).add(SMOOTH).add(WATERLOGGED).add(HAS_WATER).add(HAS_SMOKE).add(HAS_ELECTRICITY).add(HAS_ITEM).add(POWERED); }

    public boolean canPathfindThrough(BlockState blockState, BlockView blockView, BlockPos blockPos, NavigationType navigationType) { return false; }

    public static boolean canConnectFront(World world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.offset(direction));
        if (isPipe(state)) {
            return state.get(CopperPipe.FACING) != direction.getOpposite() && state.get(CopperPipe.FACING) != direction;
        } return isFitting(state);
    }

    public static boolean canConnectBack(World world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.offset(direction.getOpposite()));
        if (isPipe(state)) {
            return state.get(CopperPipe.FACING) != direction.getOpposite() && state.get(CopperPipe.FACING) != direction;
        } return isFitting(state);
    }

    public static boolean isSmooth(World world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.offset(direction));
        if (isPipe(state)) {
            return state.get(CopperPipe.FACING) == direction && !canConnectFront(world,blockPos,direction);
        } return false;
    }

    public static boolean canConnectFront(WorldAccess world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.offset(direction));
        if (isPipe(state)) {
            return state.get(CopperPipe.FACING) != direction.getOpposite() && state.get(CopperPipe.FACING) != direction;
        } return isFitting(state);
    }

    public static boolean canConnectBack(WorldAccess world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.offset(direction.getOpposite()));
        if (isPipe(state)) {
            return state.get(CopperPipe.FACING) != direction.getOpposite() && state.get(CopperPipe.FACING) != direction;
        } return isFitting(state);
    }

    public static boolean isSmooth(WorldAccess world, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos.offset(direction));
        if (isPipe(state)) {
            return state.get(CopperPipe.FACING) == direction && !canConnectFront(world,blockPos,direction);
        } return false;
    }

    public static boolean isPipe(BlockState state) {return state.getBlock() instanceof CopperPipe;}

    public static boolean isFitting(BlockState state) {return state.getBlock() instanceof CopperFitting;}

    public void randomTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        Direction direction = blockState.get(FACING);
        if (blockState.get(HAS_WATER) && direction!=Direction.UP) {
            BlockPos pos = blockPos;
            boolean hasOffset = false;
            for (int i=0; i<12; i++) { //Searches for 12 blocks
                if (direction != Direction.DOWN && !hasOffset) {
                    pos = pos.offset(direction);
                    hasOffset = true;
                }
                pos=pos.down();
                BlockState state = serverWorld.getBlockState(pos);
                if (state.getBlock() == Blocks.CAULDRON) {
                    i=99; //Stop loop if viable Cauldron is found
                    serverWorld.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState().with(Properties.LEVEL_3, 1));
                }
                if (state.getBlock() == Blocks.WATER_CAULDRON) {
                    if (state.get(Properties.LEVEL_3)!=3) { //Ignores filled Cauldrons
                        i=99; //Stop loop if viable Cauldron is found
                        serverWorld.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState().with(Properties.LEVEL_3, state.get(Properties.LEVEL_3)+1));
                    }
                }
                if (state.getBlock() == Blocks.DIRT) {
                    i=99; //Stop loop if viable Block is found
                    serverWorld.setBlockState(pos, Blocks.MUD.getDefaultState());
                }
                if (state.getBlock() == Blocks.FIRE) { serverWorld.breakBlock(pos, false); }
                if (state.isSolidBlock(serverWorld, pos)) {i=99;} //Water will "pass through" all non-full blocks (I.E. ladders, stairs). This also allows for water to "overflow" from Cauldrons down into one below if they're full or have Lava.
            }
        }

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
                        if (OXIDIZATION_INT.containsKey(block)) {
                            int m = OXIDIZATION_INT.getInt(block);
                            if (m < i) { return; }
                            if (m > i) { ++k; } else { ++j; }
                        }
                    } else if (block instanceof CopperFitting) {
                        if (CopperFitting.OXIDIZATION_INT.containsKey(block)) {
                            int m = CopperFitting.OXIDIZATION_INT.getInt(block);
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

    public static int waterLevel(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.WATER) { return 12; }
        if (state.contains(WATERLOGGED)) { if (state.get(WATERLOGGED)) { return 12; } }
        if (block instanceof CopperPipe) {
            if (world.getBlockEntity(pos) instanceof CopperPipeEntity pipe) { return MathHelper.clamp(pipe.waterLevel-1,0,12); }
        }
        if (block instanceof CopperFitting) { if (state.get(HAS_WATER)) { return 12; } }
        return 0;
    }
    public static int smokeLevel(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof CampfireBlock) { if (state.get(Properties.LIT)) {return 12;} }
        if (block instanceof CopperPipe) {
            if (world.getBlockEntity(pos) instanceof CopperPipeEntity pipe) { return MathHelper.clamp(pipe.smokeLevel-1,0,12); }
        }
        if (block instanceof CopperFitting) { if (state.get(HAS_SMOKE)) { return 12; } }
        return 0;
    }

    public static int canWater(World world, BlockPos blockPos, BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {return 12;}
        BlockPos p = blockPos.offset(blockState.get(FACING).getOpposite());
        if (!world.isChunkLoaded(p)) { return 0; }
        return waterLevel(world, p);
    }

    public static int canWaterFitting(World world, BlockPos pos, BlockState state) {
        if (state.get(WATERLOGGED)) {return 12;}
        if (state.getBlock() instanceof CopperPipe) {
            if (!world.isChunkLoaded(pos)) {return 0;}
            return waterLevel(world, pos);
        } return 0;
    }

    public static int canSmoke(World world, BlockPos blockPos, BlockState blockState) {
        BlockPos p = blockPos.offset(blockState.get(FACING).getOpposite());
        if (!world.isChunkLoaded(p)) { return 0; }
        return smokeLevel(world, p);
    }

    public static int canSmokeFitting(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            if (!world.isChunkLoaded(pos)) {return 0;}
            return smokeLevel(world, pos);
        } return 0;
    }

    public boolean hasRandomTicks(BlockState blockState) {
        Block block = blockState.getBlock();
        return block==CopperPipe.COPPER_PIPE || block==CopperPipe.EXPOSED_PIPE || block==CopperPipe.WEATHERED_PIPE || blockState.get(HAS_WATER);
    }

    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        Direction direction = blockState.get(FACING);
        BlockState offsetState = world.getBlockState(blockPos.offset(direction));
        boolean waterInFront = offsetState.getBlock()==Blocks.WATER;
        boolean canWaterOrSmokeExtra = ((offsetState.getBlock()!=Blocks.AIR && !waterInFront) || direction==Direction.DOWN);
        if (blockState.get(HAS_WATER) && direction!=Direction.UP) {
            world.addParticle(ParticleTypes.DRIPPING_WATER, blockPos.getX()+getDripX(direction), blockPos.getY()+getDripY(direction), blockPos.getZ()+getDripZ(direction),0,0,0);
            if (canWaterOrSmokeExtra) {
                double x = blockPos.getX()+getDripX(direction, random);
                double y = blockPos.getY()+getDripY(direction, random);
                double z = blockPos.getZ()+getDripZ(direction, random);
                world.addParticle(ParticleTypes.DRIPPING_WATER, x,y,z,0,0,0);
            }
        }
        if (random.nextInt(5) == 0) {
            if (blockState.get(HAS_SMOKE)) {
                CampfireBlock.spawnSmokeParticle(world, blockPos.offset(direction), false, false);
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, blockPos.getX()+getDripX(direction), blockPos.getY()+getDripY(direction), blockPos.getZ()+getDripZ(direction),0.0D, 0.07D, 0.0D);
                if (canWaterOrSmokeExtra) {
                    double x = blockPos.getX()+getDripX(direction, random);
                    double y = blockPos.getY()+getDripY(direction, random);
                    double z = blockPos.getZ()+getDripZ(direction, random);
                    world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x,y,z,0.0D, 0.07D, 0.0D);
                }
            }
        }
        if (blockState.get(HAS_ELECTRICITY)) {
            ParticleUtil.spawnParticle(direction.getAxis(), world, blockPos, 0.4D, ParticleTypes.ELECTRIC_SPARK, UniformIntProvider.create(1, 2));
        }
        if (waterInFront) {
            double x = blockPos.getX()+getDripX(direction, random);
            double y = blockPos.getY()+getDripY(direction, random);
            double z = blockPos.getZ()+getDripZ(direction, random);
            world.addParticle(ParticleTypes.BUBBLE, x,y,z,direction.getOffsetX()*0.7D, direction.getOffsetY()*0.7D, direction.getOffsetZ()*0.7D);
        }
    }

    public double getRan(Random random) { return UniformIntProvider.create(-25,25).get(random) * 0.01; }

    public double getDripX(Direction direction, Random random) {
        return switch (direction) {
            case DOWN, SOUTH, NORTH -> 0.5 + getRan(random);
            case UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }
    public double getDripY(Direction direction, Random random) {
        return switch (direction) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, WEST, EAST, SOUTH -> 0.5 + getRan(random);
        };
    }
    public double getDripZ(Direction direction, Random random) {
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

    public static Position getOutputLocation(BlockPointer blockPointer) {
        Direction direction = blockPointer.getBlockState().get(CopperPipe.FACING);
        return new PositionImpl(
                blockPointer.getX() + 0.7D * (double)direction.getOffsetX(),
                blockPointer.getY() + 0.7D * (double)direction.getOffsetY(),
                blockPointer.getZ() + 0.7D * (double)direction.getOffsetZ());
    }

    public static boolean isWaterPipeNearby(WorldView worldView, BlockPos blockPos, int x) {
        Iterator<BlockPos> var2 = BlockPos.iterate(blockPos.add(-x, 0, -x), blockPos.add(x, 12, x)).iterator();
        BlockPos blockPos2;
        do {
            if (!var2.hasNext()) { return false; }
            blockPos2 = var2.next();
        } while(!isWaterPipe(worldView.getBlockState(blockPos2)));
        return true;
    }

    public static boolean isWaterPipeNearby(BlockView blockView, BlockPos blockPos, int x) {
        Iterator<BlockPos> var2 = BlockPos.iterate(blockPos.add(-x, 0, -x), blockPos.add(x, 12, x)).iterator();
        BlockPos blockPos2;
        do {
            if (!var2.hasNext()) {
                return false;
            }
            blockPos2 = var2.next();
        } while(!isWaterPipe(blockView.getBlockState(blockPos2)));
        return true;
    }

    public static boolean isWaterPipe(BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            return state.get(HAS_WATER);
        } return false;
    }

    public static boolean hasItem(BlockState state) {
        if (state.getBlock() instanceof CopperPipe || state.getBlock() instanceof CopperFitting) {
            return state.get(HAS_ITEM) || state.get(HAS_ELECTRICITY);
        } return false;
    }

    public static int getLuminance(BlockState state) {
        if (state.getBlock() instanceof CopperPipe || state.getBlock() instanceof CopperFitting) {
            if (state.get(HAS_ELECTRICITY)) {return 5;}
            if (state.get(HAS_ITEM)) {return 3;}
        } return 1;
    }

    public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.hasBlockEntity() && !(blockState2.getBlock() instanceof CopperPipe)) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) {
                ItemScatterer.spawn(world, blockPos, (CopperPipeEntity)blockEntity);
                world.updateComparators(blockPos, this);
            } world.removeBlockEntity(blockPos);
        } else {
            BlockEntity entity = world.getBlockEntity(blockPos);
            if (entity!=null) {
                if (entity instanceof CopperPipeEntity pipe) {
                    boolean waterlogged = blockState.get(WATERLOGGED);
                    if (waterlogged && !pipe.wasPreviouslyWaterlogged) {
                        if (world.random.nextFloat() < 0.05688889F) {
                            if (world.random.nextFloat() < 0.36F) {
                                Block stateBlock = blockState.getBlock();
                                if (NEXT_STAGE.containsKey(stateBlock)) {
                                    Block nextStage = NEXT_STAGE.get(blockState.getBlock());
                                    blockState = makeCopyOf(blockState, nextStage);
                                    pipe.wasPreviouslyWaterlogged = true;
                                    world.setBlockState(blockPos, blockState);
                                }
                            }
                        }
                    } else { if (!waterlogged) {pipe.wasPreviouslyWaterlogged=false;} }
                }
            }
        }
    }

    public static boolean isReceivingRedstonePower(BlockPos blockPos, World world) {
        for (Direction direction : Direction.values()) {
            if (world.getEmittedRedstonePower(blockPos.offset(direction), direction) > 0) {return true;}
        } return false;
    }

    public static void makeCopyOf(BlockState state, World world, BlockPos blockPos, Block block) {
        if (block instanceof CopperPipe) {
            world.setBlockState(blockPos, block.getDefaultState().with(FACING, state.get(FACING)).with(FRONT_CONNECTED, state.get(FRONT_CONNECTED))
                    .with(BACK_CONNECTED, state.get(BACK_CONNECTED)).with(WATERLOGGED, state.get(WATERLOGGED))
                    .with(HAS_WATER, state.get(HAS_WATER)).with(SMOOTH, state.get(SMOOTH))
                    .with(HAS_ITEM, state.get(HAS_ITEM)).with(HAS_SMOKE, state.get(HAS_SMOKE)).with(HAS_ELECTRICITY, state.get(HAS_ELECTRICITY)).with(POWERED, state.get(POWERED)));
        }
    }
    public static BlockState makeCopyOf(BlockState state, Block block) {
        if (block instanceof CopperPipe) {
            return block.getDefaultState().with(FACING, state.get(FACING)).with(FRONT_CONNECTED, state.get(FRONT_CONNECTED))
                    .with(BACK_CONNECTED, state.get(BACK_CONNECTED)).with(WATERLOGGED, state.get(WATERLOGGED))
                    .with(HAS_WATER, state.get(HAS_WATER)).with(SMOOTH, state.get(SMOOTH))
                    .with(HAS_ITEM, state.get(HAS_ITEM)).with(HAS_SMOKE, state.get(HAS_SMOKE)).with(HAS_ELECTRICITY, state.get(HAS_ELECTRICITY)).with(POWERED, state.get(POWERED));
        } else return null;
    }

    static {
        FACING = Properties.FACING;
        FRONT_CONNECTED = CopperPipeProperties.FRONT_CONNECTED;
        BACK_CONNECTED = CopperPipeProperties.BACK_CONNECTED;
        SMOOTH = CopperPipeProperties.SMOOTH;
        WATERLOGGED = Properties.WATERLOGGED;
        HAS_WATER = CopperPipeProperties.HAS_WATER;
        HAS_SMOKE = CopperPipeProperties.HAS_SMOKE;
        HAS_ELECTRICITY = CopperPipeProperties.HAS_ELECTRICITY;
        HAS_ITEM = CopperPipeProperties.HAS_ITEM;
        POWERED = Properties.POWERED;

        UP_SHAPE = VoxelShapes.union(Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D),Block.createCuboidShape(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D));
        DOWN_SHAPE = VoxelShapes.union(Block.createCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 16.0D, 12.0D),Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D));
        EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D),Block.createCuboidShape(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
        WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(2.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D),Block.createCuboidShape(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D));
        NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 16.0D),Block.createCuboidShape(3.0D, 3.0D, 0.D, 13.0D, 13.0D, 2.0D));
        SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 14.0D),Block.createCuboidShape(3.0D, 3.0D, 14.D, 13.0D, 13.0D, 16.0D));

        UP_BACK = VoxelShapes.union(Block.createCuboidShape(4.0D, -4.0D, 4.0D, 12.0D, 14.0D, 12.0D),Block.createCuboidShape(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D));
        DOWN_BACK = VoxelShapes.union(Block.createCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 20.0D, 12.0D),Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D));
        EAST_BACK = VoxelShapes.union(Block.createCuboidShape(-4.0D, 4.0D, 4.0D, 14.0D, 12.0D, 12.0D),Block.createCuboidShape(14.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
        WEST_BACK = VoxelShapes.union(Block.createCuboidShape(2.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D),Block.createCuboidShape(0.0D, 3.0D, 3.0D, 2.0D, 13.0D, 13.0D));
        NORTH_BACK = VoxelShapes.union(Block.createCuboidShape(4.0D, 4.0D, 2.0D, 12.0D, 12.0D, 20.0D),Block.createCuboidShape(3.0D, 3.0D, 0.D, 13.0D, 13.0D, 2.0D));
        SOUTH_BACK = VoxelShapes.union(Block.createCuboidShape(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 14.0D),Block.createCuboidShape(3.0D, 3.0D, 14.D, 13.0D, 13.0D, 16.0D));

        UP_BACK_SMOOTH = Block.createCuboidShape(4.0D, -4.0D, 4.0D, 12.0D, 16.0D, 12.0D);
        DOWN_BACK_SMOOTH = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 20.0D, 12.0D);
        EAST_BACK_SMOOTH = Block.createCuboidShape(-4.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
        WEST_BACK_SMOOTH = Block.createCuboidShape(0.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D);
        NORTH_BACK_SMOOTH = Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 20.0D);
        SOUTH_BACK_SMOOTH = Block.createCuboidShape(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 16.0D);

        UP_FRONT = VoxelShapes.union(Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 18.0D, 12.0D),Block.createCuboidShape(3.0D, 18.0D, 3.0D, 13.0D, 20.0D, 13.0D));
        DOWN_FRONT = VoxelShapes.union(Block.createCuboidShape(4.0D, -2.0D, 4.0D, 12.0D, 16.0D, 12.0D),Block.createCuboidShape(3.0D, -4.0D, 3.0D, 13.0D, -2.0D, 13.0D));
        EAST_FRONT = VoxelShapes.union(Block.createCuboidShape(0.0D, 4.0D, 4.0D, 18.0D, 12.0D, 12.0D),Block.createCuboidShape(18.0D, 3.0D, 3.0D, 20.0D, 13.0D, 13.0D));
        WEST_FRONT = VoxelShapes.union(Block.createCuboidShape(-2.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D),Block.createCuboidShape(-4.0D, 3.0D, 3.0D, -2.0D, 13.0D, 13.0D));
        NORTH_FRONT = VoxelShapes.union(Block.createCuboidShape(4.0D, 4.0D, -2.0D, 12.0D, 12.0D, 16.0D),Block.createCuboidShape(3.0D, 3.0D, -4.0D, 13.0D, 13.0D, -2.0D));
        SOUTH_FRONT = VoxelShapes.union(Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 18.0D),Block.createCuboidShape(3.0D, 3.0D, 18.D, 13.0D, 13.0D, 20.0D));

        EAST_DOUBLE = Block.createCuboidShape(-4.0D, 4.0D, 4.0D, 20.0D, 12.0D, 12.0D);
        NORTH_DOUBLE = Block.createCuboidShape(4.0D, 4.0D, -4.0D, 12.0D, 12.0D, 20.0D);
        DOWN_DOUBLE = Block.createCuboidShape(4.0D, -4.0D, 4.0D, 12.0D, 20.0D, 12.0D);

        EAST_SMOOTH = Block.createCuboidShape(0.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
        NORTH_SMOOTH = Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 16.0D);
        DOWN_SMOOTH = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    }

    public static final Block OXIDIZED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.TEAL).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2,false,12, ParticleTypes.SQUID_INK);
    public static final Block WEATHERED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.DARK_AQUA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2,false,15, ParticleTypes.SQUID_INK);
    public static final Block EXPOSED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2,false,18, ParticleTypes.SQUID_INK);
    public static final Block COPPER_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2,false, 20, ParticleTypes.SQUID_INK);

    public static final Block WAXED_OXIDIZED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.TEAL).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1,true,12, ParticleTypes.SQUID_INK);
    public static final Block WAXED_WEATHERED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.DARK_AQUA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1,true,15, ParticleTypes.SQUID_INK);
    public static final Block WAXED_EXPOSED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1,true,18, ParticleTypes.SQUID_INK);
    public static final Block WAXED_COPPER_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1,true,20, ParticleTypes.SQUID_INK);

    public static final Block CORRODED_PIPE = new CopperPipe(Settings
            .of(Material.METAL, MapColor.TERRACOTTA_ORANGE)
            .requiresTool()
            .strength(2F, 3.5F)
            .sounds(new BlockSoundGroup(1.0f, 1.25f,
                    Main.CORRODED_COPPER_PLACE,
                    Main.CORRODED_COPPER_STEP,
                    Main.CORRODED_COPPER_BREAK,
                    Main.CORRODED_COPPER_FALL,
                    Main.CORRODED_COPPER_HIT
    )), 8,false,7, ParticleTypes.SQUID_INK);

    public static final Block BLACK_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,8, ParticleTypes.SQUID_INK);
    public static final Block RED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.RED).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,9, Main.RED_INK);
    public static final Block ORANGE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,10, Main.ORANGE_INK);
    public static final Block YELLOW_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,11, Main.YELLOW_INK);
    public static final Block LIME_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIME).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,12, Main.LIME_INK);
    public static final Block GREEN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.GREEN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,13, Main.GREEN_INK);
    public static final Block CYAN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.CYAN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,14, Main.CYAN_INK);
    public static final Block LIGHT_BLUE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,15, Main.LIGHT_BLUE_INK);
    public static final Block BLUE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,16, Main.BLUE_INK);
    public static final Block PURPLE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,17, Main.PURPLE_INK);
    public static final Block MAGENTA_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,18, Main.MAGENTA_INK);
    public static final Block PINK_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,19, Main.PINK_INK);
    public static final Block WHITE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,20, Main.WHITE_INK);
    public static final Block LIGHT_GRAY_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,21, Main.LIGHT_GRAY_INK);
    public static final Block GRAY_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,22, Main.GRAY_INK);
    public static final Block BROWN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BROWN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,23, Main.BROWN_INK);

    public static final Block GLOWING_BLACK_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,7, ParticleTypes.SQUID_INK);
    public static final Block GLOWING_RED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.RED).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,8, Main.RED_INK);
    public static final Block GLOWING_ORANGE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,9, Main.ORANGE_INK);
    public static final Block GLOWING_YELLOW_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,10, Main.YELLOW_INK);
    public static final Block GLOWING_LIME_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIME).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,11, Main.LIME_INK);
    public static final Block GLOWING_GREEN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.GREEN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,12, Main.GREEN_INK);
    public static final Block GLOWING_CYAN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.CYAN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,13, Main.CYAN_INK);
    public static final Block GLOWING_LIGHT_BLUE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false, 14, Main.LIGHT_BLUE_INK);
    public static final Block GLOWING_BLUE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,15, Main.BLUE_INK);
    public static final Block GLOWING_PURPLE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,16, Main.PURPLE_INK);
    public static final Block GLOWING_MAGENTA_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,17, Main.MAGENTA_INK);
    public static final Block GLOWING_PINK_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,18, Main.PINK_INK);
    public static final Block GLOWING_WHITE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,19, Main.WHITE_INK);
    public static final Block GLOWING_LIGHT_GRAY_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,20, Main.LIGHT_GRAY_INK);
    public static final Block GLOWING_GRAY_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,21, Main.GRAY_INK);
    public static final Block GLOWING_BROWN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BROWN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,22, Main.BROWN_INK);

    public static final Object2ObjectMap<Block, Block> NEXT_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(COPPER_PIPE, EXPOSED_PIPE);
        object2IntOpenHashMap.put(EXPOSED_PIPE, WEATHERED_PIPE);
        object2IntOpenHashMap.put(WEATHERED_PIPE, OXIDIZED_PIPE);
        object2IntOpenHashMap.put(OXIDIZED_PIPE, CORRODED_PIPE);
    }));
    public static final Object2ObjectMap<Block, Block> PREVIOUS_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(CORRODED_PIPE, OXIDIZED_PIPE);
        object2IntOpenHashMap.put(OXIDIZED_PIPE, WEATHERED_PIPE);
        object2IntOpenHashMap.put(WEATHERED_PIPE, EXPOSED_PIPE);
        object2IntOpenHashMap.put(EXPOSED_PIPE, COPPER_PIPE);
        object2IntOpenHashMap.put(WAXED_COPPER_PIPE, COPPER_PIPE);
        object2IntOpenHashMap.put(WAXED_EXPOSED_PIPE, EXPOSED_PIPE);
        object2IntOpenHashMap.put(WAXED_WEATHERED_PIPE, WEATHERED_PIPE);
        object2IntOpenHashMap.put(WAXED_OXIDIZED_PIPE, OXIDIZED_PIPE);
    }));
    public static final Object2ObjectMap<Block, Block> WAX_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(COPPER_PIPE, WAXED_COPPER_PIPE);
        object2IntOpenHashMap.put(EXPOSED_PIPE, WAXED_EXPOSED_PIPE);
        object2IntOpenHashMap.put(WEATHERED_PIPE, WAXED_WEATHERED_PIPE);
        object2IntOpenHashMap.put(OXIDIZED_PIPE, WAXED_OXIDIZED_PIPE);
    }));
    public static final Object2ObjectMap<Block, Block> GLOW_STAGE = Object2ObjectMaps.unmodifiable(Util.make(new Object2ObjectOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(RED_PIPE, GLOWING_RED_PIPE);
        object2IntOpenHashMap.put(ORANGE_PIPE, GLOWING_ORANGE_PIPE);
        object2IntOpenHashMap.put(YELLOW_PIPE, GLOWING_YELLOW_PIPE);
        object2IntOpenHashMap.put(GREEN_PIPE, GLOWING_GREEN_PIPE);
        object2IntOpenHashMap.put(CYAN_PIPE, GLOWING_CYAN_PIPE);
        object2IntOpenHashMap.put(LIGHT_BLUE_PIPE, GLOWING_LIGHT_BLUE_PIPE);
        object2IntOpenHashMap.put(BLUE_PIPE, GLOWING_BLUE_PIPE);
        object2IntOpenHashMap.put(PURPLE_PIPE, GLOWING_PURPLE_PIPE);
        object2IntOpenHashMap.put(MAGENTA_PIPE, GLOWING_MAGENTA_PIPE);
        object2IntOpenHashMap.put(PINK_PIPE, GLOWING_PINK_PIPE);
        object2IntOpenHashMap.put(WHITE_PIPE, GLOWING_WHITE_PIPE);
        object2IntOpenHashMap.put(LIGHT_GRAY_PIPE, GLOWING_LIGHT_GRAY_PIPE);
        object2IntOpenHashMap.put(GRAY_PIPE, GLOWING_GRAY_PIPE);
        object2IntOpenHashMap.put(BLACK_PIPE, GLOWING_BLACK_PIPE);
        object2IntOpenHashMap.put(BROWN_PIPE, GLOWING_BROWN_PIPE);
    }));
    public static final Object2IntMap<Block> OXIDIZATION_INT = Object2IntMaps.unmodifiable(Util.make(new Object2IntOpenHashMap<>(), (object2IntOpenHashMap) -> {
        object2IntOpenHashMap.put(COPPER_PIPE, 0);
        object2IntOpenHashMap.put(EXPOSED_PIPE, 1);
        object2IntOpenHashMap.put(WEATHERED_PIPE, 2);
        object2IntOpenHashMap.put(OXIDIZED_PIPE, 3);
    }));

}

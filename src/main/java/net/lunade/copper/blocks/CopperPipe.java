package net.lunade.copper.blocks;

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
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.TagKey;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CopperPipe extends BlockWithEntity implements Waterloggable {

    public int cooldown;
    public boolean waxed;
    public int dispenserShotLength;
    public int inkInt;

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

    public CopperPipe(Settings settings, int cooldown, boolean waxed, int dispenserShotLength, int inkInt) {
        super(settings);
        this.cooldown=cooldown;
        this.waxed=waxed;
        this.dispenserShotLength=dispenserShotLength;
        this.inkInt=inkInt;
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
        boolean front = canConnectFront(worldAccess, blockPos, blockState.get(CopperPipeProperties.FACING));
        boolean back = canConnectBack(worldAccess, blockPos, blockState.get(CopperPipeProperties.FACING));
        boolean smooth = isSmooth(worldAccess, blockPos, blockState.get(CopperPipeProperties.FACING));
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
            return CopperPipe.checkType(blockEntityType, Main.COPPER_PIPE_ENTITY, (world1, blockPos, blockState1, copperPipeEntity) -> copperPipeEntity.serverTick(world1, blockPos, blockState1, (CopperPipeEntity) Objects.requireNonNull(world1.getBlockEntity(blockPos))));
        } return null;
    }

    @Nullable
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        if (blockEntity instanceof CopperPipeEntity pipeEntity) {
            return pipeEntity.getGameEventListener();
        } return null;
    }

    public static boolean shouldEmitEvent(BlockPos pos, World world) {
        if (tagInSphere(pos, 8, Main.BLOCK_LISTENERS, world)) {return true;}
        List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, new Box(
                pos.getX() -18, pos.getY() -18, pos.getZ() -18,
                pos.getX() +18, pos.getY() +18, pos.getZ() +18)
        );
        Iterator<LivingEntity> var11 = entities.iterator();
        LivingEntity entity;
        while(var11.hasNext()) {
            entity = var11.next();
            if(Math.floor(Math.sqrt(entity.getBlockPos().getSquaredDistance(pos))) <= 16 && entity.getType().isIn(Main.ENTITY_LISTENERS)) { return true; }
        } return false;
    }

    public static boolean tagInSphere(BlockPos pos, int radius, TagKey<Block> block, World world) {
        if (pos == null) { return false; }
        int bx = pos.getX();
        int by = pos.getY();
        int bz = pos.getZ();
        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));
                    if(distance < radius * radius) {
                        BlockPos l = new BlockPos(x, y, z);
                        if (world.getBlockState(l).isIn(block)) { return true; }
                    }
                }
            }
        } return false;
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
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof CopperPipeEntity) {
                playerEntity.openHandledScreen((CopperPipeEntity) blockEntity);
                playerEntity.incrementStat(Stats.INSPECT_HOPPER);
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
        if (blockState.get(HAS_WATER) && blockState.get(FACING)!=Direction.UP) {
            Direction direction = blockState.get(FACING);
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
                if (state.getBlock() == Blocks.FIRE) { serverWorld.breakBlock(pos, false); }
                if (state.isSolidBlock(serverWorld, pos)) {i=99;} //Water will "pass through" all non-full blocks (I.E. ladders, stairs). This also allows for water to "overflow" from Cauldrons down into one below if they're full or have Lava.
            }
        }

        if (random.nextFloat() < 0.05688889F) {
            if (random.nextFloat() < 0.15F) {
                if (getNextStage(serverWorld, blockPos) != null) {
                    makeCopyOf(blockState, serverWorld, blockPos, getNextStage(serverWorld, blockPos));
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

    public static ArrayList<BlockPos> getOutputPipe(World world, BlockPos blockPos, BlockState blockState) {
        BlockPos p = blockPos;
        BlockState b = blockState;
        ArrayList<BlockPos> poses = new ArrayList<>();
        ArrayList<BlockPos> exits = new ArrayList<>();
        if (b.getBlock() instanceof CopperPipe) {
            if (world.getBlockState(p.offset(b.get(FACING))).isAir() || world.getBlockState(p.offset(b.get(FACING))).getBlock()==Blocks.WATER) {
                exits.add(p);
                return exits;
            }
            for (int l = 0; l < 36; l++) {
                if (b.getBlock() instanceof CopperPipe) {
                    p = p.offset(b.get(CopperPipe.FACING));
                    b = world.getBlockState(p);
                    if (world.isChunkLoaded(p) && !poses.contains(p)) {
                        poses.add(p);
                        if (b.getBlock() instanceof CopperFitting) {
                            ArrayList<BlockPos> news = CopperFitting.getOutputPipe(world, p, poses);
                            exits.addAll(news);
                        } else if (b.getBlock() instanceof CopperPipe) {
                            if (world.getBlockState(p.offset(b.get(FACING))).isAir() || world.getBlockState(p.offset(b.get(FACING))).getBlock() == Blocks.WATER) {exits.add(p);
                            }
                        }
                    }
                }
            }
        } return exits;
    }
    public static ArrayList<BlockPos> getOutputPipeFitting(World world, BlockPos blockPos, BlockState blockState, ArrayList<BlockPos> poses) {
        BlockPos p = blockPos;
        BlockState b = blockState;
        ArrayList<BlockPos> exits = new ArrayList<>();
        if (b.getBlock() instanceof CopperPipe) {
            if (world.getBlockState(p.offset(b.get(FACING))).isAir() || world.getBlockState(p.offset(b.get(FACING))).getBlock()==Blocks.WATER) {
                exits.add(p);
                return exits;
            }
            for (int l = 0; l < 36; l++) {
                if (b.getBlock() instanceof CopperPipe) {
                    p = p.offset(b.get(CopperPipe.FACING));
                    b = world.getBlockState(p);
                    if (world.isChunkLoaded(p) && !poses.contains(p)) {
                        poses.add(p);
                        if (b.getBlock() instanceof CopperFitting) {
                            ArrayList<BlockPos> news = CopperFitting.getOutputPipe(world, p, poses);
                            exits.addAll(news);
                        } else if (b.getBlock() instanceof CopperPipe) {
                            if (world.getBlockState(p.offset(b.get(FACING))).isAir() || world.getBlockState(p.offset(b.get(FACING))).getBlock() == Blocks.WATER) {
                                exits.add(p);
                            }
                        }
                    }
                }
            }
        } return exits;
    }

    public boolean hasRandomTicks(BlockState blockState) {
        Block block = blockState.getBlock();
        return block==CopperPipe.COPPER_PIPE || block==CopperPipe.EXPOSED_PIPE || block==CopperPipe.WEATHERED_PIPE || blockState.get(HAS_WATER);
    }

    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        if (blockState.get(HAS_WATER) && blockState.get(FACING)!=Direction.UP) {
            world.addParticle(ParticleTypes.DRIPPING_WATER, blockPos.getX()+getDripX(blockState), blockPos.getY()+getDripY(blockState), blockPos.getZ()+getDripZ(blockState),0,0,0);
            if ((world.getBlockState(blockPos.offset(blockState.get(FACING))).getBlock()!=Blocks.AIR &&
            world.getBlockState(blockPos.offset(blockState.get(FACING))).getBlock()!=Blocks.WATER) || blockState.get(FACING)==Direction.DOWN) {
                double x = blockPos.getX()+getDripX(blockState, random);
                double y = blockPos.getY()+getDripY(blockState, random);
                double z = blockPos.getZ()+getDripZ(blockState, random);
                world.addParticle(ParticleTypes.DRIPPING_WATER, x,y,z,0,0,0);
            }
        }
        if (random.nextInt(5) == 0) {
            if (blockState.get(HAS_SMOKE)) {
                CampfireBlock.spawnSmokeParticle(world, blockPos.offset(blockState.get(CopperPipe.FACING)), false, false);
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, blockPos.getX()+getDripX(blockState), blockPos.getY()+getDripY(blockState), blockPos.getZ()+getDripZ(blockState),0.0D, 0.07D, 0.0D);
                if ((world.getBlockState(blockPos.offset(blockState.get(FACING))).getBlock()!=Blocks.AIR && world.getBlockState(blockPos.offset(blockState.get(FACING))).getBlock()!=Blocks.WATER) || blockState.get(FACING)==Direction.DOWN) {
                    double x = blockPos.getX()+getDripX(blockState, random);
                    double y = blockPos.getY()+getDripY(blockState, random);
                    double z = blockPos.getZ()+getDripZ(blockState, random);
                    world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x,y,z,0.0D, 0.07D, 0.0D);
                }
            }
        }
        if (blockState.get(HAS_ELECTRICITY)) {
            ParticleUtil.spawnParticle(blockState.get(FACING).getAxis(), world, blockPos, 0.4D, ParticleTypes.ELECTRIC_SPARK, UniformIntProvider.create(1, 2));
        }
        if (world.getBlockState(blockPos.offset(blockState.get(FACING))).getBlock()==Blocks.WATER) {
            double x = blockPos.getX()+getDripX(blockState, random);
            double y = blockPos.getY()+getDripY(blockState, random);
            double z = blockPos.getZ()+getDripZ(blockState, random);
            world.addParticle(ParticleTypes.BUBBLE, x,y,z,blockState.get(FACING).getOffsetX()*0.7D, blockState.get(FACING).getOffsetY()*0.7D, blockState.get(FACING).getOffsetZ()*0.7D);
        }
    }

    public double getRan(Random random) { return UniformIntProvider.create(-25,25).get(random) * 0.01; }

    public double getDripX(BlockState state, Random random) {
        return switch (state.get(FACING)) {
            case DOWN, SOUTH, NORTH -> 0.5 + getRan(random);
            case UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }
    public double getDripY(BlockState state, Random random) {
        return switch (state.get(FACING)) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, WEST, EAST, SOUTH -> 0.5 + getRan(random);
        };
    }
    public double getDripZ(BlockState state, Random random) {
        return switch (state.get(FACING)) {
            case DOWN, EAST, WEST -> 0.5 + getRan(random);
            case UP -> 0.5;
            case NORTH -> -0.05;
            case SOUTH -> 1.05;
        };
    }
    public double getDripX(BlockState state) {
        return switch (state.get(FACING)) {
            case DOWN, SOUTH, NORTH, UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }
    public double getDripY(BlockState state) {
        return switch (state.get(FACING)) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, SOUTH, EAST, WEST -> 0.5;
        };
    }
    public double getDripZ(BlockState state) {
        return switch (state.get(FACING)) {
            case DOWN, WEST, EAST, UP -> 0.5;
            case NORTH -> -0.05;
            case SOUTH -> 1.05;
        };
    }

    public static Position getOutputLocation(BlockPointer blockPointer) {
        Direction direction = blockPointer.getBlockState().get(CopperPipe.FACING);
        double d = blockPointer.getX() + 0.7D * (double)direction.getOffsetX();
        double e = blockPointer.getY() + 0.7D * (double)direction.getOffsetY();
        double f = blockPointer.getZ() + 0.7D * (double)direction.getOffsetZ();
        return new PositionImpl(d, e, f);
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
            return state.get(CopperPipeProperties.HAS_ITEM) || state.get(CopperPipeProperties.HAS_ELECTRICITY);
        } return false;
    }

    public static int getLuminance(BlockState state) {
        if (state.getBlock() instanceof CopperPipe || state.getBlock() instanceof CopperFitting) {
            if (state.get(CopperPipeProperties.HAS_ELECTRICITY)) {return 5;}
            if (state.get(CopperPipeProperties.HAS_ITEM)) {return 3;}
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
                    if (blockState.get(WATERLOGGED) && !pipe.wasPreviouslyWaterlogged) {
                        if (world.random.nextFloat() < 0.05688889F) {
                            if (world.random.nextFloat() < 0.36F) {
                                if (getNextStage(blockState.getBlock()) != null) {
                                    blockState = makeCopyOf(blockState, getNextStage(blockState.getBlock()));
                                    pipe.wasPreviouslyWaterlogged=true;
                                    world.setBlockState(blockPos, blockState);
                                }
                            }
                        }
                    } else { if (!blockState.get(WATERLOGGED)) {pipe.wasPreviouslyWaterlogged=false;} }
                }
            }
        }
    }

    public boolean isReceivingRedstonePower(BlockPos blockPos, World world) {
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
    public static Block getNextStage(World world, BlockPos blockPos) {
        if (world.getBlockState(blockPos).getBlock() instanceof CopperPipe pipe) {
            if (pipe==COPPER_PIPE) { return EXPOSED_PIPE; }
            if (pipe==EXPOSED_PIPE) { return WEATHERED_PIPE; }
            if (pipe==WEATHERED_PIPE) { return OXIDIZED_PIPE; }
            if (pipe==OXIDIZED_PIPE) { return CORRODED_PIPE; }
        } return null;
    }
    public static Block getNextStage(Block pipe) {
        if (pipe==COPPER_PIPE) { return EXPOSED_PIPE; }
        if (pipe==EXPOSED_PIPE) { return WEATHERED_PIPE; }
        if (pipe==WEATHERED_PIPE) { return OXIDIZED_PIPE; }
        if (pipe==OXIDIZED_PIPE) { return CORRODED_PIPE; }
        return null;
    }
    public static Block getWaxStage(World world, BlockPos blockPos) {
        if (world.getBlockState(blockPos).getBlock() instanceof CopperPipe pipe) {
            if (pipe==COPPER_PIPE) { return WAXED_COPPER_PIPE; }
            if (pipe==EXPOSED_PIPE) { return WAXED_EXPOSED_PIPE; }
            if (pipe==WEATHERED_PIPE) { return WAXED_WEATHERED_PIPE; }
            if (pipe==OXIDIZED_PIPE) { return WAXED_OXIDIZED_PIPE; }
        } return null;
    }
    public static Block getPreviousStage(World world, BlockPos blockPos) {
        if (world.getBlockState(blockPos).getBlock() instanceof CopperPipe pipe) {
            if (pipe==EXPOSED_PIPE) { return COPPER_PIPE; }
            if (pipe==WEATHERED_PIPE) { return EXPOSED_PIPE; }
            if (pipe==OXIDIZED_PIPE) { return WEATHERED_PIPE; }
            if (pipe==CORRODED_PIPE) { return OXIDIZED_PIPE; }
            if (pipe==WAXED_COPPER_PIPE) { return COPPER_PIPE; }
            if (pipe==WAXED_EXPOSED_PIPE) { return EXPOSED_PIPE; }
            if (pipe==WAXED_WEATHERED_PIPE) { return WEATHERED_PIPE; }
            if (pipe==WAXED_OXIDIZED_PIPE) { return OXIDIZED_PIPE; }
        } return null;
    }
    public static Block getGlowingStage(World world, BlockPos blockPos) {
        if (world.getBlockState(blockPos).getBlock() instanceof CopperPipe pipe) {
            if (pipe==RED_PIPE) { return GLOWING_RED_PIPE; }
            if (pipe==ORANGE_PIPE) { return GLOWING_ORANGE_PIPE; }
            if (pipe==YELLOW_PIPE) { return GLOWING_YELLOW_PIPE; }
            if (pipe==LIME_PIPE) { return GLOWING_LIME_PIPE; }
            if (pipe==GREEN_PIPE) { return GLOWING_GREEN_PIPE; }
            if (pipe==CYAN_PIPE) { return GLOWING_CYAN_PIPE; }
            if (pipe==LIGHT_BLUE_PIPE) { return GLOWING_LIGHT_BLUE_PIPE; }
            if (pipe==BLUE_PIPE) { return GLOWING_BLUE_PIPE; }
            if (pipe==PURPLE_PIPE) { return GLOWING_PURPLE_PIPE; }
            if (pipe==MAGENTA_PIPE) { return GLOWING_MAGENTA_PIPE; }
            if (pipe==PINK_PIPE) { return GLOWING_PINK_PIPE; }
            if (pipe==WHITE_PIPE) { return GLOWING_WHITE_PIPE; }
            if (pipe==LIGHT_GRAY_PIPE) { return GLOWING_LIGHT_GRAY_PIPE; }
            if (pipe==GRAY_PIPE) { return GLOWING_GRAY_PIPE; }
            if (pipe==BLACK_PIPE) { return GLOWING_BLACK_PIPE; }
            if (pipe==BROWN_PIPE) { return GLOWING_BROWN_PIPE; }
        } return null;
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

    public static final Block OXIDIZED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.TEAL).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2,false,12, 0);
    public static final Block WEATHERED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.DARK_AQUA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2,false,15, 0);
    public static final Block EXPOSED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2,false,18, 0);
    public static final Block COPPER_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 2,false, 20, 0);

    public static final Block WAXED_OXIDIZED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.TEAL).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1,true,12, 0);
    public static final Block WAXED_WEATHERED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.DARK_AQUA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1,true,15, 0);
    public static final Block WAXED_EXPOSED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1,true,18, 0);
    public static final Block WAXED_COPPER_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 1,true,20, 0);

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
    )), 8,false,7, 0);

    public static final Block BLACK_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,8, 0);
    public static final Block RED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.RED).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,9, Main.colorToInt("red"));
    public static final Block ORANGE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,10, Main.colorToInt("orange"));
    public static final Block YELLOW_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,11, Main.colorToInt("yellow"));
    public static final Block LIME_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIME).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,12, Main.colorToInt("lime"));
    public static final Block GREEN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.GREEN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,13, Main.colorToInt("green"));
    public static final Block CYAN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.CYAN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,14, Main.colorToInt("cyan"));
    public static final Block LIGHT_BLUE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,15, Main.colorToInt("light_blue"));
    public static final Block BLUE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,16, Main.colorToInt("blue"));
    public static final Block PURPLE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,17, Main.colorToInt("purple"));
    public static final Block MAGENTA_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,18, Main.colorToInt("magenta"));
    public static final Block PINK_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,19, Main.colorToInt("pink"));
    public static final Block WHITE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,20, Main.colorToInt("white"));
    public static final Block LIGHT_GRAY_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,21, Main.colorToInt("light_gray"));
    public static final Block GRAY_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,22, Main.colorToInt("gray"));
    public static final Block BROWN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BROWN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER), 4,false,23, Main.colorToInt("brown"));

    public static final Block GLOWING_BLACK_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,7, 0);
    public static final Block GLOWING_RED_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.RED).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,8, Main.colorToInt("red"));
    public static final Block GLOWING_ORANGE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.ORANGE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,9, Main.colorToInt("orange"));
    public static final Block GLOWING_YELLOW_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.YELLOW).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,10, Main.colorToInt("yellow"));
    public static final Block GLOWING_LIME_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIME).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,11, Main.colorToInt("lime"));
    public static final Block GLOWING_GREEN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.GREEN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,12, Main.colorToInt("green"));
    public static final Block GLOWING_CYAN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.CYAN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,13, Main.colorToInt("cyan"));
    public static final Block GLOWING_LIGHT_BLUE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIGHT_BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false, 14, Main.colorToInt("light_blue"));
    public static final Block GLOWING_BLUE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BLUE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,15, Main.colorToInt("blue"));
    public static final Block GLOWING_PURPLE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.PURPLE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,16, Main.colorToInt("purple"));
    public static final Block GLOWING_MAGENTA_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.MAGENTA).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,17, Main.colorToInt("magenta"));
    public static final Block GLOWING_PINK_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.PINK).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,18, Main.colorToInt("pink"));
    public static final Block GLOWING_WHITE_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.WHITE).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,19, Main.colorToInt("white"));
    public static final Block GLOWING_LIGHT_GRAY_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.LIGHT_GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,20, Main.colorToInt("light_gray"));
    public static final Block GLOWING_GRAY_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,21, Main.colorToInt("gray"));
    public static final Block GLOWING_BROWN_PIPE = new CopperPipe(Settings.of(Material.METAL, MapColor.BROWN).requiresTool().strength(1.5F, 3.0F).sounds(BlockSoundGroup.COPPER).luminance(CopperPipe::getLuminance).emissiveLighting((state, world, pos) -> CopperPipe.hasItem(state)), 4,false,22, Main.colorToInt("brown"));

}

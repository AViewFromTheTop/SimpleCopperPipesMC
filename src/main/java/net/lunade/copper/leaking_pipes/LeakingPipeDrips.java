package net.lunade.copper.leaking_pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LeakingPipeDrips {

    private static final Map<Block, DripOn> BLOCKS_TO_DRIPS = new HashMap<>();

    public static void register(Block block, DripOn drip) {
        BLOCKS_TO_DRIPS.put(block, drip);
    }

    @Nullable
    public static DripOn getDrip(Block block) {
        if (BLOCKS_TO_DRIPS.containsKey(block)) {
            return BLOCKS_TO_DRIPS.get(block);
        }
        return null;
    }

    @FunctionalInterface
    public interface DripOn {
        void dripOn(ServerLevel world, BlockPos pos, BlockState state);
    }

    public static void init() {
        register(Blocks.CAULDRON, ((world, pos, state) -> world.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 1))));
        register(Blocks.WATER_CAULDRON, ((world, pos, state) -> {
            if (state.getValue(BlockStateProperties.LEVEL_CAULDRON) != 3) {
                world.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().cycle(BlockStateProperties.LEVEL_CAULDRON));
            }
        }));
        register(Blocks.DIRT, ((world, pos, state) -> world.setBlockAndUpdate(pos, Blocks.MUD.defaultBlockState())));
        register(Blocks.FIRE, ((world, pos, state) -> world.destroyBlock(pos, true)));
    }

}

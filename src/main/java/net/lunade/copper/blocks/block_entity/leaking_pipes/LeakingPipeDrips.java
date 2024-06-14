package net.lunade.copper.blocks.block_entity.leaking_pipes;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

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

    public static void init() {
        register(Blocks.CAULDRON, ((lava, world, pos, state) -> {
            if (!lava) {
                world.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 1));
            } else {
                world.setBlockAndUpdate(pos, Blocks.LAVA_CAULDRON.defaultBlockState());
            }
        }));

        register(Blocks.WATER_CAULDRON, ((lava, world, pos, state) -> {
            if (state.getValue(BlockStateProperties.LEVEL_CAULDRON) != 3 && !lava) {
                world.setBlockAndUpdate(pos, state.cycle(BlockStateProperties.LEVEL_CAULDRON));
            }
        }));

        register(Blocks.DIRT, ((lava, world, pos, state) -> {
            if (!lava) {
                world.setBlockAndUpdate(pos, Blocks.MUD.defaultBlockState());
            }
        }));

        register(Blocks.FIRE, ((lava, world, pos, state) -> {
            if (!lava) {
                world.destroyBlock(pos, true);
            }
        }));
    }

    @FunctionalInterface
    public interface DripOn {
        void dripOn(boolean lava, ServerLevel world, BlockPos pos, BlockState state);
    }

}

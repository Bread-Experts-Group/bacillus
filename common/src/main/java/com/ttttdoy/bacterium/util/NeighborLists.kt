package com.ttttdoy.bacterium.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class NeighborLists {
    public static final BlockPos[] close = new BlockPos[]{
            new BlockPos(-1, -1, -1),
            new BlockPos(0, -1, -1),
            new BlockPos(1, -1, -1),
            new BlockPos(-1, 0, -1),
            new BlockPos(0, 0, -1),
            new BlockPos(1, 0, -1),
            new BlockPos(-1, 1, -1),
            new BlockPos(0, 1, -1),
            new BlockPos(1, 1, -1),
            new BlockPos(-1, -1, 0),
            new BlockPos(0, -1, 0),
            new BlockPos(1, -1, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(-1, 1, 0),
            new BlockPos(0, 1, 0),
            new BlockPos(1, 1, 0),
            new BlockPos(-1, -1, 1),
            new BlockPos(0, -1, 1),
            new BlockPos(1, -1, 1),
            new BlockPos(-1, 0, 1),
            new BlockPos(0, 0, 1),
            new BlockPos(1, 0, 1),
            new BlockPos(-1, 1, 1),
            new BlockPos(0, 1, 1),
            new BlockPos(1, 1, 1)
    };

    public static final BlockPos[] far = new BlockPos[]{
            new BlockPos(-2,0,0),
            new BlockPos(2,0,0),
            new BlockPos(0,-2,0),
            new BlockPos(0,2,0),
            new BlockPos(0,0,-2),
            new BlockPos(0,0,2),
            new BlockPos(-3,0,0),
            new BlockPos(3,0,0),
            new BlockPos(0,-3,0),
            new BlockPos(0,3,0),
            new BlockPos(0,0,-3),
            new BlockPos(0,0,3)
    };

    @Nullable
    public static BlockPos nextPlace(Level level, BlockPos pos, Set<Block> filter, RandomSource random) {
        int randI = random.nextInt(27);
        return nextPlace(level, pos, filter, randI);
    }

    @Nullable
    public static BlockPos nextPlace(Level level, BlockPos pos, Set<Block> filter, int startI) {
        int i = startI;

        do {
            if (filter != null && filter.contains(level.getBlockState(NeighborLists.close[i].offset(pos)).getBlock())) {
                return NeighborLists.close[i].offset(pos);
            } else i = (i + 1) % 27;
        } while (i != startI);

        for (i = 0; i < 12; i++) {
            if (filter != null && filter.contains(level.getBlockState(NeighborLists.far[i].offset(pos)).getBlock()))
                return NeighborLists.far[i].offset(pos);
        }

        return null;
    }
}

package com.ttttdoy.bacterium.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

object NeighborLists {
    fun getNextPositionFiltered(level: Level, pos: BlockPos, filter: Set<Block>): BlockPos? {
        val list = buildList {
            for (x in -1 .. 1) {
                for (y in -1 .. 1) {
                    for (z in -1 .. 1) {
                        val nextPos = pos.offset(x, y, z)
                        if (filter.contains(level.getBlockState(nextPos).block)) add(nextPos)
                    }
                }
            }
        }
        return if (list.isEmpty()) null else list[level.random.nextInt(list.size)]
    }
}

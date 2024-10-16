package com.ttttdoy.bacterium.util

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

object NeighborLists {
    fun getNextPositionFiltered(level: Level, pos: BlockPos, filter: Set<Block>, random : RandomSource? = null): BlockPos? {
        for (x in -1 .. 1) {
            for (y in -1 .. 1) {
                for (z in -1 .. 1) {
                    val nextPos = pos.offset(x, y, z)
                    if (((random != null && random.nextInt(1, 10) > 2) || (random == null)) &&
                        filter.contains(level.getBlockState(nextPos).block)) return nextPos
                }
            }
        }
        return null
    }
}

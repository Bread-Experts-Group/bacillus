package com.ttttdoy.bacillus.util

import net.minecraft.core.BlockPos
import net.minecraft.core.SectionPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.chunk.LevelChunk

object NeighborLists {
    fun getNextPositionFiltered(level: Level, block: Block, pos: BlockPos, filter: Set<Block>?, except : Block?): BlockPos? {
        var chunk: LevelChunk? = null
        for (x in -1 .. 1) {
            for (y in -1 .. 1) {
                for (z in -1 .. 1) {
                    val nextPos = pos.offset(x, y, z)

                    val coordX = SectionPos.blockToSectionCoord(nextPos.x)
                    val coordY = SectionPos.blockToSectionCoord(nextPos.z)
                    if (chunk == null || chunk.pos.x != coordX || chunk.pos.z != coordY) chunk = level.getChunk(coordX, coordY)
                    val nextState = chunk.getBlockState(nextPos)

                    if (
                        !nextState.isAir && nextState.block != block &&
                        (except == null || nextState.block != except) &&
                        (filter == null || filter.contains(nextState.block))
                    ) return nextPos
                }
            }
        }
        return null
    }
}

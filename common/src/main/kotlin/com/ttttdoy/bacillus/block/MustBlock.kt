package com.ttttdoy.bacillus.block

import com.ttttdoy.bacillus.util.General.getNextPositionFiltered
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty

class MustBlock : Block(Properties.ofFullCopy(Blocks.SPONGE)), BonemealableBlock {

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(AGE)
    }

    override fun isValidBonemealTarget(levelReader: LevelReader, blockPos: BlockPos, blockState: BlockState): Boolean =
        true

    override fun isBonemealSuccess(
        level: Level,
        randomSource: RandomSource,
        blockPos: BlockPos,
        blockState: BlockState
    ): Boolean = getNextPositionFiltered(level, blockState.block, blockPos, filter, null) != null

    override fun performBonemeal(
        serverLevel: ServerLevel,
        randomSource: RandomSource,
        blockPos: BlockPos,
        blockState: BlockState
    ) {
        val i = blockState.getValue(AGE) + randomSource.nextInt(3) + 1
        if (i < 16) serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(AGE, i))
        if (randomSource.nextInt(8) + 8 < i) {
            val next = getNextPositionFiltered(serverLevel, blockState.block, blockPos, filter, null)
            if (next != null) {
                serverLevel.setBlockAndUpdate(next, this.defaultBlockState())
                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(AGE, randomSource.nextInt(10)))
            }
        }
    }

    override fun isRandomlyTicking(blockState: BlockState): Boolean = true
    override fun randomTick(
        blockState: BlockState,
        serverLevel: ServerLevel,
        blockPos: BlockPos,
        randomSource: RandomSource
    ) = performBonemeal(serverLevel, randomSource, blockPos, blockState)

    init {
        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0))
    }

    companion object {
        private val AGE: IntegerProperty = BlockStateProperties.AGE_15
        private val filter: Set<Block> = setOf(Blocks.WATER)
    }
}

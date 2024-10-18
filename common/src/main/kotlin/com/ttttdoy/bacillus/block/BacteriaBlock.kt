package com.ttttdoy.bacillus.block

import com.mojang.serialization.MapCodec
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacillus.registry.ModBlockEntityTypes
import com.ttttdoy.bacillus.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.apache.logging.log4j.LogManager

/**
 * ### Main bacteria block class for the destroyer and replacer.
 *
 * - Replacer: Place any full block on top of the replacer, and it will convert the block type below it to whatever was placed on top.
 * - Destroyer: Place a set of blocks above the destroyer, then it will spread and consume those blocks.
 * - The bacteria has a 3x3x3 block reach. If it runs out of valid blocks within range, it will disappear.
 * - The bacteria has a limited range, controlled by [BacteriaBlockEntity.active]. Once it reaches 0, the bacteria will no longer spread.
 */
class BacteriaBlock : BaseEntityBlock(Properties.ofFullCopy(Blocks.SPONGE).instabreak()) {
    val codec: MapCodec<BacteriaBlock> = simpleCodec { this }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) = BacteriaBlockEntity(blockPos, blockState)

    override fun <T : BlockEntity> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? =
        if (level is ServerLevel) createTickerHelper(
            blockEntityType,
            ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY.get()
        ) { level, pos, state, blockEntity -> blockEntity.tick(level as ServerLevel, pos) }
        else null

    override fun neighborChanged(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        block: Block,
        neighborPos: BlockPos,
        moved: Boolean
    ) {
        if (level.hasNeighborSignal(blockPos)) level.getBlockEntity(blockPos)?.let { entity ->
            val up = level.getBlockState(blockPos.above())
            val down = level.getBlockState(blockPos.below())
            val myBlock = blockState.block
            println(myBlock)
            if (
                (entity is BacteriaBlockEntity && !down.isAir) &&
                (
                        (!up.isAir && myBlock == ModBlocks.REPLACER.get().block && up.block != ModBlocks.REPLACER.get().block) ||
                        (myBlock == ModBlocks.DESTROYER.get().block && down.block != ModBlocks.DESTROYER.get().block)
                )
            ) {
                entity.getIO(level)
                entity.active = 500
            }
        }
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = codec
    override fun getRenderShape(blockState: BlockState): RenderShape = RenderShape.MODEL
    override fun hasDynamicShape(): Boolean = true

    // todo IT NOT WORK
    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val entity = level.getBlockEntity(pos) as? BacteriaBlockEntity ?: return Shapes.block()
        return if (entity.consumingBlockData == null) Shapes.block() else {
            val consumed = entity.consumingBlockData!!
            val logger = LogManager.getLogger()
            logger.info("consumed block state: ${consumed.first}")
            logger.info("consumed block pos: ${consumed.second}")
            logger.info("consumed block shape: ${consumed.third}")
            return consumed.third
        }
    }
}

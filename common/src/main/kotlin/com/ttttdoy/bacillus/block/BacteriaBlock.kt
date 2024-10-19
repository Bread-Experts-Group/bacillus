package com.ttttdoy.bacillus.block

import com.mojang.serialization.MapCodec
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacillus.registry.ModBlockEntityTypes
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
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

/**
 * ### Main bacteria block class for the destroyer and replacer.
 *
 * - Replacer: Place any full block on top of the replacer, and it will convert the block type below it to whatever was placed on top.
 * - Destroyer: Place a set of blocks above the destroyer, then it will spread and consume those blocks.
 * - The bacteria has a 3x3x3 block reach. If it runs out of valid blocks within range, it will disappear.
 * - The bacteria has a limited range, controlled by [BacteriaBlockEntity.active]. Once it reaches 0, the bacteria will no longer spread.
 */
class BacteriaBlock : BaseEntityBlock(Properties.ofFullCopy(Blocks.SPONGE).instabreak().noOcclusion()) {
    val codec: MapCodec<BacteriaBlock> = simpleCodec { this }

    init {
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(BlockStateProperties.ENABLED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.ENABLED)
        super.createBlockStateDefinition(builder)
    }

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

    private fun start(level: Level, blockPos: BlockPos, blockState: BlockState) = level.getBlockEntity(blockPos)?.let { entity ->
        if (entity is BacteriaBlockEntity && entity.getIO(level)) {
            entity.active = 500
            level.setBlock(blockPos, blockState.setValue(BlockStateProperties.ENABLED, true), 2)
        }
    }

    override fun neighborChanged(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        block: Block,
        neighborPos: BlockPos,
        moved: Boolean
    ) {
        if (blockState.getValue(BlockStateProperties.ENABLED)) return
        if (!level.hasNeighborSignal(blockPos)) return
        start(level, blockPos, blockState)
    }

    override fun onPlace(blockState: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        if (blockState.getValue(BlockStateProperties.ENABLED)) return
        if (!level.hasNeighborSignal(pos)) return
        start(level, pos, blockState)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = codec
    override fun getRenderShape(blockState: BlockState): RenderShape = RenderShape.INVISIBLE
    override fun hasDynamicShape(): Boolean = true

    // todo IT NOT WORK
    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val entity = level.getBlockEntity(pos) as? BacteriaBlockEntity ?: return Shapes.block()
//        return entity.consumingBlockData?.let {
//            return if (it.third == Shapes.empty()) Shapes.block()
//            else it.third
//        } ?: Shapes.block()
        return Shapes.block()
    }
}

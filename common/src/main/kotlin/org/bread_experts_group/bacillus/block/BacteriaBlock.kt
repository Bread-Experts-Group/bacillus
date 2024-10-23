package org.bread_experts_group.bacillus.block

import com.mojang.serialization.MapCodec
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
import net.minecraft.world.level.redstone.Orientation
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.bread_experts_group.bacillus.block.entity.BacteriaBlockEntity
import org.bread_experts_group.bacillus.registry.ModBlockEntityTypes
import org.bread_experts_group.bacillus.util.General.setId

/**
 * ### Main bacteria block class for the destroyer and replacer.
 *
 * - Replacer: Place any full block on top of the replacer,
 * and it will convert the block type below it to whatever was placed on top.
 * - Destroyer: Place a set of blocks above the destroyer, then it will spread and consume those blocks.
 * - The bacteria has a 3 x 3 x 3 block reach (defined in [org.bread_experts_group.bacillus.util.General]).
 * If it runs out of valid blocks within range, it will disappear.
 * - The bacteria has a limited range, controlled by [BacteriaBlockEntity.active].
 * Once [BacteriaBlockEntity.active] reaches 0, the bacteria will no longer spread
 * @see BacteriaBlockEntity.active
 * @see BacteriaBlockEntity.grace
 * @since 1.0.0
 */
class BacteriaBlock(id: String) : BaseEntityBlock(
    Properties.ofFullCopy(Blocks.SPONGE).instabreak().noOcclusion().setId(id)
) {
    val codec: MapCodec<BacteriaBlock> = simpleCodec { this }

    init {
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(BlockStateProperties.ENABLED, false)
                .setValue(BlockStateProperties.TRIGGERED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.ENABLED, BlockStateProperties.TRIGGERED)
        super.createBlockStateDefinition(builder)
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) = BacteriaBlockEntity(blockPos, blockState)

    override fun propagatesSkylightDown(blockState: BlockState): Boolean = true

    override fun <T : BlockEntity> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? =
        if (level is ServerLevel) createTickerHelper(
            blockEntityType,
            ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY.get()
        ) { tLevel, tPos, _, tBlockEntity -> tBlockEntity.tick(tLevel, tPos) }
        else null

    private fun start(level: Level, blockPos: BlockPos, blockState: BlockState) =
        level.getBlockEntity(blockPos)?.let { entity ->
            if (entity is BacteriaBlockEntity && entity.getIO(level)) {
                entity.active = 5
                level.setBlock(blockPos, blockState.setValue(BlockStateProperties.ENABLED, true), 2)
            }
        }

    override fun neighborChanged(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        block: Block,
        orientation: Orientation?,
        bl: Boolean
    ) {
        if (blockState.getValue(BlockStateProperties.ENABLED)) return
        if (!level.hasNeighborSignal(blockPos)) return
        start(level, blockPos, blockState)
    }

    override fun onPlace(
        blockState: BlockState,
        level: Level,
        pos: BlockPos,
        oldState: BlockState,
        movedByPiston: Boolean
    ) {
        if (blockState.getValue(BlockStateProperties.ENABLED)) return
        if (!level.hasNeighborSignal(pos)) return
        start(level, pos, blockState)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = codec
    override fun getRenderShape(blockState: BlockState): RenderShape =
        if (blockState.getValue(BlockStateProperties.TRIGGERED)) RenderShape.INVISIBLE
        else RenderShape.MODEL

    override fun hasDynamicShape(): Boolean = true

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val entity = level.getBlockEntity(pos) as? BacteriaBlockEntity ?: return Shapes.block()
        return entity.consumedBlockState?.getVisualShape(level, pos, context) ?: Shapes.block()
    }

    override fun getCollisionShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val entity = level.getBlockEntity(pos) as? BacteriaBlockEntity ?: return Shapes.block()
        return entity.consumedBlockState?.getCollisionShape(level, pos, context) ?: Shapes.block()
    }
}

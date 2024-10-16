package com.ttttdoy.bacterium.block

import com.mojang.serialization.MapCodec
import com.ttttdoy.bacterium.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacterium.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class BacteriaBlock private constructor(properties : Properties) : BaseEntityBlock(properties) {
    constructor() : this(Properties.ofFullCopy(Blocks.SPONGE).instabreak())

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) = BacteriaBlockEntity(blockPos, blockState)

    override fun <T : BlockEntity> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? =
        if (level is ServerLevel) createTickerHelper<BacteriaBlockEntity, T>(
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
        if (level.hasNeighborSignal(blockPos)) level.getBlockEntity(blockPos)?.let {
            if (it is BacteriaBlockEntity) it.active = 500
        }
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC
    override fun getRenderShape(blockState: BlockState): RenderShape = RenderShape.MODEL

    companion object {
        val CODEC: MapCodec<BacteriaBlock> = simpleCodec<BacteriaBlock> { properties -> BacteriaBlock(properties) }
    }
}

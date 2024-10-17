package com.ttttdoy.bacterium.block

import com.mojang.serialization.MapCodec
import com.ttttdoy.bacterium.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacterium.registry.ModBlockEntityTypes
import com.ttttdoy.bacterium.registry.ModBlocks
import dev.architectury.platform.Platform
import dev.architectury.utils.Env
import net.minecraft.client.Minecraft
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
import java.awt.Color
import kotlin.random.Random

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
        if (level.hasNeighborSignal(blockPos)) level.getBlockEntity(blockPos)?.let {
            val up = level.getBlockState(blockPos.above())
            val down = level.getBlockState(blockPos.below())
            val myBlock = blockState.block
            println(myBlock)
            if (
                (it is BacteriaBlockEntity && !down.isAir) &&
                (
                        (!up.isAir && myBlock == ModBlocks.REPLACER.get().block && up.block != ModBlocks.REPLACER.get().block) ||
                        (myBlock == ModBlocks.DESTROYER.get().block && down.block != ModBlocks.DESTROYER.get().block)
                )
            ) {
                it.getIO(level)
                it.active = 500
            }
        }
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = codec
    override fun getRenderShape(blockState: BlockState): RenderShape = RenderShape.MODEL

    companion object {
        init {
            if (Platform.getEnvironment() == Env.CLIENT) {
                val minecraft = Minecraft.getInstance()
                minecraft.blockColors.register(
                    { blockState, level, blockPos, index -> Color(Random(204).nextInt(1, 255), 0, 0).rgb },
                    ModBlocks.DESTROYER.get().block, ModBlocks.REPLACER.get().block
                )
            }
        }
    }
}

package com.ttttdoy.bacillus.block.entity

import com.ttttdoy.bacillus.registry.ModBlockEntityTypes
import com.ttttdoy.bacillus.registry.ModBlocks
import com.ttttdoy.bacillus.util.NeighborLists
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Container
import net.minecraft.world.Containers
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.VoxelShape
import org.apache.logging.log4j.LogManager
import kotlin.math.log

class BacteriaBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY.get(), blockPos, blockState) {
    var cached : Pair<Set<Block>, Block>? = null
    /**
     * Used in the renderer to set the bacteria's model and shape to the block it's consuming
     */
    var consumingBlockData: Triple<BlockState, BlockPos, VoxelShape>? = null
    fun getIO(level: Level): Pair<Set<Block>, Block> {
        if (cached != null) return cached!!
        val output : Block
        val input : Set<Block>

        val down = level.getBlockState(blockPos.below()).block
        if (blockState.block == ModBlocks.DESTROYER.get().block) {
            input = buildSet {
                var position = blockPos.below()
                do {
                    val state = level.getBlockState(position)
                    if (state.isAir) break
                    if (state.block != ModBlocks.DESTROYER.get().block) add(state.block)
                    position = position.above()
                } while (true)
            }
            output = Blocks.AIR
        } else {
            input = setOf(down)
            output = level.getBlockState(blockPos.above()).block
        }

        cached = input to output
        return cached!!
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> =
        ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        super.getUpdateTag(registries).also { saveAdditional(it, registries) }

    /**
     * Dictates how long the bacteria lasts for before disappearing.
     */
    var active = -1

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        cached?.let {
            compoundTag.putString("inputs", it.first.joinToString("#") { block -> BuiltInRegistries.BLOCK.getKey(block).toString() })
            compoundTag.putString("outputs", BuiltInRegistries.BLOCK.getKey(it.second).toString())
        }
        compoundTag.putInt("active", active)
        compoundTag.putInt("grace", grace)
        super.saveAdditional(compoundTag, provider)
    }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        val input = compoundTag.getString("inputs").split("#").map { BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(it)) }.toSet()
        val output = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(compoundTag.getString("outputs")))
        cached = input to output
        active = compoundTag.getInt("active")
        grace = compoundTag.getInt("grace")
        super.loadAdditional(compoundTag, provider)
    }

    private fun replace(level: Level, pos: BlockPos) {
        if (level.getBlockEntity(pos) != null) {
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity is Container) {
                Containers.dropContents(level, pos, blockEntity as Container)
                level.updateNeighborsAt(pos, level.getBlockState(pos).block)
            }
        }

        val germinationState =
            if (blockState.block == ModBlocks.DESTROYER.get().block) ModBlocks.DESTROYER.get().block.defaultBlockState()
            else ModBlocks.REPLACER.get().block.defaultBlockState()

        level.setBlockAndUpdate(pos, germinationState)
        val newBacteria = BacteriaBlockEntity(pos, germinationState)
        newBacteria.cached = cached
        newBacteria.active = level.random.nextInt(0, active + level.random.nextInt(0, 10))
        level.setBlockEntity(newBacteria)
        level.playSound(null, pos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 0.8f, 1f)
    }

    /**
     * Grace counter for the bacteria to spread.
     * If bacteria can't spread within this time, it will decay.
     * Measured in Minecraft game ticks.
     * @author Miko Elbrecht
     * @since 1.0.0
     */
    var grace = (20 * 2.5).toInt()
    fun tick(level: ServerLevel, pos: BlockPos) {
        cached?.let {
            if (level.random.nextInt(1, 10) > 2) return
            if ((active == -1 || globalJamState) && !globalKillState) return

            val next = NeighborLists.getNextPositionFiltered(level, pos, it.first)
            if (active > 0 && next != null && !globalKillState) {
                consumingBlockData = Triple(level.getBlockState(next), next, level.getBlockState(next).getCollisionShape(level, next))
                replace(level, next)
                grace = -1
            } else if (grace == -1 || globalKillState) {
                level.setBlockAndUpdate(pos, it.second.defaultBlockState())
                setRemoved()
            } else grace--
        }
    }

    companion object {
        var globalJamState = false
        var globalKillState = false
    }
}

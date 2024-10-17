package com.ttttdoy.bacterium.block.entity

import com.ttttdoy.bacterium.registry.ModBlockEntityTypes
import com.ttttdoy.bacterium.registry.ModBlocks
import com.ttttdoy.bacterium.util.NeighborLists
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
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

class BacteriaBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY.get(), blockPos, blockState) {
    private var cached : Pair<Set<Block>, Block>? = null
    fun getIO(level: Level): Pair<Set<Block>, Block> {
        if (cached != null) return cached!!
        val output : Block
        val input : Set<Block>

        if (blockState.block == ModBlocks.DESTROYER.get().block) {
            input = buildSet {
                var position = blockPos.above()
                do {
                    val state = level.getBlockState(position)
                    if(state.isAir) break
                    if (state.block != ModBlocks.DESTROYER.get().block) add(state.block)
                    position = position.above()
                } while (true)
            }
            output = Blocks.AIR
        } else {
            input = setOf(level.getBlockState(blockPos.below()).block)
            output = level.getBlockState(blockPos.above()).block
        }

        cached = input to output
        return cached!!
    }

    var active = -1

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        level?.let {
            val io = getIO(it)
            compoundTag.putString("inputs", io.first.joinToString("#") { BuiltInRegistries.BLOCK.getKey(it).toString() })
            compoundTag.putString("outputs", BuiltInRegistries.BLOCK.getKey(io.second).toString())
        }
        super.saveAdditional(compoundTag, provider)
    }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        val input = compoundTag.getString("inputs").split("#").map { BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(it)) }.toSet()
        val output = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(compoundTag.getString("outputs")))
        cached = input to output
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
        newBacteria.cached = getIO(level)
        newBacteria.active = level.random.nextInt(0, active + level.random.nextInt(0, 10))
        level.setBlockEntity(newBacteria)
        level.playSound(null, pos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 0.8f, 1f)
    }

    fun tick(level: ServerLevel, pos: BlockPos) {
        if (level.random.nextInt(1, 10) > 6) return
        if ((active == -1 || globalJamState) && !globalKillState) return
        val io = getIO(level)

        val next = NeighborLists.getNextPositionFiltered(level, pos, io.first, level.random)
        if (active > 0 && next != null && !globalKillState) replace(level, next)
        else {
            level.setBlockAndUpdate(pos, io.second.defaultBlockState())
            setRemoved()
        }
    }

    companion object {
        var globalJamState = false
        var globalKillState = false
    }
}

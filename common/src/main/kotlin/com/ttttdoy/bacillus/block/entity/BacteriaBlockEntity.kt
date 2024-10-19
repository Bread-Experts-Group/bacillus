package com.ttttdoy.bacillus.block.entity

import com.ttttdoy.bacillus.registry.ModBlockEntityTypes
import com.ttttdoy.bacillus.registry.ModBlockTags
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
import net.minecraft.world.Clearable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.apache.logging.log4j.LogManager

class BacteriaBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY.get(), blockPos, blockState) {
    var cached : Pair<Set<Block>?, Block>? = null
    var consumedBlockState: BlockState? = null
    fun getIO(level: Level): Boolean {
        if (cached != null) return false
        var input : MutableList<Block>? = mutableListOf()
        var position = blockPos.below()
        do {
            val state = level.getBlockState(position)
            if (state.isAir) break
            if (
                !state.`is`(ModBlockTags.UNFILTERABLE) && position != blockPos &&
                (blockState.block == ModBlocks.DESTROYER.get().block && !state.`is`(ModBlockTags.UNREMOVABLE)) ||
                (blockState.block == ModBlocks.REPLACER.get().block && !state.`is`(ModBlockTags.UNREPLACEABLE))
            ) if (state.block == ModBlocks.EVERYTHING.get().block) {
                input = null
                break
            } else input!!.add(state.block)
            position = position.above()
        } while (true)

        val output : Block = if (blockState.block == ModBlocks.REPLACER.get().block) {
            val above = level.getBlockState(blockPos.above())
            if (above.isAir || above == blockState.block) return false else above.block
        } else Blocks.AIR

        cached = input?.toMutableSet()?.also { it.remove(output) } to output
        return true
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> =
        ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        super.getUpdateTag(registries).also { saveAdditional(it, registries) }

    var active = -1

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        cached?.let {
            if (it.first != null) compoundTag.putString("inputs", it.first!!.joinToString("#") { block -> BuiltInRegistries.BLOCK.getKey(block).toString() })
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

    val germinationState =
        (if (blockState.block == ModBlocks.DESTROYER.get().block) ModBlocks.DESTROYER.get().block.defaultBlockState()
        else ModBlocks.REPLACER.get().block.defaultBlockState()).setValue(BlockStateProperties.ENABLED, true)

    private fun replace(level: Level, pos: BlockPos) {
        level.getBlockEntity(pos)?.let(Clearable::tryClear)

        val newBacteria = BacteriaBlockEntity(pos, germinationState)
        newBacteria.consumedBlockState = level.getBlockState(pos)
        LogManager.getLogger().info(newBacteria.consumedBlockState!!.properties)
        newBacteria.cached = cached
        newBacteria.active = active--
        level.setBlock(pos, germinationState, 2)
        level.setBlockEntity(newBacteria)
        level.playSound(null, pos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 0.8f, 1f)
    }

    /**
     * Controls how often the block ticks, based on random chance.
     * Lower numbers will result in the block ticking more often, higher is less frequent.
     */
    val tickChance = 40

    /**
     * Grace counter for the bacteria to spread.
     * If bacteria can't spread within this time, it will decay.
     * Measured in Minecraft game ticks.
     * @author Miko Elbrecht
     * @since 1.0.0
     */
    var grace = 5
    fun tick(level: ServerLevel, pos: BlockPos) {
        cached?.let {
            if ((active == -1 || globalJamState) && !globalKillState) return

            val next = NeighborLists.getNextPositionFiltered(level, blockState.block, blockPos, it.first, it.second)
            if (active > 0 && next != null && !globalKillState) {
                if (level.random.nextInt(1, tickChance) != 1) return
                replace(level, next)
                grace = -1
            } else if (grace == -1 || globalKillState) {
                level.setBlock(pos, it.second.defaultBlockState(), 2)
                setRemoved()
            } else grace--
        }
    }

    companion object {
        var globalJamState = false
        var globalKillState = false
    }
}

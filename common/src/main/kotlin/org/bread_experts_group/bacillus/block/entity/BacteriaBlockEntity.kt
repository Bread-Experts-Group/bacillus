package org.bread_experts_group.bacillus.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Clearable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.shapes.Shapes
import org.bread_experts_group.bacillus.registry.ModBlockEntityTypes
import org.bread_experts_group.bacillus.registry.ModBlockTags
import org.bread_experts_group.bacillus.registry.ModBlocks
import org.bread_experts_group.bacillus.util.General
import org.bread_experts_group.bacillus.util.General.deserializeBlockState
import org.bread_experts_group.bacillus.util.General.getBlock
import org.bread_experts_group.bacillus.util.General.serializeInto
import org.joml.SimplexNoise.noise
import kotlin.math.max
import kotlin.math.roundToInt

class BacteriaBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY.get(), blockPos, blockState) {
    var cached: Pair<Set<Block>?, Block>? = null
    fun getIO(level: Level): Boolean {
        if (cached != null) return false
        var input: MutableList<Block>? = mutableListOf()
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

        val output: Block = if (blockState.block == ModBlocks.REPLACER.get().block) {
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

    /**
     * The operative state of the bacteria.
     * * -1 means the bacteria is inactive.
     * * 0 means the bacteria is dead and will decay.
     * * Any positive number means the bacteria is active and will spread.
     * @since 1.0.0
     */
    var active = -1

    /**
     * The block state this bacteria was spawned over for use in rendering.
     * @since 1.0.0
     */
    var consumedBlockState: BlockState? = null

    /**
     * Controls how often the block ticks (after decay checks), based on random chance.
     * Lower numbers will result in the block ticking more often, higher is less frequent.
     * @since 1.0.0
     */
    var tickChance = 55

    /**
     * Grace counter for the bacteria to spread.
     * If bacteria can't spread within this time, it will decay.
     * Measured in Minecraft game ticks.
     * @author Miko Elbrecht
     * @since 1.0.0
     */
    var grace = 120

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        cached?.let {
            if (it.first != null) compoundTag.putString(
                "inputs",
                (it.first ?: return@let).joinToString("#") { block ->
                    BuiltInRegistries.BLOCK.getKey(block).toString()
                })
            compoundTag.putString("outputs", BuiltInRegistries.BLOCK.getKey(it.second).toString())
        }
        compoundTag.putInt("active", active)
        compoundTag.putInt("grace", grace)
        if (consumedBlockState != null) CompoundTag().also {
            (consumedBlockState ?: return@also).serializeInto(it)
            compoundTag.put("consumedBlockState", it)
        }
        super.saveAdditional(compoundTag, provider)
    }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        val input =
            compoundTag.getString("inputs").split("#").map { getBlock(ResourceLocation.tryParse(it), Blocks.DIRT) }.toSet()
        val output = getBlock(ResourceLocation.tryParse(compoundTag.getString("outputs")), Blocks.DIRT)
        cached = input to output
        active = compoundTag.getInt("active")
        grace = compoundTag.getInt("grace")
        if (compoundTag.contains("consumedBlockState"))
            consumedBlockState = compoundTag.getCompound("consumedBlockState").deserializeBlockState()
        super.loadAdditional(compoundTag, provider)
    }

    private val germinationState: BlockState =
        (if (blockState.block == ModBlocks.DESTROYER.get().block) ModBlocks.DESTROYER.get().block.defaultBlockState()
        else ModBlocks.REPLACER.get().block.defaultBlockState()).setValue(BlockStateProperties.ENABLED, true)

    private fun replace(level: Level, pos: BlockPos) {
        level.getBlockEntity(pos)?.let(Clearable::tryClear)

        val newBacteria = BacteriaBlockEntity(pos, germinationState)
        val consumeBlockState = level.getBlockState(pos)
        if (consumeBlockState.renderShape == RenderShape.MODEL) {
            val shape = consumeBlockState.getShape(level, pos)
            if (shape != Shapes.block() && shape != Shapes.empty()) newBacteria.consumedBlockState = consumeBlockState
        }
        newBacteria.cached = cached
        newBacteria.active = active
        newBacteria.tickChance = (newBacteria.tickChance * max(
            1f,
            1 + noise((pos.x * 0.1).toFloat(), (pos.y * 0.1).toFloat(), (pos.z * 0.1).toFloat())
        )).roundToInt()
        level.setBlockAndUpdate(
            pos,
            germinationState.setValue(BlockStateProperties.TRIGGERED, newBacteria.consumedBlockState != null)
        )
        level.setBlockEntity(newBacteria)

        level.playSound(null, pos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 0.8f, 1f)
    }

    fun tick(level: Level, pos: BlockPos) {
        val cache = cached ?: return
        if ((active == -1 || globalJamState) && !globalKillState) return

        if (globalKillState || active == 0 || grace == 0) {
            level.setBlockAndUpdate(pos, cache.second.defaultBlockState())
            setRemoved()
        } else {
            grace--
            if (level.random.nextInt(tickChance) != 0) return
            General.getNextPositionFiltered(level, blockState.block, blockPos, cache.first, cache.second)?.let {
                replace(level, it)
            }
        }
    }

    companion object {
        var globalJamState = false
        var globalKillState = false
    }
}

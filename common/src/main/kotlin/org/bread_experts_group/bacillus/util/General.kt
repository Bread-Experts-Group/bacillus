package org.bread_experts_group.bacillus.util

import net.minecraft.core.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.chunk.LevelChunk
import org.apache.logging.log4j.LogManager
import java.util.*

// THIS CLASS IS HAZARDOUS
object General {
    val LOGGER = LogManager.getLogger()

    fun getNextPositionFiltered(
        level: Level,
        block: Block,
        pos: BlockPos,
        filter: Set<Block>?,
        except: Block?
    ): BlockPos? {
        var chunk: LevelChunk? = null
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    val nextPos = pos.offset(
                        if (level.random.nextBoolean()) -x else x,
                        if (level.random.nextBoolean()) -y else y,
                        if (level.random.nextBoolean()) -z else z
                    )

                    val coordX = SectionPos.blockToSectionCoord(nextPos.x)
                    val coordY = SectionPos.blockToSectionCoord(nextPos.z)
                    if (chunk == null || chunk.pos.x != coordX || chunk.pos.z != coordY) chunk =
                        level.getChunk(coordX, coordY)
                    val nextState = (chunk ?: return null).getBlockState(nextPos)

                    if (
                        !nextState.isAir && nextState.block != block &&
                        (except == null || nextState.block != except) &&
                        (filter == null || filter.contains(nextState.block))
                    ) return nextPos
                }
            }
        }
        return null
    }

    fun Optional<Holder.Reference<Block>>.getBlock() = this.get().unwrap().right().get()

    fun BlockState.serializeInto(tag: CompoundTag) {
        tag.putString("block", BuiltInRegistries.BLOCK.getKey(this.block).toString())
        tag.put("properties", CompoundTag().also { pTag ->
            this.properties.forEach {
                when (it) {
                    is BooleanProperty -> pTag.putBoolean(it.name, this.getValue(it))
                    is IntegerProperty -> pTag.putInt(it.name, this.getValue(it))
                    is EnumProperty -> pTag.putString(it.name, this.getValue(it).name)
                    else -> LOGGER.info("Cannot serialize: ${it::class.qualifiedName}")
                }
            }
        })
    }

    fun CompoundTag.deserializeBlockState(): BlockState {
        var state =
            BuiltInRegistries.BLOCK.get(ResourceLocation.parse(this.getString("block"))).getBlock().defaultBlockState()
//        var state = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(this.getString("block")))
        this.getCompound("properties").let { tag ->
            state.properties.forEach {
                when (it) {
                    is BooleanProperty -> state = state.setValue(it, tag.getBoolean(it.name))
                    is IntegerProperty -> state = state.setValue(it, tag.getInt(it.name))
                    is EnumProperty -> state =
                        General::class.java.methods.first { method -> method.name == "setStateCrazy" }.invoke(
                            General,
                            state,
                            it,
                            it.valueClass.methods.first { method -> method.name == "valueOf" }
                                .invoke(null, tag.getString(it.name))
                        ) as BlockState

                    else -> LOGGER.info("Cannot deserialize: ${it::class.qualifiedName}")
                }
            }
        }
        return state
    }

    // DANGER: this code is terrifying
    @Suppress("UNCHECKED_CAST", "UNUSED")
    fun <T : Comparable<T>> setStateCrazy(state: BlockState, property: EnumProperty<*>, any: Any): BlockState {
        return state.setValue(property as Property<T>, any as T)
    }
}
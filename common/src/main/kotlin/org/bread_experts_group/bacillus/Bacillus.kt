package org.bread_experts_group.bacillus

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import org.bread_experts_group.bacillus.registry.Registry

/**
 * Main common side mod object.
 */
object Bacillus {
    /**
     * Mod id for bacterium.
     */
    const val MOD_ID: String = "bacillus"

    /**
     * Main mod initializer.
     */
    @JvmStatic
    fun init() = Registry.registerAll()

    /**
     * Main mod client initializer.
     */
    @JvmStatic
    fun initClient() = Registry.registerAllClient()

    fun modLocation(vararg path: String, override: Boolean = false): ResourceLocation =
        path.toMutableList().let {
            ResourceLocation.fromNamespaceAndPath(if (override) it.removeFirst() else MOD_ID, it.joinToString("/"))
        }

    fun modTranslatable(type: String = "misc", vararg path: String, args: List<Any> = listOf()): MutableComponent =
        Component.translatable("$type.$MOD_ID.${path.joinToString(".")}", *args.toTypedArray())
}

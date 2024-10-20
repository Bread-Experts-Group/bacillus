package com.ttttdoy.bacillus

import com.ttttdoy.bacillus.registry.Registry

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
}

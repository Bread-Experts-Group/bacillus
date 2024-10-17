package com.ttttdoy.bacterium

import com.ttttdoy.bacterium.registry.Registry

/**
 * Main common side mod object.
 */
object Bacterium {
    /**
     * Mod id for bacterium.
     */
    const val MOD_ID: String = "bacterium"

    /**
     * Main mod initializer.
     */
    @JvmStatic
    fun init() = Registry.registerAll()
}

package com.ttttdoy.bacterium

import com.ttttdoy.bacterium.registry.Registry

object Bacterium {
    const val MOD_ID: String = "bacterium"

    @JvmStatic
    fun init() = Registry.registerAll()
}

package org.bread_experts_group.bacillus.fabric

import net.fabricmc.api.ModInitializer
import org.bread_experts_group.bacillus.Bacillus.init

class BacillusFabric : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.

        init()
    }
}

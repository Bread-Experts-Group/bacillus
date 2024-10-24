package org.bread_experts_group.bacillus.quilt

import org.bread_experts_group.bacillus.Bacillus.init
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

class BacillusQuilt : ModInitializer {
    override fun onInitialize(modContainer: ModContainer) {
        init()
    }
}

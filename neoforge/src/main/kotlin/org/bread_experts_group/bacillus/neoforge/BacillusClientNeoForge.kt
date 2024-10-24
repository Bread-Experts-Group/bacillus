package org.bread_experts_group.bacillus.neoforge

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.Bacillus.initClient

@Mod(value = Bacillus.MOD_ID, dist = [Dist.CLIENT])
class BacillusClientNeoForge {
    init {
        // Run our client setup.
        initClient()
    }
}
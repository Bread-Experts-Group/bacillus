package org.bread_experts_group.bacillus.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import org.bread_experts_group.bacillus.Bacillus;

@Mod(value = Bacillus.MOD_ID, dist = {Dist.CLIENT})
public final class BacillusClientNeoForge {
    public BacillusClientNeoForge() {
        // Run our client setup.
        Bacillus.initClient();
    }
}
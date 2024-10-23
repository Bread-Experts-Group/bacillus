package org.bread_experts_group.bacillus.neoforge;

import net.neoforged.fml.common.Mod;
import org.bread_experts_group.bacillus.Bacillus;

@Mod(Bacillus.MOD_ID)
public final class BacillusNeoForge {
    public BacillusNeoForge() {
        // Run our common setup.
        Bacillus.init();
    }
}

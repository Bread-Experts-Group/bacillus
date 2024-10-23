package org.bread_experts_group.bacillus.fabric;

import net.fabricmc.api.ModInitializer;
import org.bread_experts_group.bacillus.Bacillus;

public final class BacillusFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Bacillus.init();
    }
}

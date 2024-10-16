package com.ttttdoy.bacterium.fabric;

import com.ttttdoy.bacterium.Bacterium;
import net.fabricmc.api.ModInitializer;

public final class BacteriumFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Bacterium.init();
    }
}

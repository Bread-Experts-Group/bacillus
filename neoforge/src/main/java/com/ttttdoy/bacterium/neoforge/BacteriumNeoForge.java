package com.ttttdoy.bacterium.neoforge;

import com.ttttdoy.bacterium.Bacterium;
import net.neoforged.fml.common.Mod;

@Mod(Bacterium.MOD_ID)
public final class BacteriumNeoForge {
    public BacteriumNeoForge() {
        // Run our common setup.
        Bacterium.init();
    }
}

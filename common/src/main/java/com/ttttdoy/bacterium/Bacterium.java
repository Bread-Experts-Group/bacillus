package com.ttttdoy.bacterium;

import com.ttttdoy.bacterium.registry.Registry;

public final class Bacterium {
    public static final String MOD_ID = "bacterium";

    public static void init() {
        Registry.registerAll();
    }
}

package com.ttttdoy.bacillus.neoforge;

import com.ttttdoy.bacillus.Bacillus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = Bacillus.MOD_ID, dist = {Dist.CLIENT})
public class BacillusClientNeoForge {
    public BacillusClientNeoForge() {
        Bacillus.initClient();
    }
}

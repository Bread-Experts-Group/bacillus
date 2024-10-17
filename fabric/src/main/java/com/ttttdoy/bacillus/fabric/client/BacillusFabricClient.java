package com.ttttdoy.bacillus.fabric.client;

import com.ttttdoy.bacillus.Bacillus;
import net.fabricmc.api.ClientModInitializer;

public final class BacillusFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        Bacillus.initClient();
    }
}

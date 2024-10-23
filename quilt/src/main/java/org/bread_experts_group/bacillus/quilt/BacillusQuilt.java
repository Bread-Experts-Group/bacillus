package org.bread_experts_group.bacillus.quilt;

import org.bread_experts_group.bacillus.Bacillus;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public final class BacillusQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer modContainer) {
        Bacillus.init();
    }
}

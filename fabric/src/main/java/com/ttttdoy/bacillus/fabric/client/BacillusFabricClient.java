package com.ttttdoy.bacillus.fabric.client;

import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity;
import com.ttttdoy.bacillus.client.render.BacteriaBlockRenderer;
import com.ttttdoy.bacillus.registry.ModBlockEntityTypes;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class BacillusFabricClient implements ClientModInitializer {
    private final RegistrySupplier<BlockEntityType<BacteriaBlockEntity>> bacteriaBlockEntity = ModBlockEntityTypes.INSTANCE.getBACTERIA_BLOCK_ENTITY();

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(bacteriaBlockEntity.get(), BacteriaBlockRenderer::new);
    }
}

package org.bread_experts_group.bacillus.fabric.client;

import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.bread_experts_group.bacillus.Bacillus;
import org.bread_experts_group.bacillus.block.entity.BacteriaBlockEntity;
import org.bread_experts_group.bacillus.client.render.BacteriaBlockRenderer;
import org.bread_experts_group.bacillus.registry.ModBlockEntityTypes;

public final class BacillusFabricClient implements ClientModInitializer {
    private final RegistrySupplier<BlockEntityType<BacteriaBlockEntity>> bacteriaBlockEntity = ModBlockEntityTypes.INSTANCE.getBACTERIA_BLOCK_ENTITY();

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(bacteriaBlockEntity.get(), BacteriaBlockRenderer::new);
        Bacillus.initClient();
    }
}

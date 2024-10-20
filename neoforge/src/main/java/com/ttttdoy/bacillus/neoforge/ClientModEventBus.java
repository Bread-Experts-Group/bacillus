package com.ttttdoy.bacillus.neoforge;

import com.ttttdoy.bacillus.Bacillus;
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity;
import com.ttttdoy.bacillus.client.render.BacteriaBlockRenderer;
import com.ttttdoy.bacillus.registry.ModBlockEntityTypes;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Bacillus.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientModEventBus {
    private static final RegistrySupplier<BlockEntityType<BacteriaBlockEntity>> bacteriaBlockEntity = ModBlockEntityTypes.INSTANCE.getBACTERIA_BLOCK_ENTITY();

    @SubscribeEvent
    public static void registerBlockEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(bacteriaBlockEntity.get(), BacteriaBlockRenderer::new);
    }
}
package org.bread_experts_group.bacillus.neoforge

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.client.render.BacteriaBlockRenderer
import org.bread_experts_group.bacillus.client.screen.FilterScreen
import org.bread_experts_group.bacillus.registry.ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY
import org.bread_experts_group.bacillus.registry.ModMenuTypes

@EventBusSubscriber(modid = Bacillus.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ClientModEventBus {
    private val bacteriaBlockEntity = BACTERIA_BLOCK_ENTITY

    @SubscribeEvent
    fun registerBlockEntityRenderers(event: RegisterRenderers) {
        event.registerBlockEntityRenderer(
            bacteriaBlockEntity.get()
        ) { context: BlockEntityRendererProvider.Context -> BacteriaBlockRenderer(context) }
    }

    @SubscribeEvent
    fun onClientSetup(event: RegisterMenuScreensEvent) {
        event.register(ModMenuTypes.FILTER.get()) { menu, inventory, title ->
            FilterScreen(
                menu,
                inventory,
                title
            )
        }
    }
}
package org.bread_experts_group.bacillus.fabric.client

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import org.bread_experts_group.bacillus.Bacillus.initClient
import org.bread_experts_group.bacillus.client.render.BacteriaBlockRenderer
import org.bread_experts_group.bacillus.client.screen.FilterScreen
import org.bread_experts_group.bacillus.registry.ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY
import org.bread_experts_group.bacillus.registry.ModMenuTypes

class BacillusFabricClient : ClientModInitializer {
    private val bacteriaBlockEntity = BACTERIA_BLOCK_ENTITY

    override fun onInitializeClient() {
        BlockEntityRenderers.register(
            bacteriaBlockEntity.get()
        ) { context: BlockEntityRendererProvider.Context -> BacteriaBlockRenderer(context) }

        MenuScreens.register(ModMenuTypes.FILTER.get()) { menu, inventory, title ->
            FilterScreen(
                menu,
                inventory,
                title
            )
        }

        initClient()
    }
}

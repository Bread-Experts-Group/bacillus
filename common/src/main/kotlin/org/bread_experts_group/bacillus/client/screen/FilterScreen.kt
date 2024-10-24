package org.bread_experts_group.bacillus.client.screen

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import org.bread_experts_group.bacillus.Bacillus.modLocation
import org.bread_experts_group.bacillus.menu.FilterMenu

class FilterScreen(
    menu: FilterMenu,
    inventory: Inventory,
    title: Component
) : AbstractContainerScreen<FilterMenu>(menu, inventory, title) {
    val texture = modLocation("gui", "container", "filter.png")

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        guiGraphics.blit(RenderType::guiTextured, texture, leftPos, topPos, 0f, 0f, imageWidth, imageHeight, 256, 256)
    }
}
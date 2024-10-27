package org.bread_experts_group.bacillus.client.screen

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import org.apache.logging.log4j.LogManager

class FakeSlot(x: Int, y: Int, private val id: Int) : AbstractWidget(x, y, 16, 16, Component.empty()) {
    val minecraft: Minecraft = Minecraft.getInstance()
    var heldItem: ItemStack = ItemStack.EMPTY

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val playerCarried = minecraft.player?.containerMenu?.carried ?: return
        if (this.isHovered) AbstractContainerScreen.renderSlotHighlight(guiGraphics, x, y, 0)
        if (heldItem == ItemStack.EMPTY) return
        if (this.isHovered && playerCarried.isEmpty) {
            guiGraphics.renderTooltip(
                minecraft.font,
                buildList {
                    add(
                        Component.literal(heldItem.hoverName.string)
                            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC).visualOrderText
                    )
                },
                mouseX, mouseY
            )
        }
        guiGraphics.renderFakeItem(heldItem, x, y)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val playerCarried = minecraft.player?.containerMenu?.carried ?: return false
        if (!this.isActive && !this.visible) return false
        return if (this.isHovered && !playerCarried.isEmpty && isValidClickButton(button) && heldItem == ItemStack.EMPTY) {
            return if (this.clicked(mouseX, mouseY)) {
                onClick(mouseX, mouseY)
                val stack = ItemStack(playerCarried.item)
                heldItem = stack
                LogManager.getLogger().info("setting slot to $stack")
                true
            } else false
        } else if (this.isHovered && button == 1) {
            LogManager.getLogger().info("clearing slot")
            heldItem = ItemStack.EMPTY
            true
        } else false
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput)
    }
}
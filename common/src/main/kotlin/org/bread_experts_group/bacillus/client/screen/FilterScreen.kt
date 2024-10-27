package org.bread_experts_group.bacillus.client.screen

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import org.bread_experts_group.bacillus.Bacillus.modLocation
import org.bread_experts_group.bacillus.item.FilterContents
import org.bread_experts_group.bacillus.menu.FilterMenu
import org.bread_experts_group.bacillus.registry.ModDataComponents

class FilterScreen(
    menu: FilterMenu,
    private val inventory: Inventory,
    title: Component
) : AbstractContainerScreen<FilterMenu>(menu, inventory, title) {
    private val texture = modLocation("gui", "container", "filter.png")
    private val fakeSlots: MutableList<Pair<Int, FakeSlot>> = mutableListOf()

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, texture)

        guiGraphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight)
    }

    private fun getFilterStack(): ItemStack {
        val player = minecraft?.player!!
        return player.mainHandItem
//        val filtered = menu.items.filter { it.hoverName == ModItems.FILTER.get().defaultInstance.hoverName }
//        return filtered[0]
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun onClose() {
        val contents =
            buildList { fakeSlots.forEach { (_, slot) -> if (slot.heldItem != ItemStack.EMPTY) add(slot.heldItem) } }.toMutableList()
        getFilterStack().set(ModDataComponents.FILTER_CONTENTS.get(), FilterContents(contents))
        super.onClose()
    }

    private var id = 0

    // todo resizing the gui causes the items to disappear until reopening the gui
    // todo  having multiple filters confuses the game and "shares" the data between multiple filters,
    //  reloading the world causes the original filter to be "overwritten" until removing the other filters
    // todo opening the gui after resizing the game window somehow duplicates the contents, investigate init function (might be resolved?)

    override fun init() {
        super.init()
        val filter = getFilterStack()
        val filterContents = filter.get(ModDataComponents.FILTER_CONTENTS.get()) ?: FilterContents.EMPTY
//        if (filterContents.items.size != 24) {
//            repeat(24 - filterContents.items.size) { filterContents.items.add(ItemStack.EMPTY) }
//        }
        repeat(3) { y ->
            repeat(8) { x ->
                val fakeSlot = FakeSlot(leftPos + 8 + x * 18, topPos + 17 + y * 18, id)
//                fakeSlot.heldItem = filterContents[id]
                addRenderableWidget(fakeSlot)
                fakeSlots.add(id to fakeSlot)
                id++
            }
        }
        id = 0
        repeat(filterContents.items.size) { id ->
            fakeSlots[id].second.heldItem = filterContents.items[id]
            this.id++
        }
        id = 0
    }
}
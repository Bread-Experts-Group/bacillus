package org.bread_experts_group.bacillus.menu

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import org.bread_experts_group.bacillus.registry.ModItems
import org.bread_experts_group.bacillus.registry.ModMenuTypes

class FilterMenu(id: Int, inventory: Inventory) : AbstractContainerMenu(ModMenuTypes.FILTER.get(), id) {

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun stillValid(player: Player): Boolean = player.isHolding(ModItems.FILTER.get())

    init {
        repeat(9) { addSlot(Slot(inventory, it, 8 + it * 18, 142)) }
        repeat(3) { y -> repeat(9) { x -> addSlot(Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18)) } }
    }
}
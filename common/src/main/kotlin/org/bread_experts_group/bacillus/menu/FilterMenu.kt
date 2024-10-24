package org.bread_experts_group.bacillus.menu

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import org.bread_experts_group.bacillus.registry.ModItems
import org.bread_experts_group.bacillus.registry.ModMenuTypes

class FilterMenu(id: Int, inventory: Inventory) : AbstractContainerMenu(ModMenuTypes.FILTER.get(), id) {

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun stillValid(player: Player): Boolean = player.isHolding(ModItems.FILTER.get())
}
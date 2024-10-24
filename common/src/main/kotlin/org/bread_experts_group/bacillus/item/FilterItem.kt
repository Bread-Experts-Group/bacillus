package org.bread_experts_group.bacillus.item

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.menu.FilterMenu
import org.bread_experts_group.bacillus.util.General.setId

class FilterItem : Item(Properties().setId("filter")), MenuProvider {
    override fun createMenu(id: Int, inventory: Inventory, player: Player): AbstractContainerMenu =
        FilterMenu(id, inventory)

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(usedHand)
        if (player is ServerPlayer) player.openMenu(this)
        return InteractionResult.SUCCESS
    }

    override fun getDisplayName(): Component = Component.translatable("${Bacillus.MOD_ID}.container.filter")

}

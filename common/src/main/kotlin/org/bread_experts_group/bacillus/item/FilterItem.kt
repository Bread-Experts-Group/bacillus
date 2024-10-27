package org.bread_experts_group.bacillus.item

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.menu.FilterMenu
import org.bread_experts_group.bacillus.registry.ModDataComponents

class FilterItem : Item(
    Properties().component(ModDataComponents.FILTER_CONTENTS.get(), FilterContents.EMPTY)
), MenuProvider {
    override fun createMenu(id: Int, inventory: Inventory, player: Player): AbstractContainerMenu =
        FilterMenu(id, inventory)

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (level !is ServerLevel) return InteractionResultHolder.fail(player.getItemInHand(usedHand))
        val stack = player.getItemInHand(usedHand)
        if (player is ServerPlayer) player.openMenu(this)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    override fun getDisplayName(): Component = Component.translatable("${Bacillus.MOD_ID}.container.filter")
}

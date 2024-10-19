package com.ttttdoy.bacillus.item

import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * Used in "pausing" and "killing" bacteria in the world.
 *
 * - When held in the main hand: pauses active bacteria
 * - When held in off-hand: kills nearby bacteria
 */
class Jammer : Item(Properties()) {
    override fun use(level: Level, player: Player, interactionHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(interactionHand))
        if (interactionHand == InteractionHand.MAIN_HAND) BacteriaBlockEntity.globalJamState = !BacteriaBlockEntity.globalJamState
        else BacteriaBlockEntity.globalKillState = !BacteriaBlockEntity.globalKillState
        player.sendSystemMessage(Component.literal("Jamming: ${BacteriaBlockEntity.globalJamState}, Killing: ${BacteriaBlockEntity.globalKillState}"))
        return InteractionResultHolder.success<ItemStack>(player.getItemInHand(interactionHand))
    }
}

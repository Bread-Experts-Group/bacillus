package com.ttttdoy.bacillus.item

import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level

/**
 * Used in "pausing" and "killing" bacteria in the world.
 *
 * - When held in the main hand: pauses active bacteria
 * - When held in off-hand: kills nearby bacteria
 */
class Jammer : Item(Properties().stacksTo(1).rarity(Rarity.UNCOMMON)) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(interactionHand))
        if (interactionHand == InteractionHand.MAIN_HAND) BacteriaBlockEntity.GLOBAL_JAM_STATE =
            !BacteriaBlockEntity.GLOBAL_JAM_STATE
        else BacteriaBlockEntity.GLOBAL_KILL_STATE = !BacteriaBlockEntity.GLOBAL_KILL_STATE
        player.sendSystemMessage(
            Component.literal("Jamming: ${BacteriaBlockEntity.GLOBAL_JAM_STATE}, Killing: ${BacteriaBlockEntity.GLOBAL_KILL_STATE}")
        )
        return InteractionResultHolder.success(player.getItemInHand(interactionHand))
    }
}

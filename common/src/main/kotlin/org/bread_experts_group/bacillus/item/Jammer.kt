package org.bread_experts_group.bacillus.item

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level
import org.bread_experts_group.bacillus.block.entity.BacteriaBlockEntity

/**
 * Used in "pausing" and "killing" bacteria in the world.
 *
 * - When held in the main hand: pauses active bacteria
 * - When held in off-hand: kills nearby bacteria
 */
class Jammer : Item(Properties().stacksTo(1).rarity(Rarity.RARE)) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(interactionHand))
        if (interactionHand == InteractionHand.MAIN_HAND) {
            BacteriaBlockEntity.Companion.globalJamState = !BacteriaBlockEntity.Companion.globalJamState
        } else {
            BacteriaBlockEntity.Companion.globalKillState = !BacteriaBlockEntity.Companion.globalKillState
        }
        player.displayClientMessage(
            Component.literal("Jamming: ${BacteriaBlockEntity.Companion.globalJamState}, Killing: ${BacteriaBlockEntity.Companion.globalKillState}"),
            true
        )
        return InteractionResultHolder.success(player.getItemInHand(interactionHand))
    }
}

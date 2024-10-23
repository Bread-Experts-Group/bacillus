package org.bread_experts_group.bacillus.item

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.block.entity.BacteriaBlockEntity

/**
 * Used in "pausing" and "killing" bacteria in the world.
 *
 * - When held in the main hand: pauses active bacteria
 * - When held in off-hand: kills nearby bacteria
 */
class Jammer : Item(
    Properties().stacksTo(1).rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, Bacillus.modLocation("jammer")))
) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResult {
        if (level.isClientSide) return InteractionResult.PASS
        if (interactionHand == InteractionHand.MAIN_HAND) {
            BacteriaBlockEntity.Companion.globalJamState = !BacteriaBlockEntity.Companion.globalJamState
        } else {
            BacteriaBlockEntity.Companion.globalKillState = !BacteriaBlockEntity.Companion.globalKillState
        }
        player.displayClientMessage(
            Component.literal("Jamming: ${BacteriaBlockEntity.Companion.globalJamState}, Killing: ${BacteriaBlockEntity.Companion.globalKillState}"),
            true
        )
        return InteractionResult.SUCCESS
    }
}

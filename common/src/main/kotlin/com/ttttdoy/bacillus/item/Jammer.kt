package com.ttttdoy.bacillus.item

import com.ttttdoy.bacillus.Bacillus.modLocation
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level

/**
 * Used in "pausing" and "killing" bacteria in the world.
 *
 * - When held in the main hand: pauses active bacteria
 * - When held in off-hand: kills nearby bacteria
 */
class Jammer : Item(
    Properties().stacksTo(1).rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, modLocation("jammer")))
) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResult {
        if (level.isClientSide) return InteractionResult.PASS
        if (interactionHand == InteractionHand.MAIN_HAND) {
            BacteriaBlockEntity.globalJamState = !BacteriaBlockEntity.globalJamState
        } else {
            BacteriaBlockEntity.globalKillState = !BacteriaBlockEntity.globalKillState
        }
        player.displayClientMessage(
            Component.literal("Jamming: ${BacteriaBlockEntity.globalJamState}, Killing: ${BacteriaBlockEntity.globalKillState}"),
            true
        )
        return InteractionResult.SUCCESS
    }
}

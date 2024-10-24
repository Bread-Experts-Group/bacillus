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
import org.bread_experts_group.bacillus.registry.ModDataComponents

/**
 * Used in "pausing" and "killing" bacteria in the world.
 *
 * - When held in the main hand: pauses active bacteria
 * - When held in off-hand: kills nearby bacteria
 */
class Jammer : Item(
    Properties()
        .stacksTo(1)
        .rarity(Rarity.RARE)
        // todo figure out how to implement this into the jamming/killing logic
        //  (side quest, make it work with the item properties in Registry)
        .component(ModDataComponents.JAMMING.get(), false)
        .component(ModDataComponents.KILLING.get(), false)
) {
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(interactionHand)
        if (level.isClientSide) return InteractionResultHolder.pass(stack)
        if (interactionHand == InteractionHand.MAIN_HAND) {
            if (stack.get(ModDataComponents.JAMMING.get())!!) stack.set(
                ModDataComponents.JAMMING.get(),
                true
            ) else stack.set(ModDataComponents.JAMMING.get(), false)
            BacteriaBlockEntity.globalJamState = !BacteriaBlockEntity.globalJamState
        } else {
            if (stack.get(ModDataComponents.KILLING.get())!!) stack.set(
                ModDataComponents.KILLING.get(),
                true
            ) else stack.set(ModDataComponents.KILLING.get(), false)
            BacteriaBlockEntity.globalKillState = !BacteriaBlockEntity.globalKillState
        }
        player.displayClientMessage(
            Component.literal("Jamming: ${BacteriaBlockEntity.globalJamState}, Killing: ${BacteriaBlockEntity.globalKillState}"),
            true
        )
        return InteractionResultHolder.consume(player.getItemInHand(interactionHand))
    }
}

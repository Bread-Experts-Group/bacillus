package org.bread_experts_group.bacillus.item

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block
import org.apache.logging.log4j.LogManager
import org.bread_experts_group.bacillus.Bacillus

class DestroyerItem(block: Block) :
    BlockItem(
        block,
        Properties().setId(ResourceKey.create(Registries.ITEM, Bacillus.modLocation("destroyer"))).useBlockDescriptionPrefix()
    ) {
    val logger = LogManager.getLogger()

    override fun overrideOtherStackedOnMe(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        action: ClickAction,
        player: Player,
        access: SlotAccess
    ): Boolean =
        if (action == ClickAction.SECONDARY && slot.allowModification(player) && !other.isEmpty) {
            logger.info(other)
            other.shrink(other.count)
            playTrashSound(player)
            true
        } else false

    override fun overrideStackedOnOther(stack: ItemStack, slot: Slot, action: ClickAction, player: Player): Boolean {
        val item = slot.item

        return if (action == ClickAction.SECONDARY && !item.isEmpty) {
            item.shrink(item.count)
            playTrashSound(player)
            true
        } else false
    }

    private fun playTrashSound(player: Player) {
        if (player.level().isClientSide) {
            val instance = Minecraft.getInstance()
            (instance.player ?: return).playSound(SoundEvents.CHORUS_FLOWER_GROW, 1f, 1f)
        }
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltipComponents.addAll(
            listOf(
                Bacillus.modTranslatable("item", "destroyer", "tooltip_1").withStyle(ChatFormatting.GOLD),
                Bacillus.modTranslatable("item", "destroyer", "tooltip_2"),
                Bacillus.modTranslatable("item", "destroyer", "tooltip_3")
            )
        )
    }
}
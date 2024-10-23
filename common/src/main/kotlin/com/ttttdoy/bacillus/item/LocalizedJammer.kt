package com.ttttdoy.bacillus.item

import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacillus.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB

class LocalizedJammer : Item(Properties().stacksTo(1).rarity(Rarity.UNCOMMON)) {
    private var jammedAmount = 0
    private var alreadyJammedAmount = 0

    override fun use(
        level: Level,
        player: Player,
        usedHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(usedHand))
        if (usedHand == InteractionHand.MAIN_HAND) {
            val aabb = AABB(player.blockPosition()).inflate(20.0)
            BlockPos.betweenClosedStream(aabb).forEach { pos ->
                if (level.getBlockState(pos).`is`(ModBlocks.DESTROYER.get().block)
                    || level.getBlockState(pos).`is`(ModBlocks.REPLACER.get().block)
                ) {
                    val entity = level.getBlockEntity(pos) as BacteriaBlockEntity
                    if (entity.active != -1) {
                        entity.active = -1
                        jammedAmount++
                    } else alreadyJammedAmount++
                }
            }
            player.displayClientMessage(
                Component.literal("Jammed $jammedAmount bacteria | $alreadyJammedAmount already jammed"), true
            )
            jammedAmount = 0
            alreadyJammedAmount = 0
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand))
    }
}
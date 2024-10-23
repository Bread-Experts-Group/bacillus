package org.bread_experts_group.bacillus.item

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.block.entity.BacteriaBlockEntity
import org.bread_experts_group.bacillus.registry.ModBlocks

class LocalizedJammer : Item(
    Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
        .setId(ResourceKey.create(Registries.ITEM, Bacillus.modLocation("localized_jammer")))
) {
    private var jammedAmount = 0
    private var alreadyJammedAmount = 0

    override fun use(
        level: Level,
        player: Player,
        usedHand: InteractionHand
    ): InteractionResult {
        if (level.isClientSide) return InteractionResult.PASS
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
        return InteractionResult.SUCCESS
    }
}
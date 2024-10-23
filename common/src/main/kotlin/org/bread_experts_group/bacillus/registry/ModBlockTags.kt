package org.bread_experts_group.bacillus.registry

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import org.bread_experts_group.bacillus.Bacillus

object ModBlockTags {
    /**
     * Blocks that can't be filtered for in bacteria (replacer/destroyer).
     * @since 1.0.0
     */
    val UNFILTERABLE: TagKey<Block> =
        TagKey.create(Registries.BLOCK, Bacillus.modLocation("unfilterable"))

    /**
     * Blocks that can't be replaced by replacer bacteria.
     * @since 1.0.0
     */
    val UNREPLACEABLE: TagKey<Block> =
        TagKey.create(Registries.BLOCK, Bacillus.modLocation("unreplaceable"))

    /**
     * Blocks that can't be destroyed by destroyer bacteria.
     * @since 1.0.0
     */
    val UNREMOVABLE: TagKey<Block> =
        TagKey.create(Registries.BLOCK, Bacillus.modLocation("unremovable"))
}

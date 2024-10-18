package com.ttttdoy.bacillus.registry

import com.ttttdoy.bacillus.Bacillus
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

object ModBlockTags {
    /**
     * Blocks that can't be filtered for in bacteria (replacer/destroyer).
     * @since 1.0.0
     */
    val UNFILTERABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacillus.MOD_ID, "unfilterable"))

    /**
     * Blocks that can't be replaced by replacer bacteria.
     * @since 1.0.0
     */
    val UNREPLACEABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacillus.MOD_ID, "unreplaceable"))

    /**
     * Blocks that can't be destroyed by destroyer bacteria.
     * @since 1.0.0
     */
    val UNREMOVABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacillus.MOD_ID, "unremovable"))
}

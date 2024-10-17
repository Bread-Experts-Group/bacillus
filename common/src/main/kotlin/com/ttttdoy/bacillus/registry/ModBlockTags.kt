package com.ttttdoy.bacillus.registry

import com.ttttdoy.bacillus.Bacillus
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

object ModBlockTags {
    /**
     * Marks blocks as unbreakable to destroyer/replacer bacteria
     */
    val UNBREAKABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacillus.MOD_ID, "unbreakable"))
    /**
     * Marks blocks as unreplaceable to replacer bacteria.
     */
    val UNREPLACEABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacillus.MOD_ID, "unreplaceable"))
}

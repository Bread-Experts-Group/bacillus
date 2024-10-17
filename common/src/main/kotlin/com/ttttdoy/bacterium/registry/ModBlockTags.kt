package com.ttttdoy.bacterium.registry

import com.ttttdoy.bacterium.Bacterium
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

object ModBlockTags {
    val unbreakable = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacterium.MOD_ID, "unbreakable"))
    val unplaceable = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacterium.MOD_ID, "unplaceable"))
}

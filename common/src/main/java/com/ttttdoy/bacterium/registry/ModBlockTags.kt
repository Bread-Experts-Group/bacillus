package com.ttttdoy.bacterium.registry

import com.ttttdoy.bacterium.Bacterium
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object ModBlockTags {
    val unbreakable = TagKey.create<Block>(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacterium.MOD_ID, "unbreakable"))
    val unplaceable = TagKey.create<Block>(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacterium.MOD_ID, "unplaceable"))
}

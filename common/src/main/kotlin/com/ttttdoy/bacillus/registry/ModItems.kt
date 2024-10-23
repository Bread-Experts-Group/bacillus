package com.ttttdoy.bacillus.registry

import com.ttttdoy.bacillus.Bacillus
import com.ttttdoy.bacillus.item.Jammer
import com.ttttdoy.bacillus.item.LocalizedJammer
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

@Suppress("unused")
object ModItems {
    val ITEM_REGISTRY: DeferredRegister<Item> = DeferredRegister.create(Bacillus.MOD_ID, Registries.ITEM)

    // todo helper book goes here (if patchouli keeps being a pain and not working, maybe the folder needs to be named patchouli_book instead of patchouli_books?)

    // todo work on filter item concept (gui and item texture)

    val JAMMER: RegistrySupplier<Item> = ITEM_REGISTRY.register("jammer") { Jammer() }
    val LOCALIZED_JAMMER: RegistrySupplier<Item> = ITEM_REGISTRY.register("localized_jammer") { LocalizedJammer() }
    val COMPACTED_MUST: RegistrySupplier<Item> = ITEM_REGISTRY.register("compacted_must") { Item(Item.Properties()) }
}
package com.ttttdoy.bacillus.registry

import com.ttttdoy.bacillus.Bacillus
import com.ttttdoy.bacillus.item.Jammer
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

@Suppress("unused")
object ModItems {
    val ITEM_REGISTRY: DeferredRegister<Item> = DeferredRegister.create(Bacillus.MOD_ID, Registries.ITEM)

    // helper book goes here

    val JAMMER: RegistrySupplier<Item> = ITEM_REGISTRY.register("jammer") { Jammer() }
    val COMPACTED_MUST: RegistrySupplier<Item> = ITEM_REGISTRY.register("compacted_must") { Item(Item.Properties()) }
}
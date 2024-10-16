package com.ttttdoy.bacterium.registry

import com.ttttdoy.bacterium.Bacterium
import com.ttttdoy.bacterium.item.Jammer
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

@Suppress("unused")
object ModItems {
    val ITEM_REGISTRY: DeferredRegister<Item> = DeferredRegister.create<Item?>(Bacterium.MOD_ID, Registries.ITEM)

    val JAMMER: RegistrySupplier<Item> = ITEM_REGISTRY.register<Item?>("jammer") { Jammer() }
    val COMPACTED_MUST: RegistrySupplier<Item> = ITEM_REGISTRY.register<Item>("compacted_must") { Item(Item.Properties()) }
}

package com.ttttdoy.bacterium.registry

import com.ttttdoy.bacterium.Bacterium
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab

@Suppress("unused")
object ModCreativeTab {
    val CREATIVE_TAB_REGISTRY = DeferredRegister.create(Bacterium.MOD_ID, Registries.CREATIVE_MODE_TAB)

    val BACTERIUM_TAB: RegistrySupplier<CreativeModeTab> = CREATIVE_TAB_REGISTRY.register("main") {
        CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable(Bacterium.MOD_ID + ".itemGroup"))
            .icon { ModBlocks.DESTROYER.get().asItem().defaultInstance }
            .displayItems { _, output ->
                ModItems.ITEM_REGISTRY.forEach { itemSupplier -> output.accept(itemSupplier.get()) }
            }.build()
    }
}

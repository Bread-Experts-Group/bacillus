package com.ttttdoy.bacterium.registry

import com.ttttdoy.bacterium.Bacterium
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import java.util.function.Supplier

@Suppress("unused")
object ModCreativeTab {
    val CREATIVE_TAB_REGISTRY = DeferredRegister.create<CreativeModeTab>(Bacterium.MOD_ID, Registries.CREATIVE_MODE_TAB)

    val BACTERIUM_TAB: RegistrySupplier<CreativeModeTab> = CREATIVE_TAB_REGISTRY.register<CreativeModeTab>("main") {
        CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable(Bacterium.MOD_ID + ".itemGroup"))
            .icon(Supplier { ModBlocks.DESTROYER.get().asItem().defaultInstance })
            .displayItems { itemDisplayParameters, output ->
                ModItems.ITEM_REGISTRY.forEach { itemSupplier -> output.accept(itemSupplier.get()) }
            }.build()
    }
}

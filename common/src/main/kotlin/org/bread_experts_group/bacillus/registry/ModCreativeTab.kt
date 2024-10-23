package org.bread_experts_group.bacillus.registry

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import org.bread_experts_group.bacillus.Bacillus

@Suppress("unused")
object ModCreativeTab {
    val CREATIVE_TAB_REGISTRY = DeferredRegister.create(Bacillus.MOD_ID, Registries.CREATIVE_MODE_TAB)

    val BACILLUS_TAB: RegistrySupplier<CreativeModeTab> = CREATIVE_TAB_REGISTRY.register("main") {
        CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable(Bacillus.MOD_ID + ".itemGroup"))
            .icon { ModBlocks.DESTROYER.get().asItem().defaultInstance }
            .displayItems { _, output ->
                ModItems.ITEM_REGISTRY.forEach { itemSupplier -> output.accept(itemSupplier.get()) }
            }.build()
    }
}

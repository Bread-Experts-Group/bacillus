package org.bread_experts_group.bacillus.registry

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.menu.FilterMenu

object ModMenuTypes {
    val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> = DeferredRegister.create(Bacillus.MOD_ID, Registries.MENU)

    val FILTER: RegistrySupplier<MenuType<FilterMenu>> = MENU_TYPE_REGISTRY.register("filter_menu") {
        MenuType({ id, inventory -> FilterMenu(id, inventory) }, FeatureFlags.VANILLA_SET)
    }
}
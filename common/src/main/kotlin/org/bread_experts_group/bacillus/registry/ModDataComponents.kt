package org.bread_experts_group.bacillus.registry

import com.mojang.serialization.Codec
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.item.FilterContents

object ModDataComponents {
    val DATA_COMPONENT_REGISTRY: DeferredRegister<DataComponentType<*>> =
        DeferredRegister.create(Bacillus.MOD_ID, Registries.DATA_COMPONENT_TYPE)

    val KILLING: RegistrySupplier<DataComponentType<Boolean>> = DATA_COMPONENT_REGISTRY.register("killing") {
        DataComponentType.builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
    }

    val JAMMING: RegistrySupplier<DataComponentType<Boolean>> = DATA_COMPONENT_REGISTRY.register("jamming") {
        DataComponentType.builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
    }

    /**
     * Used for storing and accessing the filter item's contents
     */
    val FILTER_CONTENTS: RegistrySupplier<DataComponentType<FilterContents>> =
        DATA_COMPONENT_REGISTRY.register("filter_contents") {
            DataComponentType.builder<FilterContents>()
                .persistent(FilterContents.CODEC)
                .networkSynchronized(FilterContents.STREAM_CODEC)
                .cacheEncoding()
                .build()
        }
}
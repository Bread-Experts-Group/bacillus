package com.ttttdoy.bacillus.registry

import com.ttttdoy.bacillus.Bacillus
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

object ModBlockEntityTypes {
    val BLOCK_ENTITY_TYPE_REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Bacillus.MOD_ID, Registries.BLOCK_ENTITY_TYPE)

    val BACTERIA_BLOCK_ENTITY: RegistrySupplier<BlockEntityType<BacteriaBlockEntity>> =
        BLOCK_ENTITY_TYPE_REGISTRY.register("bacteria_entity") {
            BlockEntityType(
                { blockPos, blockState -> BacteriaBlockEntity(blockPos, blockState) },
                setOf(ModBlocks.REPLACER.get().block, ModBlocks.DESTROYER.get().block)
            )
        }
}

package com.ttttdoy.bacterium.registry

import com.ttttdoy.bacterium.Bacterium
import com.ttttdoy.bacterium.block.BacteriaBlock
import com.ttttdoy.bacterium.block.MustBlock
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

@Suppress("unused")
object ModBlocks {
    val BLOCK_REGISTRY: DeferredRegister<Block> = DeferredRegister.create<Block>(Bacterium.MOD_ID, Registries.BLOCK)

    val REPLACER: RegistrySupplier<BlockItem> = registerBlockItem<BacteriaBlock>("replacer", { BacteriaBlock() }, Item.Properties())
    val DESTROYER: RegistrySupplier<BlockItem> = registerBlockItem<BacteriaBlock>("destroyer", { BacteriaBlock() }, Item.Properties())

    val MUST: RegistrySupplier<BlockItem> = registerBlockItem<MustBlock>("must", { MustBlock() }, Item.Properties())

    private fun <T : Block> registerBlockItem(
        id: String, block: Supplier<T>, properties: Item.Properties
    ): RegistrySupplier<BlockItem> {
        val blockSupplier: RegistrySupplier<Block> = BLOCK_REGISTRY.register<Block>(id, block)
        return ModItems.ITEM_REGISTRY.register<BlockItem>(id) { BlockItem(blockSupplier.get(), properties) }
    }
}

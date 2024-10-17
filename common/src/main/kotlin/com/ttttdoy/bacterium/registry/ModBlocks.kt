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
    val BLOCK_REGISTRY: DeferredRegister<Block> = DeferredRegister.create(Bacterium.MOD_ID, Registries.BLOCK)

    /**
     * @see BacteriaBlock
     */
    val REPLACER: RegistrySupplier<BlockItem> = registerBlockItem("replacer", { BacteriaBlock() }, Item.Properties())
    /**
     * @see BacteriaBlock
     */
    val DESTROYER: RegistrySupplier<BlockItem> = registerBlockItem("destroyer", { BacteriaBlock() }, Item.Properties())

    /**
     * @see MustBlock
     */
    val MUST: RegistrySupplier<BlockItem> = registerBlockItem("must", { MustBlock() }, Item.Properties())

    private fun <T : Block> registerBlockItem(
        id: String, block: Supplier<T>, properties: Item.Properties
    ): RegistrySupplier<BlockItem> {
        val blockSupplier: RegistrySupplier<Block> = BLOCK_REGISTRY.register(id, block)
        return ModItems.ITEM_REGISTRY.register(id) { BlockItem(blockSupplier.get(), properties) }
    }
}

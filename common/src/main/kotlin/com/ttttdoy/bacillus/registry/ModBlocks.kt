package com.ttttdoy.bacillus.registry

import com.ttttdoy.bacillus.Bacillus
import com.ttttdoy.bacillus.block.BacteriaBlock
import com.ttttdoy.bacillus.block.MustBlock
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Supplier

@Suppress("unused")
object ModBlocks {
    val BLOCK_REGISTRY: DeferredRegister<Block> = DeferredRegister.create(Bacillus.MOD_ID, Registries.BLOCK)

    /**
     * @see BacteriaBlock
     */
    val REPLACER: RegistrySupplier<BlockItem> = registerBlockItem("replacer", { BacteriaBlock() }, Item.Properties())
    /**
     * @see BacteriaBlock
     */
    val DESTROYER: RegistrySupplier<BlockItem> = registerBlockItem("destroyer", { BacteriaBlock() }, Item.Properties())

    // todo textures.
    /**
     * Activating a destroyer block with this above it marks every block in the game as destroyable.
     * - Excludes: Bacteria, Air, blocks in the unreplaceable and/or unbreakable tag.
     */
    val EVERYTHING: RegistrySupplier<BlockItem> = registerBlockItem("everything", { Block(BlockBehaviour.Properties.of()) }, Item.Properties())

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

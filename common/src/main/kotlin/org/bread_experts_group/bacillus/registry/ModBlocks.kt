package org.bread_experts_group.bacillus.registry

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.block.BacteriaBlock
import org.bread_experts_group.bacillus.block.MustBlock
import org.bread_experts_group.bacillus.item.DestroyerItem

@Suppress("unused")
object ModBlocks {
    val BLOCK_REGISTRY: DeferredRegister<Block> = DeferredRegister.create(Bacillus.MOD_ID, Registries.BLOCK)

    /**
     * @see BacteriaBlock
     */
    val REPLACER: RegistrySupplier<BlockItem> =
        BLOCK_REGISTRY.registerBlockItem("replacer", { BacteriaBlock() }, Item.Properties())

    /**
     * @see BacteriaBlock
     */
    val DESTROYER: RegistrySupplier<BlockItem> =
        BLOCK_REGISTRY.registerBlockItem("destroyer", { BacteriaBlock() }, { block -> DestroyerItem(block) })

    // todo textures.
    /**
     * Activating a destroyer block with this above, it marks every block in the game as destroyable.
     * - Excludes: Bacteria, Air, blocks in the unreplaceable and/or unbreakable tag.
     */
    val EVERYTHING: RegistrySupplier<BlockItem> =
        BLOCK_REGISTRY.registerBlockItem("everything", { Block(BlockBehaviour.Properties.of()) }, Item.Properties())

    /**
     * @see MustBlock
     */
    val MUST: RegistrySupplier<BlockItem> = BLOCK_REGISTRY.registerBlockItem("must", { MustBlock() }, Item.Properties())

    private fun DeferredRegister<Block>.registerBlockItem(
        id: String,
        block: () -> Block,
        properties: Item.Properties
    ): RegistrySupplier<BlockItem> = this.register(id, block).let { blockSupply ->
        ModItems.ITEM_REGISTRY.register(id) { BlockItem(blockSupply.get(), properties) }
    }

    private fun DeferredRegister<Block>.registerBlockItem(
        id: String,
        block: () -> Block,
        item: (block: Block) -> BlockItem
    ): RegistrySupplier<BlockItem> = this.register(id, block).let { blockSupply ->
        ModItems.ITEM_REGISTRY.register(id) { item(blockSupply.get()) }
    }
}

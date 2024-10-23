package org.bread_experts_group.bacillus.registry

import dev.architectury.registry.item.ItemPropertiesRegistry
import org.apache.logging.log4j.LogManager
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.block.entity.BacteriaBlockEntity

/**
 * Main common registry.
 */
object Registry {
    private val logger = LogManager.getLogger("Bacillus Registry")

    /**
     * Registers all the mod's assets.
     */
    fun registerAll() {
        logger.info("Registering blocks")
        ModBlocks.BLOCK_REGISTRY.register()
        logger.info("Registering items")
        ModItems.ITEM_REGISTRY.register()
        logger.info("Registering block entities")
        ModBlockEntityTypes.BLOCK_ENTITY_TYPE_REGISTRY.register()
        logger.info("Registering creative tabs")
        ModCreativeTab.CREATIVE_TAB_REGISTRY.register()

        registerItemProperties()
    }

    fun registerItemProperties() {
        logger.info("Registering item properties")
        ItemPropertiesRegistry.register(
            ModItems.JAMMER.get(),
            Bacillus.modLocation("killing")
        ) { stack, _, livingEntity, _ ->
            if (livingEntity != null && BacteriaBlockEntity.Companion.globalKillState) 1f else 0f
        }
        ItemPropertiesRegistry.register(
            ModItems.JAMMER.get(),
            Bacillus.modLocation("jamming")
        ) { stack, _, livingEntity, _ ->
            if (livingEntity != null && BacteriaBlockEntity.Companion.globalJamState) 1f else 0f
        }
    }
}

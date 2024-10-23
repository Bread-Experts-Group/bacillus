package com.ttttdoy.bacillus.registry

import com.ttttdoy.bacillus.Bacillus.modLocation
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import dev.architectury.registry.item.ItemPropertiesRegistry
import org.apache.logging.log4j.LogManager

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
            modLocation("killing")
        ) { stack, _, livingEntity, _ ->
            if (livingEntity != null && BacteriaBlockEntity.globalKillState) 1f else 0f
        }
        ItemPropertiesRegistry.register(
            ModItems.JAMMER.get(),
            modLocation("jamming")
        ) { stack, _, livingEntity, _ ->
            if (livingEntity != null && BacteriaBlockEntity.globalJamState) 1f else 0f
        }
    }
}

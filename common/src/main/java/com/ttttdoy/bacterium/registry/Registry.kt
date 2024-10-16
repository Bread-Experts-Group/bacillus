package com.ttttdoy.bacterium.registry

object Registry {
    fun registerAll() {
        ModBlocks.BLOCK_REGISTRY.register()
        ModItems.ITEM_REGISTRY.register()
        ModBlockEntityTypes.BLOCK_ENTITY_TYPE_REGISTRY.register()
        ModCreativeTab.CREATIVE_TAB_REGISTRY.register()
    }
}

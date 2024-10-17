package com.ttttdoy.bacterium.registry

/**
 * Main common registry.
 */
object Registry {
    /**
     * Registers all the mod's assets.
     */
    fun registerAll() {
        ModBlocks.BLOCK_REGISTRY.register()
        ModItems.ITEM_REGISTRY.register()
        ModBlockEntityTypes.BLOCK_ENTITY_TYPE_REGISTRY.register()
        ModCreativeTab.CREATIVE_TAB_REGISTRY.register()

//        ClientLifecycleEvent.CLIENT_STARTED.register { minecraft ->
//            minecraft.blockColors.register(
//                { blockState, level, blockPos, index -> println("You're so fluffy"); Color(Random(204).nextInt(1, 255), 0, 0).rgb },
//                ModBlocks.DESTROYER.get().block, ModBlocks.REPLACER.get().block
//            )
//        } // BERs look like a better solution.
    }
}

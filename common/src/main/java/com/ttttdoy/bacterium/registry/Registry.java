package com.ttttdoy.bacterium.registry;

public class Registry {
    public static void registerAll() {
        ModBlocks.BLOCK_REGISTRY.register();
        ModItems.ITEM_REGISTRY.register();
        ModBlockEntityTypes.BLOCK_ENTITY_TYPE_REGISTRY.register();
        ModCreativeTab.CREATIVE_TAB_REGISTRY.register();
        ModBlockTags.registerTags();
    }
}

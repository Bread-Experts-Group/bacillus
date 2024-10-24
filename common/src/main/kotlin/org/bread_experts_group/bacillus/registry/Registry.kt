package org.bread_experts_group.bacillus.registry

import dev.architectury.event.events.common.LifecycleEvent
import dev.architectury.registry.item.ItemPropertiesRegistry
import dev.architectury.registry.registries.DeferredRegister
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.block.entity.BacteriaBlockEntity

/**
 * Main common registry.
 */
object Registry {
//    private val logger = LogManager.getLogger("Bacillus Registry")

    private val registries: List<DeferredRegister<*>> = listOf(
        ModDataComponents.DATA_COMPONENT_REGISTRY,
        ModMenuTypes.MENU_TYPE_REGISTRY,
        ModBlocks.BLOCK_REGISTRY,
        ModItems.ITEM_REGISTRY,
        ModBlockEntityTypes.BLOCK_ENTITY_TYPE_REGISTRY,
        ModCreativeTab.CREATIVE_TAB_REGISTRY
    )

    /**
     * Registers all the mod's assets.
     */
    fun registerAll() = registries.forEach { it.register() }

    /**
     * Registers all the mod's client assets.
     */
    fun registerAllClient() {
        LifecycleEvent.SETUP.register {
            registerItemProperties()
//            registerMenuScreens()

//            RenderTypeRegistry.register(
//                ModRenderType.solidTextured(Bacillus.modLocation("textures/block/bacteria_blank.png")),
//                ModBlocks.EVERYTHING.get().block
//            )
        }
    }

    // todo figure out how mojang makes ItemProperties sync to other clients
    //  worrying thought, registering this on both platforms instead of here and then it miraculously works
    fun registerItemProperties() {
        ItemPropertiesRegistry.register(
            ModItems.JAMMER.get(),
            Bacillus.modLocation("killing")
        ) { stack, _, livingEntity, _ ->
            if (livingEntity != null && BacteriaBlockEntity.globalKillState) 1f else 0f
        }
        ItemPropertiesRegistry.register(
            ModItems.JAMMER.get(),
            Bacillus.modLocation("jamming")
        ) { stack, _, livingEntity, _ ->
            if (livingEntity != null && BacteriaBlockEntity.globalJamState) 1f else 0f
        }
    }
}

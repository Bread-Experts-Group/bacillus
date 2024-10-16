package com.ttttdoy.bacterium.registry;

import com.ttttdoy.bacterium.Bacterium;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB_REGISTRY =
            DeferredRegister.create(Bacterium.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> BACTERIUM_TAB = CREATIVE_TAB_REGISTRY.register(
            "main",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .title(Component.translatable(Bacterium.MOD_ID + ".itemGroup"))
                    .icon(() -> ModBlocks.DESTROYER_STARTER.get().asItem().getDefaultInstance())
                    .displayItems(((itemDisplayParameters, output) ->
                            ModItems.ITEM_REGISTRY.forEach((itemSupplier -> {
                                Item item = itemSupplier.get();
                                output.accept(item);
                    }))))
                    .build()
    );
}

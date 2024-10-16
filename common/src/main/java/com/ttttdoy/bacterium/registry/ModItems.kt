package com.ttttdoy.bacterium.registry;

import com.ttttdoy.bacterium.Bacterium;
import com.ttttdoy.bacterium.item.Jammer;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class ModItems {
    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(Bacterium.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> JAMMER = ITEM_REGISTRY.register("jammer", Jammer::new);

    public static final RegistrySupplier<Item> COMPACTED_MUST =
            ITEM_REGISTRY.register("compacted_must", () -> new Item(new Item.Properties()));
}

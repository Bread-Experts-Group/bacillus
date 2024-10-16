package com.ttttdoy.bacterium.registry;

import com.ttttdoy.bacterium.Bacterium;
import com.ttttdoy.bacterium.block.entity.BacteriaBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTRY = DeferredRegister.create(Bacterium.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<BacteriaBlockEntity>> BACTERIA_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPE_REGISTRY.register(
                    "bacteria_entity",
                    () -> BlockEntityType.Builder.of(
                            BacteriaBlockEntity::new,
                            ModBlocks.REPLACER.get(), ModBlocks.DESTROYER.get()
                    ).build(null)
            );
}

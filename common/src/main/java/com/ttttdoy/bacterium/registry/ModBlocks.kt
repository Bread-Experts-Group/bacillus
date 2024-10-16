package com.ttttdoy.bacterium.registry;

import com.ttttdoy.bacterium.Bacterium;
import com.ttttdoy.bacterium.block.BacteriaBlock;
import com.ttttdoy.bacterium.block.DestroyerStarter;
import com.ttttdoy.bacterium.block.MustBlock;
import com.ttttdoy.bacterium.block.ReplacerStarter;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static com.ttttdoy.bacterium.registry.ModItems.ITEM_REGISTRY;

@SuppressWarnings("unused")
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(Bacterium.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> REPLACER = BLOCK_REGISTRY.register("replacer", BacteriaBlock::new);

    public static final RegistrySupplier<BlockItem> REPLACER_STARTER = registerBlockItem(
            "replacer_starter",
            ReplacerStarter::new,
            new Item.Properties()
    );

    public static final RegistrySupplier<Block> DESTROYER = BLOCK_REGISTRY.register("destroyer", BacteriaBlock::new);

    public static final RegistrySupplier<BlockItem> DESTROYER_STARTER = registerBlockItem(
            "destroyer_starter",
            DestroyerStarter::new,
            new Item.Properties()
    );

    public static final RegistrySupplier<BlockItem> MUST = registerBlockItem(
            "must",
            MustBlock::new,
            new Item.Properties()
    );

    private static <T extends Block> RegistrySupplier<BlockItem> registerBlockItem (
            String id, Supplier<T> block, Item.Properties properties
    ) {
        final RegistrySupplier<Block> blockSupplier = ModBlocks.BLOCK_REGISTRY.register(id, block);
        return ITEM_REGISTRY.register(id, () -> new BlockItem(blockSupplier.get(), properties));
    }
}

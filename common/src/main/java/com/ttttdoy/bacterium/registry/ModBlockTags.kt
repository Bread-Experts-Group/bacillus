package com.ttttdoy.bacterium.registry;

import com.ttttdoy.bacterium.Bacterium;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static TagKey<Block> unbreakable;
    public static TagKey<Block> unplaceable;

    public static void registerTags() {
        unbreakable = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacterium.MOD_ID, "unbreakable"));
        unplaceable = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Bacterium.MOD_ID, "unplaceable"));
    }
}

package org.bread_experts_group.bacillus.item

import com.mojang.serialization.Codec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

// todo needs an id attached to this for the filter screen to initalize the slots with their right items
//  Alternative would be to just auto sort the filter contents from the first to last slot
//  (though it'll be a pain figuring out how to turn these codecs into one that holds an int and an itemstack)
//  (stream codec can use StreamCodec.composite)

class FilterContents(val items: MutableList<ItemStack>) {
    companion object {
        val EMPTY: FilterContents = FilterContents(mutableListOf())
        val CODEC: Codec<FilterContents> = ItemStack.CODEC.listOf().xmap(
            { items -> FilterContents(items) },
            { contents -> contents.items }
        )
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, FilterContents> = ItemStack.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(
                { items -> FilterContents(items) },
                { contents -> contents.items }
            )
    }
}
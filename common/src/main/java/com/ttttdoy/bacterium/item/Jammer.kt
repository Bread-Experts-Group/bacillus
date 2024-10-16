package com.ttttdoy.bacterium.item;

import com.ttttdoy.bacterium.block.entity.BacteriaBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class Jammer extends Item {
    public Jammer() { super(new Properties()); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BacteriaBlockEntity.setJammed(level);
        return InteractionResultHolder.success(itemStack);
    }
}

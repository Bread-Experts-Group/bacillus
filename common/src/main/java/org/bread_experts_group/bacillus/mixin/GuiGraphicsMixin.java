package org.bread_experts_group.bacillus.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.bread_experts_group.bacillus.Bacillus;
import org.bread_experts_group.bacillus.registry.ModBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
abstract class GuiGraphicsMixin {
    @Shadow
    public abstract void blit(ResourceLocation atlasLocation, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight);

    @Shadow
    @Final
    private PoseStack pose;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private final ResourceLocation bacillus$location = Bacillus.INSTANCE.modLocation(new String[]{"textures", "icon", "trash_can.png"}, false);

    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z")
    )
    private void renderItemDecorations(
            final Font font, final ItemStack stack, final int x, final int y, final String text, final CallbackInfo ci
    ) {
        final RegistrySupplier<BlockItem> destroyerBlock = ModBlocks.INSTANCE.getDESTROYER();
        final BlockItem destroyer = destroyerBlock.get();
        assert minecraft.player != null;
        final InventoryMenu inventoryMenu = minecraft.player.inventoryMenu;
        final ItemStack carried = inventoryMenu.getCarried();

        if (stack.is(destroyer.asItem()) && !carried.isEmpty()) {
            this.pose.pushPose();
            this.pose.translate((float) x, (float) y, 200.0F);
            RenderSystem.enableBlend();
            this.pose.scale(0.50F, 0.50F, 0.50F);
            this.blit(bacillus$location, 0, 0, 0, 0, 0, 16, 16, 16, 16);
            RenderSystem.disableBlend();
            this.pose.popPose();
        }
    }
}

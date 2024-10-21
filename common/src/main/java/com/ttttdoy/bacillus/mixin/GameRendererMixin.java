package com.ttttdoy.bacillus.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.ttttdoy.bacillus.registry.ModRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Map;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {
    @Shadow
    @Final
    private Map<String, ShaderInstance> shaders;

    @Inject(method = "reloadShaders", at = @At("TAIL"))
    private void reloadShaders(final ResourceProvider resourceProvider, final CallbackInfo ci) {
        try {
            final ShaderInstance solidTextureShader = new ShaderInstance(
                    resourceProvider,
                    "rendertype_solid_texture",
                    DefaultVertexFormat.BLOCK
            );
            ModRenderType.INSTANCE.setSolidInstance(solidTextureShader);
            shaders.put(solidTextureShader.getName(), solidTextureShader);
        } catch (final IOException exception) {
            throw new RuntimeException("[BACILLUS] could not reload shaders", exception);
        }
    }
}

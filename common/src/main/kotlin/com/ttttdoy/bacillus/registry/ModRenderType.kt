package com.ttttdoy.bacillus.registry

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.Util
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderStateShard.LightmapStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation

object ModRenderType {
    fun solidTexture(location: ResourceLocation) = SOLID_TEXTURE.apply(location)

    val SOLID_TEXTURE = Util.memoize<ResourceLocation, RenderType> { location: ResourceLocation ->
        val compositeState: RenderType.CompositeState = RenderType.CompositeState.builder()
            .setLightmapState(LightmapStateShard.LIGHTMAP)
            .setShaderState(RenderStateShard.RENDERTYPE_SOLID_SHADER)
            .setTextureState(RenderStateShard.TextureStateShard(location, false, true))
            .createCompositeState(true)
        RenderType.create(
            "solid_texture",
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            4194304,
            true,
            false,
            compositeState
        )
    }
}
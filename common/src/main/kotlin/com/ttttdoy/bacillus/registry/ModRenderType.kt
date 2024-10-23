package com.ttttdoy.bacillus.registry

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.Util
import net.minecraft.client.renderer.RenderStateShard.*
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import java.util.function.Function

object ModRenderType {
//    var solidInstance: ShaderInstance? = null
//    private val solidTextureShader = ShaderStateShard { solidInstance }

//    private val solidTextureRenderType: Function<ResourceLocation, RenderType> =
//        Util.memoize { location: ResourceLocation ->
//            val compositeState: RenderType.CompositeState = RenderType.CompositeState.builder()
//                .setLightmapState(LIGHTMAP)
//                .setCullState(CULL)
//                .setTransparencyState(NO_TRANSPARENCY)
//                .setShaderState(solidTextureShader)
//                .setTextureState(TextureStateShard(location, false, true))
//                .createCompositeState(true)
//            RenderType.create(
//                "solid_texture",
//                DefaultVertexFormat.BLOCK,
//                VertexFormat.Mode.QUADS,
//                4194304,
//                true,
//                false,
//                compositeState
//            )
//        }

    /**
     * Replica of [RenderType.solid] that takes in a [textureLocation]
     */
//    fun solidTextured(textureLocation: ResourceLocation) = solidTextureRenderType.apply(textureLocation)
}
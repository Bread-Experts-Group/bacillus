package com.ttttdoy.bacillus.registry

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.Util
import net.minecraft.client.renderer.RenderStateShard.*
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.resources.ResourceLocation
import java.util.function.Function

object ModRenderType {
    fun solidTextureTest(location: ResourceLocation) = renderTypeSolidTextureTest.apply(location)

    var solidInstance: ShaderInstance? = null
    val testRenderStateShard = ShaderStateShard { solidInstance }

    val renderTypeSolidTextureTest: Function<ResourceLocation, RenderType> = Util.memoize { location: ResourceLocation ->
        val compositeState: RenderType.CompositeState = RenderType.CompositeState.builder()
            .setLightmapState(LIGHTMAP)
            .setShaderState(testRenderStateShard)
            .setTextureState(TextureStateShard(location, false, true))
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

//    val renderTypeTextureTest: Function<ResourceLocation, RenderType> = Util.memoize { resourceLocation: ResourceLocation ->
//        val compositeState = RenderType.CompositeState.builder()
//            .setLightmapState(RenderStateShard.LIGHTMAP)
//            .setShaderState(testRenderStateShard)
//            .setTextureState(TextureStateShard(resourceLocation, false, false))
//            .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
//            .setOverlayState(RenderStateShard.OVERLAY)
//            .createCompositeState(true)
//        RenderType.create(
//            "texture_test",
//            DefaultVertexFormat.NEW_ENTITY,
//            VertexFormat.Mode.QUADS,
//            4194304,
//            true,
//            false,
//            compositeState
//        )
//    }
//
//    val blockVertexFormat = VertexFormat.builder()
//        .add("Position", VertexFormatElement.POSITION)
//        .add("Color", VertexFormatElement.COLOR)
//        .add("UV0", VertexFormatElement.UV0)
//        .add("UV2", VertexFormatElement.UV2)
//        .add("Normal", VertexFormatElement.NORMAL)
//        .padding(1)
//        .build()
}
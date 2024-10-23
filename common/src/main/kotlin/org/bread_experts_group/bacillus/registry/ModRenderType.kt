package org.bread_experts_group.bacillus.registry

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.Util
import net.minecraft.client.renderer.RenderStateShard.*
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShaderDefines
import net.minecraft.client.renderer.ShaderProgram
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.TriState
import org.bread_experts_group.bacillus.Bacillus
import java.util.function.Function

object ModRenderType {
    var solidInstance: ShaderProgram =
        ShaderProgram(
            Bacillus.modLocation("core/rendertype_solid_texture"),
            DefaultVertexFormat.BLOCK,
            ShaderDefines.EMPTY
        )
    private val solidTextureShader = ShaderStateShard(solidInstance)

    private val solidTextureRenderType: Function<ResourceLocation, RenderType> =
        Util.memoize { location: ResourceLocation ->
            val compositeState: RenderType.CompositeState = RenderType.CompositeState.builder()
                .setLightmapState(LIGHTMAP)
//                .setCullState(CULL)
//                .setTransparencyState(NO_TRANSPARENCY)
                .setShaderState(solidTextureShader)
                .setTextureState(TextureStateShard(location, TriState.FALSE, true))
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

    /**
     * Replica of [RenderType.solid] that takes in a [textureLocation]
     */
    fun solidTextured(textureLocation: ResourceLocation) = solidTextureRenderType.apply(textureLocation)
}
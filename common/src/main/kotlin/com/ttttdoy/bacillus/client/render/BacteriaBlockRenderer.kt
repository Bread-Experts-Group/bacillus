package com.ttttdoy.bacillus.client.render

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacillus.registry.ModRenderType
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks

class BacteriaBlockRenderer(
    val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<BacteriaBlockEntity> {
    val instance: Minecraft = Minecraft.getInstance()

    override fun render(
        blockEntity: BacteriaBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val texture = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/diamond_ore.png")

//        val model = context.blockRenderDispatcher.getBlockModel(Blocks.OAK_FENCE_GATE.defaultBlockState())

//        context.blockRenderDispatcher.modelRenderer.tesselateBlock() // ???????

        // ModelBlockRenderer#renderModelFaceFlat (this has calls inside that make up the shape of the model, could be what we're looking for?)

        instance.blockRenderer.modelRenderer.renderModel(
            poseStack.last(),
            bufferSource.getBuffer(RenderType.solid()),
            Blocks.DIAMOND_BLOCK.defaultBlockState(),
            instance.modelManager.blockModelShaper.getBlockModel(Blocks.OAK_FENCE_GATE.defaultBlockState()),
            1f, 1f, 1f,
            15728880, packedOverlay
        )
        // packed light is just dark for some reason.
    }
}
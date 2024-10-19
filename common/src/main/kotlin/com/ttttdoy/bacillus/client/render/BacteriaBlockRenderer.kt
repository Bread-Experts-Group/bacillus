package com.ttttdoy.bacillus.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacillus.registry.ModBlocks
import com.ttttdoy.bacillus.registry.ModRenderType
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import java.awt.Color

class BacteriaBlockRenderer(
    val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<BacteriaBlockEntity> {
    val instance: Minecraft = Minecraft.getInstance()
    val debugMode = false

    override fun render(
        blockEntity: BacteriaBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        if (!blockEntity.blockState.getValue(BlockStateProperties.TRIGGERED)) return
        val texture = ResourceLocation.fromNamespaceAndPath(
            "bacillus",
            "textures/block/${if (blockEntity.blockState.block == ModBlocks.DESTROYER.get().block) "destroyer" else "replacer"}.png"
        )

        blockEntity.consumedBlockState?.let {
            context.blockRenderDispatcher.modelRenderer.renderModel(
                poseStack.last(),
                bufferSource.getBuffer(ModRenderType.solidTextureTest(texture)),
                it,
                instance.modelManager.blockModelShaper.getBlockModel(it),
                1f, 1f, 1f,
                packedLight, packedOverlay
            )
        }

        if (!debugMode) return
        renderText(
            "active: ${blockEntity.active}",
            0.5, 1.5, 0.5,
            Color.WHITE.rgb,
            Color(0.0f, 0.0f, 0.0f, 0.5f).rgb,
            poseStack,
            bufferSource
        )
        renderText(
            "grace: ${blockEntity.grace}",
            0.5, 1.8, 0.5,
            Color.WHITE.rgb,
            Color(0f, 0f, 0f, 0.5f).rgb,
            poseStack,
            bufferSource
        )
        renderText(
            "output: ${blockEntity.cached?.second}",
            0.5, 2.0, 0.5,
            Color.WHITE.rgb,
            Color(0f, 0f, 0f, 0.5f).rgb,
            poseStack, bufferSource
        )

        blockEntity.consumedBlockState?.let {
            renderText(
                "consumedBlockState: $it",
                0.5, 2.3, 0.5,
                Color.WHITE.rgb,
                Color(0f, 0f, 0f, 0.5f).rgb,
                poseStack,
                bufferSource
            )
        }

        var pos = 2.6
        blockEntity.cached?.first?.forEach { block: Block ->
            renderText(
                "input: $block",
                0.5, pos, 0.5,
                Color.WHITE.rgb,
                Color(0f, 0f, 0f, 0.5f).rgb,
                poseStack, bufferSource
            )
            pos += 0.2
        }
        pos = 2.2
    }

    private fun renderText(
        text: String,
        x: Double,
        y: Double,
        z: Double,
        color: Int,
        bgColor: Int,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource
    ) {
        poseStack.pushPose()
        poseStack.translate(x, y, z)
        poseStack.mulPose(Axis.XN.rotationDegrees(180f))
        poseStack.mulPose(Axis.YN.rotationDegrees(180f))
        poseStack.mulPose(Axis.YN.rotationDegrees(-instance.player!!.yHeadRot))
        poseStack.mulPose(Axis.XN.rotationDegrees(instance.player!!.xRot))
        poseStack.scale(0.02f, 0.02f, 0.02f)
        context.font.drawInBatch(
            text,
            -context.font.width(text) / 2f, 0f,
            color,
            false,
            poseStack.last().pose(),
            bufferSource,
            Font.DisplayMode.NORMAL,
            bgColor,
            15728880
        )
        poseStack.popPose()
    }
}
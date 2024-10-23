package com.ttttdoy.bacillus.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.PoseStack.Pose
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import com.ttttdoy.bacillus.Bacillus.modLocation
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacillus.registry.ModBlocks
import com.ttttdoy.bacillus.registry.ModRenderType
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.ModelBlockRenderer
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.Vec3
import java.awt.Color
import java.text.DecimalFormat

/**
 * Renderer for the Destroyer and Bacteria
 *
 * * Only renders when the block is converting a block that doesn't have a full block collision and the trigger state is true
 */
class BacteriaBlockRenderer(
    private val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<BacteriaBlockEntity> {
    private val instance: Minecraft = Minecraft.getInstance()
    private val debugMode = false
    private var pos = 2.5
    private var formatter = DecimalFormat("#0.00")

    private val destroyerTexture = modLocation("textures/block/destroyer.png")
    private val replacerTexture = modLocation("textures/block/replacer.png")
    private val blankTexture = modLocation("textures/block/bacteria_blank.png")

    /**
     * Stops rendering this [BlockEntityRenderer] if [BlockStateProperties.TRIGGERED] isn't **true**.
     * @since 1.2.0
     */
    override fun shouldRender(blockEntity: BacteriaBlockEntity, cameraPos: Vec3): Boolean =
        blockEntity.blockState.getValue(BlockStateProperties.TRIGGERED)

    /**
     * Renders this [BlockEntityRenderer]
     */
    override fun render(
        blockEntity: BacteriaBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val formattedGrace = formatter.format(blockEntity.grace.toFloat() / 200f)
        val timer = formattedGrace.toFloat()
        val decay = if (blockEntity.active == 0) timer else 1f
        val texture =
            if (blockEntity.blockState.block == ModBlocks.DESTROYER.get().block) destroyerTexture else replacerTexture

        context.blockRenderDispatcher.getBlockModel(Blocks.HAY_BLOCK.defaultBlockState())

        blockEntity.consumedBlockState?.let {
            renderBetterModel(
                poseStack.last(),
                bufferSource.getBuffer(ModRenderType.solidTextured(texture)),
                it,
                context.blockRenderDispatcher.getBlockModel(it),
                1f, 1f, 0f, 1f,
                packedLight, packedOverlay
            )
        }

        if (!debugMode) return
        renderText(
            "active: ${blockEntity.active}",
            1.5,
            Color.WHITE.rgb,
            Color(0.0f, 0.0f, 0.0f, 0.5f).rgb,
            poseStack,
            bufferSource
        )
        renderText(
            "grace: ${blockEntity.grace} | $formattedGrace",
            1.8,
            Color.WHITE.rgb,
            Color(0f, 0f, 0f, 0.5f).rgb,
            poseStack,
            bufferSource
        )
        renderText(
            "output: ${blockEntity.cached?.second}",
            2.0,
            Color.WHITE.rgb,
            Color(0f, 0f, 0f, 0.5f).rgb,
            poseStack, bufferSource
        )

        blockEntity.consumedBlockState?.let {
            renderText(
                "consumedBlockState: $it",
                2.3,
                Color.WHITE.rgb,
                Color(0f, 0f, 0f, 0.5f).rgb,
                poseStack,
                bufferSource
            )
        }

        blockEntity.cached?.first?.forEach { block: Block ->
            renderText(
                "input: $block",
                pos,
                Color.WHITE.rgb,
                Color(0f, 0f, 0f, 0.5f).rgb,
                poseStack, bufferSource
            )
            pos += 0.2
        }
        pos = 2.5
    }

    private fun renderText(
        text: String,
        y: Double,
        color: Int,
        bgColor: Int,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource
    ) {
        val player = instance.player ?: return

        poseStack.pushPose()
        poseStack.translate(0.5, y, 0.5)
        poseStack.mulPose(Axis.XN.rotationDegrees(180f))
        poseStack.mulPose(Axis.YN.rotationDegrees(180f))
        poseStack.mulPose(Axis.YN.rotationDegrees(-player.yHeadRot))
        poseStack.mulPose(Axis.XN.rotationDegrees(player.xRot))
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

    private fun renderBetterModel(
        pose: Pose,
        consumer: VertexConsumer,
        state: BlockState?,
        model: BakedModel,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val random = RandomSource.create()
        val long = 42L

        for (direction: Direction in Direction.entries) {
            random.setSeed(long)
            renderBetterQuadList(
                pose,
                consumer,
                red,
                green,
                blue,
                alpha,
                model.getQuads(state, direction, random),
                packedLight,
                packedOverlay
            )
        }

        random.setSeed(long)
        renderBetterQuadList(
            pose,
            consumer,
            red,
            green,
            blue,
            alpha,
            model.getQuads(state, null, random),
            packedLight,
            packedOverlay
        )
    }

    /**
     * [ModelBlockRenderer.renderQuadList] with the tint check stripped out.
     */
    private fun renderBetterQuadList(
        pose: Pose,
        consumer: VertexConsumer,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
        quads: List<BakedQuad>,
        packedLight: Int,
        packedOverlay: Int
    ) {
        for (bakedQuad: BakedQuad in quads) {
            consumer.putBulkData(pose, bakedQuad, red, green, blue, alpha, packedLight, packedOverlay)
        }
    }
}
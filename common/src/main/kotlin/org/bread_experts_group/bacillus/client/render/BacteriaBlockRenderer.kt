package org.bread_experts_group.bacillus.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.Vec3
import org.bread_experts_group.bacillus.Bacillus
import org.bread_experts_group.bacillus.block.entity.BacteriaBlockEntity
import org.bread_experts_group.bacillus.registry.ModBlocks
import org.bread_experts_group.bacillus.registry.ModRenderType
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

    //    private val blankTexture = Bacillus.modLocation("textures/block/bacteria_blank.png")
    private val destroyerTexture = Bacillus.modLocation("textures/block/destroyer.png")
    private val replacerTexture = Bacillus.modLocation("textures/block/replacer.png")

//    /**
//     * Linear interpolation.
//     * @author Miko Elbrecht, from https://stackoverflow.com/a/4353537/7693129
//     * @since 1.0.0
//     */
//    fun lerp(a: Float, b: Float, f: Float) = a * (1.0f - f) + (b * f)

    override fun shouldRender(blockEntity: BacteriaBlockEntity, cameraPos: Vec3) =
        super.shouldRender(blockEntity, cameraPos) && blockEntity.blockState.getValue(BlockStateProperties.TRIGGERED)

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
        val texture = if (blockEntity.blockState.block == ModBlocks.DESTROYER.get().block) destroyerTexture
        else replacerTexture
//        val decay = 1 - (blockEntity.grace / 120f)
//        val tintR: Float
//        val tintG: Float
//        val tintB: Float
//        if (blockEntity.blockState.block == ModBlocks.DESTROYER.get().block) {
//            // 131, 43, 189
//            tintR = lerp(0.514f, 0.5f, decay)
//            tintG = lerp(0.169f, 0.5f, decay)
//            tintB = lerp(0.741f, 0.5f, decay)
//        } else {
//            // 255, 63, 46
//            tintR = lerp(1.000f, 0.5f, decay)
//            tintG = lerp(0.247f, 0.5f, decay)
//            tintB = lerp(0.180f, 0.5f, decay)
//        }

//        val state = blockEntity.consumedBlockState ?: blockEntity.blockState
//        renderModel(
//            poseStack.last(),
//            bufferSource.getBuffer(ModRenderType.solidTextured(blankTexture)),
//            state,
//            context.blockRenderDispatcher.getBlockModel(state),
//            tintR, tintG, tintB,
//            packedLight, packedOverlay
//        )

        if (blockEntity.blockState.getValue(BlockStateProperties.TRIGGERED)) {
            blockEntity.consumedBlockState?.let {
                context.blockRenderDispatcher.modelRenderer.renderModel(
                    poseStack.last(),
                    bufferSource.getBuffer(ModRenderType.solidTextured(texture)),
                    it,
                    context.blockRenderDispatcher.getBlockModel(it),
                    1f, 1f, 1f,
                    packedLight, packedOverlay
                )
            }
        } else {
            context.blockRenderDispatcher.modelRenderer.renderModel(
                poseStack.last(),
                bufferSource.getBuffer(ModRenderType.solidTextured(texture)),
                blockEntity.blockState,
                context.blockRenderDispatcher.getBlockModel(blockEntity.blockState),
                1f, 1f, 1f,
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
        val formattedGrace = formatter.format(blockEntity.grace.toFloat() / 200f)
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

//    private val randomSource = XoroshiroRandomSource(42)
//    private fun renderModel(
//        pose: Pose, consumer: VertexConsumer,
//        state: BlockState, model: BakedModel,
//        red: Float, green: Float, blue: Float,
//        packedLight: Int, packedOverlay: Int
//    ) {
//        Direction.entries.forEach {
//            renderQuadList(
//                pose, consumer,
//                red, green, blue,
//                model.getQuads(state, it, randomSource),
//                packedLight, packedOverlay
//            )
//        }
//
//        renderQuadList(
//            pose, consumer,
//            red, green, blue,
//            model.getQuads(state, null, randomSource),
//            packedLight, packedOverlay
//        )
//    }
//
//    private fun renderQuadList(
//        pose: Pose, consumer: VertexConsumer,
//        red: Float, green: Float, blue: Float,
//        quads: List<BakedQuad>,
//        packedLight: Int, packedOverlay: Int
//    ) {
//        quads.forEach {
//            consumer.putBulkData(
//                pose, it,
//                red, green, blue, 1.0f,
//                packedLight, packedOverlay
//            )
//        }
//    }
}
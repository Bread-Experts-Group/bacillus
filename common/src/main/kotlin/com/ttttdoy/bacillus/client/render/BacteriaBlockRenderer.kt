package com.ttttdoy.bacillus.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.ttttdoy.bacillus.block.entity.BacteriaBlockEntity
import com.ttttdoy.bacillus.registry.ModRenderType
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.resources.model.Material
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.level.block.Blocks

class BacteriaBlockRenderer(
    val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<BacteriaBlockEntity> {
    val instance: Minecraft = Minecraft.getInstance()
//    val random = RandomSource.create(42)
//    var quadNumber = 0

    override fun render(
        blockEntity: BacteriaBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val texture = ResourceLocation.fromNamespaceAndPath("bacillus", "textures/block/destroyer.png")

//        val model = context.blockRenderDispatcher.getBlockModel(Blocks.OAK_FENCE_GATE.defaultBlockState())

        val material = Material(InventoryMenu.BLOCK_ATLAS, texture)
//        val consumer = material.buffer(bufferSource) { RenderType.solid() }


//        for (direction: Direction in Direction.entries) {
//            model.getQuads(Blocks.OAK_FENCE_GATE.defaultBlockState(), direction, random).forEach {
//                LogManager.getLogger().info("QUAD $quadNumber")
//                it.vertices.forEach { vertex ->
//                    LogManager.getLogger().info("vertices output: $vertex")
//                }
//                quadNumber++
//            }
//        }
//        quadNumber = 0

        // ModelBlockRenderer#renderModelFaceFlat (this has calls inside that make up the shape of the model, could be what we're looking for?)
        instance.blockRenderer.modelRenderer.renderModel(
            poseStack.last(),
            bufferSource.getBuffer(ModRenderType.solidTextureTest(texture)),
            Blocks.DIAMOND_BLOCK.defaultBlockState(),
            instance.modelManager.blockModelShaper.getBlockModel(Blocks.GRASS_BLOCK.defaultBlockState()),
            1f, 1f, 1f,
            15728880, packedOverlay
        )
    }
}
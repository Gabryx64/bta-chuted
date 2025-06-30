package com.github.gabryx64.chuted

import net.minecraft.client.render.block.model.{BlockModel, BlockModelStandard}
import net.minecraft.core.block.{Block, BlockLogic}
import net.fabricmc.api.Environment
import net.fabricmc.api.EnvType
import net.minecraft.client.render.block.color.{
  BlockColor,
  BlockColorDispatcher
}
import net.minecraft.client.render.tessellator.Tessellator
import net.minecraft.core.util.helper.Side
import net.minecraft.core.util.phys.AABB
import org.lwjgl.opengl.GL11

@Environment(EnvType.CLIENT)
class BlockModelChute[T <: BlockLogic](b: Block[T])
  extends BlockModelStandard[T](b) {
  override def render(
    tessellator: Tessellator,
    x: Int,
    y: Int,
    z: Int
  ): Boolean = {
    val bounds = block.getBounds
    val onePix = 0.0625

    bounds.set(0, 0.5 + onePix, 0, 1, 0.5 + onePix * 2, 1)
    renderStandardBlock(tessellator, bounds, x, y, z)

    bounds.set(
      onePix * 2,
      0.5 - onePix * 2,
      onePix * 2,
      1 - onePix * 2,
      0.5 + onePix,
      1 - onePix * 2
    )
    renderStandardBlock(tessellator, bounds, x, y, z)

    setRenderSide(Side.EAST, false)
    setRenderSide(Side.WEST, false)
    setRenderSide(Side.BOTTOM, false)
    bounds.set(0, 0.5 + onePix, 0, 1, 1, onePix)
    renderStandardBlock(tessellator, bounds, x, y, z)

    bounds.set(0, 0.5 + onePix, 1 - onePix, 1, 1, 1)
    renderStandardBlock(tessellator, bounds, x, y, z)

    BlockModel.renderBlocks.renderBitMask = 0
    setRenderSide(Side.NORTH, false)
    setRenderSide(Side.SOUTH, false)
    setRenderSide(Side.BOTTOM, false)
    bounds.set(0, 0.5 + onePix, 0, onePix, 1, 1)
    renderStandardBlock(tessellator, bounds, x, y, z)

    bounds.set(1 - onePix, 0.5 + onePix, 0, 1, 1, 1)
    renderStandardBlock(tessellator, bounds, x, y, z)
    BlockModel.renderBlocks.renderBitMask = 0
    bounds.set(
      0.5 - onePix * 2,
      onePix * 5.5,
      0.5 - onePix * 2,
      0.5 + onePix * 2,
      0.5 - onePix,
      0.5 + onePix * 2
    )
    renderStandardBlock(tessellator, bounds, x, y, z)

    Side.getSideById(
      BlockModel.renderBlocks.blockAccess.getBlockMetadata(x, y, z)
    ) match {
      case Side.BOTTOM =>
        bounds.set(
          0.5 - onePix * 2,
          0,
          0.5 - onePix * 2,
          0.5 + onePix * 2,
          onePix * 5.5,
          0.5 + onePix * 2
        )
      case Side.SOUTH =>
        bounds.set(
          0.5 - onePix * 2,
          onePix * 1.5,
          0.5 - onePix * 2,
          0.5 + onePix * 2,
          onePix * 5.5,
          1
        )
      case Side.NORTH =>
        bounds.set(
          0.5 - onePix * 2,
          onePix * 1.5,
          0,
          0.5 + onePix * 2,
          onePix * 5.5,
          0.5 + onePix * 2
        )
      case Side.EAST =>
        bounds.set(
          0.5 - onePix * 2,
          onePix * 1.5,
          0.5 - onePix * 2,
          1,
          onePix * 5.5,
          0.5 + onePix * 2
        )
      case Side.WEST =>
        bounds.set(
          0,
          onePix * 1.5,
          0.5 - onePix * 2,
          0.5 + onePix * 2,
          onePix * 5.5,
          0.5 + onePix * 2
        )
      case _ =>
        bounds.set(0, 0, 0, 1, 1, 1)
        return true
    }

    renderStandardBlock(tessellator, bounds, x, y, z)

    bounds.set(0, 0, 0, 1, 1, 1)
    true
  }

  override def shouldItemRender3d(): Boolean = false
}

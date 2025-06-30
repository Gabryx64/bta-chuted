package com.github.gabryx64.chuted

import net.minecraft.client.render.block.color.BlockColorDispatcher
import net.minecraft.client.render.{
  EntityRenderDispatcher,
  TileEntityRenderDispatcher
}
import net.minecraft.client.render.block.model.BlockModelDispatcher
import net.minecraft.client.render.item.model.ItemModelDispatcher
import net.minecraft.core.util.helper.Side
import turniplabs.halplibe.helper.ModelHelper
import turniplabs.halplibe.util.ModelEntrypoint

object ChutedModels extends ModelEntrypoint {
  override def initBlockModels(dispatcher: BlockModelDispatcher): Unit = {
    ModelHelper.setBlockModel(
      ChutedBlocks.chute,
      () =>
        new BlockModelChute(ChutedBlocks.chute)
          .setTex(0, s"${ChutedMod.MODID}:block/chute", Side.sides*)
    )
  }

  override def initItemModels(dispatcher: ItemModelDispatcher): Unit = {}

  override def initTileEntityModels(
    dispatcher: TileEntityRenderDispatcher
  ): Unit = {}

  override def initEntityModels(dispatcher: EntityRenderDispatcher): Unit = {}

  override def initBlockColors(dispatcher: BlockColorDispatcher): Unit = {}
}

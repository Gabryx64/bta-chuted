package com.github.gabryx64.chuted

import net.minecraft.core.block.entity.TileEntityActivator
import net.minecraft.core.block.material.Material
import net.minecraft.core.block.{Block, BlockLogic}
import net.minecraft.core.entity.Mob
import net.minecraft.core.entity.player.Player
import net.minecraft.core.enums.PlacementMode
import net.minecraft.core.item.Item
import net.minecraft.core.player.inventory.container.Container
import net.minecraft.core.util.helper.{Direction, Side}
import net.minecraft.core.world.World
import turniplabs.halplibe.helper.ModelHelper

class BlockLogicChute(b: Block[?]) extends BlockLogic(b, Material.metal) {
  override def isSolidRender: Boolean = false

  override def onBlockPlacedByMob(
    world: World,
    x: Int,
    y: Int,
    z: Int,
    side: Side,
    mob: Mob,
    xPlaced: Double,
    yPlaced: Double
  ): Unit = {
    val meta = side.getOpposite.getId
    world.setBlockMetadataWithNotify(
      x,
      y,
      z,
      if meta == Side.TOP.getId then Side.BOTTOM.getId else meta
    )
  }

  override def onBlockPlacedOnSide(
    world: World,
    x: Int,
    y: Int,
    z: Int,
    side: Side,
    xPlaced: Double,
    yPlaced: Double
  ): Unit = {
    val meta = side.getOpposite.getId
    world.setBlockMetadataWithNotify(
      x,
      y,
      z,
      if meta == Side.TOP.getId then Side.BOTTOM.getId else meta
    )
  }

  override def onBlockRightClicked(
    world: World,
    x: Int,
    y: Int,
    z: Int,
    player: Player,
    side: Side,
    xHit: Double,
    yHit: Double
  ): Boolean = if world.isClientSide then true
  else {
    world.getTileEntity(x, y, z) match {
      case te: TileEntityChute =>
        te.isLocked = !te.isLocked
        world.notifyBlockChange(x, y, z, ChutedBlocks.chute.id)

      case _ =>
    }
    true
  }

  override def onActivatorInteract(
    world: World,
    x: Int,
    y: Int,
    z: Int,
    activator: TileEntityActivator,
    direction: Direction
  ): Unit = {
    super.onActivatorInteract(world, x, y, z, activator, direction)
    world.getTileEntity(x, y, z) match {
      case te: TileEntityChute =>
        te.isLocked = !te.isLocked
        world.notifyBlockChange(x, y, z, ChutedBlocks.chute.id)

      case _ =>
    }
  }
}

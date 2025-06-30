package com.github.gabryx64.chuted

import com.mojang.nbt.tags.{CompoundTag, ListTag}
import net.minecraft.core.block.entity.TileEntity
import net.minecraft.core.entity.player.Player
import net.minecraft.core.entity.vehicle.EntityMinecart
import net.minecraft.core.item.ItemStack
import net.minecraft.core.player.inventory.container.Container
import net.minecraft.core.util.helper.Side
import net.minecraft.core.util.phys.AABB
import scala.jdk.CollectionConverters.*

class TileEntityChute extends TileEntity with Container {
  private var contents: Option[ItemStack] = None
  private var itsSuckinTime               = false
  private var tickTimer: Int              = 1

  override def getContainerSize: Int      = 1
  override def getMaxStackSize: Int       = 64
  override def getItem(i: Int): ItemStack = contents.orNull
  override def removeItem(i: Int, amount: Int): ItemStack = {
    contents match {
      case Some(x: ItemStack) =>
        if x.stackSize <= amount then {
          contents = None
          setChanged()
          x
        } else {
          val ret = x.splitStack(amount)
          contents = if x.stackSize <= 0 then None else Some(x)
          setChanged()
          ret
        }
      case None => null
    }
  }

  def getNameTranslationKey: String =
    s"tile.${ChutedMod.MODID}.block.chute.name"

  def setItem(i: Int, itemStack: ItemStack): Unit = itemStack match {
    case null => contents = None
    case _ =>
      if itemStack.stackSize > getMaxStackSize then {
        itemStack.stackSize = getMaxStackSize
      }
      contents = Some(itemStack)
  }

  def sortContainer(): Unit = {}

  def stillValid(player: Player): Boolean =
    worldObj != null && (worldObj.getTileEntity(x, y, z) eq this) && player
      .distanceToSqr(
        x.toDouble + 0.5,
        y.toDouble + 0.5,
        z.toDouble + 0.5
      ) <= 64.0

  override def readFromNBT(nbttagcompound: CompoundTag): Unit = {
    super.readFromNBT(nbttagcompound)
    val tags = nbttagcompound.getList("Items")
    for i <- 0 until tags.tagCount do {
      val comp = tags.tagAt(i).asInstanceOf[CompoundTag]
      if comp.getByte("Slot").toInt == 0 then
        contents = Some(ItemStack.readItemStackFromNbt(comp))
    }
  }

  override def writeToNBT(nbttagcompound: CompoundTag): Unit = {
    super.writeToNBT(nbttagcompound)
    val tags = new ListTag
    if contents.isDefined then {
      val comp = new CompoundTag
      comp.putByte("Slot", 0.toByte)
      contents.get.writeToNBT(comp)
      tags.addTag(comp)
    }
    nbttagcompound.put("Items", tags)
  }

  override def tick(): Unit = {
    getBlock.getLogic match {
      case logic: BlockLogicChute =>
        if tickTimer == 0 then {
          tickTimer = 1
          itsSuckinTime = !itsSuckinTime
          if itsSuckinTime then
            if !(this <>< worldObj.getTileEntity(
                x,
                y + 1,
                z
              ))
            then {
              val ents = worldObj.getEntitiesWithinAABB(
                classOf[EntityMinecart],
                AABB.getTemporaryBB(
                  x - 0.5,
                  y + 0.5,
                  z - 0.5,
                  x + 0.5,
                  y + 1.5,
                  z + 0.5
                )
              )

              ents.asScala
                .filter(ent => ent.getType == EntityMinecart.CHEST_CART)
                .foreach(ent => {
                  this <>< ent
                })
            }
          else {
            val side = Side.getSideById(worldObj.getBlockMetadata(x, y, z))
            if !(this ><> worldObj.getTileEntity(
                x + side.getOffsetX,
                y + side.getOffsetY,
                z + side.getOffsetZ
              ))
            then {
              val ents = worldObj.getEntitiesWithinAABB(
                classOf[EntityMinecart],
                AABB.getTemporaryBB(
                  x - 0.5,
                  y - 1.5,
                  z - 0.5,
                  x + 0.5,
                  y - 0.5,
                  z + 0.5
                )
              )

              ents.asScala
                .filter(ent => ent.getType == EntityMinecart.CHEST_CART)
                .foreach(ent => {
                  this ><> ent
                })
            }
          }
        } else tickTimer -= 1

      case _ =>
    }
  }
}

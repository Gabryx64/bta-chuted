package com.github.gabryx64.chuted

import com.mojang.nbt.tags.CompoundTag
import net.minecraft.core.player.inventory.container.Container
import net.minecraft.core.entity.Entity
import net.minecraft.core.block.entity.{
  TileEntity,
  TileEntityBasket,
  TileEntityFurnace,
  TileEntityTrommel
}
import net.minecraft.core.item.{Item, ItemStack}

import scala.util.boundary
import boundary.break

private def slotsFromContainer(self: Container): IndexedSeq[Int] = {
  for i <- 0 until self.getContainerSize if self.getItem(i) != null
  yield i
}

private def slotsFromFurnace(
  self: TileEntityFurnace
): IndexedSeq[Int] = {
  if self.getItem(2) == null then IndexedSeq()
  else IndexedSeq(2)
}

private def containerDestSlot(
  self: Container,
  item: ItemStack
): Option[Int] = {
  var destSlot: Option[Int] = None
  boundary {
    for i <- 0 until self.getContainerSize do {
      val itemStack = self.getItem(i)
      if itemStack == null && destSlot.isEmpty then {
        destSlot = Some(i)
      } else if itemStack != null && itemStack.canStackWith(item)
        && itemStack.stackSize < (itemStack.getMaxStackSize min self.getMaxStackSize)
      then {
        destSlot = Some(i)
        break()
      }
    }
  }
  destSlot
}

private def trommelDestSlot(
  self: TileEntityTrommel,
  item: ItemStack,
  isOnTop: Boolean
): Option[Int] = {
  if !isOnTop then {
    val itemStack = self.getItem(4)

    return if itemStack == null || (itemStack.canStackWith(item)
        && itemStack.stackSize < (itemStack.getMaxStackSize min self.getMaxStackSize))
    then Some(4)
    else None
  }

  var destSlot: Option[Int] = None
  boundary {
    for i <- 0 until 4 do {
      val itemStack = self.getItem(i)
      if itemStack == null && destSlot.isEmpty then {
        destSlot = Some(i)
      } else if itemStack != null && itemStack.canStackWith(item)
        && itemStack.stackSize < (itemStack.getMaxStackSize min self.getMaxStackSize)
      then {
        destSlot = Some(i)
        break()
      }
    }
  }
  destSlot
}

private def furnaceDestSlot(
  self: TileEntityFurnace,
  item: ItemStack,
  isOnTop: Boolean
): Option[Int] = {
  val destSlot  = if isOnTop then 0 else 1
  val itemStack = self.getItem(destSlot)
  if itemStack == null || (itemStack.canStackWith(item)
      && itemStack.stackSize < (itemStack.getMaxStackSize min self.getMaxStackSize))
  then Some(destSlot)
  else None
}

private def itemIntoContainerSlot(
  self: Container,
  item: ItemStack,
  slot: Int
): Boolean = {
  val itemStack = self.getItem(slot)
  if itemStack == null then self.setItem(slot, item)
  else {
    itemStack.stackSize += 1
    self.setItem(slot, itemStack)
  }

  true
}

extension (self: TileEntity & Container) {
  def getSlotsFromSource: IndexedSeq[Int] =
    self match {
      case self: TileEntityTrommel => IndexedSeq()
      case self: TileEntityFurnace => slotsFromFurnace(self)
      case _                       => slotsFromContainer(self)
    }

  def getDestSlot(item: ItemStack, isOnTop: Boolean): Option[Int] =
    self match {
      case self: TileEntityTrommel => trommelDestSlot(self, item, isOnTop)
      case self: TileEntityFurnace => furnaceDestSlot(self, item, isOnTop)
      case _                       => containerDestSlot(self, item)
    }

  def putItemIntoSlot(item: ItemStack, slot: Int): Boolean =
    itemIntoContainerSlot(self, item, slot)

  def ><>(other_entity: TileEntity): Boolean = {
    if self == null || !other_entity.isInstanceOf[Container] then return false

    val other = other_entity.asInstanceOf[TileEntity & Container]
    for srcSlot <- self.getSlotsFromSource do {
      other.getDestSlot(self.getItem(srcSlot), self.y > other.y) match {
        case Some(destSlot) =>
          other.putItemIntoSlot(self.removeItem(srcSlot, 1), destSlot)
          return true
        case _ =>
      }
    }
    false
  }

  def <><(other: TileEntity): Boolean = {
    other match {
      case basket: TileEntityBasket => return fromBasket(basket)
      case _                        =>
    }
    if !other.isInstanceOf[Container] then return false

    other.asInstanceOf[TileEntity & Container] ><> self
  }

  def ><>(other: Entity & Container): Boolean = {
    other match {
      case basket: TileEntityBasket => return toBasket(basket)
      case _                        =>
    }
    if other == null then return false
    for srcSlot <- self.getSlotsFromSource do {
      containerDestSlot(other, self.getItem(srcSlot)) match {
        case Some(destSlot) =>
          itemIntoContainerSlot(other, self.removeItem(srcSlot, 1), destSlot)
          return true
        case _ =>
      }
    }
    false
  }

  def <><(other: Entity & Container): Boolean = {
    if self == null || other == null then return false
    for srcSlot <- slotsFromContainer(other) do {
      self.getDestSlot(other.getItem(srcSlot), self.y > other.y) match {
        case Some(destSlot) =>
          self.putItemIntoSlot(other.removeItem(srcSlot, 1), destSlot)
          return true
        case _ =>
      }
    }
    false
  }
}

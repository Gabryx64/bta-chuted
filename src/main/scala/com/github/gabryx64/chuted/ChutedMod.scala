package com.github.gabryx64.chuted

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.util.collection.NamespaceID
import org.slf4j.{Logger, LoggerFactory}
import turniplabs.halplibe.helper.EntityHelper
import turniplabs.halplibe.util.GameStartEntrypoint

object ChutedMod extends ModInitializer, GameStartEntrypoint {
  final val MODID: String  = "chuted"
  final val LOGGER: Logger = LoggerFactory.getLogger(MODID)

  override def onInitialize(): Unit = {
    EntityHelper.createTileEntity(
      classOf[TileEntityChute],
      NamespaceID.getTemp(MODID, "chute")
    )
  }

  override def beforeGameStart(): Unit = {}
  override def afterGameStart(): Unit  = {}
}

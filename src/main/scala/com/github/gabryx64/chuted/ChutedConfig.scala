package com.github.gabryx64.chuted

import turniplabs.halplibe.util.TomlConfigHandler
import turniplabs.halplibe.util.toml.Toml

object ChutedConfig {
  val TOML = new Toml("Chuted's TOML Config")
  TOML.addEntry("chuteId", "Default: 8664 (id < 16000)", 8664)

  val CFG = new TomlConfigHandler(ChutedMod.MODID, TOML)

}

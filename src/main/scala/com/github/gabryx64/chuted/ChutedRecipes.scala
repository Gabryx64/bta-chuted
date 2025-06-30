package com.github.gabryx64.chuted

import turniplabs.halplibe.util.RecipeEntrypoint

object ChutedRecipes extends RecipeEntrypoint {
  override def initNamespaces(): Unit = {
    ChutedMod.LOGGER.info("initNamespaces.")
  }

  override def onRecipesReady(): Unit = {
    ChutedMod.LOGGER.info("onRecipesReady.")
  }
}

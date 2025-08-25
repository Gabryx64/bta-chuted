package com.github.gabryx64.chuted

import net.minecraft.core.data.registry.Registries
import net.minecraft.core.data.registry.recipe.RecipeNamespace
import net.minecraft.core.item.Items
import turniplabs.halplibe.helper.RecipeBuilder
import turniplabs.halplibe.util.RecipeEntrypoint

object ChutedRecipes extends RecipeEntrypoint {
  val namespace = new RecipeNamespace
  override def initNamespaces(): Unit = {
    ChutedMod.LOGGER.info("initNamespaces.")
    Registries.RECIPES.register(ChutedMod.MODID, namespace)
  }

  override def onRecipesReady(): Unit = {
    ChutedMod.LOGGER.info("onRecipesReady.")
    RecipeBuilder
      .Shaped(ChutedMod.MODID)
      .setShape("i i", "i i", " i ")
      .addInput('i', Items.INGOT_IRON)
      .create("chute_recipe", ChutedBlocks.chute.getDefaultStack)
  }
}

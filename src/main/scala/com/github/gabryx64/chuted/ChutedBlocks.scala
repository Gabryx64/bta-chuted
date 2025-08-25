package com.github.gabryx64.chuted

import net.minecraft.core.block.material.Material
import net.minecraft.core.block.tag.BlockTags
import net.minecraft.core.block.{Block, BlockLogicFullyRotatable}
import net.minecraft.core.sound.BlockSounds
import turniplabs.halplibe.helper.BlockBuilder
import turniplabs.halplibe.util.TomlConfigHandler
import turniplabs.halplibe.util.toml.Toml

object ChutedBlocks {
  lazy val chute: Block[BlockLogicChute] = new BlockBuilder(ChutedMod.MODID)
    .setHardness(3)
    .setResistance(10)
    .setBlockSound(BlockSounds.METAL)
    .setTags(BlockTags.MINEABLE_BY_PICKAXE)
    .setTileEntity(() => new TileEntityChute)
    .build(
      "block.chute",
      "block/chute",
      ChutedConfig.CFG.getInt("chuteId"),
      b => new BlockLogicChute(b)
    )
}

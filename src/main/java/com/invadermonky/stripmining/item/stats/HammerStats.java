package com.invadermonky.stripmining.item.stats;

import com.google.common.collect.Sets;
import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class HammerStats extends ToolStatsBase {
    public int breakareaHeight;
    public int breakareaWidth;

    public THashSet<Block> effectiveBlocks = new THashSet<>(
            Sets.newHashSet(
                Blocks.ACTIVATOR_RAIL,
                Blocks.COAL_ORE,
                Blocks.COBBLESTONE,
                Blocks.DETECTOR_RAIL,
                Blocks.DIAMOND_BLOCK,
                Blocks.DIAMOND_ORE,
                Blocks.DOUBLE_STONE_SLAB,
                Blocks.GOLDEN_RAIL,
                Blocks.GOLD_BLOCK,
                Blocks.GOLD_ORE,
                Blocks.ICE,
                Blocks.IRON_BLOCK,
                Blocks.IRON_ORE,
                Blocks.LAPIS_BLOCK,
                Blocks.LAPIS_ORE,
                Blocks.LIT_REDSTONE_ORE,
                Blocks.MOSSY_COBBLESTONE,
                Blocks.NETHERRACK,
                Blocks.PACKED_ICE,
                Blocks.RAIL,
                Blocks.REDSTONE_ORE,
                Blocks.SANDSTONE,
                Blocks.RED_SANDSTONE,
                Blocks.STONE,
                Blocks.STONE_SLAB,
                Blocks.STONE_BUTTON,
                Blocks.STONE_PRESSURE_PLATE
            )
    );

    public THashSet<Material> effectiveMaterials = new THashSet<>(
            Sets.newHashSet(
                    Material.IRON,
                    Material.ANVIL,
                    Material.ROCK,
                    Material.ICE,
                    Material.PACKED_ICE,
                    Material.GLASS,
                    Material.REDSTONE_LIGHT
            )
    );
}

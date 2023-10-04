package com.invadermonky.stripmining.item.stats;

import com.google.common.collect.Sets;
import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class CarpenterAxeStats extends ToolStatsBase {
    public int breakareaHeight;
    public int breakareaWidth;

    public THashSet<Block> effectiveBlocks = new THashSet<>(
            Sets.newHashSet(
                    Blocks.PLANKS,
                    Blocks.BOOKSHELF,
                    Blocks.LOG,
                    Blocks.LOG2,
                    Blocks.CHEST,
                    Blocks.LADDER,
                    Blocks.WOODEN_BUTTON,
                    Blocks.WOODEN_PRESSURE_PLATE
            )
    );

    public THashSet<Material> effectiveMaterials = new THashSet<>(
            Sets.newHashSet(
                    Material.WOOD
            )
    );
}

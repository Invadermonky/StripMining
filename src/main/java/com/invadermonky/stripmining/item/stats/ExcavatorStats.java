package com.invadermonky.stripmining.item.stats;

import com.google.common.collect.Sets;
import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class ExcavatorStats extends ToolStatsBase {
    public int breakareaHeight;
    public int breakareaWidth;

    public THashSet<Block> effectiveBlocks = new THashSet<>(
            Sets.newHashSet(
                    Blocks.CLAY,
                    Blocks.DIRT,
                    Blocks.FARMLAND,
                    Blocks.GRASS,
                    Blocks.GRAVEL,
                    Blocks.MYCELIUM,
                    Blocks.SAND,
                    Blocks.SNOW,
                    Blocks.SNOW_LAYER,
                    Blocks.SOUL_SAND,
                    Blocks.GRASS_PATH,
                    Blocks.CONCRETE_POWDER
            )
    );

    public THashSet<Material> effectiveMaterials = new THashSet<>(
            Sets.newHashSet(
                    Material.GROUND,
                    Material.GRASS,
                    Material.SAND,
                    Material.SNOW,
                    Material.CRAFTED_SNOW,
                    Material.CLAY
            )
    );
}

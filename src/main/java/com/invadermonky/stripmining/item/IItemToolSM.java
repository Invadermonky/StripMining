package com.invadermonky.stripmining.item;

import com.invadermonky.stripmining.item.stats.ToolStatsBase;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TLinkedHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

import java.awt.*;

public interface IItemToolSM {

    /** Retrieves protected tool classes. */
    TLinkedHashSet<String> getToolClasses();

    /** Retrieves protected crafting material string. Can be an ItemStack string or an OreDict string. */
    String getCraftingMaterial();

    /** Returns the tool crafting/repair material ItemStack. Can return null. */
    ItemStack getCraftingStack();

    /** Returns the base stats of the tool pulled from the config json files. */
    ToolStatsBase getToolBaseStats();

    /** Returns the base color of the tool */
    Color getToolBaseColor();

    /** Returns the efficiency of the tool */
    float getEfficiency();

    /** Gets the set of blocks the tool can harvest. */
    THashSet<Block> getEffectiveBlocks();

    /** Gets the set of materials the tool can harvest. */
    THashSet<Material> getEffectiveMaterials();
}

package com.invadermonky.stripmining.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface IAreaBrealToolSM {

    /**
     * Determines any additional blocks that will be broken by the tool when harvesting the origin block.
     *
     * @param stack The ItemStack used to harvest the blocks.
     * @param origin The BlockPos of the origin block for the break area.
     * @param player The player attempting to harvest the blocks.
     * @return A list containing all blocks that can be harvested in the tool break area.
     */
    ImmutableList<BlockPos> getAreaBreakBlocks(ItemStack stack, BlockPos origin, EntityPlayer player);

    /**
     * The default reach distance used rendering the break area for AoE tools.
     *
     * @param stack The held ItemStack.
     * @return Default player reach distance.
     */
    default float getReachDistance(ItemStack stack) { return 4.5F; }
}

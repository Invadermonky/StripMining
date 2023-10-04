package com.invadermonky.stripmining.util;

import com.invadermonky.stripmining.item.IItemToolSM;
import com.invadermonky.stripmining.item.tools.ItemCarpenterAxe;
import com.invadermonky.stripmining.item.tools.ItemExcavator;
import com.invadermonky.stripmining.item.tools.ItemHammer;
import joptsimple.internal.Strings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHelper {
    public static boolean isOreNameEqual(ItemStack stack, String oreName) {
        return OreDictionary.getOreName(getOreID(stack)).equals(oreName);
    }

    public static boolean isRepairItem(IItemToolSM tool, ItemStack stack) {
        if(tool.getCraftingStack() == null)
            return false;

        String stackName = stack.getItem().getRegistryName().toString();
        String repairName = tool.getCraftingStack().getItem().getRegistryName().toString();

        if(ItemHelper.stringHasMetaData(tool.getCraftingMaterial()))
            return stackName.equals(repairName) && stack.getMetadata() == tool.getCraftingStack().getMetadata();

        return stackName.equals(repairName);
    }

    public static int getOreID(ItemStack stack) {
        return getOreID(getOreName(stack));
    }

    public static int getOreID(String oreName) {
        return Strings.isNullOrEmpty(oreName) ? -1 : OreDictionary.getOreID(oreName);
    }

    public static String getOreName(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        return (ids != null && ids.length >= 1) ? OreDictionary.getOreName(ids[0]) : "";
    }

    public static boolean stringHasMetaData(String item) {
        return item.split(":").length == 3;
    }

    /**
     * Attempts to parse a string to determine if the string is an ItemStack.
     *
     * @param item The item string
     * @return An ItemStack, ItemStack with metadata, or null
     */
    public static ItemStack getStackFromString(String item) {
        String[] str = item.split(":");
        if(str.length == 2){
            return new ItemStack(Item.getByNameOrId(item));
        } else if(str.length == 3) {
            return new ItemStack(Item.getByNameOrId(str[0] + ":" + str[1]), 1, Integer.parseInt(str[2]));
        }
        return null;
    }

    public static boolean isExcavator(ItemTool tool) {
        return tool instanceof ItemExcavator;
    }

    public static boolean isHammer(ItemTool tool) {
        return tool instanceof ItemHammer;
    }

    public static boolean isCarpenterAxe(ItemTool tool) {
        return tool instanceof ItemCarpenterAxe;
    }

    public static String getItemTypeString(ItemTool tool) {
        if(isExcavator(tool)) return "excavator";
        else if(isHammer(tool)) return "hammer";
        else if(isCarpenterAxe(tool)) return "carpenteraxe";
        else return "";
    }
}

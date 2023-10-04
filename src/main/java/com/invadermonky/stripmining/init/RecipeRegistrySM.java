package com.invadermonky.stripmining.init;

import com.invadermonky.stripmining.handlers.ConfigHandler;
import com.invadermonky.stripmining.item.tools.ItemCarpenterAxe;
import com.invadermonky.stripmining.item.tools.ItemExcavator;
import com.invadermonky.stripmining.item.tools.ItemHammer;
import com.invadermonky.stripmining.util.ItemHelper;
import com.invadermonky.stripmining.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class RecipeRegistrySM {
    @SubscribeEvent
    public static void addRecipes(RegistryEvent.Register<IRecipe> event) {
        if(!ConfigHandler.generateRecipes)
            return;

        IForgeRegistry<IRecipe> registry = event.getRegistry();

        for(ItemTool tool : ItemRegistrySM.tools) {
            ItemStack output = new ItemStack(tool);

            ResourceLocation loc = getNameForRecipe(output);
            CraftingHelper.ShapedPrimer primer = null;

            if(ItemHelper.isExcavator(tool))
                primer = getExcavatorPrimer((ItemExcavator) tool);
            else if(ItemHelper.isHammer(tool))
                primer = getHammerPrimer((ItemHammer) tool);
            else if(ItemHelper.isCarpenterAxe(tool))
                primer = getCarpenterAxePrimer((ItemCarpenterAxe) tool);

            if(primer != null) {
                ShapedRecipes recipe = new ShapedRecipes(output.getItem().getRegistryName().toString(), primer.width, primer.height, primer.input, output);
                recipe.setRegistryName(loc);
                registry.register(recipe);
            }
        }

        LogHelper.info("Recipes added.");
    }

    public static CraftingHelper.ShapedPrimer getExcavatorPrimer(ItemExcavator tool) {
        Object ingot;
        if(tool.getCraftingStack() == null)
            ingot = tool.getCraftingMaterial();
        else if(ItemHelper.stringHasMetaData(tool.getCraftingMaterial()))
            ingot = tool.getCraftingStack();
        else
            ingot = tool.getCraftingStack().getItem();

        return CraftingHelper.parseShaped(" I ", "IRI", " R ", 'I', ingot, 'R', "stickWood");
    }

    public static CraftingHelper.ShapedPrimer getHammerPrimer(ItemHammer tool) {
        Object ingot;
        if(tool.getCraftingStack() == null)
            ingot = tool.getCraftingMaterial();
        else if(ItemHelper.stringHasMetaData(tool.getCraftingMaterial()))
            ingot = tool.getCraftingStack();
        else
            ingot = tool.getCraftingStack().getItem();

        return CraftingHelper.parseShaped("III", "IRI", " R ", 'I', ingot, 'R', "stickWood");
    }

    public static CraftingHelper.ShapedPrimer getCarpenterAxePrimer(ItemCarpenterAxe tool) {
        Object ingot;
        if(tool.getCraftingStack() == null)
            ingot = tool.getCraftingMaterial();
        else if(ItemHelper.stringHasMetaData(tool.getCraftingMaterial()))
            ingot = tool.getCraftingStack();
        else
            ingot = tool.getCraftingStack().getItem();

        return CraftingHelper.parseShaped("III", "IR ", " R ", 'I', ingot, 'R', "stickWood");
    }

    private static ResourceLocation getNameForRecipe(ItemStack output) {
        ResourceLocation baseLoc = new ResourceLocation(output.getItem().getRegistryName().toString());
        ResourceLocation recipeLoc = baseLoc;
        int index = 0;
        while(CraftingManager.REGISTRY.containsKey(recipeLoc)) {
            index++;
            recipeLoc = new ResourceLocation(baseLoc.getPath() + "_" + index);
        }
        return recipeLoc;
    }
}

package com.invadermonky.stripmining.init;

import com.invadermonky.stripmining.handlers.ConfigHandler;
import com.invadermonky.stripmining.handlers.FileHandler;
import com.invadermonky.stripmining.handlers.JsonHandler;
import com.invadermonky.stripmining.item.stats.CarpenterAxeStats;
import com.invadermonky.stripmining.item.stats.ExcavatorStats;
import com.invadermonky.stripmining.item.stats.HammerStats;
import com.invadermonky.stripmining.item.stats.ProspectingPickStats;
import com.invadermonky.stripmining.item.tools.ItemCarpenterAxe;
import com.invadermonky.stripmining.item.tools.ItemExcavator;
import com.invadermonky.stripmining.item.tools.ItemHammer;
import com.invadermonky.stripmining.item.tools.ItemProspectingPick;
import com.invadermonky.stripmining.util.LogHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@EventBusSubscriber
public class ItemRegistrySM {
    public static ArrayList<ItemTool> tools = new ArrayList<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        if(ConfigHandler.enableExcavators) {
            LogHelper.userDebug("Retrieving Excavator config files.");
            HashMap<String, String> excavatorConfigs = FileHandler.getToolConfigs("excavators");
            excavatorConfigs.forEach((fileName, fileContents) -> {
                ExcavatorStats excavatorStats = JsonHandler.parseExcavatorJson(fileName, fileContents);
                if (excavatorStats != null) {
                    ItemExcavator itemExcavator = new ItemExcavator(excavatorStats);
                    addToolToRegister(itemExcavator);
                }
            });
        }

        if(ConfigHandler.enableHammers) {
            LogHelper.userDebug("Retrieving Hammer config files.");
            HashMap<String, String> hammerConfigs = FileHandler.getToolConfigs("hammers");
            hammerConfigs.forEach((fileName, fileContents) -> {
                HammerStats hammerStats = JsonHandler.parseHammerJson(fileName, fileContents);
                if (hammerStats != null) {
                    ItemHammer itemHammer = new ItemHammer(hammerStats);
                    addToolToRegister(itemHammer);
                }
            });
        }

        if(ConfigHandler.enableCarpenterAxes) {
            LogHelper.userDebug("Retrieving Carpenter Axe config files.");
            HashMap<String, String> carpenterAxeConfigs = FileHandler.getToolConfigs("carpenteraxes");
            carpenterAxeConfigs.forEach((fileName, fileContents) -> {
                CarpenterAxeStats carpenterAxeStats = JsonHandler.parseCarpenterAxeJson(fileName, fileContents);
                if(carpenterAxeStats != null) {
                    ItemCarpenterAxe itemCarpenterAxe = new ItemCarpenterAxe(carpenterAxeStats);
                    addToolToRegister(itemCarpenterAxe);
                }
            });
        }

        if(ConfigHandler.enableProspectingPicks) {
            LogHelper.userDebug("Regrieving Carpenter Axe config files.");
            HashMap<String, String> prospectingPIckConfigs = FileHandler.getToolConfigs("prospectingpicks");
            prospectingPIckConfigs.forEach((fileName, fileContents) -> {
                ProspectingPickStats prospectingPickStats = JsonHandler.parseProspectingPickJson(fileName, fileContents);
                if(prospectingPickStats != null) {
                    ItemProspectingPick itemProspectingPick = new ItemProspectingPick(prospectingPickStats);
                    addToolToRegister(itemProspectingPick);
                }
            });
        }

        tools.sort(Comparator.comparing(t -> t.getRegistryName().getPath()));

        for(Item tool : tools) {
            registry.register(tool);
        }

        LogHelper.debug("Registered all items.");
    }

    public static void addToolToRegister(ItemTool tool) {
        if(tool != null) {
            boolean toAdd = true;
            for(ItemTool checkTool : tools) {
                if(tool.getRegistryName().toString().equals(checkTool.getRegistryName().toString())) {
                    toAdd = false;
                    LogHelper.error("Error registering tool, identical item Id found: " + tool.getRegistryName().toString());
                }
            }
            if(toAdd)
                tools.add(tool);
        }
    }
}

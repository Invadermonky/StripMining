package com.invadermonky.stripmining.handlers;

import com.invadermonky.stripmining.StripMining;
import com.invadermonky.stripmining.util.LogHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigHandler {
    private static Configuration config = null;

    public static boolean enableExcavators = true;
    public static boolean enableHammers = true;
    public static boolean enableCarpenterAxes = true;
    public static boolean generateRecipes = true;
    public static boolean loadDefaults = false;
    public static boolean enableDebug = false;

    public static void preInit() {
        File configFile = new File(Paths.get(Loader.instance().getConfigDir().toString(), StripMining.MOD_ID, StripMining.MOD_ID + ".cfg").toString());
        config = new Configuration(configFile);
        config.load();

        enableExcavators = config.getBoolean("enableExcavators", Configuration.CATEGORY_GENERAL, enableExcavators, "Enable area digging tools.");
        enableHammers = config.getBoolean("enableHammers", Configuration.CATEGORY_GENERAL, enableHammers, "Enable area mining tools.");
        enableCarpenterAxes = config.getBoolean("enableCarpenterAxes", Configuration.CATEGORY_GENERAL, enableCarpenterAxes, "Enable full tree harvesting tools.");
        generateRecipes = config.getBoolean("generateRecipes", Configuration.CATEGORY_GENERAL, generateRecipes, "Generate default recipes for tools.");
        loadDefaults = config.getBoolean("loadDefaults", Configuration.CATEGORY_GENERAL, loadDefaults,  "Loads all default tool json files. This config option will default to \"false\" after load is complete.\n\tWARNING: This option will delete and reload all tool config files. Be sure to backup any files you wish to save.");
        enableDebug = config.getBoolean("robustLogging", Configuration.CATEGORY_GENERAL, enableDebug, "Enables robust logging. Used to debug tool json files.");

        if (loadDefaults) {
            loadDefaultExcavators();
            loadDefaultHammers();
            loadDefaultCarpenterAxes();
            //TODO Remove comment before release.
            config.getCategory(Configuration.CATEGORY_GENERAL).get("loadDefaults").set(false);
        } else {
            if(!configFolderExists("excavators") && enableExcavators) {
                loadDefaultExcavators();
            }
            if(!configFolderExists("hammers") && enableHammers) {
                loadDefaultHammers();
            }
            if(!configFolderExists("carpenteraxes") && enableCarpenterAxes) {
                loadDefaultCarpenterAxes();
            }
        }

        if(config.hasChanged())
            config.save();
    }

    private static boolean configFolderExists(String tool) {
        return Files.exists(Paths.get(Loader.instance().getConfigDir().getAbsolutePath(), StripMining.MOD_ID, tool));
    }

    private static void loadDefaultExcavators() {
        LogHelper.info("Loading default excavator configs.");
        loadFromJar("excavators");
    }

    private static void loadDefaultHammers() {
        LogHelper.info("Loading default hammer configs.");
        loadFromJar("hammers");
    }

    private static void loadDefaultCarpenterAxes() {
        LogHelper.info("Loading default carpenter axe configs.");
        loadFromJar("carpenteraxes");
    }

    private static void loadFromJar(String tool) {
        try {
            String folderPath = "/assets/" + StripMining.MOD_ID + "/data/" + tool + "/";
            File configFile = new File(Paths.get(Loader.instance().getConfigDir().getAbsolutePath(), StripMining.MOD_ID, tool).toString());

            //Creates the directory for the passed tool string
            if(!configFolderExists(tool)) {
                configFile.mkdirs();
                FileHandler.copyFromJar(folderPath, configFile.toPath());
            }
            //Cleans tool directory and loads default values.
            else if(ConfigHandler.loadDefaults) {
                FileUtils.cleanDirectory(configFile);
                FileHandler.copyFromJar(folderPath, configFile.toPath());
            }

        } catch (Exception e) {
            LogHelper.debug(e);
            e.printStackTrace();
        }
    }
}

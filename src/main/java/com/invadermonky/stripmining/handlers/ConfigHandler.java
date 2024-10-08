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
    private static final String CATEGORY_PROSPECTING = "prospecting_settings";

    //General Config
    public static boolean enableExcavators = true;
    public static boolean enableHammers = true;
    public static boolean enableCarpenterAxes = true;
    public static boolean enableProspectingPicks = true;
    public static boolean generateRecipes = true;
    public static boolean loadDefaults = false;
    public static boolean enableDebug = false;

    //Prospecting Config
    public static boolean enableGameStageSupport = true;
    public static boolean enableProspectingSound = true;
    public static String[] oreBlacklist = new String[] {};
    public static String[] oreWhitelist = new String[] {"type=ore"};
    public static int veinTrace = 2;
    public static int veinSmall = 6;
    public static int veinMedium = 10;
    public static int veinLarge = 16;

    public static void preInit() {
        File configFile = new File(Paths.get(Loader.instance().getConfigDir().toString(), StripMining.MOD_ID, StripMining.MOD_ID + ".cfg").toString());
        config = new Configuration(configFile);
        config.load();

        //General
        enableExcavators = config.getBoolean("enableExcavators", Configuration.CATEGORY_GENERAL, enableExcavators, "Enable area digging tools.");
        enableHammers = config.getBoolean("enableHammers", Configuration.CATEGORY_GENERAL, enableHammers, "Enable area mining tools.");
        enableCarpenterAxes = config.getBoolean("enableCarpenterAxes", Configuration.CATEGORY_GENERAL, enableCarpenterAxes, "Enable area wood chopping tools.");
        enableProspectingPicks = config.getBoolean("enableProspectingPicks", Configuration.CATEGORY_GENERAL, enableProspectingPicks, "Enable ore scanning tools.");
        generateRecipes = config.getBoolean("generateRecipes", Configuration.CATEGORY_GENERAL, generateRecipes, "Generate default recipes for tools.");
        loadDefaults = config.getBoolean("loadDefaults", Configuration.CATEGORY_GENERAL, loadDefaults,  "Loads all default tool json files. This config option will default to \"false\" after load is complete.\n\tWARNING: This option will delete and reload all tool config files. Be sure to backup any files you wish to save.");
        enableDebug = config.getBoolean("robustLogging", Configuration.CATEGORY_GENERAL, enableDebug, "Enables robust logging. Used to debug tool json files.");

        //Prospecting
        enableGameStageSupport = config.getBoolean("enableGameStageSupport", CATEGORY_PROSPECTING, enableGameStageSupport, "Enables Game Stages support. Prospected staged ores will be hidden or have their names obscured if the player does not have the correct game stage.");
        enableProspectingSound = config.getBoolean("enableProspectingSound", CATEGORY_PROSPECTING, enableProspectingSound, "Enables the sound effect when prospecting.");
        oreBlacklist = config.getStringList("oreBlacklist", CATEGORY_PROSPECTING, oreBlacklist, "Blacklisted ores that will not be detected by Prospecting Picks. Additional ores can be blacklisted on a per-tool basis using the individual tool json.\nFormat:\n\tore=oreIron\n\ttype=ore\n\tblock=minecraft:iron_ore\n\tblock=minecraft:iron_ore:0\n");
        oreWhitelist = config.getStringList("oreWhitelist", CATEGORY_PROSPECTING, oreWhitelist, "Additional ores that will be detected by Prospecting Picks. Additional ores can be whitelisted on a per-tool basis using the individual tool json.\nFormat:\n\tore=oreIron\n\ttype=ore\n\tblock=minecraft:iron_ore\n\tblock=minecraft:iron_ore:0\n");
        veinTrace = config.getInt("veinTrace", CATEGORY_PROSPECTING, veinTrace, 1, 100, "The maximum number of ore blocks (as a percentage of nearby stone) that can qualify as a trace amount.");
        veinSmall = config.getInt("veinSmall", CATEGORY_PROSPECTING, veinSmall, 1, 100, "The maximum number of ore blocks (as a percentage of nearby stone) that can qualify as a small sample.");
        veinMedium = config.getInt("veinMedium", CATEGORY_PROSPECTING, veinMedium, 1, 100, "The maximum number of ore blocks (as a percentage of nearby stone) that can qualify as a medium sample.");
        veinLarge = config.getInt("veinLarge", CATEGORY_PROSPECTING, veinLarge, 1, 100, "The maximum number of ore blocks (as a percentage of nearby stone) that can qualify as a large sample.");

        if (loadDefaults) {
            loadDefaultExcavators();
            loadDefaultHammers();
            loadDefaultCarpenterAxes();
            loadDefaultProspectingPicks();
            //TODO: Remove comment before release.
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
            if(!configFolderExists("prospectingpicks") && enableProspectingPicks) {
                loadDefaultProspectingPicks();
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

    private static void loadDefaultProspectingPicks() {
        LogHelper.info("Loading default prospecting pick configs.");
        loadFromJar("prospectingpicks");
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

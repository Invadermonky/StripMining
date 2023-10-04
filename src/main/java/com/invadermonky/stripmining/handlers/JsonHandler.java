package com.invadermonky.stripmining.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.invadermonky.stripmining.item.stats.*;
import com.invadermonky.stripmining.util.LogHelper;
import com.invadermonky.stripmining.util.SpriteHelper;

import java.awt.*;
import java.nio.charset.StandardCharsets;

public class JsonHandler {
    private static JsonObject jsonObject;

    /*
        Json parsing for specific tools
    */

    public static ExcavatorStats parseExcavatorJson(String fileName, String fileContents) {
        try {
            LogHelper.userDebug("Parsing File: " + fileName);
            jsonObject = new Gson().fromJson(fileContents, JsonObject.class);
            ExcavatorStats stats = new ExcavatorStats();

            //Reading required property "name"
            stats.name = getStringProperty("name").toLowerCase().trim();
            stats.unlocName = "excavator_" + stats.name;
            LogHelper.userDebug("\tRegistry name set to: " + stats.unlocName);

            //Reading optional property "displayName"
            stats.displayNameRaw = getDisplayNameProperty();

            //Reading requried property "craftingMaterial"
            stats.craftingMaterial = getStringProperty("craftingMaterial");
            LogHelper.userDebug("\tProperty \"craftingMaterial\" set to: " + stats.craftingMaterial);

            //Reading required property "breakarea"
            int[] breakarea = getBreakAreaProperty();
            stats.breakareaHeight = breakarea[0];
            stats.breakareaWidth = breakarea[1];

            //Reading optional property "tier"
            stats.tier = getTierProperty();

            //Reading required property "color"
            stats.color = getBaseColorProperty();

            stats.harvestLevel = getIntProperty("harvestLevel", ExcavatorStatsDefaults.HARVEST_LEVEL);
            stats.durability = getIntProperty("durability", ExcavatorStatsDefaults.DURABILITY);
            stats.efficiency = getFloatProperty("efficiency", ExcavatorStatsDefaults.EFFICIENCY);
            stats.damage = getFloatProperty("damage", ExcavatorStatsDefaults.DAMAGE);
            stats.attackspeed = getFloatProperty("attackspeed", ExcavatorStatsDefaults.ATTACK_SPEED);
            stats.enchantability = getIntProperty("enchantability", ExcavatorStatsDefaults.ENCHANTABILITY);
            stats.isEnchantable = getBooleanProperty("enchantable", ExcavatorStatsDefaults.ENCHANTABLE);
            stats.isRepairable = getBooleanProperty("repairable", ExcavatorStatsDefaults.REPAIRABLE);

            getTemplateColors(stats);

            return stats;

        } catch (Exception e) {
            LogHelper.info("Error parsing file: " + fileName);
            LogHelper.info(e);
            return null;
        }
    }

    public static HammerStats parseHammerJson(String fileName, String fileContents) {
        try {
            LogHelper.userDebug("Parsing File: " + fileName);
            jsonObject = new Gson().fromJson(fileContents, JsonObject.class);
            HammerStats stats = new HammerStats();

            //Reading required property "name"
            stats.name = getStringProperty("name").toLowerCase().trim();
            stats.unlocName = "hammer_" + stats.name;
            LogHelper.userDebug("\tRegistry name set to: " + stats.unlocName);

            //Reading optional property "displayName"
            stats.displayNameRaw = getDisplayNameProperty();

            //Reading requried property "craftingMaterial"
            stats.craftingMaterial = getStringProperty("craftingMaterial");
            LogHelper.userDebug("\tProperty \"craftingMaterial\" set to: " + stats.craftingMaterial);

            //Reading required property "breakarea"
            int[] breakarea = getBreakAreaProperty();
            stats.breakareaHeight = breakarea[0];
            stats.breakareaWidth = breakarea[1];

            //Reading optional property "tier"
            stats.tier = getTierProperty();

            //Reading required property "color"
            stats.color = getBaseColorProperty();

            stats.harvestLevel = getIntProperty("harvestLevel", HammerStatsDefaults.HARVEST_LEVEL);
            stats.durability = getIntProperty("durability", HammerStatsDefaults.DURABILITY);
            stats.efficiency = getFloatProperty("efficiency", HammerStatsDefaults.EFFICIENCY);
            stats.damage = getFloatProperty("damage", HammerStatsDefaults.DAMAGE);
            stats.attackspeed = getFloatProperty("attackspeed", HammerStatsDefaults.ATTACK_SPEED);
            stats.enchantability = getIntProperty("enchantability", HammerStatsDefaults.ENCHANTABILITY);
            stats.isEnchantable = getBooleanProperty("enchantable", HammerStatsDefaults.ENCHANTABLE);
            stats.isRepairable = getBooleanProperty("repairable", HammerStatsDefaults.REPAIRABLE);

            getTemplateColors(stats);

            return stats;

        } catch (Exception e) {
            LogHelper.info("Error parsing file: " + fileName);
            LogHelper.info(e);
            return null;
        }
    }

    public static CarpenterAxeStats parseCarpenterAxeJson(String fileName, String fileContents) {
        try {
            LogHelper.userDebug("Parsing File: " + fileName);
            jsonObject = new Gson().fromJson(fileContents, JsonObject.class);
            CarpenterAxeStats stats = new CarpenterAxeStats();

            //Reading required property "name"
            stats.name = getStringProperty("name").toLowerCase().trim();
            stats.unlocName = "carpenter_axe_" + stats.name;
            LogHelper.userDebug("\tRegistry name set to: " + stats.unlocName);

            //Reading optional property "displayName"
            stats.displayNameRaw = getDisplayNameProperty();

            //Reading requried property "craftingMaterial"
            stats.craftingMaterial = getStringProperty("craftingMaterial");
            LogHelper.userDebug("\tProperty \"craftingMaterial\" set to: " + stats.craftingMaterial);

            //Reading required property "breakarea"
            int[] breakarea = getBreakAreaProperty();
            stats.breakareaHeight = breakarea[0];
            stats.breakareaWidth = breakarea[1];

            //Carpenter Axes only have one sprite option
            stats.tier = 0;

            //Reading required property "color"
            stats.color = getBaseColorProperty();

            stats.harvestLevel = getIntProperty("harvestLevel", CarpenterAxeStatsDefaults.HARVEST_LEVEL);
            stats.durability = getIntProperty("durability", CarpenterAxeStatsDefaults.DURABILITY);
            stats.efficiency = getFloatProperty("efficiency", CarpenterAxeStatsDefaults.EFFICIENCY);
            stats.damage = getFloatProperty("damage", CarpenterAxeStatsDefaults.DAMAGE);
            stats.attackspeed = getFloatProperty("attackspeed", CarpenterAxeStatsDefaults.ATTACK_SPEED);
            stats.enchantability = getIntProperty("enchantability", CarpenterAxeStatsDefaults.ENCHANTABILITY);
            stats.isEnchantable = getBooleanProperty("enchantable", CarpenterAxeStatsDefaults.ENCHANTABLE);
            stats.isRepairable = getBooleanProperty("repairable", CarpenterAxeStatsDefaults.REPAIRABLE);

            getTemplateColors(stats);

            return stats;

        } catch (Exception e) {
            LogHelper.info("Error parsing file: " + fileName);
            LogHelper.info(e);
            return null;
        }
    }


    /**
     * Gets the override template colors from the json file. This is only called if the templateColor property exists.
     * @param baseStats
     */
    private static void getTemplateColors(ToolStatsBase baseStats) {
        baseStats.borderDark = SpriteHelper.darkenColor(baseStats.color, 0.20F);
        baseStats.borderLight = SpriteHelper.darkenColor(baseStats.color, 0.40F);
        baseStats.shadingDark = SpriteHelper.darkenColor(baseStats.color, 0.60F);
        baseStats.shadingLight = SpriteHelper.darkenColor(baseStats.color, 0.80F);
        baseStats.reflectDark = SpriteHelper.lightenColor(baseStats.color, 0.40F);
        baseStats.reflectLight = SpriteHelper.lightenColor(baseStats.color, 0.60F);

        if(!jsonObject.has("templateColors")) {
            LogHelper.userDebug("\tProperty \"templateColors\" not found. Using default generated template colors.");
        } else {
            LogHelper.userDebug("\tProperty \"templateColors\" found. Reading configured template colors.");
            JsonObject colorObject = jsonObject.getAsJsonObject("templateColors");
            baseStats.borderDark =  getTemplateColorProperty(colorObject, "borderDark", baseStats.borderDark);
            baseStats.borderLight =  getTemplateColorProperty(colorObject, "borderLight", baseStats.borderLight);
            baseStats.shadingDark =  getTemplateColorProperty(colorObject, "shadingDark", baseStats.shadingDark);
            baseStats.shadingLight =  getTemplateColorProperty(colorObject, "shadingLight", baseStats.shadingLight);
            baseStats.reflectDark =  getTemplateColorProperty(colorObject, "reflectDark", baseStats.reflectDark);
            baseStats.reflectLight =  getTemplateColorProperty(colorObject, "reflectLight", baseStats.reflectLight);
        }
    }

    /*
        General retrieval methods
    */

    /**
     * Retrieves a string property value from the tool config json. Throws an exception if the property is not found.
     *
     * @param prop The property name
     * @return The string value if found
     */
    private static String getStringProperty(String prop) throws NullPointerException {
        if(jsonObject.has(prop)) {
            return  jsonObject.get(prop).getAsString();
        } else {
            throw new NullPointerException("Required Property \"" + prop + "\" not found.");
        }
    }

    /**
     * Retrieves an integer property value from the tool config json or returns a default value if the property is not found.
     *
     * @param prop The property name
     * @param defaultValue The the default property value
     * @return
     */
    public static int getIntProperty(String prop, int defaultValue) {
        if(jsonObject.has(prop)) {
            int value = jsonObject.get(prop).getAsInt();
            LogHelper.userDebug("\tProperty \"" + prop + "\" set to: " + value);
            return value;
        } else {
            LogHelper.userDebug("\tProperty \"" + prop + "\" not found. Using default value: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Retrieves a float property value from the tool config json or returns a default value if the property is not found.
     *
     * @param prop The property name
     * @param defaultValue The default property value
     * @return
     */
    public static float getFloatProperty(String prop, float defaultValue) {
        if(jsonObject.has(prop)) {
            float value = jsonObject.get(prop).getAsFloat();
            LogHelper.userDebug("\tProperty \"" + prop + "\" set to: " + value);
            return value;
        } else {
            LogHelper.userDebug("\tProperty \"" + prop + "\" not found. Using default value: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Retrieves a boolean property value from the tool config json or returns a default value if the property is not found.
     *
     * @param prop The property name
     * @param defaultValue The default property value
     * @return
     */
    private static boolean getBooleanProperty(String prop, boolean defaultValue) {
        if(jsonObject.has(prop)) {
            boolean value = jsonObject.get(prop).getAsBoolean();
            LogHelper.userDebug("\tProperty \"" + prop + "\" set to: " + value);
            return value;
        } else {
            LogHelper.userDebug("\tProperty \"" + prop + "\" not found. Using default value: " + defaultValue);
            return defaultValue;
        }
    }

    /*
        Specific retrieval methods
    */

    /**
     * Gets the byte array of the display name from the tool config json.
     *
     * @return The bytecode relating to the display name string
     */
    private static byte[] getDisplayNameProperty() {
        if(jsonObject.has("displayName")) {
            byte[] rawName = jsonObject.get("displayName").getAsString().getBytes(StandardCharsets.UTF_8);
            LogHelper.userDebug("\tProperty \"displayName\" set to: " + new String(rawName, StandardCharsets.UTF_8));
            return rawName;
        } else {
            LogHelper.userDebug("\tProperty \"displayName\" not found. Display name will default to Minecraft language files.");
            return null;
        }
    }

    /**
     * Gets the tier property from the tool config json and checks whether it is a valid value.
     *
     * @return Integer value of 1, 2 or 3
     */
    private static int getTierProperty() throws NullPointerException, IllegalArgumentException {
        int tier = 2;
        if(jsonObject.has("tier")) {
            tier = jsonObject.get("tier").getAsInt();

            if(tier < 1 || tier > 3)
                throw new IllegalArgumentException("\tProperty \"tier\" value invalid. Accepted values are 1, 2 or 3.");

            LogHelper.userDebug("\tProperty \"tier\" set to: " + tier);
        } else {
            LogHelper.userDebug("\tProperty \"tier\" not found. Using default value: " + tier);
        }
        return tier;
    }

    /**
     * Retrieves the "breakarea" property from the tool config json and throws an exception if the property is missing
     * or the values are out of bounds.
     *
     * @return An integer array relating to the break area tool setting. Returned format is {height, width}
     */
    private static int[] getBreakAreaProperty() throws Exception, NullPointerException {
        if(jsonObject.has("breakarea")) {
            int height, width;

            //Getting property "height"
            if(jsonObject.getAsJsonObject("breakarea").has("height"))
                height = jsonObject.getAsJsonObject("breakarea").get("height").getAsInt();
            else
                throw new NullPointerException("Required \"breakarea\" sub-Property \"height\" not found.");

            //Getting property "width"
            if(jsonObject.getAsJsonObject("breakarea").has("width"))
                width = jsonObject.getAsJsonObject("breakarea").get("width").getAsInt();
            else
                throw new NullPointerException("Required \"breakarea\" sub-Property \"width\" not found.");

            if(height < 1)
                throw new Exception("Sub-Property \"height\" out of bounds. Value must be greater than 0.");
            if(width < 1)
                throw new Exception("Sub-Property \"width\" out of bounds. Value must be greater than 0.");

            LogHelper.userDebug("\tBreak Area:");
            LogHelper.userDebug("\t\tHeight: " + height);
            LogHelper.userDebug("\t\tWidth:  " + width);

            return new int[] {height, width};
        } else {
            throw new NullPointerException("Required Property \"breakarea\" not found.");
        }
    }

    /**
     * Gets the "color" property from the tool config json.
     *
     * @return The base tool color
     */
    private static Color getBaseColorProperty() throws NullPointerException, IllegalArgumentException {
        if(jsonObject.has("color")) {
            JsonObject colorObject = jsonObject.getAsJsonObject("color");
            Color toolColor = getColorProperty(colorObject);

            LogHelper.userDebug("\tRGB Base Tool Color set to:");
            LogHelper.userDebug("\t\tRed:   " + toolColor.getRed());
            LogHelper.userDebug("\t\tGreen: " + toolColor.getGreen());
            LogHelper.userDebug("\t\tBlue:  " + toolColor.getBlue());

            return toolColor;
        } else {
            throw new NullPointerException("Required Property \"color\" missing or invalid.");
        }
    }

    /**
     * Attempts to retrieve any custom template adjustments from the tool config json.
     *
     * @param colorObject The "templateColors" json object
     * @param prop The specific color property name to retrieve. Accepted values are "borderDark",
     *             "borderLight", "shadingDark", "shadingLight", "reflectDark", and "reflectLight"
     * @param defaultValue The default color value generated from the base color
     * @return The property color value or the default value if it does not exist.
     */
    private static Color getTemplateColorProperty(JsonObject colorObject, String prop, Color defaultValue) {
        Color templateColor = defaultValue;
        if(colorObject.has(prop)) {
            try {
                templateColor = getColorProperty(colorObject.getAsJsonObject(prop));
                LogHelper.userDebug("\tRGB template Color \"" + prop + "\" set to:");
            } catch(Exception e) {
                LogHelper.userDebug("\tError parsing \"" + prop + "\" property. Using default value.");
                return templateColor;
            }
        } else {
            LogHelper.userDebug("\tProperty \"" + prop + "\" not found. Using default value:");
        }

        LogHelper.userDebug("\t\tRed:   " + templateColor.getRed());
        LogHelper.userDebug("\t\tGreen: " + templateColor.getGreen());
        LogHelper.userDebug("\t\tBlue:  " + templateColor.getBlue());

        return templateColor;
    }

    /**
     * Attempts to retrieve an RGB color property from the passed json object.
     *
     * @param colorObject The json object to retrieve the color values from
     * @return The generated color value
     */
    private static Color getColorProperty(JsonObject colorObject) throws NullPointerException, IllegalArgumentException {
        int r,g,b;

        if(colorObject.has("red")) {
            r = colorObject.get("red").getAsInt();
        } else {
            throw new NullPointerException("Required property \"red\" not found.");
        }

        if(colorObject.has("green")) {
            g = colorObject.get("green").getAsInt();
        } else {
            throw new NullPointerException("Required property \"green\" not found.");
        }

        if(colorObject.has("blue")) {
            b = colorObject.get("blue").getAsInt();
        } else {
            throw new NullPointerException("Required property \"blue\" not found.");
        }
        return new Color(r,g,b);
    }
}

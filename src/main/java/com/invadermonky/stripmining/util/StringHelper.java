package com.invadermonky.stripmining.util;

import com.invadermonky.stripmining.StripMining;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class StringHelper {
    public static String getItemUnlocalizedName(String locName) {
        return getItemUnlocalizedName(locName, StripMining.MOD_ID);
    }

    public static String getItemUnlocalizedName(String locName, String modId) {
        return String.format("item.%s:%s", modId.toLowerCase(), stripUnlocalizedName(locName));
    }

    public static String getItemID(String locName) {
        return getItemID(locName, StripMining.MOD_ID);
    }

    public static String getItemID(String locName, String modId) {
        return String.format("%s:%s", modId, locName);
    }

    public static String stripUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    public static String translateString(String unlocalizedStr, String type) {
        return translateString(unlocalizedStr, type, StripMining.MOD_ID);
    }

    public static String translateString(String unlocalizedStr, String type, String modIdStr) {
        String modId = modIdStr;
        if (modId == null || modId.equals(""))
            modId = StripMining.MOD_ID;
        if (type == null || type.equals(""))
            return new TextComponentTranslation(modId.toLowerCase() + ":" + unlocalizedStr).getFormattedText();
        return new TextComponentTranslation(String.format("%s.%s:%s", type, modId.toLowerCase(), unlocalizedStr)).getFormattedText();
    }

    public static String translateFormattedSTring(String unlocalizedStr, String type, Object... parameters) {
        return translateFormattedString(unlocalizedStr, type, StripMining.MOD_ID, parameters);
    }

    public static String translateFormattedString(String unlocalizedStr, String type, String modIdStr, Object... parameters) {
        String modId = modIdStr;
        if (modId == null || modId.equals(""))
            modId = StripMining.MOD_ID;
        if (type == null || type.equals(""))
            return new TextComponentTranslation(String.format(modId.toLowerCase() + ":" + unlocalizedStr, parameters)).getFormattedText();
        return new TextComponentTranslation(String.format(String.format("%s.%s:%s", type, modId.toLowerCase(), unlocalizedStr), parameters)).getFormattedText();
    }

    public static String[] localizeAll(String[] input) {
        String[] ret = new String[input.length];
        for(int i = 0; i < input.length; i++) {
            ret[i] = I18n.format(input[i]);
        }
        return ret;
    }
}

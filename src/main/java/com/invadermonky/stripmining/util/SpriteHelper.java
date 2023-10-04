package com.invadermonky.stripmining.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.function.Function;

public class SpriteHelper {
    private static final int[] MISSINGNO_DATA = new int[256];

    public static boolean isMissingno(TextureAtlasSprite sprite) {
        return "missingno".equals(sprite.getIconName()) || "minecraft:missingno".equals(sprite.getIconName());
    }

    public static TextureAtlasSprite getSprite(ResourceLocation location, Function<ResourceLocation,TextureAtlasSprite> getter) {
        TextureAtlasSprite sprite = getter.apply(location);
        if(sprite == null) {
            sprite = getter.apply(TextureMap.LOCATION_MISSING_TEXTURE);
            if(sprite == null) {
                throw new RuntimeException("Could not load " + location + " or fallback.");
            }
        }
        if(isMissingno(sprite)) {
            LogHelper.error("Could not locate texture: " + location);
        }
        return sprite;
    }

    public static int[] getFrameData(TextureAtlasSprite sprite) {
        if(isMissingno(sprite))
            return MISSINGNO_DATA;

        int[][] data = null;
        if(sprite.getFrameCount() <= 0) {
            LogHelper.error("Could not read texture data for " + sprite.getIconName() + " - Invalid frame count: " + sprite.getFrameCount());
            return MISSINGNO_DATA;
        }
        try{
            data = sprite.getFrameTextureData(0);
        } catch (Exception e) {
            LogHelper.error(e + " Could not read texture data: " + sprite.getIconName());
            return MISSINGNO_DATA;
        }

        if(data == null || data.length <= 0 || data[0] == null || data[0].length <= 0) {
            LogHelper.error("Could not read texture data: " + sprite.getIconName() + " - frame 0 array missing");
            return MISSINGNO_DATA;
        }
        return data[0];
    }

    public static String getTemplateSpriteName(ItemTool tool, int tier) {
        return  "template_" + ItemHelper.getItemTypeString(tool) + "_" + tier;
    }

    public static String getOverlaySpriteName(ItemTool tool, int tier) {
        return "overlay_" + ItemHelper.getItemTypeString(tool) + "_" + tier;
    }

    public static Color darkenColor(Color color, float factor) {
        return new Color(
                Math.min(255, Math.round(((float) color.getRed()) * factor)),
                Math.min(255, Math.round(((float) color.getGreen()) * factor)),
                Math.min(255, Math.round(((float) color.getBlue()) * factor))
        );
    }

    public static Color lightenColor(Color color, float factor) {
        //I hate this function with a passion, but I'm too lazy to come up with something better.
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        r = Math.min(255, Math.round((255F - r) * factor + (float) r));
        g = Math.min(255, Math.round((255F - g) * factor + (float) g));
        b = Math.min(255, Math.round((255F - b) * factor + (float) b));

        return new Color(r,g,b);
    }
}

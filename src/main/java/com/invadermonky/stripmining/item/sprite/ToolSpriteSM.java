package com.invadermonky.stripmining.item.sprite;

import com.google.common.collect.ImmutableSet;
import com.invadermonky.stripmining.StripMining;
import com.invadermonky.stripmining.item.IItemToolSM;
import com.invadermonky.stripmining.item.stats.ToolStatsBase;
import com.invadermonky.stripmining.util.SpriteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Collection;
import java.util.function.Function;

public class ToolSpriteSM extends TextureAtlasSprite {
    private final ResourceLocation template;
    private final ResourceLocation overlay;
    private final Color baseColor;
    private final ToolStatsBase baseStats;

    /**
     * Generates a tool sprite using the tool stats obtained from the tool config json.
     *
     * @param spriteName The string resource location of the newly generated tool sprite
     * @param tool The tool to generate the sprite for
     */
    public ToolSpriteSM(String spriteName, ItemTool tool) {
        super(spriteName);

        int toolTier = ((IItemToolSM) tool).getToolBaseStats().tier;

        this.template = new ResourceLocation(StripMining.MOD_ID, "items/" + SpriteHelper.getTemplateSpriteName(tool, toolTier));
        this.overlay = new ResourceLocation(StripMining.MOD_ID, "items/" + SpriteHelper.getOverlaySpriteName(tool, toolTier));
        this.baseColor = ((IItemToolSM) tool).getToolBaseColor();
        this.baseStats = ((IItemToolSM) tool).getToolBaseStats();
    }

    protected void addFrameTextureData(int[] data) {
        int[][] templateData = new int[Minecraft.getMinecraft().getTextureMapBlocks().getMipmapLevels() + 1][];
        templateData[0] = data;
        framesTextureData.add(templateData);
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableSet.of(template, overlay);
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        TextureAtlasSprite baseTex = SpriteHelper.getSprite(template, textureGetter);

        int[] templateData = new int[baseTex.getIconHeight() * baseTex.getIconWidth()];
        int[] templateInput = SpriteHelper.getFrameData(baseTex);
        for(int ix = 0; ix < baseTex.getIconWidth(); ix++) {
            for(int iy = 0; iy < baseTex.getIconHeight(); iy++) {
                int ip = iy * baseTex.getIconWidth() + ix;

                if(templateInput[ip] == 0)
                    continue;

                Color pixel = new Color(templateInput[ip]);

                //See TEMPLATE_COLORS.md
                if(pixel.equals(Color.RED))
                    templateData[ip] = baseStats.borderDark.getRGB() | 0xFF000000;
                else if(pixel.equals(Color.GREEN))
                    templateData[ip] = baseStats.borderLight.getRGB() | 0xFF000000;
                else if(pixel.equals(Color.BLUE))
                    templateData[ip] = baseStats.shadingDark.getRGB() | 0xFF000000;
                else if(pixel.equals(Color.YELLOW))
                    templateData[ip] = baseStats.shadingLight.getRGB() | 0xFF000000;
                else if(pixel.equals(Color.MAGENTA))
                    templateData[ip] = baseStats.reflectDark.getRGB() | 0xFF000000;
                else if(pixel.equals(Color.CYAN))
                    templateData[ip] = baseStats.reflectLight.getRGB() | 0xFF000000;
                else if(pixel.equals(Color.WHITE))
                    templateData[ip] = baseColor.getRGB() | 0xFF000000;
            }
        }

        setIconWidth(baseTex.getIconWidth());
        setIconHeight(baseTex.getIconHeight());
        addFrameTextureData(templateData);

        return false;
    }
}

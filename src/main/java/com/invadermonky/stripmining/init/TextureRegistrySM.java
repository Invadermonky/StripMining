package com.invadermonky.stripmining.init;

import com.invadermonky.stripmining.StripMining;
import com.invadermonky.stripmining.item.IItemToolSM;
import com.invadermonky.stripmining.item.sprite.ToolSpriteSM;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class TextureRegistrySM {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerTextures(TextureStitchEvent.Pre event) {
        for(ItemTool tool : ItemRegistrySM.tools) {
            if(tool instanceof IItemToolSM) {
                ResourceLocation spriteLoc = new ResourceLocation(StripMining.MOD_ID, "items/" + tool.getRegistryName().getPath());
                ToolSpriteSM toolSprite = new ToolSpriteSM(spriteLoc.toString(), tool);
                event.getMap().setTextureEntry(toolSprite);
            }
        }
    }
}

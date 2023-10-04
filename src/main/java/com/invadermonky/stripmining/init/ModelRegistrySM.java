package com.invadermonky.stripmining.init;

import com.google.common.collect.ImmutableMap;
import com.invadermonky.stripmining.StripMining;
import com.invadermonky.stripmining.item.IItemToolSM;
import com.invadermonky.stripmining.util.LogHelper;
import com.invadermonky.stripmining.util.SpriteHelper;
import com.invadermonky.stripmining.util.StringHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class ModelRegistrySM {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerItemRenders(ModelRegistryEvent event) {
        for(ItemTool tool : ItemRegistrySM.tools)
            registerItemRender(tool);

        LogHelper.debug("Registered all item renders.");
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void modelBake(ModelBakeEvent event) {
        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/handheld"));

            for(ItemTool tool : ItemRegistrySM.tools) {
                LogHelper.debug("Registry Name: " + tool.getRegistryName().toString());
                ResourceLocation toolColor = new ResourceLocation(StripMining.MOD_ID, "items/" + tool.getRegistryName().getPath());
                ResourceLocation toolOverlay = new ResourceLocation(StripMining.MOD_ID, "items/" + SpriteHelper.getOverlaySpriteName(tool, ((IItemToolSM) tool).getToolBaseStats().tier));

                IModel retexturedModel = baseModel.retexture(
                        ImmutableMap.of(
                                "layer0", toolColor.toString(),
                                "layer1", toolOverlay.toString()
                        )
                );

                IBakedModel bakedModel = retexturedModel.bake(ModelRotation.X0_Y0, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
                ModelResourceLocation bakedModelLocation = new ModelResourceLocation(tool.delegate.name(), "inventory");
                event.getModelRegistry().putObject(bakedModelLocation, bakedModel);
            }
            LogHelper.debug("Finished baking models.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerItemRender(Item item) {
        if(item == null)
            return;
        LogHelper.debug("Registering model for item: " + item.getRegistryName().toString());
        ModelResourceLocation loc = new ModelResourceLocation(StringHelper.stripUnlocalizedName(item.getTranslationKey()), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }
}

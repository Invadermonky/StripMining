package com.invadermonky.stripmining.proxy;

import com.invadermonky.stripmining.event.AnvilEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(AnvilEvent.INSTANCE);
    }
    public void init(FMLInitializationEvent event) {}
    public void postInit(FMLPostInitializationEvent event) {}
}

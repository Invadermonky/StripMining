package com.invadermonky.stripmining;

import com.invadermonky.stripmining.handlers.ConfigHandler;
import com.invadermonky.stripmining.handlers.NetworkHandler;
import com.invadermonky.stripmining.proxy.CommonProxy;
import com.invadermonky.stripmining.util.LogHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = StripMining.MOD_ID,
        name = StripMining.MOD_NAME,
        version = StripMining.MOD_VERSION,
        acceptedMinecraftVersions = StripMining.MC_VERSION
)
public class StripMining {
    public static final String MOD_ID = "stripmining";
    public static final String MOD_NAME = "Strip Mining";
    public static final String MOD_VERSION = "1.1.0";
    public static final String MC_VERSION = "[1.12.2]";

    public static final String ProxyClientClass = "com.invadermonky.stripmining.proxy.ClientProxy";
    public static final String ProxyServerClass = "com.invadermonky.stripmining.proxy.CommonProxy";

    @Mod.Instance(MOD_ID)
    public static StripMining INSTANCE;

    @SidedProxy(clientSide = ProxyClientClass, serverSide = ProxyServerClass)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.info("Starting Strip Mining.");
        proxy.preInit(event);
        ConfigHandler.preInit();
        LogHelper.debug("Finished preInit phase.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        NetworkHandler.init();
        LogHelper.debug("Finished init phase.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        LogHelper.debug("Finished postInit phase.");
    }
}

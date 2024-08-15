package com.invadermonky.stripmining.init;

import com.invadermonky.stripmining.StripMining;
import com.invadermonky.stripmining.handlers.ConfigHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = StripMining.MOD_ID)
public class SoundRegistrySM {
    public static final SoundEvent PROSPECTING;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        if(ConfigHandler.enableProspectingPicks) {
            registry.register(PROSPECTING);
        }
    }

    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation loc = new ResourceLocation(StripMining.MOD_ID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    static {
        PROSPECTING = makeSoundEvent("prospecting");
    }
}

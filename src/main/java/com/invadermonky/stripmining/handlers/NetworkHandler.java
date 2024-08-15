package com.invadermonky.stripmining.handlers;

import com.invadermonky.stripmining.StripMining;
import com.invadermonky.stripmining.util.ChatHelper;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(StripMining.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(ChatHelper.PacketNoSpamMessage.Handler.class, ChatHelper.PacketNoSpamMessage.class, 0, Side.CLIENT);
    }
}

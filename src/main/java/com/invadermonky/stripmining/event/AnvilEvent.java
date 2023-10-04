package com.invadermonky.stripmining.event;

import com.invadermonky.stripmining.item.IItemToolSM;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilEvent {
    public static final AnvilEvent INSTANCE = new AnvilEvent();

    @SubscribeEvent
    public void checkRepairable(AnvilUpdateEvent event) {
        ItemStack right =  event.getRight();
        ItemStack left = event.getLeft();

        if(right.isEmpty() || left.isEmpty())
            return;

        if(right.getItem() instanceof IItemToolSM) {
            if(!right.getItem().isRepairable()) {
                event.setCanceled(true);
            }
        } else if(left.getItem() instanceof IItemToolSM) {
            if(!left.getItem().isRepairable()) {
                event.setCanceled(true);
            }
        }
    }
}

package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;

public class Iteminfo {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static ItemStack selectedItem;

    public static void runItemInfoKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MlummClient.GetItemInfoKey.wasPressed()) {
                if (selectedItem != null) { attemptItemInfo(null); }
            }});}

    /** Pass {@code null} as an argument to display iteminfo; pass an {@code ItemStack} object to set the var */
    public static void attemptItemInfo(ItemStack item) {
        if (item != null) { selectedItem = item; }
        if (item == null) { displayIteminfoFromGUI(selectedItem); }
    }

    public static void displayIteminfoFromGUI(ItemStack item) {
        LOGGER.info("Displaying iteminfo for " + item.getName());
    }
}
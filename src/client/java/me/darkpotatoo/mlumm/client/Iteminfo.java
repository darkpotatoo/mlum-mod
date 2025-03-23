package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.slf4j.Logger;

public class Iteminfo {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void initItemInfoKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MlummClient.GetItemInfoKey.wasPressed()) {
                client.player.sendMessage(Text.literal("Key 1 was pressed!"), false);
                displayIteminfoFromGUI();
            }
        });
    }

    public static void displayIteminfoFromGUI() {
        LOGGER.info("Attempting to display iteminfo");
        MinecraftClient client = MinecraftClient.getInstance();
        ItemStack item = client.player.currentScreenHandler.getCursorStack();
        if (!item.isEmpty()) {
            String itemName = item.getName().getString();
            client.player.sendMessage(Text.literal("Held Item: " + itemName), false);
        }
    }
}
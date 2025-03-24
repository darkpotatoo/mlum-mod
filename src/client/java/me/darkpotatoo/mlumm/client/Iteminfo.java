package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Objects;

public class Iteminfo {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static ItemStack selectedItem;
    public static ArrayList<Item> items = new ArrayList<>();

    public static void runItemInfoKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MlummClient.GetItemInfoKey.wasPressed()) {
                if (selectedItem != null) { attemptItemInfo(null); }
            }});}

    /** Pass {@code null} as an argument to display iteminfo; pass an {@code ItemStack} object to set the var */
    public static void attemptItemInfo(ItemStack item) {
        if (item != null) { selectedItem = item; }
        if (item == null && selectedItem != null) {
            displayIteminfoFromGUI(selectedItem);
            selectedItem = null;
        }
    }

    private static void displayIteminfoFromGUI(ItemStack item) {
        String itemName = item.getName().toString().replaceAll("^literal\\{|}$", "");
        MinecraftClient client = MinecraftClient.getInstance();
        for (Item thing : items) {
            if (Objects.equals(thing.name, itemName)) {
                client.setScreen(new IteminfoScreen(thing));
                LOGGER.info("Displaying iteminfo for " + itemName);
                return;
            }
        }
        sendCustomToast("Iteminfo Failed", "There is no iteminfo for your currently hovered item.");
    }

    private static void sendCustomToast(String text, String text2) {
        MinecraftClient client = MinecraftClient.getInstance();
        ToastManager toastManager = client.getToastManager();
        toastManager.add(new SystemToast(SystemToast.Type.WORLD_BACKUP, Text.of(text), Text.of(text2)));
    }

    public static void InitItems() {
        LOGGER.info("Registering items for iteminfo");
        Item baton = new Item(
                ItemType.Weapon,
                "Baton",
                0,
                new String[]{"Cannot be crafted"},
                ItemSource.Guard,
                new ItemStack(Items.STONE_SWORD));

    }
}


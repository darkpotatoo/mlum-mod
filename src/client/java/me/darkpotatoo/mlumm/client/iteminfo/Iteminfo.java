package me.darkpotatoo.mlumm.client.iteminfo;

import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.MlummClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.darkpotatoo.mlumm.client.misc.UtilMethods.sendCustomToast;

public class Iteminfo {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static ItemStack selectedItem;
    public static ArrayList<Item> items = new ArrayList<>();

    public static void runItemInfoKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MlummClient.getItemInfoKey.wasPressed()) {
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
        String itemName = item.getName().toString();

        MinecraftClient client = MinecraftClient.getInstance();
        for (Item thing : items) {
            Pattern pattern = Pattern.compile(Pattern.quote(thing.name), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(itemName);
            if (matcher.find()) {
                client.setScreen(new IteminfoScreen(thing));
                LOGGER.debug("Displaying iteminfo for " + itemName);
                return;
            }
        }
        sendCustomToast("Iteminfo Failed", "There is no iteminfo for your currently hovered item.");
    }

    // This is VERY long. This registers ALL items. There is nothing under this to see.
    public static void initItems() {
        LOGGER.info("Registering items for iteminfo");
        new Item(
                ItemType.Weapon,
                "Baton",
                0,
                new String[]{"Cannot be crafted"},
                ItemSource.Guard,
                new ItemStack(Items.STONE_SWORD));
        new Item(
                ItemType.Escape,
                "Grappling Hook",
                39,
                new String[]{"1x Tool Handle", "2x Rope", "1x Grapple Head"},
                ItemSource.Crafting,
                new ItemStack(Items.FISHING_ROD));
    }
}


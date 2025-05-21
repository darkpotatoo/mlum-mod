package me.darkpotatoo.mlumm.client.iteminfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class ItemCosts {

    public static void updateTooltip(List<Text> lines) {
        PlayerInventory inventory = MinecraftClient.getInstance().player.getInventory();

        for (int i = 1; i < lines.size(); i++) {
            Text line = lines.get(i);
            String lineString = line.getString();

            if (lineString.startsWith("• ")) {
                String[] parts = lineString.split(" ", 3);
                if (parts.length < 3 || !parts[1].endsWith("x")) continue;

                int requiredQuantity = Integer.parseInt(parts[1].substring(0, parts[1].length() - 1));
                String itemName = parts[2];
                int count = 0;
                boolean hasItem = false;
                for (ItemStack item : inventory.main) {
                    if (item.getName().getString().equals(itemName)) {
                        count += item.getCount();
                        if (count >= requiredQuantity) {
                            hasItem = true;
                        }
                    }
                }

                String modifiedLine = (hasItem ? "§a✔" : "§c❌") + " " + requiredQuantity + "x " + itemName;
                lines.set(i, Text.literal(modifiedLine));
            }
        }
    }
}
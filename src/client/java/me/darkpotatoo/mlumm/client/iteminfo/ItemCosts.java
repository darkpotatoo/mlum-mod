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

            if (lineString.startsWith(" - ")) {
                String content = lineString.substring(3);

                int firstSpace = content.indexOf(' ');
                if (firstSpace == -1) continue;

                String quantityPart = content.substring(0, firstSpace);
                String itemName = content.substring(firstSpace + 1);

                if (!quantityPart.endsWith("x")) continue;

                int requiredQuantity;
                try {
                    requiredQuantity = Integer.parseInt(quantityPart.substring(0, quantityPart.length() - 1));
                } catch (NumberFormatException e) {
                    continue;
                }

                int count = 0;
                boolean hasItem = false;
                for (int slot = 0; slot < PlayerInventory.MAIN_SIZE; slot++) {
                    ItemStack item = inventory.getStack(slot);
                    if (item.getName().getString().equals(itemName)) {
                        count += item.getCount();
                        if (count >= requiredQuantity) {
                            hasItem = true;
                            break;
                        }
                    }
                }

                String modifiedLine = (hasItem ? "§a✔" : "§c❌") + " " + requiredQuantity + "x " + itemName;
                lines.set(i, Text.literal(modifiedLine));
            }
        }
    }
}
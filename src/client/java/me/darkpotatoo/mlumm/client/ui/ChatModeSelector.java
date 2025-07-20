package me.darkpotatoo.mlumm.client.ui;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChatModeSelector {

    private static final Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static String selectedMode = ""; // "" = ALL
    public static String typed = "";

    private static final Map<String, String> modes = new LinkedHashMap<>() {{
        put("ALL", "");               // No prefix for ALL
        put("GANG", "/ggch ");
        put("TEAM", "/teamchat ");
        put("STAFF", "/sch ");
    }};

    public static void render(DrawContext context) {
        if (!config.chatmode) return;

        int y = client.getWindow().getScaledHeight() - 30;
        int x = 4;
        int height = 14;
        int bgColor = 0xC0101010;
        int textColor = 0xFFFFFF;

        for (Map.Entry<String, String> entry : modes.entrySet()) {
            String label = entry.getKey();
            String mode = entry.getValue();
            int width = client.textRenderer.getWidth(label) + 8;

            boolean isSelected = selectedMode.equals(mode) || (mode.isEmpty() && selectedMode.isEmpty());
            int fillColor = isSelected ? getHighlightColor(mode) : bgColor;

            context.fill(x, y, x + width, y + height, fillColor);
            context.drawText(client.textRenderer, label, x + 4, y + 3, textColor, false);

            x += width + 4;
        }
    }

    public static void onClick(double mouseX, double mouseY) {
        if (!config.chatmode) return;

        int y = client.getWindow().getScaledHeight() - 30;
        int x = 4;

        for (Map.Entry<String, String> entry : modes.entrySet()) {
            String label = entry.getKey();
            String mode = entry.getValue();
            int width = client.textRenderer.getWidth(label) + 8;

            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 14) {
                selectedMode = mode;
                return;
            }
            x += width + 4;
        }
    }

    public static void updateTyped(String fullText) {
        for (String prefix : modes.values()) {
            if (!prefix.isEmpty() && fullText.startsWith(prefix)) {
                selectedMode = prefix;
                typed = fullText.substring(prefix.length());
                return;
            }
        }

        // No known prefix = ALL mode
        selectedMode = "";
        typed = fullText;
    }

    public static void clear() {
        typed = "";
        // Leave selectedMode to persist
    }

    private static int getHighlightColor(String mode) {
        return switch (mode) {
            case "" -> 0xC000FF00;            // ALL
            case "/ggch " -> 0xC0008000;      // GANG
            case "/teamchat " -> 0xC0FF0000;  // TEAM
            case "/sch " -> 0xC0FFAA00;       // STAFF
            default -> 0xC0101010;
        };
    }
}
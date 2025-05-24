package me.darkpotatoo.mlumm.client.misc;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ChatModeSelector {

    private static final Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
    public static String selectedMode = "";
    public static String typed = "";

    public void render(DrawContext context) {
        if (!config.chatmode) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int s_height = client.getWindow().getScaledHeight();
        int bgc = 0xC0101010;
        int highlight = switch (selectedMode) {
            case "", "/ach" -> 0xC000FF00;
            case "/ggch" -> 0xC0008000;
            case "/teamchat" -> 0xC0FF0000;
            case "/sch" -> 0xC0FFAA00;
            case "/rch" -> 0xC055FFFF;
            default -> 0xC0101010;
        };
        int textColor = 0xFFFFFF;

        // ALL
        int allWidth = client.textRenderer.getWidth("ALL") + 8;
        int x = 4;
        int y = s_height - 30;
        context.fill(x, y, x + allWidth, y + 14, selectedMode.equals("/ach") || selectedMode.isEmpty() ? highlight : bgc);
        context.drawText(client.textRenderer, "ALL", x + 4, y + 3, textColor, false);

        // GANG
        int gangWidth = client.textRenderer.getWidth("GANG") + 8;
        int gangX = x + allWidth + 4;
        context.fill(gangX, y, gangX + gangWidth, y + 14, selectedMode.equals("/ggch") ? highlight : bgc);
        context.drawText(client.textRenderer, "GANG", gangX + 4, y + 3, textColor, false);

        // TEAM
        int teamWidth = client.textRenderer.getWidth("TEAM") + 8;
        int teamX = gangX + gangWidth + 4;
        context.fill(teamX, y, teamX + teamWidth, y + 14, selectedMode.equals("/teamchat") ? highlight : bgc);
        context.drawText(client.textRenderer, "TEAM", teamX + 4, y + 3, textColor, false);

        // STAFF
        int staffWidth = client.textRenderer.getWidth("STAFF") + 8;
        int staffX = teamX + teamWidth + 4;
        context.fill(staffX, y, staffX + staffWidth, y + 14, selectedMode.equals("/sch") ? highlight : bgc);
        context.drawText(client.textRenderer, "STAFF", staffX + 4, y + 3, textColor, false);

        // REC
        int recWidth = client.textRenderer.getWidth("REC") + 8;
        int recX = staffX + staffWidth + 4;
        context.fill(recX, y, recX + recWidth, y + 14, selectedMode.equals("/rch") ? highlight : bgc);
        context.drawText(client.textRenderer, "REC", recX + 4, y + 3, textColor, false);
    }

    public static void onClick(double mouseX, double mouseY) {
        MinecraftClient client = MinecraftClient.getInstance();
        int s_height = client.getWindow().getScaledHeight();

        int allWidth = client.textRenderer.getWidth("ALL") + 8;
        int allX = 4;
        int allY = s_height - 30;

        int gangWidth = client.textRenderer.getWidth("GANG") + 8;
        int gangX = allX + allWidth + 4;

        int teamWidth = client.textRenderer.getWidth("TEAM") + 8;
        int teamX = gangX + gangWidth + 4;

        int staffWidth = client.textRenderer.getWidth("STAFF") + 8;
        int staffX = teamX + teamWidth + 4;

        int recWidth = client.textRenderer.getWidth("REC") + 8;
        int recX = staffX + staffWidth + 4;

        if (mouseX >= allX && mouseX <= allX + allWidth && mouseY >= allY && mouseY <= allY + 14) {
            selectedMode = "/ach";
            updateChatInput(selectedMode);
        }

        if (mouseX >= gangX && mouseX <= gangX + gangWidth && mouseY >= allY && mouseY <= allY + 14) {
            selectedMode = "/ggch";
            updateChatInput(selectedMode);
        }

        if (mouseX >= teamX && mouseX <= teamX + teamWidth && mouseY >= allY && mouseY <= allY + 14) {
            selectedMode = "/teamchat";
            updateChatInput(selectedMode);
        }

        if (mouseX >= staffX && mouseX <= staffX + staffWidth && mouseY >= allY && mouseY <= allY + 14) {
            selectedMode = "/sch";
            updateChatInput(selectedMode);
        }

        if (mouseX >= recX && mouseX <= recX + recWidth && mouseY >= allY && mouseY <= allY + 14) {
            selectedMode = "/rch";
            updateChatInput(selectedMode);
        }
    }

    private static void updateChatInput(String mode) {
        typed = mode;
    }
}
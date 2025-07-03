package me.darkpotatoo.mlumm.client.ui;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ProgTrack {

    static int rake = 0;
    static int mop = 0;
    static int bees = 0;
    static int trash = 0;

    public static void mop() {
        mop++;
        if (mop >= 3) mop = 0;
    }
    public static void rake() {
        rake++;
        if (rake >= 10) rake = 0;
    }
    public static void bees() {
        bees++;
        if (bees >= 3) bees = 0;
    }
    public static void trash() {
        trash++;
        if (trash >= 2) mop = 0;
    }

    public void render(DrawContext context) {
        Configuration config = AutoConfig.getConfigHolder(Configuration .class).getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        String txt = String.format("§6Rake: §e%d/10 §8| §6Mop: §e%d/3 §8| §6Bees: §e%d/3 §8| §6Trash: §e%d/2", rake, mop, bees, trash);
        int x = screenWidth / 2 - client.textRenderer.getWidth(txt) / 2;
        int y = screenHeight - 60 - config.progtrackhigh;
        context.drawText(client.textRenderer, Text.of(txt), x, y, 0xFFFFFF, true);
    }
}

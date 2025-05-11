package me.darkpotatoo.mlumm.client.riot;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.Stack;

// TODO: Add more style messages and stuff when the season acc out

public class RiotMeterHud {

    private static double score = 0;
    private static final Stack<RiotMessage> things = new Stack<>();

    private static final Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

    public String getRank() {
        if (score >= 1500) return "§6§lMLUMTRAKILL";
        else if (score >= 1000) return "§4§lSSS§fʜᴀɴᴋꜱᴛᴏʀᴍ";
        else if (score >= 850) return "§4§lSS§fᴀᴅɪꜱᴛɪᴄ";
        else if (score >= 700) return "§c§lS§fᴜᴘʀᴇᴍᴇ";
        else if (score >= 500) return "§6§lA§fɴᴀʀᴄʜɪᴄ";
        else if (score >= 400) return "§e§lB§fʀᴜᴛᴀʟ";
        else if (score >= 300) return "§a§lC§fʜᴀᴏᴛɪᴄ";
        else if (score >= 100) return "§9§lD§fᴇꜱᴛʀᴜᴄᴛɪᴠᴇ";
        else return "DONTSHOWTHEFLIPPINGSCOREMETER";
    }

    public static void add(String message, double value) {
        things.add(new RiotMessage(message, 200));
        score += value;
    }
    public static void add(double value) {
        score += value;
    }
    public static void tick() {
        things.removeIf(timedMessage -> --timedMessage.ticksRemaining <= 0);
    }

    public static void decayScore() {
        if (score >= 2000) score = 2000;
        if (score >= 1500) score -= (double) 100 /20;
        else if (score >= 1000) score -= (double) 85 /20;
        else if (score >= 850) score -= (double) 70 /20;
        else if (score >= 700) score -= (double) 60 /20;
        else if (score >= 500) score -= (double) 50 /20;
        else if (score >= 400) score -= (double) 40 /20;
        else if (score >= 300) score -= (double) 35 /20;
        else if (score >= 100) score -= (double) 30 /20;
        else score -= (double) 25/20;
    }

    public void render(DrawContext context) {

        // stuff
        String rank = getRank();
        if (Objects.equals(rank, "DONTSHOWTHEFLIPPINGSCOREMETER") || !config.stylemeter) return;
        MinecraftClient client = MinecraftClient.getInstance();
        int s_width = client.getWindow().getScaledWidth();
        int width = 100;
        int height = 200;
        int x = s_width - width - 10;
        int y = 10;

        // flash red
        long time = System.currentTimeMillis();
        float oscillation = (float) (Math.sin(time / 200.0) * 0.5 + 0.5);
        int redIntensity = Math.min(255, (int) (score / 1500 * 255));
        int borderColor = (int) (redIntensity * oscillation) << 16 | 0xFF000000;

        // normal stuff
        context.fillGradient(x, y, x + width, y + height, 0xC0101010, 0xD0101010);
        context.fill(x - 1, y - 1, x + width + 1, y, borderColor);
        context.fill(x - 1, y + height, x + width + 1, y + height + 1, borderColor);
        context.fill(x - 1, y, x, y + height, borderColor);
        context.fill(x + width, y, x + width + 1, y + height, borderColor);
        context.drawText(client.textRenderer, Text.of(rank), x + 5, y + 5, 0xFFFFFF, true);

        // prog bar to next rank
        int progressBarColor = getColorForProgressBar(rank);
        float progress = getProgress();
        int progressBarWidth = (int) (width * progress);
        context.fill(x + 5, y + 25, x + 5 + progressBarWidth, y + 26, progressBarColor);

        // messages like "+THING HERE"
        tick();
        int i = 0;
        for (RiotMessage timedMessage : things) {
            context.drawText(MinecraftClient.getInstance().textRenderer, Text.of(timedMessage.message), x + 5, y + 30 + i * 15, 0xFFFFFF, false);
            i++;
            if (i>= 12) break;
        }

    }

    private int getColorForProgressBar(String rank) {
        if (rank.length() < 2 || rank.charAt(0) != '§') return 0xFFFFFFFF; // Default to white
        char colorCode = rank.charAt(1);
        return switch (colorCode) {
            case '4' -> 0xFFAA0000; // Dark Red
            case '6' -> 0xFFFFAA00; // Gold
            case '9' -> 0xFF5555FF; // Blue
            case 'a' -> 0xFF55FF55; // Green
            case 'c' -> 0xFFFF5555; // Red
            case 'e' -> 0xFFFFFF55; // Yellow
            default -> 0xFFFFFFFF;  // Default to white
        };
    }

    private static float getProgress() {
        double currentThreshold = 0;
        double nextThreshold = 0;
        if (score >= 1500) {
            currentThreshold = 1500;
            nextThreshold = 2000;
        } else if (score >= 1000) {
            currentThreshold = 1000;
            nextThreshold = 1500;
        } else if (score >= 850) {
            currentThreshold = 850;
            nextThreshold = 1000;
        } else if (score >= 700) {
            currentThreshold = 700;
            nextThreshold = 850;
        } else if (score >= 500) {
            currentThreshold = 500;
            nextThreshold = 700;
        } else if (score >= 400) {
            currentThreshold = 400;
            nextThreshold = 500;
        } else if (score >= 300) {
            currentThreshold = 300;
            nextThreshold = 400;
        } else if (score >= 100) {
            currentThreshold = 100;
            nextThreshold = 300;
        }
        float progress = (float) ((score - currentThreshold) / (nextThreshold - currentThreshold));
        return progress;
    }
    private static class RiotMessage {
        String message;
        int ticksRemaining;

        RiotMessage(String message, int ticks) {
            this.message = message;
            this.ticksRemaining = ticks;
        }
    }
}
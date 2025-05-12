package me.darkpotatoo.mlumm.client.riot;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.time.chrono.MinguoEra;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

// TODO: Add more style messages and stuff when the season acc out

public class RiotMeter {

    private static double score = 0;
    public static int combo = 0;
    public static long combolastupdated = 0;
    private long sounddelay = System.currentTimeMillis();
    private static final Stack<RiotMessage> things = new Stack<>();
    public static ArrayList<ItemStack> arsenal = new ArrayList<>();
    private static final Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

    public static boolean tryArsenal() {
        boolean e = arsenal.size() >= 2;
        arsenal = new ArrayList<>();
        return e;
    }
    public static void resetComboCheck() {
        if (System.currentTimeMillis() - combolastupdated >= 5000) combo = 0;
    }

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
        things.add(new RiotMessage(message, 400));
        score += value;
    }
    public static void add(String message, double value, int time) {
        things.add(new RiotMessage(message, time));
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
        if (score < 0) score = 0;
    }

    private String previousRank = "DONTSHOWTHEFLIPPINGSCOREMETER";
    private double previousScore = 0;

    public void render(DrawContext context) {
        tick();

        if (System.currentTimeMillis() - sounddelay > 15000) sounddelay = System.currentTimeMillis(); previousScore = 0;

        String rank = getRank();
        if (!rank.equals(previousRank) && score > previousScore) {
            previousRank = rank;
            previousScore = score;
            if (score >= 1500) MinecraftClient.getInstance().player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.8F, 2F);
            else MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.1F, (float) ( 0.00107142857*score + 0.392857143));
        }

        if (Objects.equals(rank, "DONTSHOWTHEFLIPPINGSCOREMETER") || !config.stylemeter) return;
        MinecraftClient client = MinecraftClient.getInstance();
        int s_width = client.getWindow().getScaledWidth();
        int width = 100;
        int height = 200;
        int x = s_width - width - 10;
        int y = 10;

        long time = System.currentTimeMillis();
        float oscillation = (float) (Math.sin(time / 200.0) * 0.5 + 0.5);
        int redIntensity = Math.min(255, (int) (score / 1500 * 255));
        int borderColor = (int) (redIntensity * oscillation) << 16 | 0xFF000000;

        context.fillGradient(x, y, x + width, y + height, 0xC0101010, 0xD0101010);
        context.fill(x - 1, y - 1, x + width + 1, y, borderColor);
        context.fill(x - 1, y + height, x + width + 1, y + height + 1, borderColor);
        context.fill(x - 1, y, x, y + height, borderColor);
        context.fill(x + width, y, x + width + 1, y + height, borderColor);
        context.drawText(client.textRenderer, Text.of(rank), x + 5, y + 5, 0xFFFFFF, true);

        int progressBarColor = getColorForProgressBar(rank);
        int progressBarWidth = (int) (90 * getProgress());
        context.fill(x + 5, y + 19, x + progressBarWidth +5, y + 20, progressBarColor);

        int i = 0;
        for (int j = things.size() - 1; j >= 0; j--) {
            RiotMessage timedMessage = things.get(j);
            context.drawText(client.textRenderer, Text.of(timedMessage.message), x + 5, y + 25 + i * 15, 0xFFFFFF, false);
            i++;
            if (i >= 12) break;
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
        return Math.max(0, Math.min(1, progress));
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
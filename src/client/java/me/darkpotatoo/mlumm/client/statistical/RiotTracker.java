package me.darkpotatoo.mlumm.client.statistical;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class RiotTracker {

    public static boolean isEnabled = false;
    public static double damageDealt = 0;
    public static double damageTaken = 0;
    public static int itemsPicked = 0;
    public static int riotTicks = 0;
    public static int hitsDealt = 0;
    public static int hitsTaken = 0;
    public static int kills = 0;

    public static void startRiot() {
        damageDealt = 0;
        damageTaken = 0;
        hitsTaken = 0;
        itemsPicked = 0;
        hitsDealt = 0;
        riotTicks = 0;
        kills = 0;
        isEnabled = true;
    }

    public static void endRiot() {
        isEnabled = false;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        int mins = 0;
        int secs = 0;
        while (riotTicks >= 1200) {
            riotTicks -= 1200;
            mins++;
        }
        while (riotTicks >= 20) {
            riotTicks -= 20;
            secs++;
        }
        player.sendMessage(Text.of("§e§lRiot ended! §6Time: " + mins + "m" + secs + "s"));
        player.sendMessage(Text.of("§7- §fHits taken / damage taken: §e" + hitsTaken + " / " + damageTaken));
        player.sendMessage(Text.of("§7- §fHits dealt / damage dealt: §e" + hitsDealt + " / " + damageDealt));
        player.sendMessage(Text.of("§7- §fKills: §e" + kills));
        player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE);
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("startriot")
                .executes(context -> {
                    MinecraftClient.getInstance().execute(RiotTracker::startRiot);
                    context.getSource().sendFeedback(Text.of("§a» §fStarted a riot session! §7(/endriot to end)"));
                    return 1;
                }));
        dispatcher.register(ClientCommandManager.literal("endriot")
                .executes(context -> {
                    MinecraftClient.getInstance().execute(RiotTracker::endRiot);
                    context.getSource().sendFeedback(Text.of("§a» §fEnded riot session!"));
                    return 1;
                }));
    }


}

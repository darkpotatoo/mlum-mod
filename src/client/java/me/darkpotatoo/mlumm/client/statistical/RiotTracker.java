package me.darkpotatoo.mlumm.client.statistical;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class RiotTracker {

    public static boolean isEnabled = true;
    public static double damageDealt = 0;
    public static double damageTaken = 0;

    public static void startRiot() {
        damageDealt = 0;
        damageTaken = 0;
        isEnabled = true;
    }

    public static void endRiot() {
        isEnabled = false;
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("startriot")
                .executes(context -> {
                    MinecraftClient.getInstance().execute(RiotTracker::startRiot);
                    context.getSource().sendFeedback(Text.of("§a» §fStarted a riot session! §7(/endriot to end"));
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

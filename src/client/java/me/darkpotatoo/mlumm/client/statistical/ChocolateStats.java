package me.darkpotatoo.mlumm.client.statistical;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.iteminfo.IteminfoScreen;
import me.darkpotatoo.mlumm.client.util.Delay;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;

public class ChocolateStats {

    public static int chocoCounted = 0;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static int sec;
    public static boolean isEnabled = false;

    public static void startCountingChocolate(int sec) {
        chocoCounted = 0;
        ChocolateStats.sec=sec;
        new Delay(sec, ChocolateStats::endCountingChocolate);
        isEnabled = true;
    }

    public static void endCountingChocolate() {
        LOGGER.info("Ended chocolate session with " + chocoCounted + " collected.");
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        player.sendMessage(Text.of("§a§lChocolate session completed!"));
        player.sendMessage(Text.of("§7- §fChoco/minute: §e" + chocoCounted/(sec/60)));
        player.sendMessage(Text.of("§7- §fChoco/second: §e" + chocoCounted/sec));
        player.sendMessage(Text.of("§7- §fTotal chocolate grinded: §e" + chocoCounted));
        player.sendMessage(Text.of("§7- §fSession time: §e" + sec/60 + " minutes"));
        isEnabled = false;
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("startchocolatesession")
                .then(ClientCommandManager.argument("minutes", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            int minutes = IntegerArgumentType.getInteger(context, "minutes");
                            MinecraftClient.getInstance().execute(() -> ChocolateStats.startCountingChocolate(minutes * 60));
                            context.getSource().sendFeedback(Text.of("§a» §fStarted chocolate session for §e" + minutes + " minutes"));
                            return 1;
                        })));
    }

}

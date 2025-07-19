package me.darkpotatoo.mlumm.client.misc;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class EscapeAnnouncer {

    private static boolean dontplayts = false;

    public static void register() {
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        if (!config.escsounds) return;
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (!client.inGameHud.getChatHud().getMessageHistory().isEmpty()) {
                    String lastMessage = client.inGameHud.getChatHud().getMessageHistory().getLast();
                    if ((lastMessage.contains("escape ") || lastMessage.contains("esc ")) && MlummClient.escapeTicks <= 0) {
                        MlummClient.escapeTicks = 100;
                        client.player.sendMessage(Text.of("§6» §rEscape mentioned in chat!"), false);
                    }
                }
            });
        });
    }

}
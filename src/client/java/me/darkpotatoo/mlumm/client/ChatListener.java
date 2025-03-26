package me.darkpotatoo.mlumm.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class ChatListener {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.player != null && client.inGameHud.getChatHud().getMessageHistory().size() > 0) {
                    String lastMessage = client.inGameHud.getChatHud().getMessageHistory().get(client.inGameHud.getChatHud().getMessageHistory().size() - 1);
                    if ((lastMessage.contains("escape ") || lastMessage.contains("esc ")) && MlummClient.escapeTicks <= 0) {
                        MlummClient.escapeTicks = 100;
                        client.player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, 10, 1);
                        client.player.sendMessage(Text.of("§6» §rEscape mentioned in chat!"), false);
                    }
                }
            });
        });
    }
}
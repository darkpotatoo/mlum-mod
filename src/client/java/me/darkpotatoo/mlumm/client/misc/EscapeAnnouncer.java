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

    static SoundEvent sound;
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
                        getSoundEvent(config);
                        if (!dontplayts) client.player.playSound(sound, 10, 1);
                        client.player.sendMessage(Text.of("§6» §rEscape mentioned in chat!"), false);
                    }
                }
            });
        });
    }

    private static void getSoundEvent(Configuration config) {
        if (config.escape_sound == EscapeSounds.AMETHYST) { sound = SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK; }
        if (config.escape_sound == EscapeSounds.ANVIL) { sound = SoundEvents.BLOCK_ANVIL_LAND; }
        if (config.escape_sound == EscapeSounds.COPPER) { sound = SoundEvents.BLOCK_COPPER_BREAK; }
        if (config.escape_sound == EscapeSounds.TRAPDOOR) { sound = SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN; }
        if (config.escape_sound == EscapeSounds.EXPLODE) { sound = SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE; }
        if (config.escape_sound == EscapeSounds.ILLUSIONER) { sound = SoundEvents.ENTITY_ILLUSIONER_MIRROR_MOVE; }
        if (config.escape_sound == EscapeSounds.SILENT) { sound = null; dontplayts = true; }
    }
}
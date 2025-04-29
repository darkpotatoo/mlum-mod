package me.darkpotatoo.mlumm.client.misc;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.util.escSound;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class EscapeAnnouncer {

    static SoundEvent sound;

    public static void register() {
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.player != null && client.inGameHud.getChatHud().getMessageHistory().size() > 0) {
                    String lastMessage = client.inGameHud.getChatHud().getMessageHistory().get(client.inGameHud.getChatHud().getMessageHistory().size() - 1);
                    if ((lastMessage.contains("escape ") || lastMessage.contains("esc ")) && MlummClient.escapeTicks <= 0) {
                        MlummClient.escapeTicks = 100;
                        getSoundEvent(config);
                        client.player.playSound(sound, 10, 1);
                        client.player.sendMessage(Text.of("§6» §rEscape mentioned in chat!"), false);
                    }
                }
            });
        });
    }

    private static void getSoundEvent(Configuration config) {
        if (config.escape_sound == escSound.AMETHYST) { sound = SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK; }
        if (config.escape_sound == escSound.ANVIL) { sound = SoundEvents.BLOCK_ANVIL_LAND; }
        if (config.escape_sound == escSound.COPPER) { sound = SoundEvents.BLOCK_COPPER_BREAK; }
        if (config.escape_sound == escSound.TRAPDOOR) { sound = SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN; }
        if (config.escape_sound == escSound.EXPLODE) { sound = SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE; }
        if (config.escape_sound == escSound.ILLUSIONER) { sound = SoundEvents.ENTITY_ILLUSIONER_MIRROR_MOVE; }
        if (config.escape_sound == escSound.SILENT) { sound = SoundEvents.ITEM_GOAT_HORN_PLAY; }
    }
}
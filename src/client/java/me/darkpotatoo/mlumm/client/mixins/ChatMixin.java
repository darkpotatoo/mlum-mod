package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.misc.EscapeSounds;
import me.darkpotatoo.mlumm.client.riot.RiotMeter;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatMixin {

    Configuration config;

    @Inject(method = "addMessage", at = @At("HEAD"))
    private void onChatMessage(Text message, CallbackInfo ci) {
        config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        String mss = message.getString();

        // style meter stuff
        if (mss.contains("! You knocked out")) RiotMeter.combatlogtime = System.currentTimeMillis();
        else if (mss.contains("[-] ")) RiotMeter.tryCombatlog();

        if (mss.startsWith("+ RICOSHOT x")) {
            int number = Integer.parseInt(mss.substring(12));
            RiotMeter.add("+ §bRICOSHOT §fx" + number, number * 20);
        }
        if (mss.startsWith("+ CHARGEBACK")) RiotMeter.add("+ §6CHARGEBACK", 160);

        if (mss.startsWith("+ FISTFUL OF DOLLAR x")) {
            int number = Integer.parseInt(mss.substring(21));
            RiotMeter.add("  §bDOLLAR §fx" + number, 0);
            RiotMeter.add("+ §bFISTFUL OF", number * 20);
        }

        // escape detector stuff
        if ((mss.contains("escape") || mss.contains("esc")) && MlummClient.escapeTicks <= 0) {
            MlummClient.escapeTicks = 100;
            getSoundEvent(config);
            MinecraftClient client = MinecraftClient.getInstance();
            client.player.playSound(sound, 10, 1);
            client.player.sendMessage(Text.of("§6» §rEscape mentioned in chat!"), false);
        }

    }

    private static SoundEvent sound;
    private static void getSoundEvent(Configuration config) {
        if (config.escape_sound == EscapeSounds.AMETHYST) { sound = SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK; }
        if (config.escape_sound == EscapeSounds.ANVIL) { sound = SoundEvents.BLOCK_ANVIL_LAND; }
        if (config.escape_sound == EscapeSounds.COPPER) { sound = SoundEvents.BLOCK_COPPER_BREAK; }
        if (config.escape_sound == EscapeSounds.TRAPDOOR) { sound = SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN; }
        if (config.escape_sound == EscapeSounds.EXPLODE) { sound = SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE; }
        if (config.escape_sound == EscapeSounds.ILLUSIONER) { sound = SoundEvents.ENTITY_ILLUSIONER_MIRROR_MOVE; }
        if (config.escape_sound == EscapeSounds.SILENT) { sound = SoundEvents.ITEM_GOAT_HORN_PLAY; }
    }

}
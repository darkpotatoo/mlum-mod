package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.ui.RiotMeter;
import me.darkpotatoo.mlumm.client.misc.CombatHandler;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatMixin {

    @Unique
    Configuration config;

    @Inject(method = "addMessage", at = @At("HEAD"))
    private void onChatMessage(Text message, CallbackInfo ci) {
        config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        String mss = message.getString().toLowerCase();

        if (mss.contains("a guard slot has opened")) {
            if (config.rolenotifs) {
                MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_BELL_USE, 1.0f, 1.0f);
                MinecraftClient.getInstance().inGameHud.setTitle(Text.of("§9§lGuard Slot!"));
            }
        }

        if (mss.contains("[-]")) RiotMeter.combatlogtime = System.currentTimeMillis();

        if (mss.contains("! you knocked out")) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player != null) {
                CombatHandler.onKnockoutDetected(client.player);
            }
            RiotMeter.tryCombatlog();
        }

        if (mss.startsWith("+ ricoshot x")) {
            int number = Integer.parseInt(mss.substring(12));
            RiotMeter.add("+ §bRICOSHOT §fx" + number, number * 20);
        }
        if (mss.startsWith("+ chargeback")) RiotMeter.add("+ §6CHARGEBACK", 160);
        if (mss.startsWith("! you were knocked out")) RiotMeter.add("- §4DEATH", -5000);
        if (mss.startsWith("! you claimed ")) RiotMeter.add("+ §6BOUNTY HUNTED", 300);

        if (mss.startsWith("+ fistful of dollar x")) {
            int number = Integer.parseInt(mss.substring(21));
            RiotMeter.add("  §bDOLLAR §fx" + number, 0);
            RiotMeter.add("+ §bFISTFUL OF", number * 20);
        }

        if (mss.contains("stylish!")) RiotMeter.add("+ §dSTYLISH", 100);

        if (mss.contains("you gained a strength boost")) RiotMeter.add("+ §7STRENGTH", 30);
        if (mss.contains("you gained a speed boost")) RiotMeter.add("+ §7SPEED", 30);
    }
}
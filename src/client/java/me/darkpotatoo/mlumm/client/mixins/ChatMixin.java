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

import java.util.Objects;

@Mixin(ChatHud.class)
public abstract class ChatMixin {

    Configuration config;
    private static SoundEvent sound;
    private static boolean dontplayts = false;

    @Inject(method = "addMessage", at = @At("HEAD"))
    private void onChatMessage(Text message, CallbackInfo ci) {
        config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        String mss = message.getString().toLowerCase(); // Convert to lowercase

        // style meter stuff
        if (mss.contains("! you knocked out")) RiotMeter.combatlogtime = System.currentTimeMillis();
        else if (mss.contains("[-] ")) RiotMeter.tryCombatlog();

        if (mss.startsWith("+ ricoshot x")) {
            int number = Integer.parseInt(mss.substring(12));
            RiotMeter.add("+ §bRICOSHOT §fx" + number, number * 20);
        }
        if (mss.startsWith("+ chargeback")) RiotMeter.add("+ §6CHARGEBACK", 160);

        if (mss.startsWith("+ fistful of dollar x")) {
            int number = Integer.parseInt(mss.substring(21));
            RiotMeter.add("  §bDOLLAR §fx" + number, 0);
            RiotMeter.add("+ §bFISTFUL OF", number * 20);
        }

        if (mss.equals("stylish!")) RiotMeter.add("+ §dSTYLISH", 150);
    }

}
package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.riot.RiotMeter;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatMixin {

    @Inject(method = "addMessage", at = @At("HEAD"))
    private void onChatMessage(Text message, CallbackInfo ci) {
        String mss = message.getString();

        if (mss.contains("! You knocked out")) RiotMeter.combatlogtime = System.currentTimeMillis();
        else if (mss.contains("[-] ")) RiotMeter.tryCombatlog();

        if (mss.startsWith("+ RICOSHOT x")) {
            int number = Integer.parseInt(mss.substring(12));
            RiotMeter.add("+ §bRICOSHOT §fx" + number, number * 20);
        }
    }

}
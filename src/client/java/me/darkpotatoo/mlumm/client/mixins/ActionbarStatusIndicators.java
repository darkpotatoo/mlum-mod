package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.misc.ChocolateStats;
import me.darkpotatoo.mlumm.client.misc.RiotTracker;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(InGameHud.class)
public class ActionbarStatusIndicators {

    @Unique
    boolean isUpdatingOverlayMessage = false;
    @Unique
    Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

    @Inject(method = "setOverlayMessage", at = @At("TAIL"))
    private void onSetOverlayMessage(Text message, boolean overlay, CallbackInfo ci) {
        if (!isUpdatingOverlayMessage && message.getString().contains("Period:") && config.actionbar_status) {
            MinecraftClient client = MinecraftClient.getInstance();
            isUpdatingOverlayMessage = true;
            String input = message.getString();
            String time = input.substring(input.indexOf("Time: ") + 6, input.indexOf(" |"));
            String period = input.substring(input.indexOf("Period: ") + 8);
            String text = "§6Time: §e" + time + "§8 | §6Period: §e" + period;
            if (MlummClient.combatTicks > 0) {
                text += " §8| §6Combat Timer: §e" + ((int) Math.ceil(((double) MlummClient.combatTicks / 20))) + "s";
            }
            if (RiotTracker.isEnabled) {
                text += " §8| §aTracking Riot";
            }
            if (ChocolateStats.isEnabled) {
                text += " §8| §aTracking Chocolate";
            }
            var glowingEffect = MinecraftClient.getInstance().player.getStatusEffect(StatusEffects.GLOWING);
            if (glowingEffect != null) {
                int dur = glowingEffect.getDuration();
                if (dur > 0) {
                    text += " §8| §cDetected: §e" + ((int) Math.ceil(((double) dur / 20))) + "s";
                }
            }
            client.inGameHud.setOverlayMessage(Text.of(text), false);
            isUpdatingOverlayMessage = false;
        }
    }
}
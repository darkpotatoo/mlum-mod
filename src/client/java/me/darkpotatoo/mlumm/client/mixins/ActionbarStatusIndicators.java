package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.statistical.ChocolateStats;
import me.darkpotatoo.mlumm.client.statistical.RiotTracker;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ActionbarStatusIndicators {

    private boolean isUpdatingOverlayMessage = false;
    Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

    @Inject(method = "setOverlayMessage", at = @At("TAIL"))
    private void onSetOverlayMessage(Text message, boolean overlay, CallbackInfo ci) {
        if (!isUpdatingOverlayMessage && message.getString().contains("Period:") && config.actionbar_status) {
            isUpdatingOverlayMessage = true;
            if (MlummClient.combatTicks > 0) {
                message = Text.of(message + " §8| §6Combat Timer: §e" + (MlummClient.combatTicks / 20) + "s");
            }
            if (RiotTracker.isEnabled) {
                message = Text.of(message + " §8| §eTracking Riot");
            }
            if (ChocolateStats.isEnabled) {
                message = Text.of(message + " §8| §eTracking Chocolate");
            }
            MinecraftClient client = MinecraftClient.getInstance();
            client.inGameHud.setOverlayMessage(message, false);
            isUpdatingOverlayMessage = false;
        }
    }
}
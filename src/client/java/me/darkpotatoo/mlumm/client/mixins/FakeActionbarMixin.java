package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.statistical.ChocolateStats;
import me.darkpotatoo.mlumm.client.statistical.RiotTracker;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class FakeActionbarMixin {

    Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

    @Inject(method = "render", at = @At("TAIL"))
    private void renderCustomHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!config.actionbar_status) return; // no do if turned off

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        String text = "";
        if (MlummClient.combatTicks > 0) {
            text += "§6Combat Timer: §e" + (MlummClient.combatTicks / 20) + "s";
        }
        if (RiotTracker.isEnabled) {
            if (!text.isEmpty()) {
                text += "  §7| ";
            }
            text += "§aTracking Riot";
        }
        if (ChocolateStats.isEnabled) {
            if (!text.isEmpty()) {
                text += "  §7| ";
            }
            text += "§aTracking Chocolate";
        }

        if (!text.isEmpty()) {
            int textWidth = client.textRenderer.getWidth(text);
            int x = (screenWidth - textWidth) / 2; // Center horizontally
            int y = screenHeight - 82; // Slightly above the action bar

            context.drawTextWithShadow(
                    client.textRenderer,
                    text,
                    x, y,
                    0xFFFFFF
            );
        }
    }
}
package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.MlummClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    private boolean isUpdatingOverlayMessage = false;

    @Inject(method = "setOverlayMessage", at = @At("TAIL"))
    private void onSetOverlayMessage(Text message, boolean overlay, CallbackInfo ci) {
        if (!isUpdatingOverlayMessage && message.getString().contains("Period:") && MlummClient.combatTicks > 0) {
            isUpdatingOverlayMessage = true;
            Text msg = Text.of(message.getString() + " §8| §6Combat Timer: §e" + (MlummClient.combatTicks / 20) + "s");
            MinecraftClient client = MinecraftClient.getInstance();
            client.inGameHud.setOverlayMessage(msg, false);
            isUpdatingOverlayMessage = false;
        }
    }
}
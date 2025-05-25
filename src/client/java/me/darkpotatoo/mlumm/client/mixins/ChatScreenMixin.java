package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.misc.ChatModeSelector;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {

    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button == 0) ChatModeSelector.onClick(mouseX, mouseY);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo ci) {
        ChatModeSelector.typed = chatField.getText()
                .replace("/ggch", "")
                .replace("/ach", "")
                .replace("/teamchat", "")
                .replace("/ggc", "")
                .replace("/teamcha", "")
                .replace("/ac", "")
                .replace("/sch", "")
                .replace("/sc", "");
        chatField.setText(ChatModeSelector.selectedMode +  ChatModeSelector.typed);
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void onRemoved(CallbackInfo ci) {
        ChatModeSelector.typed = "";
        ChatModeSelector.selectedMode = "";
    }
}
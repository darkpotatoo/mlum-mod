package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.misc.ChatModeSelector;
import me.darkpotatoo.mlumm.client.riot.RiotMeter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow @Final private ChatHud chatHud;
    @Unique
    private final RiotMeter hud = new RiotMeter();
    @Unique
    private final ChatModeSelector cms = new ChatModeSelector();
    @Inject(method = "render", at = @At("TAIL"))
    private void renderRiotMeterHudAndItemCost(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        hud.render(context);
        if (chatHud.isChatFocused()) cms.render(context);
    }
}
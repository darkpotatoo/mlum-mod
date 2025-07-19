package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.ui.ChatModeSelector;
import me.darkpotatoo.mlumm.client.ui.RiotMeter;
import me.shedaniel.autoconfig.AutoConfig;
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
    private final RiotMeter riotMeter = new RiotMeter();
    @Unique
    private final ChatModeSelector chatSelector = new ChatModeSelector();
    @Inject(method = "render", at = @At("TAIL"))
    private void renderRiotMeterHudAndItemCost(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        Configuration config = AutoConfig.getConfigHolder(Configuration .class).getConfig();

        if (config.stylemeter) riotMeter.render(context);
        if (chatHud.isChatFocused() && config.chatmode) chatSelector.render(context);
    }
}
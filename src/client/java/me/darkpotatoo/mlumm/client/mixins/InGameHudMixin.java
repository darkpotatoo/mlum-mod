package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.riot.RiotMeterHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    private final RiotMeterHud hud = new RiotMeterHud();
    @Inject(method = "render", at = @At("TAIL"))
    private void renderRiotMeterHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        hud.render(context);
    }
}
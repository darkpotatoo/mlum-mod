package me.darkpotatoo.mlumm.client.mixins;

import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.Iteminfo;
import me.darkpotatoo.mlumm.client.MlummClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
@Environment(EnvType.CLIENT)
public abstract class ScreenMixin {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Shadow public abstract Text getTitle();

    @Inject(method = "onDisplayed", at = @At(value = "HEAD"))
    protected void onDisplayed(CallbackInfo ci) {
        if (getTitle().getString().contains("Loot Barrel")) {
            MlummClient.crateTicks = 7200;
        }
        if (getTitle().getString().contains("Loot Desk")) {
            MlummClient.deskTicks = 7200;
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (MlummClient.GetItemInfoKey.matchesKey(keyCode, scanCode)) {
            Iteminfo.displayIteminfoFromGUI();
            cir.setReturnValue(true);
        }
    }
}

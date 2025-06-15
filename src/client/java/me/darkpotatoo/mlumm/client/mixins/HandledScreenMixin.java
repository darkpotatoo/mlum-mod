package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.ui.BMScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.darkpotatoo.mlumm.client.iteminfo.Iteminfo;

@Mixin(HandledScreen.class)
@Environment(EnvType.CLIENT)
public abstract class HandledScreenMixin {

    @Shadow
    protected Slot focusedSlot;

    @Unique
    private static ItemStack currentItem;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (focusedSlot != null && !focusedSlot.getStack().isEmpty()) {
            currentItem = focusedSlot.getStack();
            Iteminfo.attemptItemInfo(currentItem);
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInventoryOpen(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.currentScreen != null) {
            Text title = client.currentScreen.getTitle();
            if (title.getString().contains("Black Market")) {
                client.setScreen(new BMScreen());
            }
        }
    }

}
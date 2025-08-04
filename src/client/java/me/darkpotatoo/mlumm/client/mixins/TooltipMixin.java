package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.MlummClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TooltipBackgroundRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class TooltipMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private static void render(DrawContext context, int x, int y, int width, int height, int z, Identifier texture, CallbackInfo ci) {
        if (MlummClient.tooltipIsContraband) {
            int newColor = ColorHelper.getArgb(120, 110, 0, 0);
            int i = x - 3;
            int j = y - 3;
            int k = width + 6;
            int l = height + 6;

            drawAHorizontalLine(context, i, j - 1, k, z, newColor);
            drawAHorizontalLine(context, i, j + l, k, z, newColor);
            drawARectangle(context, i, j, k, l, z, newColor);
            drawAVerticalLine(context, i - 1, j, l, z, newColor);
            drawAVerticalLine(context, i + k, j, l, z, newColor);
            drawABorder(context, i, j + 1, k, l, z, 1358888960, 1350500352);
        }
    }

    private static void drawABorder(DrawContext context, int x, int y, int width, int height, int z, int startColor, int endColor) {
        drawAVerticalGradientLine(context, x, y, height - 2, z, startColor, endColor);
        drawAVerticalGradientLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        drawAHorizontalLine(context, x, y - 1, width, z, startColor);
        drawAHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }

    private static void drawAVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void drawAVerticalGradientLine(DrawContext context, int x, int y, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    private static void drawAHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }

    private static void drawARectangle(DrawContext context, int x, int y, int width, int height, int z, int color) {
        context.fill(x, y, x + width, y + height, z, color);
    }
}
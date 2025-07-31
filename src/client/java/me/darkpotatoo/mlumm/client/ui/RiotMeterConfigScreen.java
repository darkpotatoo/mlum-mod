package me.darkpotatoo.mlumm.client.ui;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class RiotMeterConfigScreen extends Screen {
    private final Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
    private boolean dragging = false;
    private boolean resizingW = false;
    private boolean resizingH = false;
    private int dragOffsetX, dragOffsetY;

    public RiotMeterConfigScreen() {
        super(Text.of("Style Meter Config"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        int x = config.riotmeter_x;
        int y = config.riotmeter_y;
        int width = config.riotmeter_width;
        int height = config.riotmeter_height;

        // Border
        context.fill(x - 1, y - 1, x + width + 1, y, 0xFFFFFFFF);
        context.fill(x - 1, y + height, x + width + 1, y + height + 1, 0xFFFFFFFF);
        context.fill(x - 1, y, x, y + height, 0xFFFFFFFF);
        context.fill(x + width, y, x + width + 1, y + height, 0xFFFFFFFF);

        // Main Box
        context.fill(x, y, x + width, y + height, 0xAA101010);
        context.drawText(textRenderer, Text.of("Drag: LMB | Resize W: RMB | Resize H: MMB"), x + 5, y + 5, 0xFFFFFF, false);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = config.riotmeter_x;
        int y = config.riotmeter_y;
        int width = config.riotmeter_width;
        int height = config.riotmeter_height;

        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                dragging = true;
                dragOffsetX = (int) mouseX - x;
                dragOffsetY = (int) mouseY - y;
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                resizingW = true;
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
                resizingH = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        resizingW = false;
        resizingH = false;
        AutoConfig.getConfigHolder(Configuration.class).save();
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (dragging) {
            config.riotmeter_x = (int) mouseX - dragOffsetX;
            config.riotmeter_y = (int) mouseY - dragOffsetY;
        } else if (resizingW) {
            config.riotmeter_width = Math.max(50, (int) mouseX - config.riotmeter_x);
        } else if (resizingH) {
            config.riotmeter_height = Math.max(50, (int) mouseY - config.riotmeter_y);
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_E) {
            MinecraftClient.getInstance().setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}

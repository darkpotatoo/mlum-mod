package me.darkpotatoo.mlumm.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class IteminfoScreen extends Screen {

    protected IteminfoScreen(String itemname) {
        super(Text.of("Item Info: " + itemname));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawText(this.textRenderer, "Custom Text", this.width / 2, this.height / 2, 0xFFFFFF, false);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

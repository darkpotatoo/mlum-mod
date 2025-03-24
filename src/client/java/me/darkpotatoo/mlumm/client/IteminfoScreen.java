package me.darkpotatoo.mlumm.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IteminfoScreen extends Screen {

    private final Item item;
    private int backgroundWidth;
    private int backgroundHeight;
    private int x;
    private int y;
    private static final Identifier background = Identifier.of("minecraft", "textures/gui/options_background.png");

    protected IteminfoScreen(Item item) {
        super(Text.of("Item Info: " + item.name));
        this.item = item;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        backgroundWidth = this.width / 4;
        backgroundHeight = this.height / 2;
        int x = (this.width - backgroundWidth) / 2;
        int y = (this.height - backgroundHeight) / 2;
        context.drawTexture(background, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawItem(item.literalItem, x + backgroundWidth - 16, (this.height - backgroundHeight) / 2);

        x = (this.width - this.textRenderer.getWidth("Item info for " + item.name)) / 2;
        y = this.height / 4 + 15;
        context.drawText(this.textRenderer, "Item info for " + item.name, x, y, 0xFFFFFF, false);
        x = (this.width - this.textRenderer.getWidth("------------------------")) / 2;
        context.drawText(this.textRenderer, "------------------------", x, y+7, 0xFFFFFF, false);
        x = (this.width - this.textRenderer.getWidth("Item type: " + item.type)) / 2;
        context.drawText(this.textRenderer, "Item type: " + item.type, x, y+13, 0xFFFFFF, false);

    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

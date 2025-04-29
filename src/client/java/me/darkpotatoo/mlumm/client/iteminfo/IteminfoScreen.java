package me.darkpotatoo.mlumm.client.iteminfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class IteminfoScreen extends Screen {

    private final Item item;
    private int backgroundWidth;
    private int backgroundHeight;
    private int x;
    private int y;

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
        x = (this.width - backgroundWidth) / 2;
        y = (this.height - backgroundHeight) / 2;
        context.fillGradient(x, y, x + backgroundWidth, y + backgroundHeight, 0xC0101010, 0xD0101010);
        int borderColor = 0xFFFFFFFF; // White color
        context.fill(x - 1, y - 1, x + backgroundWidth + 1, y, borderColor); // Top border
        context.fill(x - 1, y + backgroundHeight, x + backgroundWidth + 1, y + backgroundHeight + 1, borderColor); // Bottom border
        context.fill(x - 1, y, x, y + backgroundHeight, borderColor); // Left border
        context.fill(x + backgroundWidth, y, x + backgroundWidth + 1, y + backgroundHeight, borderColor); // Right border
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        x = ((this.width - backgroundWidth) / 2) + backgroundWidth - 20;
        y = this.height / 4 + 15;
        context.drawItem(item.literalItem, x, ((this.height - backgroundHeight) / 2) + 4);
        x = (this.width - this.textRenderer.getWidth("Item info for " + item.name)) / 2;
        context.drawText(this.textRenderer, "Item info for " + item.name, x, y, 0xFFFFFF, false);
        x = (this.width - backgroundWidth) / 2 + 10;
        context.drawText(this.textRenderer, "Item type: " + item.type, x, y+14, 0xFFFFFF, false);
        if (item.chocoCost == 0) {
            context.drawText(this.textRenderer, "Obtained from: " + item.howto, x, y + 23, 0xFFFFFF, false);
        } else {
            context.drawText(this.textRenderer, "Obtained from: " + item.howto + " (for " + item.chocoCost + " chocolate)", x, y + 23, 0xFFFFFF, false);
        }
        context.drawText(this.textRenderer, "Crafting recipe: ", x, y+41, 0xFFFFFF, false);
        for (int i = 0; i < item.craftingRecipe.length; i++) {
            context.drawText(this.textRenderer, " - " + item.craftingRecipe[i], x, y + 50 + (i * 9), 0xFFFFFF, false);
        }

    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

package me.darkpotatoo.mlumm.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

import java.util.List;

public class BMScreen extends Screen {

    private static final int swp = 40;
    private static final int shp = 40;

    private TextFieldWidget bidInput;
    private ButtonWidget bidButton;

    void bidIt(String bid) {
        MinecraftClient client = MinecraftClient.getInstance();
        //client.setScreen(null);
        client.interactionManager.clickSlot(
                client.player.currentScreenHandler.syncId,
                15,
                0,
                SlotActionType.PICKUP,
                client.player
        );
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                //SignBlockEntity sign = ((AbstractSignEditScreenAccessor) client.currentScreen).getBlockEntity();
                for (char cha : bid.toCharArray()) {
                    client.currentScreen.charTyped(cha, 0);
                }
                client.currentScreen.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public BMScreen() {
        super(Text.of("Black Market"));
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}

    @Override
    protected void init() {
        super.init();

        int screenWidth = (int) (this.width * (swp / 100.0));
        int screenHeight = (int) (this.height * (shp / 100.0));
        int screenX = (this.width - screenWidth) / 2;
        int screenY = (this.height - screenHeight) / 2;
        int leftSectionWidth = screenWidth / 3;
        int middleSectionX = screenX + leftSectionWidth;
        int middleSectionWidth = screenWidth / 3;

        bidInput = new TextFieldWidget(this.textRenderer, middleSectionX + 10, screenY + 10, middleSectionWidth - 20, 20, Text.of("Enter bid"));
        this.addDrawableChild(bidInput);
        bidButton = ButtonWidget.builder(Text.of("BID"), button -> bidIt(bidInput.getText())).dimensions(middleSectionX + (middleSectionWidth - 100) / 2, screenY + 40, 100, 20).build();
        this.addDrawableChild(bidButton);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int screenWidth = (int) (this.width * (swp / 100.0));
        int screenHeight = (int) (this.height * (shp / 100.0));
        int screenX = (this.width - screenWidth) / 2;
        int screenY = (this.height - screenHeight) / 2;

        context.fill(screenX, screenY, screenX + screenWidth, screenY + screenHeight, 0xCC202020);
        int leftSectionWidth = screenWidth / 3;
        context.fill(screenX - 1, screenY - 1, screenX + leftSectionWidth + 1, screenY + screenHeight + 1, 0xFF555555);
        context.fill(screenX, screenY, screenX + leftSectionWidth, screenY + screenHeight, 0x88000000); // Background
        int rightSectionX = screenX + (2 * screenWidth / 3);
        context.fill(rightSectionX - 1, screenY - 1, rightSectionX + (screenWidth / 3) + 1, screenY + screenHeight + 1, 0xFF555555);
        context.fill(rightSectionX, screenY, rightSectionX + (screenWidth / 3), screenY + screenHeight, 0x88000000); // Background

        ItemStack itemStack = MinecraftClient.getInstance().player.currentScreenHandler.getSlot(11).getStack();
        List<Text> loreLines = itemStack.getTooltip(Item.TooltipContext.DEFAULT, MinecraftClient.getInstance().player, TooltipType.ADVANCED);
        String time = "";
        if (loreLines.size() >= 3) {
            time = loreLines.get(loreLines.size() - 3).getString();
        }
        String cb = MinecraftClient.getInstance().player.currentScreenHandler.getSlot(15).getStack().getName().getString().replace("Top Bid: ", "").replace(" chocolate", "");
        String bidder = MinecraftClient.getInstance().player.currentScreenHandler.getSlot(22).getStack().getName().getString().replace("Top Bidder: ", "");

        int textY = screenY + 10;
        context.drawText(this.textRenderer, "§6CURRENT BID:", rightSectionX + 10, textY, 0xFFFFFF, false);
        textY += 10;
        context.drawText(this.textRenderer, "§e" + cb + " chocolate", rightSectionX + 10, textY, 0xFFFFFF, false);
        textY += 10;
        context.drawText(this.textRenderer, "§7Bidder: " + bidder, rightSectionX + 10, textY, 0xFFFFFF, false);
        textY += 40; // blank
        context.drawText(this.textRenderer, "§6TIME REMAINING:", rightSectionX + 10, textY, 0xFFFFFF, false);
        textY += 10;
        context.drawText(this.textRenderer, "§e" + time, rightSectionX + 10, textY, 0xFFFFFF, false);

        if (!itemStack.isEmpty()) {
            textY = screenY + 10;
            int i = 0;
            for (Text line : loreLines) {
                i++;
                if (i >= loreLines.size() - 1) break; // skip last
                context.drawText(this.textRenderer, line, screenX + 10, textY, 0xFFFFFF, false);
                textY += 10;
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
package me.darkpotatoo.mlumm.client.ui;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
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
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
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
                Thread.sleep(config.bmdelay);
                //SignBlockEntity sign = ((AbstractSignEditScreenAccessor) client.currentScreen).getBlockEntity();
                for (char cha : bid.toCharArray()) {
                    client.currentScreen.charTyped(cha, 0);
                    Thread.sleep(50);
                }
                if (client.currentScreen instanceof SignEditScreen signScreen) {
                    for (var child : signScreen.children()) {
                        if (child instanceof ButtonWidget button && button.getMessage().getString().equals("Done")) {
                            button.onPress();
                            break;
                        }
                    }
                }
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

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = (int) (client.getWindow().getScaledWidth() * (swp / 100.0));
        int screenHeight = (int) (client.getWindow().getScaledHeight() * (shp / 100.0));
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
        try {
            this.renderBackground(context, mouseX, mouseY, delta);

            MinecraftClient client = MinecraftClient.getInstance();
            int screenWidth = (int) (client.getWindow().getScaledWidth() * (swp / 100.0));
            int screenHeight = (int) (client.getWindow().getScaledHeight() * (shp / 100.0));
            int screenX = (client.getWindow().getScaledWidth() - screenWidth) / 2;
            int screenY = (client.getWindow().getScaledHeight() - screenHeight) / 2;

            context.fill(screenX, screenY, screenX + screenWidth, screenY + screenHeight, 0xCC202020);
            int leftSectionWidth = screenWidth / 3;
            context.fill(screenX - 1, screenY - 1, screenX + leftSectionWidth + 1, screenY + screenHeight + 1, 0xFF555555);
            context.fill(screenX, screenY, screenX + leftSectionWidth, screenY + screenHeight, 0x88000000); // Background
            int rightSectionX = screenX + (2 * screenWidth / 3);
            context.fill(rightSectionX - 1, screenY - 1, rightSectionX + (screenWidth / 3) + 1, screenY + screenHeight + 1, 0xFF555555);
            context.fill(rightSectionX, screenY, rightSectionX + (screenWidth / 3), screenY + screenHeight, 0x88000000); // Background

            ItemStack itemStack = client.player.currentScreenHandler.getSlot(11).getStack();
            List<Text> loreLines = itemStack.getTooltip(Item.TooltipContext.DEFAULT, client.player, TooltipType.ADVANCED);
            String time = "";
            if (loreLines.size() >= 3) {
                time = loreLines.get(loreLines.size() - 3).getString();
                String[] parts = time.split(":");
                int seconds = 999;
                try {
                    int minutes = Integer.parseInt(parts[0]);
                    int secs = Integer.parseInt(parts[1]);
                    seconds = minutes * 60 + secs;
                } catch (Exception ignored) {}
                if (seconds < 5) {
                    time = "§4" + time; // dark red
                } else if (seconds < 10) {
                    time = "§c" + time; // red
                } else if (seconds < 20) {
                    time = "§e" + time; // yellow
                } else {
                    time = "§a" + time; // green
                }
            }
            ItemStack b = client.player.currentScreenHandler.getSlot(22).getStack();
            List<Text> loreLines2 = b.getTooltip(Item.TooltipContext.DEFAULT, client.player, TooltipType.ADVANCED);
            String bidder = loreLines2.get(2).getString().replace("Bid Holder: ", "");
            String cb = loreLines2.get(1).getString().replace("Top Bid: ", "").replace(" chocolate", "");

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
                int count = itemStack.getCount();
                String nameText = count + "x " + itemStack.getName().getString();
                String duraText = itemStack.isDamaged()
                        ? "(" + (itemStack.getMaxDamage() - itemStack.getDamage()) + "/" + itemStack.getMaxDamage() + " dura)"
                        : "(no dura)";
                int fontHeight = this.textRenderer.fontHeight;
                int totalHeight = fontHeight * 2 + 2;

                int centerY = textY + 10;
                int startY = centerY - totalHeight / 2;

                context.drawItem(itemStack, screenX + 12, textY + 2);
                context.drawText(this.textRenderer, "§f" + nameText, screenX + 32, startY+1, 0xFFFFFF, false);
                context.drawText(this.textRenderer, duraText, screenX + 32, startY+1 + fontHeight + 2, 0xAAAAAA, false);

                textY += 20;
                int i = 0;
                for (Text line : loreLines) {
                    i++;
                    if (i == 1) continue;
                    if (i >= loreLines.size() - 1) break;
                    context.drawText(this.textRenderer, line, screenX + 10, textY, 0xFFFFFF, false);
                    textY += 10;
                }
            }

            super.render(context, mouseX, mouseY, delta);
        } catch (Exception ignored) {}
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    int getDurabilityColor(ItemStack stack) {
        float f = Math.max(0.0f, ((float)(stack.getMaxDamage() - stack.getDamage()) / (float)stack.getMaxDamage()));
        int red = (int)((1.0f - f) * 255.0f);
        int green = (int)(f * 255.0f);
        return 0xFF000000 | (red << 16) | (green << 8);
    }

}
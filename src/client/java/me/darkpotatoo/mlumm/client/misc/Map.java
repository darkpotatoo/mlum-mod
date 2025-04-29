package me.darkpotatoo.mlumm.client.misc;

import me.darkpotatoo.mlumm.client.MlummClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Map extends Screen {

    private static final Identifier MAP_TEXTURE = Identifier.of("mlumm", "textures/gui/map.png");
    private final List<POI> pois = new ArrayList<>();

    int RED = 0xFFFF0000;
    int ORANGE = 0xFFFFA500;
    int YELLOW = 0xFFFFFF00;
    int GREEN = 0xFF00FF00;
    int BLUE = 0xFF0000FF;
    int PURPLE = 0xFF800080;
    int CYAN = 0xFF00FFFF;
    int WHITE = 0xFFFFFFFF;

    public Map() {
        super(Text.of("Map"));

        pois.add(new POI(50, 50, "I am a map feature lolza", "i am a map feature lolza", RED));
        pois.add(new POI(100, 100, "Guard Spawn", "i spawn guards", CYAN));
    }

    public static void runMapKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MlummClient.openMapKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new Map());
            }
            });}

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.of("Close"), button -> this.close())
                .dimensions(this.width / 2 - 50, this.height - 30, 100, 20)
                .build());
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        int mapX = (this.width - 512) / 2;
        int mapY = (this.height - 512) / 2;
        context.drawTexture(MAP_TEXTURE, mapX, mapY, 0, 0, 512, 512);

        // Render POIs
        for (POI poi : pois) {
            int poiX = mapX + poi.x * 2;
            int poiY = mapY + poi.y * 2;
            context.fill(poiX - 2, poiY - 2, poiX + 2, poiY + 2, poi.color);

            if (mouseX >= poiX - 2 && mouseX <= poiX + 2 && mouseY >= poiY - 2 && mouseY <= poiY + 2) {
                context.drawTooltip(MinecraftClient.getInstance().textRenderer, Text.of(poi.name + ": " + poi.description), mouseX, mouseY);
            }
        }

        // Render Color Key for Categories
        int keyX = mapX + 520;
        int keyY = mapY;
        int keyWidth = 100;
        int keyHeight = 20;

        context.drawText(MinecraftClient.getInstance().textRenderer, "Color Key:", keyX, keyY, WHITE, false);
        keyY += 10;
        List<Category> categories = List.of(
                new Category("i am category", RED),
                new Category("add stuff later like orange will be pris npc idk...", CYAN)
        );

        for (Category category : categories) {
            context.fill(keyX, keyY, keyX + keyHeight, keyY + keyHeight, category.color); // Draw color box
            context.drawText(MinecraftClient.getInstance().textRenderer, category.name, keyX + keyHeight + 5, keyY + 5, WHITE, false); // Draw label
            keyY += keyHeight + 5;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    // Updated POI class
    private static class POI {
        int x, y, color;
        String name, description;

        POI(int x, int y, String name, String description, int color) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.description = description;
            this.color = color;
        }
    }

    // New Category class
    private static class Category {
        String name;
        int color;

        Category(String name, int color) {
            this.name = name;
            this.color = color;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (POI poi : pois) {
            int poiX = this.width / 2 - 128 + poi.x;
            int poiY = this.height / 2 - 128 + poi.y;
            if (mouseX >= poiX - 2 && mouseX <= poiX + 2 && mouseY >= poiY - 2 && mouseY <= poiY + 2) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
package me.darkpotatoo.mlumm.client.ui;

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

    private static final Identifier MAP_TEXTURE = Identifier.of("mlumm", "textures/map.png");
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

        pois.add(new POI(255, 255, "im a POI", "i have a short description (maybe whwat i sell if im an npc)", RED, "x,z"));
        pois.add(new POI(100, 100, "Guard Spawn", "i spawn guards", CYAN, "255,255"));
    }

    public static void runMapKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MlummClient.openMapKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new Map());
            }
        });
    }

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
        context.fill(0, 0, this.width, this.height, 0x88000000);

        int mapWidth = 512;
        int mapHeight = 512;
        int mapX = (this.width - mapWidth) / 2;
        int mapY = (this.height - mapHeight) / 2;

        int bc = 0xFF808080;
        context.fill(mapX - 4, mapY - 4, mapX + mapWidth + 4, mapY + mapHeight + 4, bc);
        context.drawTexture(MAP_TEXTURE, mapX, mapY, 0, 0, mapWidth, mapHeight, mapWidth, mapHeight);

        for (POI poi : pois) {
            int poiX = mapX + poi.x;
            int poiY = mapY + poi.y;
            context.fill(poiX - 5, poiY - 5, poiX + 5, poiY + 5, 0xFF000000);
            context.fill(poiX - 4, poiY - 4, poiX + 4, poiY + 4, poi.color);

            if (mouseX >= poiX - 5 && mouseX <= poiX + 5 && mouseY >= poiY - 5 && mouseY <= poiY + 5) {
                context.drawTooltip(MinecraftClient.getInstance().textRenderer, Text.of(poi.name + ": " + poi.description + "(" + poi.coords + ")"), mouseX, mouseY);
            }
        }

        int keyX = mapX + mapWidth + 8;
        int keyY = mapY;
        int keyHeight = 20;

        context.drawText(MinecraftClient.getInstance().textRenderer, "Color Key:", keyX, keyY, WHITE, false);
        keyY += 10;

        List<Category> categories = List.of(
                new Category("i am category", RED),
                new Category("add stuff later like orange will be pris npc idk...", CYAN)
        );

        for (Category category : categories) {
            context.fill(keyX, keyY, keyX + keyHeight, keyY + keyHeight, category.color);
            context.drawText(MinecraftClient.getInstance().textRenderer, category.name, keyX + keyHeight + 5, keyY + 5, WHITE, false);
            keyY += keyHeight + 5;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private static class POI {
        int x, y, color;
        String name, description, coords;
        POI(int x, int y, String name, String description, int color, String coords) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.description = description;
            this.color = color;
            this.coords = coords;
        }
    }

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
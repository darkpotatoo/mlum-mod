package me.darkpotatoo.mlumm.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.MinecraftClient;

public class ModMenuConfig implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new OptionsScreen(parent, MinecraftClient.getInstance().options);
    }
}
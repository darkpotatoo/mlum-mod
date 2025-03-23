package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import net.fabricmc.api.ClientModInitializer;

public class MlummClient implements ClientModInitializer {

    public static final String MODID = "mlumm";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static int combatTicks = 0;
    public static int crateTicks = 0;
    public static int deskTicks = 0;

    public static KeyBinding GetItemInfoKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("mlum mod loading...");
        Item.InitItems();

        GetItemInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Fetch info of hovered item",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_0,
                "category.mlumm"
        ));
        Iteminfo.initItemInfoKey();
    }

}
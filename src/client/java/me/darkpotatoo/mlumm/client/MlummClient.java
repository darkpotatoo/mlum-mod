package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
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
    public static int trashTicks = 0;
    public static int boxTicks = 0;
    public static int mailTicks = 0;

    public static KeyBinding GetItemInfoKey;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
        LOGGER.info("mlum mod loading...");
        Iteminfo.InitItems();

        GetItemInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Fetch info of hovered item",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_0,
                "mlum mod"
        ));
        Iteminfo.runItemInfoKey();

    }

}
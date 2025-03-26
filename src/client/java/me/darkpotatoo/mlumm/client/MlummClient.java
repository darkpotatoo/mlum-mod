package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import java.util.List;

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
    public static int escapeTicks = 0;

    public static boolean tooltipIsContraband;

    public static KeyBinding GetItemInfoKey;

    @Override
    public void onInitializeClient() {
        ChatListener.register();
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        LOGGER.info("mlum mod loading...");
        Iteminfo.InitItems();

        // Iteminfo key
        GetItemInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Fetch info of hovered item",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_0,
                "mlum mod"
        ));
        Iteminfo.runItemInfoKey();

        // Contraband tooltip getter
        ItemTooltipCallback.EVENT.register((ItemTooltipCallback)(stack, context, type, lines) -> {
            tooltipIsContraband = (checkContrabandItem(lines) && !lines.isEmpty());
        });
    }

    private boolean checkContrabandItem(List <Text> lore) {
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        for (Text text : lore) {
            if (text.getString().contains("CONTRABAND") && config.contraband_tooltip) {
                return true;
            }
        }
        return false;
    }

}
package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.iteminfo.Iteminfo;
import me.darkpotatoo.mlumm.client.misc.EscapeAnnouncer;
import me.darkpotatoo.mlumm.client.ui.Map;
import me.darkpotatoo.mlumm.client.misc.ChocolateStats;
import me.darkpotatoo.mlumm.client.riot.RiotTracker;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;

import static me.darkpotatoo.mlumm.client.iteminfo.ItemCosts.updateTooltip;

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
    public static int rodTicks = 0;
    public static boolean tooltipIsContraband;
    public static KeyBinding getItemInfoKey;
    public static KeyBinding openMapKey;
    private static Configuration config;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        LOGGER.info("mlum mod loading...");
        Iteminfo.initItems();
        EscapeAnnouncer.register();

        // Iteminfo key
        getItemInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Fetch info of hovered item",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_0,
                "mlum mod"
        )); Iteminfo.runItemInfoKey();
        // map key
        openMapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open NPC/POI map",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "mlum mod"
        )); Map.runMapKey();

        // register commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ChocolateStats.register(dispatcher);
            RiotTracker.register(dispatcher);
        });

        // tooltip getter
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (config.contraband_tooltip) tooltipIsContraband = checkItemLore(lines);
            if (config.itemcosts) updateTooltip(lines);
            //if (lines.getFirst().getString().contains("Requirements:")) ItemCosts.updateTooltip(stack, lines);
        });
    }

    private boolean checkItemLore(List <Text> lore) {
        //if (lore.isEmpty()) return false;
        for (Text text : lore) {
            if (text.getString().contains("CONTRABAND")) return true;
        }
        return false;
    }

}
package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.iteminfo.Iteminfo;
import me.darkpotatoo.mlumm.client.misc.*;
import me.darkpotatoo.mlumm.client.ui.ChatModeSelector;
import me.darkpotatoo.mlumm.client.ui.RiotMeter;
import me.darkpotatoo.mlumm.client.ui.RiotMeterConfigScreen;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.render.RenderTickCounter;
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
    public static int boxTicks = 0;
    public static int deskTicks = 0;
    public static int escapeTicks = 0;
    public static int rodTicks = 0;
    public static boolean tooltipIsContraband;
    public static KeyBinding getItemInfoKey;
    public static KeyBinding rotateKey;
    public static KeyBinding meterConfigKey;
    private static Configuration config;
    private RiotMeter riotMeter;
    private ChatModeSelector chatSelector;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        LOGGER.info("mlum mod loading...");
        Iteminfo.initItems();
        EscapeAnnouncer.register();
        TickScheduler.init();
        chatSelector = new ChatModeSelector();
        riotMeter = new RiotMeter();

        // Iteminfo key
        getItemInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Fetch info of hovered item",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_0,
                "mlum mod"
        )); Iteminfo.runItemInfoKey();

        // meter special location chooser
        meterConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open style meter location configuration",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_BACKSLASH,
                "mlum mod"
        ));

        // rotate key
        rotateKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Rotate key (for treadmill)",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "mlum mod"
        ));

        // in here

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (rotateKey.wasPressed()) {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                float yaw = player.getYaw();
                float normalizedYaw = (yaw % 360 + 360) % 360;
                float snappedYaw = 0;
                if (normalizedYaw >= 315 || normalizedYaw < 45) {
                    snappedYaw = 0; // SOUTH
                } else if (normalizedYaw >= 45 && normalizedYaw < 135) {
                    snappedYaw = 90; // WEST
                } else if (normalizedYaw >= 135 && normalizedYaw < 225) {
                    snappedYaw = 180; // NORTH
                } else {
                    snappedYaw = 270; // EAST
                }
                player.setYaw(snappedYaw);
            }
            while (meterConfigKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new RiotMeterConfigScreen());
            }
        });

        // ingamehud
        HudRenderCallback.EVENT.register(this::onHudRender);

        // riot meter / combat timer / stuff / combat idk
        CombatHandler.register();

        // register commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ChocolateStats.register(dispatcher);
            RiotTracker.register(dispatcher);
            Iteminfo.registerCommand(dispatcher);
        });

        // tooltip getter
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (config.contraband_tooltip) tooltipIsContraband = checkItemLore(lines);
            if (config.itemcosts) updateTooltip(lines);
            //if (lines.getFirst().getString().contains("Requirements:")) ItemCosts.updateTooltip(stack, lines);
        });
    }

    private void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (config.stylemeter) riotMeter.render(context);
        if (MinecraftClient.getInstance().inGameHud.getChatHud().isChatFocused() && config.chatmode)
            chatSelector.render(context);
    }


    private boolean checkItemLore(List <Text> lore) {
        //if (lore.isEmpty()) return false;
        for (Text text : lore) {
            if (text.getString().contains("CONTRABAND")) return true;
        }
        return false;
    }

}
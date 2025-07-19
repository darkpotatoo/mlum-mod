package me.darkpotatoo.mlumm.client.iteminfo;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.misc.ChocolateStats;
import me.darkpotatoo.mlumm.client.misc.TickScheduler;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.darkpotatoo.mlumm.client.misc.UtilMethods.sendCustomToast;

public class Iteminfo {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static ItemStack selectedItem;
    public static ArrayList<Item> items = new ArrayList<>();

    public static void runItemInfoKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MlummClient.getItemInfoKey.wasPressed()) {
                if (selectedItem != null) { attemptItemInfo(null); }
            }});}

    /** Pass {@code null} as an argument to display iteminfo; pass an {@code ItemStack} object to set the var */
    public static void attemptItemInfo(ItemStack item) {
        if (item != null) { selectedItem = item; }
        if (item == null && selectedItem != null) {
            if (MinecraftClient.getInstance().currentScreen != null) displayIteminfoFromGUI(selectedItem);
            selectedItem = null;
        }
    }


    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("iteminfo")
                .then(ClientCommandManager.argument("item", StringArgumentType.greedyString())
                        .suggests((context, builder) -> suggestItemNames(builder))
                        .executes(context -> {
                            String item = StringArgumentType.getString(context, "item");
                            if (displayIteminfoFromName(item)) {}
                            else { MinecraftClient.getInstance().player.sendMessage(Text.of("§4§l! §fNo item found with that name")); }
                            return 1;
                        })));
    }

    private static CompletableFuture<Suggestions> suggestItemNames(SuggestionsBuilder builder) {
        for (String itemName : Item.itemNames) {
            builder.suggest(itemName);
        }
        return builder.buildFuture();
    }

    private static void displayIteminfoFromGUI(ItemStack item) {
        String itemName = item.getName().toString();

        MinecraftClient client = MinecraftClient.getInstance();
        for (Item thing : items) {
            Pattern pattern = Pattern.compile(Pattern.quote(thing.name), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(itemName);
            if (matcher.find()) {
                client.setScreen(new IteminfoScreen(thing));
                LOGGER.debug("Displaying iteminfo for " + itemName);
                return;
            }
        }
        sendCustomToast("Iteminfo Failed", "There is no iteminfo for your currently hovered item.");
    }
    private static boolean displayIteminfoFromName(String itemName) {
        MinecraftClient client = MinecraftClient.getInstance();
        for (Item thing : items) {
            Pattern pattern = Pattern.compile(Pattern.quote(thing.name), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(itemName);
            if (matcher.find()) {
                TickScheduler.schedule(() -> {
                    MinecraftClient.getInstance().setScreen(new IteminfoScreen(thing));
                    LOGGER.debug("Displaying iteminfo for " + itemName + " (via TickScheduler)");
                });
                return true;
            }
        }
        return false;
    }

    // This is VERY long. This registers ALL items. There is nothing under this to see.
    public static void initItems() {
        LOGGER.info("Registering items for iteminfo");
        new Item(
                ItemType.Weapon,
                "Baton",
                0,
                new String[]{"Cannot be crafted"},
                ItemSource.Guard,
                new ItemStack(Items.STONE_SWORD));
        new Item(
                ItemType.Escape,
                "Grappling Hook",
                39,
                new String[]{"1x Tool Handle", "2x Rope", "1x Grapple Head"},
                ItemSource.Crafting,
                new ItemStack(Items.FISHING_ROD));
    }
}


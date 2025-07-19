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
                ItemType.Material,
                "Bar of Soap",
                1,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.PINK_DYE));
        new Item(
                ItemType.Material,
                "File",
                3,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.PRISMARINE_SHARD));
        new Item(
                ItemType.Material,
                "Glass Shard",
                10,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.GLASS_PANE));
        new Item(
                ItemType.Material,
                "Paper",
                2,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.PAPER));
        new Item(
                ItemType.Material,
                "Paperclip",
                0,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.STRING));
        new Item(
                ItemType.Material,
                "Pen",
                2,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.LEVER));
        new Item(
                ItemType.Material,
                "Putty",
                13,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.YELLOW_DYE));
        new Item(
                ItemType.Material,
                "Razor Blade",
                2,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.COBWEB));
        new Item(
                ItemType.Material,
                "Rock",
                2,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.STONE_BUTTON));
        new Item(
                ItemType.Material,
                "Roll of Duct Tape",
                3,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.GRAY_DYE));
        new Item(
                ItemType.Material,
                "Sheet of Metal",
                5,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.IRON_INGOT));
        new Item(
                ItemType.Material,
                "Sock",
                1,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.LAPIS_LAZULI));
        new Item(
                ItemType.Material,
                "Timber",
                4,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.OAK_WOOD));
        new Item(
                ItemType.Material,
                "Wire",
                1,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.STRING));
        new Item(
                ItemType.Material,
                "Timber",
                4,
                new String[]{"Bought from NPC"},
                ItemSource.NPC,
                new ItemStack(Items.STICK));
        new Item(
                ItemType.Material,
                "Tool Handle",
                7,
                new String[]{"1x Timber", "1x File"},
                ItemSource.Crafting,
                new ItemStack(Items.STICK));
        new Item(
                ItemType.Material,
                "Key Mould",
                19,
                new String[]{"1x Putty", "2x File"},
                ItemSource.Crafting,
                new ItemStack(Items.YELLOW_CANDLE));
        new Item(
                ItemType.Material,
                "Keycard Chip",
                14,
                new String[]{"1x Sheet of Metal", "3x File"},
                ItemSource.Crafting,
                new ItemStack(Items.IRON_NUGGET));
        new Item(
                ItemType.Material,
                "Shrinking Droplet",
                9,
                new String[]{"7x Bar of Soap", "1x Rock"},
                ItemSource.Crafting,
                new ItemStack(Items.LIGHT_BLUE_DYE));
        new Item(
                ItemType.Material,
                "Thruster",
                12,
                new String[]{"3x Sheet of Metal"},
                ItemSource.Crafting,
                new ItemStack(Items.IRON_BLOCK));
        new Item(
                ItemType.Armor,
                "Plated Inmate Jumpsuit",
                5,
                new String[]{"1x Inmate Jumpsuit", "1x Sheet of Metal"},
                ItemSource.Crafting,
                new ItemStack(Items.LEATHER_CHESTPLATE));
        new Item(
                ItemType.Armor,
                "Plated Inmate Pants",
                5,
                new String[]{"1x Inmate Pants", "1x Sheet of Metal"},
                ItemSource.Crafting,
                new ItemStack(Items.LEATHER_LEGGINGS));
        new Item(
                ItemType.Weapon,
                "Sock Mace",
                2,
                new String[]{"1x Bar of Soap", "1x Sock"},
                ItemSource.Crafting,
                new ItemStack(Items.LAPIS_LAZULI));
        new Item(
                ItemType.Weapon,
                "Whip",
                5,
                new String[]{"3x Wire", "1x Razor Blade"},
                ItemSource.Crafting,
                new ItemStack(Items.LEAD));
        new Item(
                ItemType.Weapon,
                "Nunchucks",
                9,
                new String[]{"2x Timber", "1x Wire"},
                ItemSource.Crafting,
                new ItemStack(Items.ACACIA_FENCE));
        new Item(
                ItemType.Weapon,
                "Bucket",
                9,
                new String[]{"1x Sheet of Metal", "1x Wire", "1x Roll of Duct Tape"},
                ItemSource.Crafting,
                new ItemStack(Items.BUCKET));
        new Item(
                ItemType.Weapon,
                "Glass Shank",
                13,
                new String[]{"1x Shard of Glass", "1x Roll of Duct Tape"},
                ItemSource.Crafting,
                new ItemStack(Items.GLASS_PANE));
        new Item(
                ItemType.Escape,
                "Shovel",
                39,
                new String[]{"1x Tool Handle", "4x Sheet of Metal", "4x Roll of Duct Tape"},
                ItemSource.Crafting,
                new ItemStack(Items.IRON_SHOVEL));
        new Item(
                ItemType.Escape,
                "Pickaxe",
                36,
                new String[]{"1x Tool Handle", "2x Crowbar", "2x Roll of Duct Tape"},
                ItemSource.Crafting,
                new ItemStack(Items.IRON_PICKAXE));
        new Item(
                ItemType.Escape,
                "Electrical Control Key",
                35,
                new String[]{"1x Key Mould", "2x Sheet of Metal", "2x File"},
                ItemSource.Crafting,
                new ItemStack(Items.TRIAL_KEY));
        new Item(
                ItemType.Escape,
                "Laser Keycard",
                18,
                new String[]{"1x Sheet of Paper", "1x Pen", "1x Keycard Chip"},
                ItemSource.Crafting,
                new ItemStack(Items.PAPER));
        new Item(
                ItemType.Escape,
                "Shrinking Powder",
                39,
                new String[]{"4x Shrinking Droplet", "1x Roll of Duct Tape"},
                ItemSource.Crafting,
                new ItemStack(Items.LIGHT_BLUE_CONCRETE_POWDER));
        new Item(
                ItemType.Escape,
                "Jetpack",
                38,
                new String[]{"2x Truster", "1x Fuel Canister"},
                ItemSource.Crafting,
                new ItemStack(Items.LEATHER_CHESTPLATE));
        new Item(
                ItemType.Escape,
                "Paperclip",
                0,
                new String[]{"1x Paperclip"},
                ItemSource.Crafting,
                new ItemStack(Items.LEVER));
        new Item(
                ItemType.Armor,
                "Fugitive Helmet",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Fugitive,
                new ItemStack(Items.RED_STAINED_GLASS));
        new Item(
                ItemType.Armor,
                "Fugitive Tunic",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Fugitive,
                new ItemStack(Items.LEATHER_CHESTPLATE));
        new Item(
                ItemType.Armor,
                "Fugitive Leggings",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Fugitive,
                new ItemStack(Items.LEATHER_LEGGINGS));
        new Item(
                ItemType.Armor,
                "Fugitive Boots",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Fugitive,
                new ItemStack(Items.LEATHER_BOOTS));
        new Item(
                ItemType.Armor,
                "Guard Helmet",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Guard,
                new ItemStack(Items.BLUE_STAINED_GLASS));
        new Item(
                ItemType.Armor,
                "Guard Vest",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Guard,
                new ItemStack(Items.IRON_CHESTPLATE));
        new Item(
                ItemType.Armor,
                "Guard Leggings",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Guard,
                new ItemStack(Items.LEATHER_LEGGINGS));
        new Item(
                ItemType.Armor,
                "Guard Boots",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Guard,
                new ItemStack(Items.LEATHER_BOOTS));
        new Item(
                ItemType.Armor,
                "Trainee Helmet",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Trainee,
                new ItemStack(Items.BLUE_STAINED_GLASS));
        new Item(
                ItemType.Armor,
                "Trainee Vest",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Trainee,
                new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        new Item(
                ItemType.Armor,
                "Trainee Leggings",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Trainee,
                new ItemStack(Items.LEATHER_LEGGINGS));
        new Item(
                ItemType.Armor,
                "Trainee Boots",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Trainee,
                new ItemStack(Items.LEATHER_BOOTS));
        new Item(
                ItemType.Armor,
                "Detective Helmet",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.CYAN_STAINED_GLASS));
        new Item(
                ItemType.Armor,
                "Detective Tunic",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.LEATHER_CHESTPLATE));
        new Item(
                ItemType.Armor,
                "Detective Leggings",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.LEATHER_LEGGINGS));
        new Item(
                ItemType.Armor,
                "Detective Boots",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.LEATHER_BOOTS));
        new Item(
                ItemType.Armor,
                "Detective Helmet",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.BLUE_STAINED_GLASS));
        new Item(
                ItemType.Armor,
                "Detective Chestplate",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        new Item(
                ItemType.Armor,
                "Detective Pants",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.LEATHER_LEGGINGS));
        new Item(
                ItemType.Weapon,
                "Baton",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Guard,
                new ItemStack(Items.STONE_SWORD));
        new Item(
                ItemType.Weapon,
                "Truncheon",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Trainee,
                new ItemStack(Items.WOODEN_SWORD));
        new Item(
                ItemType.Weapon,
                "Pocket Knife",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.IRON_SWORD));
        new Item(
                ItemType.Weapon,
                "Guard Bow",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Guard,
                new ItemStack(Items.BOW));
        new Item(
                ItemType.Weapon,
                "Bow",
                0,
                new String[]{"Uncraftable"},
                ItemSource.Detective,
                new ItemStack(Items.BOW));
    }
}


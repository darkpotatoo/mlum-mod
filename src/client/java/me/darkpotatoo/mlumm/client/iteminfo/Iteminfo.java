package me.darkpotatoo.mlumm.client.iteminfo;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.misc.ChocolateStats;
import me.darkpotatoo.mlumm.client.misc.NotifType;
import me.darkpotatoo.mlumm.client.misc.TickScheduler;
import me.darkpotatoo.mlumm.client.misc.UtilMethods;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Iteminfo {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static ItemStack selectedItem;
    public static ArrayList<Item> items = new ArrayList<>();
    private static boolean initialized = false;

    public static void runItemInfoKey() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MlummClient.getItemInfoKey.wasPressed()) {
                if (selectedItem != null) { attemptItemInfo(null); }
            }});}

    /** Pass {@code null} as an argument to display iteminfo; pass an {@code ItemStack} object to set the var */
    public static void attemptItemInfo(ItemStack item) {
        //if (item != null)
        //    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(item.toNbt(MinecraftClient.getInstance().world.getRegistryManager()).toString()));
        if (item != null) { selectedItem = item; }
        if (item == null && selectedItem != null) {
            if (MinecraftClient.getInstance().currentScreen != null && !MinecraftClient.getInstance().inGameHud.getChatHud().isChatFocused()) displayIteminfoFromGUI(selectedItem);
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
                            else { MinecraftClient.getInstance().player.sendMessage(Text.of("§4§l! §fNo item found with that name"), false); }
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
                LOGGER.debug("Displaying iteminfo for {}", itemName);
                return;
            }
        }
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
    public static void initItems() throws CommandSyntaxException {
        if (initialized) return;
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
                getItem("{components:{\"minecraft:custom_data\":{\"VV|Protocol1_21_2To1_21_4|custom_model_data\":3,contraband:1b},\"minecraft:custom_model_data\":{floats:[3.0f]},\"minecraft:custom_name\":'{\"extra\":[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Thruster\",\"underlined\":false}],\"text\":\"\"}',\"minecraft:lore\":['{\"extra\":[{\"bold\":false,\"color\":\"gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Requirements:\",\"underlined\":false}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"3\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Sheet of Metal\"}],\"text\":\"\"}'],\"minecraft:profile\":{name:\"Enginesideways\",properties:[{name:\"textures\",value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJlN2JjNjYzODlkOTZhOWM1ZjRhYTVhODc1NzllZTY2MWU4OGFkYTZmN2I4YmUxNzRiZjM5ODJhMzRmODQifX19\"}]}},count:1,id:\"minecraft:player_head\"}"));
        new Item(
                ItemType.Armor,
                "Plated Inmate Jumpsuit",
                5,
                new String[]{"1x Inmate Jumpsuit", "1x Sheet of Metal"},
                ItemSource.Crafting,
                getItem("{components:{\"minecraft:attribute_modifiers\":{modifiers:[{amount:0.0d,id:\"minecraft:c2c8cb8f-2982-4c23-9ce8-466163d3efd6\",operation:\"add_value\",slot:\"chest\",type:\"minecraft:armor\"},{amount:5.0d,id:\"minecraft:95b382e9-4866-48f5-8a6d-1bc64a44798f\",operation:\"add_value\",slot:\"chest\",type:\"minecraft:armor\"}],show_in_tooltip:0b},\"minecraft:custom_data\":{contraband:1b},\"minecraft:custom_name\":'{\"extra\":[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Plated Inmate Jumpsuit\",\"underlined\":false}],\"text\":\"\"}',\"minecraft:dyed_color\":{rgb:16486170,show_in_tooltip:0b},\"minecraft:lore\":['{\"extra\":[{\"bold\":false,\"color\":\"gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Requirements:\",\"underlined\":false}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Inmate Jumpsuit\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Sheet of Metal\"}],\"text\":\"\"}'],\"minecraft:max_damage\":200,\"minecraft:trim\":{material:{asset_name:\"iron\",description:{color:\"#ECECEC\",translate:\"trim_material.minecraft.iron\"},ingredient:\"minecraft:iron_ingot\"},pattern:{asset_id:\"minecraft:snout\",decal:0b,description:{translate:\"trim_pattern.minecraft.snout\"},template_item:\"minecraft:snout_armor_trim_smithing_template\"},show_in_tooltip:0b}},count:1,id:\"minecraft:leather_chestplate\"}"));
        new Item(
                ItemType.Armor,
                "Plated Inmate Pants",
                5,
                new String[]{"1x Inmate Pants", "1x Sheet of Metal"},
                ItemSource.Crafting,
                getItem("{components:{\"minecraft:attribute_modifiers\":{modifiers:[{amount:4.0d,id:\"minecraft:f4bc448a-bc6a-4a0b-8cdd-86928c29f15d\",operation:\"add_value\",slot:\"legs\",type:\"minecraft:armor\"}],show_in_tooltip:0b},\"minecraft:custom_data\":{contraband:1b},\"minecraft:custom_name\":'{\"extra\":[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Plated Inmate Pants\",\"underlined\":false}],\"text\":\"\"}',\"minecraft:dyed_color\":{rgb:16486170,show_in_tooltip:0b},\"minecraft:lore\":['{\"extra\":[{\"bold\":false,\"color\":\"gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Requirements:\",\"underlined\":false}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Inmate Pants\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Sheet of Metal\"}],\"text\":\"\"}'],\"minecraft:max_damage\":200,\"minecraft:trim\":{material:{asset_name:\"iron\",description:{color:\"#ECECEC\",translate:\"trim_material.minecraft.iron\"},ingredient:\"minecraft:iron_ingot\"},pattern:{asset_id:\"minecraft:snout\",decal:0b,description:{translate:\"trim_pattern.minecraft.snout\"},template_item:\"minecraft:snout_armor_trim_smithing_template\"},show_in_tooltip:0b}},count:1,id:\"minecraft:leather_leggings\"}"));
        new Item(
                ItemType.Weapon,
                "Sock Mace",
                2,
                new String[]{"1x Bar of Soap", "1x Sock"},
                ItemSource.Crafting,
                getItem("{components:{\"minecraft:custom_data\":{\"VV|Protocol1_21_2To1_21_4|custom_model_data\":2,contraband:1b},\"minecraft:custom_model_data\":{floats:[2.0f]},\"minecraft:custom_name\":'{\"extra\":[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Sock Mace\",\"underlined\":false}],\"text\":\"\"}',\"minecraft:enchantments\":{levels:{\"minecraft:sharpness\":5},show_in_tooltip:0b},\"minecraft:lore\":['{\"extra\":[{\"bold\":false,\"color\":\"gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Requirements:\",\"underlined\":false}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Sock\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Bar of Soap\"}],\"text\":\"\"}']},count:1,id:\"minecraft:lapis_lazuli\"}"));
        new Item(
                ItemType.Weapon,
                "Whip",
                5,
                new String[]{"3x Wire", "1x Razor Blade"},
                ItemSource.Crafting,
                getItem("{components:{\"minecraft:custom_data\":{\"VV|Protocol1_21_2To1_21_4|custom_model_data\":1,contraband:1b},\"minecraft:custom_model_data\":{floats:[1.0f]},\"minecraft:custom_name\":'{\"extra\":[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Whip\",\"underlined\":false}],\"text\":\"\"}',\"minecraft:enchantments\":{levels:{\"minecraft:knockback\":1,\"minecraft:sharpness\":6},show_in_tooltip:0b},\"minecraft:lore\":['{\"extra\":[{\"bold\":false,\"color\":\"gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Requirements:\",\"underlined\":false}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"3\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Wire\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Razor Blade\"}],\"text\":\"\"}']},count:1,id:\"minecraft:lead\"}"));
        new Item(
                ItemType.Weapon,
                "Nunchucks",
                9,
                new String[]{"2x Timber", "1x Wire"},
                ItemSource.Crafting,
                getItem("{components:{\"minecraft:custom_data\":{\"VV|Protocol1_21_2To1_21_4|custom_model_data\":1,contraband:1b},\"minecraft:custom_model_data\":{floats:[1.0f]},\"minecraft:custom_name\":'{\"extra\":[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Nunchucks\",\"underlined\":false}],\"text\":\"\"}',\"minecraft:enchantments\":{levels:{\"minecraft:knockback\":1,\"minecraft:sharpness\":7},show_in_tooltip:0b},\"minecraft:lore\":['{\"extra\":[{\"bold\":false,\"color\":\"gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Requirements:\",\"underlined\":false}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"2\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Timber\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Wire\"}],\"text\":\"\"}']},count:1,id:\"minecraft:acacia_fence\"}"));
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
                getItem("{components:{\"minecraft:attribute_modifiers\":{modifiers:[{amount:1.5d,id:\"minecraft:1f22377e-f6ba-408d-9162-fd4fdb32f08b\",operation:\"add_value\",slot:\"mainhand\",type:\"minecraft:attack_damage\"}],show_in_tooltip:0b},\"minecraft:custom_data\":{\"VV|Protocol1_21_2To1_21_4|custom_model_data\":2,contraband:1b},\"minecraft:custom_model_data\":{floats:[2.0f]},\"minecraft:custom_name\":'{\"extra\":[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Glass Shank\",\"underlined\":false}],\"text\":\"\"}',\"minecraft:enchantments\":{levels:{\"minecraft:sharpness\":5},show_in_tooltip:0b},\"minecraft:lore\":['{\"extra\":[{\"bold\":false,\"color\":\"gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Requirements:\",\"underlined\":false}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Glass Shard\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Roll of Duct Tape\"}],\"text\":\"\"}']},count:1,id:\"minecraft:glass_pane\"}"));
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
                new ItemStack(Items.ORANGE_STAINED_GLASS_PANE));
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
                getItem("{components:{\"minecraft:attribute_modifiers\":{modifiers:[{amount:2.0d,id:\"minecraft:a0c3be00-7ed4-414c-9b53-35fa98446086\",operation:\"add_value\",slot:\"chest\",type:\"minecraft:armor\"}],show_in_tooltip:0b},\"minecraft:custom_data\":{contraband:1b,dig:1b,jetpack:1b},\"minecraft:custom_name\":'{\"extra\":[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Jetpack\",\"underlined\":false}],\"text\":\"\"}',\"minecraft:dyed_color\":{rgb:16351261,show_in_tooltip:0b},\"minecraft:lore\":['{\"extra\":[{\"bold\":false,\"color\":\"gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\"Requirements:\",\"underlined\":false}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"2\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Thruster\"}],\"text\":\"\"}','{\"extra\":[{\"bold\":false,\"color\":\"dark_gray\",\"italic\":false,\"obfuscated\":false,\"strikethrough\":false,\"text\":\" - \",\"underlined\":false},{\"color\":\"gold\",\"italic\":false,\"text\":\"1\"},{\"color\":\"gold\",\"italic\":false,\"text\":\"x Fuel Canister\"}],\"text\":\"\"}'],\"minecraft:max_damage\":100,\"minecraft:trim\":{material:{asset_name:\"iron\",description:{color:\"#ECECEC\",translate:\"trim_material.minecraft.iron\"},ingredient:\"minecraft:iron_ingot\"},pattern:{asset_id:\"minecraft:silence\",decal:0b,description:{translate:\"trim_pattern.minecraft.silence\"},template_item:\"minecraft:silence_armor_trim_smithing_template\"},show_in_tooltip:0b}},count:1,id:\"minecraft:leather_chestplate\"}"));
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
        initialized = true;
        LOGGER.info("Registered items");
    }

    private static ItemStack getItem(String nbt) throws CommandSyntaxException {
        return ItemStack.fromNbt(MinecraftClient.getInstance().world.getRegistryManager(), StringNbtReader.parse(nbt)).get();
    }
}


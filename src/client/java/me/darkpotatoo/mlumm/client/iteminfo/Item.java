package me.darkpotatoo.mlumm.client.iteminfo;

import com.mojang.logging.LogUtils;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;

public class Item {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ItemType type;
    public String name;
    public int chocoCost;
    public String[] craftingRecipe;
    public ItemSource howto;
    public ItemStack literalItem;

    public Item(ItemType _type, String _name, int _chocoCost, String[] _craftingRecipe, ItemSource _howto, ItemStack _literalitem) {
        type = _type;
        name = _name;
        chocoCost = _chocoCost;
        craftingRecipe = _craftingRecipe;
        howto = _howto;
        literalItem = _literalitem;
        LOGGER.info("Item '" + name + "' registered");
        Iteminfo.items.add(this);
    }

}

enum ItemType {
    Weapon,
    Armor,
    Material,
    Escape,
    Other
}

enum ItemSource {
    Forge,
    Crafting,
    Guard,
    NPC

}
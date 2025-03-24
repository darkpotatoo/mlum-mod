package me.darkpotatoo.mlumm.client;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class Item {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ItemType type;
    public String name;
    public int chocoCost;
    public String[] craftingRecipe;
    public ItemSource howto;

    public Item(ItemType _type, String _name, int _chocoCost, String[] _craftingRecipe, ItemSource _howto) {
        type = _type;
        name = _name;
        chocoCost = _chocoCost;
        craftingRecipe = _craftingRecipe;
        howto = _howto;
        LOGGER.info("Item '" + name + "' registered");
        Iteminfo.items.add(this);
    }

}

enum ItemType {
    WEAPON,
    ARMOR,
    MATERIAL,
    ESCAPE,
    EXCLUSIVE
}

enum ItemSource {
    FORGED,
    CRAFTED,
    GUARD_GUARD,
    GUARD_UPGRADE_SS,
    GUARD_UPGRADE_TANK,
    GUARD_UPGRADE_BH,
    GUARD_WARDEN,
    GUARD_RO,
    GUARD_DETECTIVE,
    GUARD_LD_GUARD,
    GUARD_LD_UPGRADE_SS,
    GUARD_LD_UPGRADE_TANK,
    GUARD_LD_UPGRADE_BH,
    GUARD_LD_DETECTIVE,

}
package com.cmii.cmiicore.catalyst.items;

import com.cmii.cmiicore.catalyst.CreativeTabCatalyst;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.item.Item;

public class ItemMeteorChunk extends Item implements IHasModel {

    public ItemMeteorChunk() {
        setTranslationKey("item_meteor_chunk");
        setRegistryName("item_meteor_chunk");
        setCreativeTab(CreativeTabCatalyst.tabCatalyst);
        Data.ITEMS.add(this);
    }
}

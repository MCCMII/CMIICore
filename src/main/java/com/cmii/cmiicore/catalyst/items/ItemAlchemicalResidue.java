package com.cmii.cmiicore.catalyst.items;

import com.cmii.cmiicore.catalyst.CreativeTabCatalyst;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.item.Item;

public class ItemAlchemicalResidue extends Item implements IHasModel {

    public ItemAlchemicalResidue() {
        setTranslationKey("item_alchemical_residue");
        setRegistryName("item_alchemical_residue");
        setCreativeTab(CreativeTabCatalyst.tabCatalyst);
        Data.ITEMS.add(this);
    }
}

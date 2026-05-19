package com.cmii.cmiicore.exnihilo.items;

import com.cmii.cmiicore.exnihilo.CreativeTabExNihilo;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class ItemCookedSilkworm extends ItemFood implements IHasModel {

    public ItemCookedSilkworm() {
        super(2, 0.6f, false);
        this.setTranslationKey("item_cooked_silkworm");
        this.setRegistryName("item_cooked_silkworm");
        Data.ITEMS.add(this);
    }

    @Override
    protected boolean isInCreativeTab(CreativeTabs targetTab) {
        return targetTab == CreativeTabExNihilo.tabExNihilo || targetTab == CreativeTabs.FOOD;
    }
}
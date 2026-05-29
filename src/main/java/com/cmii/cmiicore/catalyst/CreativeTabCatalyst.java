package com.cmii.cmiicore.catalyst;

import com.cmii.cmiicore.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CreativeTabCatalyst {
    public static final CreativeTabs tabCatalyst = new CreativeTabs(Reference.MOD_ID + ".catalyst") {
        @Override
        @Nonnull
        public ItemStack createIcon() {
            return new ItemStack(ModCatalystItems.mineralite, 1, 0);
        }
    };
}

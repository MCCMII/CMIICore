package com.cmii.cmiicore.exnihilo;

import com.cmii.cmiicore.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CreativeTabExNihilo {
    public static final CreativeTabs tabExNihilo = new CreativeTabs(Reference.MOD_ID + ".exnihilo") {
        @Override
        @Nonnull
        public ItemStack createIcon() {
            return new ItemStack(net.minecraft.init.Blocks.COBBLESTONE);
        }
    };
}
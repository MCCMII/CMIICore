package com.cmii.cmiicore.exnihilo.compatibility.jei;

import mezz.jei.api.ISubtypeRegistry;
import net.minecraft.item.ItemStack;

public class IgnoreNBTandMetaInterpreter implements ISubtypeRegistry.ISubtypeInterpreter {
    @Override
    public String apply(ItemStack itemStack) {
        return itemStack.getItem().getRegistryName().toString();
    }
}
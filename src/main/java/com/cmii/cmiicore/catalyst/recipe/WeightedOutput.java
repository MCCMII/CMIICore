package com.cmii.cmiicore.catalyst.recipe;

import net.minecraft.item.ItemStack;

public class WeightedOutput {
    public final ItemStack output;
    public final int chance;

    public WeightedOutput(ItemStack output, int chance) {
        this.output = output;
        this.chance = Math.min(Math.max(chance, 0), 100);
    }
}

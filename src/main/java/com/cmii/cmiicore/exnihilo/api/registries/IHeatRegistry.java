package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import net.minecraft.item.ItemStack;

public interface IHeatRegistry extends IRegistryMap<BlockInfo, Integer> {
    void register(ItemStack stack, int heatAmount);

    int getHeatAmount(ItemStack stack);

    int getHeatAmount(BlockInfo info);
}
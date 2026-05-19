package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.Compostable;
import com.cmii.cmiicore.exnihilo.texturing.Color;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.StackInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public interface ICompostRegistry extends IRegistryMap<Ingredient, Compostable> {
    void register(ItemStack itemStack, float value, BlockInfo state, Color color);

    void register(Item item, int meta, float value, BlockInfo state, Color color);

    void register(Block block, int meta, float value, BlockInfo state, Color color);

    void register(StackInfo item, float value, BlockInfo state, Color color);

    void register(ResourceLocation location, int meta, float value, BlockInfo state, Color color);

    void register(String name, float value, BlockInfo state, Color color);

    void register(String name, float value, BlockInfo state);

    Compostable getItem(Item item, int meta);

    Compostable getItem(ItemStack stack);

    Compostable getItem(StackInfo info);

    boolean containsItem(Item item, int meta);

    boolean containsItem(ItemStack stack);

    boolean containsItem(StackInfo info);
}
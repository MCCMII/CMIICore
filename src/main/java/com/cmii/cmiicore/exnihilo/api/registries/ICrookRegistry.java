package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.CrookReward;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public interface ICrookRegistry extends IRegistryMappedList<Ingredient, CrookReward> {
    void register(Block block, int meta, ItemStack reward, float chance, float fortuneChance);

    void register(IBlockState state, ItemStack reward, float chance, float fortuneChance);

    void register(BlockInfo info, ItemStack reward, float chance, float fortuneChance);

    void register(String name, ItemStack reward, float chance, float fortuneChance);

    void register(Ingredient ingredient, CrookReward reward);

    boolean isRegistered(Block block);

    List<CrookReward> getRewards(IBlockState state);
}
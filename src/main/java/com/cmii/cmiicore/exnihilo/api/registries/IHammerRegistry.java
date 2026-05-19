package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.HammerReward;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.StackInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;
import java.util.Random;

public interface IHammerRegistry extends IRegistryMappedList<Ingredient, HammerReward> {

    void register(IBlockState state, ItemStack reward, int miningLevel, float chance, float fortuneChance);

    void register(Block block, int meta, ItemStack reward, int miningLevel, float chance, float fortuneChance);

    void register(StackInfo stackInfo, ItemStack reward, int miningLevel, float chance, float fortuneChance);

    void register(ItemStack stack, HammerReward reward);

    void register(String name, ItemStack reward, int miningLevel, float chance, float fortuneChance);

    void register(Ingredient ingredient, HammerReward reward);

    List<ItemStack> getRewardDrops(Random random, IBlockState block, int miningLevel, int fortuneLevel);

    List<HammerReward> getRewards(IBlockState block);

    List<HammerReward> getRewards(Block block, int meta);

    List<HammerReward> getRewards(BlockInfo stackInfo);

    List<HammerReward> getRewards(Ingredient ingredient);

    boolean isRegistered(IBlockState block);

    boolean isRegistered(Block block);

    boolean isRegistered(BlockInfo stackInfo);
}
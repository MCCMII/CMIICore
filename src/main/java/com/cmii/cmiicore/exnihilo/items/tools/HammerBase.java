package com.cmii.cmiicore.exnihilo.items.tools;

import com.google.common.collect.Sets;
import com.cmii.cmiicore.exnihilo.CreativeTabExNihilo;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import javax.annotation.Nullable;

public class HammerBase extends ItemTool implements IHammer, IHasModel {

    final int miningLevel;

    public HammerBase(String name, int maxUses, ToolMaterial material) {
        super(material, Sets.newHashSet(new Block[]{}));
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setMaxDamage(maxUses);
        this.miningLevel = material.getHarvestLevel();
        this.setCreativeTab(CreativeTabExNihilo.tabExNihilo);

        Data.ITEMS.add(this);
    }

    @Override
    public boolean isHammer(ItemStack stack) {
        return true;
    }

    @Override
    public int getMiningLevel(ItemStack stack) {
        return miningLevel;
    }

    @Override
    public float getDestroySpeed(@Nullable ItemStack stack, IBlockState state) {
        if (stack == null || stack.isEmpty())
            return 1.0f;

        return stack.getItem().canHarvestBlock(state) ? this.efficiency : 1.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state) {
        return ExNihiloRegistryManager.HAMMER_REGISTRY.getRewards(state).stream()
                .anyMatch(it -> it.getMiningLevel() <= this.miningLevel);
    }
}
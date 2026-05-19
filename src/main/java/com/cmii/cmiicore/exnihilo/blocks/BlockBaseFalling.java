package com.cmii.cmiicore.exnihilo.blocks;

import com.cmii.cmiicore.exnihilo.CreativeTabExNihilo;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBaseFalling extends BlockFalling implements IHasModel {
    public BlockBaseFalling(SoundType sound, String name) {
        super(Material.SAND);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setSoundType(sound);
        this.setCreativeTab(CreativeTabExNihilo.tabExNihilo);
        Data.BLOCKS.add(this);
    }
}
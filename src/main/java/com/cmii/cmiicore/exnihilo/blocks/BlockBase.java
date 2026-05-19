package com.cmii.cmiicore.exnihilo.blocks;

import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block implements IHasModel {
    public BlockBase(Material mat, String name) {
        super(mat);
        setTranslationKey(name);
        setRegistryName(name);
        Data.BLOCKS.add(this);
    }
}
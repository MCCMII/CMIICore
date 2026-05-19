package com.cmii.cmiicore.exnihilo.blocks;

import com.cmii.cmiicore.exnihilo.items.ItemBlockMeta;
import net.minecraft.block.Block;

public class ItemBlockCrucible extends ItemBlockMeta {
    public ItemBlockCrucible(Block block) {
        super(block);

        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

}
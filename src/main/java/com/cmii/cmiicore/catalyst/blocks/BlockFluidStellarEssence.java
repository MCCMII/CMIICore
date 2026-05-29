package com.cmii.cmiicore.catalyst.blocks;

import com.cmii.cmiicore.catalyst.ModCatalystFluids;
import com.cmii.cmiicore.exnihilo.util.Data;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockFluidStellarEssence extends BlockFluidClassic {

    public BlockFluidStellarEssence() {
        super(ModCatalystFluids.stellarEssence, Material.WATER);
        setRegistryName("stellar_essence");
        setTranslationKey("stellar_essence");
        Data.BLOCKS.add(this);
    }
}

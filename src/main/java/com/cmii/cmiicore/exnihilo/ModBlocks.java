package com.cmii.cmiicore.exnihilo;

import com.cmii.cmiicore.exnihilo.blocks.*;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import com.cmii.cmiicore.exnihilo.util.IHasSpecialRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final BlockInfestingLeaves infestingLeaves = new BlockInfestingLeaves();
    public static final BlockInfestedLeaves infestedLeaves = new BlockInfestedLeaves();
    public static final BlockBarrel barrel = new BlockBarrel();
    public static final BlockCrucibleStone crucibleStone = new BlockCrucibleStone();
    public static final BlockCrucibleWood crucibleWood = new BlockCrucibleWood();

    public static BlockFluidWitchwater fluidWitchwater;
    public static Block dust;
    public static Block crushedGranite;
    public static Block crushedAndesite;
    public static Block crushedDiorite;

    public static void touch() {
        // static initializer trigger
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        for (Block block : Data.BLOCKS) {
            if (!(block instanceof IHasSpecialRegistry)) {
                registry.register(block);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(ModelRegistryEvent e) {
        for (Block block : Data.BLOCKS) {
            if (block instanceof IHasModel && !(block instanceof IHasSpecialRegistry)) {
                ((IHasModel) block).initModel(e);
            }
        }
    }
}
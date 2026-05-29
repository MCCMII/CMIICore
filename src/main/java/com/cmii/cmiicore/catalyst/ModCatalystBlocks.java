package com.cmii.cmiicore.catalyst;

import com.cmii.cmiicore.catalyst.blocks.BlockFluidStellarEssence;
import com.cmii.cmiicore.catalyst.blocks.BlockMineraliteOre;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModCatalystBlocks {

    public static final BlockMineraliteOre mineraliteOre = new BlockMineraliteOre();
    public static BlockFluidStellarEssence fluidStellarEssence;

    public static final Map<Block, Integer> ORE_TO_MINERALITE_META = new HashMap<>();

    static {
        // Base Metal (0)
        ORE_TO_MINERALITE_META.put(Blocks.IRON_ORE, 0);
        // Precious Metal (1)
        ORE_TO_MINERALITE_META.put(Blocks.GOLD_ORE, 1);
        // Gem (2)
        ORE_TO_MINERALITE_META.put(Blocks.LAPIS_ORE, 2);
        ORE_TO_MINERALITE_META.put(Blocks.REDSTONE_ORE, 2);
        ORE_TO_MINERALITE_META.put(Blocks.DIAMOND_ORE, 2);
        ORE_TO_MINERALITE_META.put(Blocks.EMERALD_ORE, 2);
        // Fuel (3)
        ORE_TO_MINERALITE_META.put(Blocks.COAL_ORE, 3);
    }

    public static void touch() {
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        for (Block block : Data.BLOCKS) {
            if (!(block instanceof com.cmii.cmiicore.exnihilo.util.IHasSpecialRegistry)) {
                registry.register(block);
            }
        }
    }

    public static void registerOreMapping(Block oreBlock, int mineraliteMeta) {
        ORE_TO_MINERALITE_META.put(oreBlock, mineraliteMeta);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(ModelRegistryEvent e) {
        for (Block block : Data.BLOCKS) {
            if (block instanceof IHasModel && !(block instanceof com.cmii.cmiicore.exnihilo.util.IHasSpecialRegistry)) {
                ((IHasModel) block).initModel(e);
            }
        }
    }
}

package com.cmii.cmiicore.exnihilo;

import com.cmii.cmiicore.exnihilo.items.*;
import com.cmii.cmiicore.exnihilo.items.seeds.ItemRubberSeed;
import com.cmii.cmiicore.exnihilo.items.seeds.ItemSeedBase;
import com.cmii.cmiicore.exnihilo.items.tools.CrookBase;
import com.cmii.cmiicore.exnihilo.items.tools.EnumCrook;
import com.cmii.cmiicore.exnihilo.items.tools.HammerBase;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import com.cmii.cmiicore.exnihilo.util.IHasSpecialRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ModItems {

    public static final HammerBase hammerWood = new HammerBase("hammer_wood", 64, Item.ToolMaterial.WOOD);
    public static final HammerBase hammerStone = new HammerBase("hammer_stone", 128, Item.ToolMaterial.STONE);
    public static final HammerBase hammerIron = new HammerBase("hammer_iron", 512, Item.ToolMaterial.IRON);
    public static final HammerBase hammerDiamond = new HammerBase("hammer_diamond", 4096, Item.ToolMaterial.DIAMOND);
    public static final HammerBase hammerGold = new HammerBase("hammer_gold", 64, Item.ToolMaterial.GOLD);

    static {
        for (EnumCrook crook : EnumCrook.values()) {
            CrookBase crookItem = new CrookBase(crook.getRegistryName(), crook.getDurability(), crook.getDefaultLevel());
            if (crook == EnumCrook.BLAZE) {
                crookItem.setMaxDamage(3);
            }
        }
    }

    public static final ItemResource resources = new ItemResource();
    public static final ItemCookedSilkworm cookedSilkworm = new ItemCookedSilkworm();

    public static final ItemPebble pebbles = new ItemPebble();

    public static final ItemDoll dolls = new ItemDoll();

    public static final ArrayList<ItemSeedBase> itemSeeds = new ArrayList<>(Arrays.asList(
            new ItemSeedBase("oak", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.OAK)),
            new ItemSeedBase("spruce", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.SPRUCE)),
            new ItemSeedBase("birch", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.BIRCH)),
            new ItemSeedBase("jungle", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.JUNGLE)),
            new ItemSeedBase("acacia", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.ACACIA)),
            new ItemSeedBase("darkoak", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.DARK_OAK)),
            new ItemSeedBase("cactus", Blocks.CACTUS.getDefaultState()).setPlantType(EnumPlantType.Desert),
            new ItemSeedBase("sugarcane", Blocks.REEDS.getDefaultState()).setPlantType(EnumPlantType.Beach),
            new ItemSeedBase("carrot", Blocks.CARROTS.getDefaultState()).setPlantType(EnumPlantType.Crop),
            new ItemSeedBase("potato", Blocks.POTATOES.getDefaultState()).setPlantType(EnumPlantType.Crop)
    ));

    public static ItemRubberSeed rubberSeed;

    public static void registerItems(IForgeRegistry<Item> registry) {
        for (Item item : Data.ITEMS) {
            if (!(item instanceof IHasSpecialRegistry)) {
                registry.register(item);
            }
        }

        for (Block block : Data.BLOCKS) {
            if (!(block instanceof IHasSpecialRegistry)) {
                registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(ModelRegistryEvent e) {
        for (Item item : Data.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).initModel(e);
            }
        }
    }
}
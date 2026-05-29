package com.cmii.cmiicore.proxy;

import com.cmii.cmiicore.api.LoonuimExtraProducts;
import com.cmii.cmiicore.api.SubTilePurifyingFlower;
import com.cmii.cmiicore.catalyst.ModCatalystBlocks;
import com.cmii.cmiicore.catalyst.ModCatalystFluids;
import com.cmii.cmiicore.catalyst.ModCatalystItems;
import com.cmii.cmiicore.catalyst.recipe.MineralCrusherRecipes;
import com.cmii.cmiicore.catalyst.recipe.WeightedOutput;
import com.cmii.cmiicore.exnihilo.ModBlocks;
import com.cmii.cmiicore.exnihilo.ModItems;
import com.cmii.cmiicore.exnihilo.ModFluids;
import com.cmii.cmiicore.exnihilo.capabilities.ENCapabilities;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.util.Data;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.signature.BasicSignature;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy implements IProxy {
    private File configDirectory;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> e) {
        e.getRegistry().registerAll(Data.RECIPES.toArray(new IRecipe[0]));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        configDirectory = event.getModConfigurationDirectory();
        ENCapabilities.init();
        ModFluids.init();

        ModCatalystFluids.init();
        ModCatalystBlocks.touch();
        ModCatalystItems.touch();

        ModBlocks.touch();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ModConfig.loadConfigs();
        ExNihiloRegistryManager.loadAllRegistries(new File(
                configDirectory, "cmiicore/exnihilo"));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        registerTC6LootBags();
        registerPurifyingFlower();
        registerMineralCrusherDefaults();
    }

    private static void registerMineralCrusherDefaults() {
        ItemStack ironDust = getOreDictFirst("dustIron");
        ItemStack copperDust = getOreDictFirst("dustCopper");
        ItemStack tinDust = getOreDictFirst("dustTin");
        ItemStack goldDust = getOreDictFirst("dustGold");
        ItemStack silverDust = getOreDictFirst("dustSilver");
        ItemStack coalDust = getOreDictFirst("dustCoal");
        ItemStack certusDust = getOreDictFirst("dustCertusQuartz");
        ItemStack redstone = new ItemStack(net.minecraft.init.Items.REDSTONE);
        ItemStack lapisDust = getOreDictFirst("dustLapis");
        ItemStack glowstoneDust = getOreDictFirst("dustGlowstone");
        ItemStack netherQuartzDust = getOreDictFirst("dustNetherQuartz");
        ItemStack ironNugget = new ItemStack(net.minecraft.init.Items.IRON_NUGGET);
        ItemStack goldNugget = new ItemStack(net.minecraft.init.Items.GOLD_NUGGET);

        // Base Metal (meta 0)
        if (!ironDust.isEmpty() || !copperDust.isEmpty() || !tinDust.isEmpty()) {
            MineralCrusherRecipes.addRecipe(
                new ItemStack(ModCatalystItems.mineralite, 1, 0),
                new WeightedOutput(ironDust, 40),
                new WeightedOutput(copperDust, 30),
                new WeightedOutput(tinDust, 20),
                new WeightedOutput(ironNugget, 15)
            );
        }

        // Precious Metal (meta 1)
        if (!goldDust.isEmpty() || !silverDust.isEmpty()) {
            MineralCrusherRecipes.addRecipe(
                new ItemStack(ModCatalystItems.mineralite, 1, 1),
                new WeightedOutput(goldDust, 35),
                new WeightedOutput(silverDust, 20),
                new WeightedOutput(goldNugget, 15)
            );
        }

        // Gem (meta 2)
        if (!certusDust.isEmpty() || !lapisDust.isEmpty()) {
            MineralCrusherRecipes.addRecipe(
                new ItemStack(ModCatalystItems.mineralite, 1, 2),
                new WeightedOutput(certusDust, 35),
                new WeightedOutput(redstone, 25),
                new WeightedOutput(lapisDust, 15)
            );
        }

        // Fuel (meta 3)
        if (!coalDust.isEmpty()) {
            MineralCrusherRecipes.addRecipe(
                new ItemStack(ModCatalystItems.mineralite, 1, 3),
                new WeightedOutput(coalDust, 50)
            );
        }

        // Rare (meta 4)
        if (!glowstoneDust.isEmpty() || !netherQuartzDust.isEmpty()) {
            MineralCrusherRecipes.addRecipe(
                new ItemStack(ModCatalystItems.mineralite, 1, 4),
                new WeightedOutput(redstone, 30),
                new WeightedOutput(glowstoneDust, 20),
                new WeightedOutput(netherQuartzDust, 15)
            );
        }

        // Essentia (meta 5) — reserved for future Thaumcraft integration
    }

    private static ItemStack getOreDictFirst(String name) {
        List<ItemStack> ores = OreDictionary.getOres(name);
        return ores.isEmpty() ? ItemStack.EMPTY : ores.get(0);
    }

    public boolean runningOnServer() {
        return true;
    }

    /**
     * Register Thaumcraft 6 loot bags as extra Loonium drops.
     * Common bags appear more frequently, rare bags less so.
     */
    private static void registerTC6LootBags() {
        if (!Loader.isModLoaded("thaumcraft")) {
            return;
        }

        Item lootBag = Item.getByNameOrId("thaumcraft:loot_bag");
        if (lootBag == null) {
            return;
        }

        // Common bag - highest frequency
        LoonuimExtraProducts.addExtraLoot(new ItemStack(lootBag, 1, 0));
        LoonuimExtraProducts.addExtraLoot(new ItemStack(lootBag, 1, 0));
        LoonuimExtraProducts.addExtraLoot(new ItemStack(lootBag, 1, 0));

        // Uncommon bag - medium frequency
        LoonuimExtraProducts.addExtraLoot(new ItemStack(lootBag, 1, 1));
        LoonuimExtraProducts.addExtraLoot(new ItemStack(lootBag, 1, 1));

        // Rare bag - low frequency
        LoonuimExtraProducts.addExtraLoot(new ItemStack(lootBag, 1, 2));
    }

    /**
     * Register the Purifying Flower as a functional Botania flower.
     * The flower consumes mana to clean TC6 flux from its chunk.
     */
    private static void registerPurifyingFlower() {
        BotaniaAPI.registerSubTile(
            "purifyingFlower", SubTilePurifyingFlower.class);
        BotaniaAPI.registerSubTileSignature(
            SubTilePurifyingFlower.class,
            new BasicSignature("cmiicore.purifyingFlower"));
        BotaniaAPI.registerMiniSubTile(
            "purifyingFlower", SubTilePurifyingFlower.class, "");
        BotaniaAPI.addSubTileToCreativeMenu("purifyingFlower");
    }
}

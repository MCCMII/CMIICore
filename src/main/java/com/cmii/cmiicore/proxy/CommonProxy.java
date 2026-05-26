package com.cmii.cmiicore.proxy;

import com.cmii.cmiicore.api.LoonuimExtraProducts;
import com.cmii.cmiicore.api.SubTilePurifyingFlower;
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

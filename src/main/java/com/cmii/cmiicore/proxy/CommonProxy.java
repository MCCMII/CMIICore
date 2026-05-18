package com.cmii.cmiicore.proxy;

import com.cmii.cmiicore.api.LoonuimExtraProducts;
import com.cmii.cmiicore.api.SubTilePurifyingFlower;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.signature.BasicSignature;

public class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void init(FMLInitializationEvent event) {
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        registerTC6LootBags();
        registerPurifyingFlower();
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
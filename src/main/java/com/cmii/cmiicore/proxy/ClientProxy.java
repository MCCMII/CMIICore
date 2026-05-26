package com.cmii.cmiicore.proxy;

import com.cmii.cmiicore.Reference;
import com.cmii.cmiicore.exnihilo.ModBlocks;
import com.cmii.cmiicore.exnihilo.ModFluids;
import com.cmii.cmiicore.exnihilo.ModItems;
import com.cmii.cmiicore.exnihilo.client.models.event.RenderEvent;
import com.cmii.cmiicore.exnihilo.client.renderers.RenderBarrel;
import com.cmii.cmiicore.exnihilo.client.renderers.RenderCrucible;
import com.cmii.cmiicore.exnihilo.client.renderers.RenderInfestingLeaves;
import com.cmii.cmiicore.exnihilo.tiles.TileBarrel;
import com.cmii.cmiicore.exnihilo.tiles.TileCrucibleStone;
import com.cmii.cmiicore.exnihilo.tiles.TileCrucibleWood;
import com.cmii.cmiicore.exnihilo.tiles.TileInfestingLeaves;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels(event);
        ModItems.initModels(event);
        ModFluids.initModels();
        registerRenderers();
    }

    public static void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucibleStone.class, new RenderCrucible());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCrucibleWood.class, new RenderCrucible());
        ClientRegistry.bindTileEntitySpecialRenderer(TileInfestingLeaves.class, new RenderInfestingLeaves());
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        MinecraftForge.EVENT_BUS.register(new RenderEvent());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public boolean runningOnServer() {
        return false;
    }
}
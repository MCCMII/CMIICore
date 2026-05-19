package com.cmii.cmiicore;

import com.cmii.cmiicore.exnihilo.command.CommandHandNBT;
import com.cmii.cmiicore.exnihilo.command.CommandReloadConfig;
import com.cmii.cmiicore.exnihilo.handlers.HandlerCrook;
import com.cmii.cmiicore.exnihilo.handlers.HandlerHammer;
import com.cmii.cmiicore.exnihilo.networking.PacketHandler;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloDefaultRecipes;
import com.cmii.cmiicore.exnihilo.registries.registries.BarrelModeRegistry;
import com.cmii.cmiicore.exnihilo.util.LogUtil;
import com.cmii.cmiicore.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        useMetadata = true
)
public class CMIICore {

    @Mod.Instance(Reference.MOD_ID)
    public static CMIICore instance;

    @SidedProxy(
            modId = Reference.MOD_ID,
            clientSide = "com.cmii.cmiicore.proxy.ClientProxy",
            serverSide = "com.cmii.cmiicore.proxy.CommonProxy"
    )
    public static IProxy proxy;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("{} is starting.", Reference.MOD_NAME);

        LogUtil.setup();

        PacketHandler.initPackets();

        MinecraftForge.EVENT_BUS.register(new HandlerHammer());
        MinecraftForge.EVENT_BUS.register(new HandlerCrook());

        BarrelModeRegistry.registerDefaults();
        ExNihiloDefaultRecipes.registerDefaults();

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadConfig());
        event.registerServerCommand(new CommandHandNBT());
    }
}

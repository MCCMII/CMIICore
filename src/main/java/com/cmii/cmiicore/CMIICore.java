package com.cmii.cmiicore;

import com.cmii.cmiicore.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
}

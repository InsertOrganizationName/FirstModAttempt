package gmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GMod.MOD_ID, version = GMod.VERSION)
public class GMod {

    static final String MOD_ID = "gmod";
    static final String VERSION = "0.0.1";

    @Instance(GMod.MOD_ID)
    public static GMod instance;

    @SidedProxy(clientSide = "gmod.ClientOnlyProxy", serverSide = "gmod.DedicatedServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent fmlPreInitializationEvent) {
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent fmlInitializationEvent)
    {

    }
}

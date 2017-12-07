package examplemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    static final String MODID = "examplemod";
    static final String VERSION = "0.0.1";

    @Mod.Instance(ExampleMod.MODID)
    public static ExampleMod instance;

    @SidedProxy(clientSide = "examplemod.ClientOnlyProxy", serverSide = "examplemod.ClientOnlyProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    public static String prependModID(String name) {return MODID + ":" + name;}
}

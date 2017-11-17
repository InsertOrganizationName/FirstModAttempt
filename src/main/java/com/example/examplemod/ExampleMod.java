package com.example.examplemod;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    static final String MODID = "first_mod";
    static final String VERSION = "0.0.1";

    @SidedProxy(clientSide = "com.example.examplemod.ClientOnlyProxy", serverSide = "com.example.examplemod.ClientOnlyProxy")
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

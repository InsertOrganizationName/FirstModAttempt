package com.example.examplemod;

import net.minecraft.entity.player.EntityPlayer;

public abstract class CommonProxy {
    public void preInit() {
        StartupCommon.preInitCommon();
    }

    abstract public boolean playerIsInCreativeMode(EntityPlayer player);

    abstract public boolean isDedicatedServer();
}

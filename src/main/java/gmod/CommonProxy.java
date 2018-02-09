package gmod;

import net.minecraft.entity.player.EntityPlayer;

public abstract class CommonProxy {

    public void preInit() { }

    public void init() { }

    public void postInit() { }

    abstract public boolean playerIsInCreativeMode(EntityPlayer player);

    abstract public boolean isDedicatedServer();
}
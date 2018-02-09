package gmod;

import insertorgname.anvil.particle.BaseParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.List;

public class ClientOnlyProxy extends CommonProxy {

    public static List<KeyBinding> keyBindings;

    @Override
    public boolean playerIsInCreativeMode(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
            return entityPlayerMP.interactionManager.isCreative();
        } else if (player instanceof EntityPlayerSP) {
            return Minecraft.getMinecraft().playerController.isInCreativeMode();
        }
        return false;
    }

    @Override
    public boolean isDedicatedServer() {
        return false;
    }

    @Override
    public void preInit() {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(BaseParticle.registerAllChildren());
        MinecraftForge.EVENT_BUS.register(new KeyBindingHandler());

        keyBindings = new ArrayList<>();
        keyBindings.add(new KeyBinding("gmod.test", org.lwjgl.input.Keyboard.KEY_P, "key.gmod.category"));

        keyBindings.forEach(ClientRegistry::registerKeyBinding);
    }
}

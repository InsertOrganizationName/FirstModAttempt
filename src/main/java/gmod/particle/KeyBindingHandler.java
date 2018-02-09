package gmod.particle;

import gmod.ClientOnlyProxy;
import insertorgname.anvil.utils.Position;
import insertorgname.anvil.utils.Velocity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeyBindingHandler {

    //TODO TL 2018-02-09 : Use the param
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInputEvent(KeyInputEvent keyInputEvent) {
        ClientOnlyProxy.keyBindings.forEach(keyBinding -> {
            if (keyBinding.isPressed()) {
                System.out.println(String.format("Key Binding Pressed: %s", keyBinding.getKeyDescription()));

                EntityPlayerSP player = Minecraft.getMinecraft().player;

                Minecraft.getMinecraft().effectRenderer.addEffect(
                        FlameParticle.of(player.world,
                                Position.of(player.getPositionEyes(0)),
                                Velocity.of(player.getLookVec()))
                );
            }
        });
    }
}

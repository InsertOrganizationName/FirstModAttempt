package gmod.particle;

import gmod.ClientOnlyProxy;
import gmod.utils.Position;
import gmod.utils.Velocity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureStitcherBreathFx {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEventASFD(KeyInputEvent keyInputEvent) {
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

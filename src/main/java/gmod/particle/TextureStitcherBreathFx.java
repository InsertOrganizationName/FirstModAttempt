package gmod.particle;

import gmod.ClientOnlyProxy;
import gmod.utils.Position;
import gmod.utils.Velocity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureStitcherBreathFx {
    @SubscribeEvent
    public void stitcherEventPre(TextureStitchEvent.Pre event) {
        ResourceLocation flameRL = new ResourceLocation("gmod:entity/flame_fx");
        event.getMap().registerSprite(flameRL);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(KeyInputEvent keyInputEvent) {
        ClientOnlyProxy.keyBindings.forEach(keyBinding -> {
            if (keyBinding.isPressed()) {
                System.out.println(String.format("Key Binding Pressed: %s", keyBinding.getKeyDescription()));

                EntityPlayerSP player = Minecraft.getMinecraft().player;
                BlockPos playerPosition = player.getPosition();

                Minecraft.getMinecraft().effectRenderer.addEffect(
                        FlameParticle.of(player.world,
                                Position.of(playerPosition.getX(), playerPosition.getY() + 1, playerPosition.getZ()),
                                Velocity.of(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z))
                );
            }
        });
    }
}

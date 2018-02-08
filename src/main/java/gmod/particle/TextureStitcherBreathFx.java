package gmod.particle;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStitcherBreathFx {
    @SubscribeEvent
    public void stitcherEventPre(TextureStitchEvent.Pre event) {
        ResourceLocation flameRL = new ResourceLocation("gmod:entity/flame_fx");
        event.getMap().registerSprite(flameRL);
    }
}

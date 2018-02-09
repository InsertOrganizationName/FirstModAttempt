package gmod.particle;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

public class ParticleTextureStitchEventHandler {
    private Collection<ResourceLocation> resourceLocations;

    public ParticleTextureStitchEventHandler(Collection<ResourceLocation> resourceLocations) {
        this.resourceLocations = resourceLocations;
    }

    @SubscribeEvent
    public void stitcherEventPreASFD(TextureStitchEvent.Pre event) {
        for (ResourceLocation resourceLocation : this.resourceLocations) {
            event.getMap().registerSprite(resourceLocation);
        }
    }
}

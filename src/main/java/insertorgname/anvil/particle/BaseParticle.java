package insertorgname.anvil.particle;

import insertorgname.anvil.utils.ClassReflectionMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseParticle extends Particle {

    protected abstract ResourceLocation getResourceLocation();

    protected BaseParticle() {
        super(Minecraft.getMinecraft().world, 0, 0, 0);
        this.particleMaxAge = 0;
    }

    protected BaseParticle(BaseParticleInputContainer inputContainer) {
        super(inputContainer.getWorld(),
              inputContainer.getPosition().getX(), inputContainer.getPosition().getY(), inputContainer.getPosition().getZ(),
              inputContainer.getVelocity().getX(), inputContainer.getVelocity().getY(), inputContainer.getVelocity().getZ());

        if (!inputContainer.getShouldApplyVanillaInitialVelocityRandomization()) {
            this.motionX = inputContainer.getVelocity().getX();
            this.motionY = inputContainer.getVelocity().getY();
            this.motionZ = inputContainer.getVelocity().getZ();
        }

        this.particleGravity = inputContainer.getParticleGravity();
        this.particleAlpha = inputContainer.getParticleAlpha();
        this.particleMaxAge = inputContainer.getParticleMaxAge();

        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite(this.getResourceLocation().toString());
        this.setParticleTexture(sprite);
    }

    public static ParticleTextureStitchEventHandler registerAllChildren() {

        Collection<BaseParticle> subclasses = ClassReflectionMagic.getSubclasses(BaseParticle.class, "gmod.particle");

        Collection<ResourceLocation> resourceLocations = subclasses.stream().map(BaseParticle::getResourceLocation).collect(Collectors.toList());

        return new ParticleTextureStitchEventHandler(resourceLocations);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    private static class ParticleTextureStitchEventHandler {
        private Collection<ResourceLocation> resourceLocations;

        private ParticleTextureStitchEventHandler(Collection<ResourceLocation> resourceLocations) {
            this.resourceLocations = resourceLocations;
        }

        @SubscribeEvent
        public void handleParticleTextureStitchEvent(TextureStitchEvent.Pre event) {
            for (ResourceLocation resourceLocation : this.resourceLocations) {
                event.getMap().registerSprite(resourceLocation);
            }
        }
    }
}


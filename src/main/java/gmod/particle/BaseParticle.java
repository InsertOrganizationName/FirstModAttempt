package gmod.particle;

import gmod.utils.ClassReflectionMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseParticle extends Particle {

    protected abstract ResourceLocation getResourceLocation();

    BaseParticle() {
        super(Minecraft.getMinecraft().world, 0, 0, 0);
        this.particleMaxAge = 0;
    }

    BaseParticle(BaseParticleConstructorInputContainer inputContainer) {
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
}


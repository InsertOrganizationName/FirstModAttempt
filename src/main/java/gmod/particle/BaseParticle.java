package gmod.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public abstract class BaseParticle extends Particle {

    private final ResourceLocation resourceLocation = getResourceLocation();

    protected abstract ResourceLocation getResourceLocation();

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

    @Override
    public int getFXLayer() {
        return 1;
    }
}


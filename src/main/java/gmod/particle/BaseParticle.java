package gmod.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;

public abstract class BaseParticle extends Particle {

    private final ResourceLocation resourceLocation = getResourceLocation();

    protected abstract ResourceLocation getResourceLocation();

    public BaseParticle(BaseParticleConstructorInputContainer inputContainer) {
        super(inputContainer.getWorld(),
              inputContainer.getPositionX(), inputContainer.getPositionY(), inputContainer.getPositionZ(),
              inputContainer.getVelocityX(), inputContainer.getVelocityY(), inputContainer.getVelocityZ());

        if (!inputContainer.shouldApplyVanillaInitialVelocityRandomization()) {
            this.motionX = inputContainer.getVelocityX();
            this.motionY = inputContainer.getVelocityY();
            this.motionZ = inputContainer.getVelocityZ();
        }

        this.particleGravity = inputContainer.getParticleGravity();

    }
}


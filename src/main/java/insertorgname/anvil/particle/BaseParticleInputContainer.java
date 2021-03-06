package insertorgname.anvil.particle;

import insertorgname.anvil.utils.Position;
import insertorgname.anvil.utils.Velocity;
import net.minecraft.world.World;

public class BaseParticleInputContainer {
    private World world;
    private Position position;
    private Velocity velocity;
    private float particleGravity;
    private boolean shouldApplyVanillaInitialVelocityRandomization;
    private float particleAlpha;
    private int particleMaxAge;

    public float getParticleAlpha() {
        return particleAlpha;
    }

    public BaseParticleInputContainer setParticleAlpha(float particleAlpha) {
        this.particleAlpha = particleAlpha;
        return this;
    }

    public float getParticleGravity() {
        return particleGravity;
    }

    public BaseParticleInputContainer setParticleGravity(float particleGravity) {
        this.particleGravity = particleGravity;
        return this;
    }

    public int getParticleMaxAge() {
        return particleMaxAge;
    }

    public BaseParticleInputContainer setParticleMaxAge(int particleMaxAge) {
        this.particleMaxAge = particleMaxAge;
        return this;
    }

    public boolean getShouldApplyVanillaInitialVelocityRandomization() {
        return shouldApplyVanillaInitialVelocityRandomization;
    }

    public BaseParticleInputContainer setShouldApplyVanillaInitialVelocityRandomization(boolean shouldApplyVanillaInitialVelocityRandomization) {
        this.shouldApplyVanillaInitialVelocityRandomization = shouldApplyVanillaInitialVelocityRandomization;
        return this;
    }

    public World getWorld() {
        return world;
    }

    public BaseParticleInputContainer setWorld(World world) {
        this.world = world;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public BaseParticleInputContainer setPosition(Position position) {
        this.position = position;
        return this;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public BaseParticleInputContainer setVelocity(Velocity velocity) {
        this.velocity = velocity;
        return this;
    }
}

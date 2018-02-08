package gmod.particle;

import gmod.utils.Position;
import gmod.utils.Velocity;
import net.minecraft.world.World;

class BaseParticleConstructorInputContainer {
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

    public BaseParticleConstructorInputContainer setParticleAlpha(float particleAlpha) {
        this.particleAlpha = particleAlpha;
        return this;
    }

    public float getParticleGravity() {
        return particleGravity;
    }

    public BaseParticleConstructorInputContainer setParticleGravity(float particleGravity) {
        this.particleGravity = particleGravity;
        return this;
    }

    public int getParticleMaxAge() {
        return particleMaxAge;
    }

    public BaseParticleConstructorInputContainer setParticleMaxAge(int particleMaxAge) {
        this.particleMaxAge = particleMaxAge;
        return this;
    }

    public boolean getShouldApplyVanillaInitialVelocityRandomization() {
        return shouldApplyVanillaInitialVelocityRandomization;
    }

    public BaseParticleConstructorInputContainer setShouldApplyVanillaInitialVelocityRandomization(boolean shouldApplyVanillaInitialVelocityRandomization) {
        this.shouldApplyVanillaInitialVelocityRandomization = shouldApplyVanillaInitialVelocityRandomization;
        return this;
    }

    public World getWorld() {
        return world;
    }

    public BaseParticleConstructorInputContainer setWorld(World world) {
        this.world = world;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public BaseParticleConstructorInputContainer setPosition(Position position) {
        this.position = position;
        return this;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public BaseParticleConstructorInputContainer setVelocity(Velocity velocity) {
        this.velocity = velocity;
        return this;
    }
}

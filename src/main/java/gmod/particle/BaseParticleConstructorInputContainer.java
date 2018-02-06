package gmod.particle;

import net.minecraft.world.World;

class BaseParticleConstructorInputContainer {
    private final World world;
    private final double positionX;
    private final double positionY;
    private final double positionZ;
    private final double velocityX;
    private final double velocityY;
    private final double velocityZ;

    public BaseParticleConstructorInputContainer(World world, double positionX, double positionY, double positionZ, double velocityX, double velocityY, double velocityZ) {
        this.world = world;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    public World getWorld() {
        return world;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getPositionZ() {
        return positionZ;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityZ() {
        return velocityZ;
    }
}

package gmod.particle;

import gmod.utils.Position;
import gmod.utils.Velocity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FlameParticle extends BaseParticle {

    public static FlameParticle of(World world, Position position) {
        BaseParticleConstructorInputContainer inputContainer = getFlameParticleInputContainer()
                .setWorld(world)
                .setPosition(position);

        return new FlameParticle(inputContainer);
    }

    public static FlameParticle of(World world, Position position, Velocity velocity) {
        BaseParticleConstructorInputContainer inputContainer = getFlameParticleInputContainer()
                .setWorld(world)
                .setPosition(position)
                .setVelocity(velocity);

        return new FlameParticle(inputContainer);
    }

    private static BaseParticleConstructorInputContainer getFlameParticleInputContainer() {
        return new BaseParticleConstructorInputContainer()
                .setParticleAlpha(.99f)
                .setParticleGravity(0f)
                .setShouldApplyVanillaInitialVelocityRandomization(false)
                .setParticleMaxAge(50)
                .setVelocity(Velocity.of(0, 0, 0));
    }

    public FlameParticle() {
        super();
    }

    private FlameParticle(BaseParticleConstructorInputContainer inputContainer) {
        super(inputContainer);
    }

    @Override
    protected ResourceLocation getResourceLocation() {
        return new ResourceLocation("gmod:entity/flame_fx");
    }
}

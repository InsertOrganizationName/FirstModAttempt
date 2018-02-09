package gmod.particle;

import insertorgname.anvil.particle.BaseParticle;
import insertorgname.anvil.particle.BaseParticleInputContainer;
import insertorgname.anvil.utils.Position;
import insertorgname.anvil.utils.Velocity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FlameParticle extends BaseParticle {

    // region Constructors
    public static FlameParticle of(World world, Position position) {
        BaseParticleInputContainer inputContainer = getFlameParticleInputContainer()
                .setWorld(world)
                .setPosition(position);

        return new FlameParticle(inputContainer);
    }

    public static FlameParticle of(World world, Position position, Velocity velocity) {
        BaseParticleInputContainer inputContainer = getFlameParticleInputContainer()
                .setWorld(world)
                .setPosition(position)
                .setVelocity(velocity);

        return new FlameParticle(inputContainer);
    }

    @SuppressWarnings("unused") // Empty constructor needed for Anvil
    public FlameParticle() {
        super();
    }

    private FlameParticle(BaseParticleInputContainer inputContainer) {
        super(inputContainer);
    }
    // endregion

    @Override
    protected ResourceLocation getResourceLocation() {
        return new ResourceLocation("gmod:entity/flame_fx");
    }

    private static BaseParticleInputContainer getFlameParticleInputContainer() {
        return new BaseParticleInputContainer()
                .setParticleAlpha(.99f)
                .setParticleGravity(0f)
                .setShouldApplyVanillaInitialVelocityRandomization(false)
                .setParticleMaxAge(50)
                .setVelocity(Velocity.of(0, 0, 0));
    }
}

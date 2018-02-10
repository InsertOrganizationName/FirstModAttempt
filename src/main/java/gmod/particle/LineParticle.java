package gmod.particle;

import insertorgname.anvil.particle.BaseParticle;
import insertorgname.anvil.particle.BaseParticleInputContainer;
import insertorgname.anvil.utils.Position;
import insertorgname.anvil.utils.Velocity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LineParticle extends BaseParticle {

    public static LineParticle of(World world, EntityPlayer entityPlayer, EntityItem entityItem) {

        Vec3d positionEyes = entityPlayer.getPositionEyes(0);
        Vec3d itemPosition = entityItem.getPositionEyes(0);

        Vec3d launchDirection = itemPosition.subtract(positionEyes).normalize();


        return new LineParticle(getFlameParticleInputContainer()
                .setWorld(world)
                .setPosition(Position.of(entityPlayer.getPositionEyes(0)))
                .setVelocity(Velocity.of(launchDirection))
        );
    }

    public LineParticle() {
        super();
    }

    private LineParticle(BaseParticleInputContainer inputContainer) {
        super(inputContainer);
    }

    @Override
    protected ResourceLocation getResourceLocation() {
        return new ResourceLocation("gmod:entity/line_particle");
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

package gmod.utils;

import net.minecraft.util.math.Vec3d;

public class Velocity extends Vector3 {

    public static Velocity of(double x, double y, double z) {
        return new Velocity(x, y, z);
    }

    public static Velocity of(Vec3d vec3d) {
        return new Velocity(vec3d.x, vec3d.y, vec3d.z);
    }

    private Velocity(double x, double y, double z) {
        super(x, y, z);
    }
}

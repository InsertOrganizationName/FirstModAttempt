package insertorgname.anvil.utils;

import net.minecraft.util.math.Vec3d;

public class Position extends Vector3 {

    public static Position of(double x, double y, double z) {
        return new Position(x, y, z);
    }

    public static Position of(Vec3d vec3d) {
        return new Position(vec3d.x, vec3d.y, vec3d.z);
    }

    private Position(double x, double y, double z) {
        super(x, y, z);
    }
}


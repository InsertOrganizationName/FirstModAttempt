package insertorgname.anvil.utils;

public class Vector3 {
    private final double x;
    private final double y;
    private final double z;

    public static Vector3 of(double x, double y, double z) {
        return new Vector3(x, y, z);
    }

    Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}


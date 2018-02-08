package gmod.utils;

public class Velocity extends Vector3 {

    public static Velocity of(double x, double y, double z) {
        return new Velocity(x, y, z);
    }

    private Velocity(double x, double y, double z) {
        super(x, y, z);
    }
}

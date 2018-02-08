package gmod.utils;

public class Position extends Vector3 {

    public static Position of(double z, double y, double x) {
        return new Position(x, y, z);
    }

    private Position(double x, double y, double z) {
        super(x, y, z);
    }
}


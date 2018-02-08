package gmod.utils;

public class Position extends Vector3 {

    public static Position of(double x, double y, double z) {
        return new Position(x, y, z);
    }

    private Position(double x, double y, double z) {
        super(x, y, z);
    }
}


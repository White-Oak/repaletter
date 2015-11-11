package me.oak.imgcolor.octo;

import me.oak.imgcolor.Color;

public class Sphere extends Cube {

    int doubledRadius;
    int smallRadius;

    public static Sphere centeredAround(int x, int y, int z, int halfSize) {
	return new Sphere(x, y, z, halfSize);
    }

    public static Sphere centeredAround(Color color, int halfSize) {
	return centeredAround(color.red, color.green, color.blue, halfSize);
    }

    public static Sphere centeredAround(Cube cube, int halfSize) {
	return centeredAround(cube.getX(), cube.getY(), cube.getZ(), halfSize);
    }

    protected Sphere(int x, int y, int z, int radius) {
	super(x, y, z, (short) radius);
	calculateRadiuses(radius);
    }

    private void calculateRadiuses(int radius1) {
	doubledRadius = getSquare(radius1);
	smallRadius = SMALL_RADIUSES_CACHE[radius1];
    }

    @Override
    public Sphere increaseRadius(int delta) {
	radius += delta;
	calculateRadiuses(radius);
	return this;
    }

    @Override
    public boolean contains(Cube cube) {
	return cube.radius <= radius && contains(cube.getX(), cube.getY(), cube.getZ());
//	return cube.radius <= smallRadius;
    }

    @Override
    public boolean contains(int x, int y, int z) {
//	if (!super.contains(x, y, z)) {
//	    return false;
//	}
	//What if replace squares with lookup tables
	return (getSquare(x - getX())
		+ getSquare(y - getY())
		+ getSquare(z - getZ()) <= doubledRadius);
    }

    private final static int[] SMALL_RADIUSES_CACHE = new int[257 * 2];

    private static int getSquare(final int abs) {
	return SQUARES_CACHE[abs + 514];
    }
    private final static int[] SQUARES_CACHE = new int[257 * 2 * 2];

    static {
	for (int i = 0; i < SQUARES_CACHE.length; i++) {
	    final int name = i - 514;
	    SQUARES_CACHE[i] = name * name;
	}
	for (int i = 0; i < SMALL_RADIUSES_CACHE.length; i++) {
	    SMALL_RADIUSES_CACHE[i] = (int) (Math.sqrt(2) / 2 * i);
	}
    }

    @Override
    public boolean contains(Color color) {
	return contains(color.red, color.green, color.blue);
    }

}

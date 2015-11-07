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
	return centeredAround(cube.x, cube.y, cube.z, halfSize);
    }

    protected Sphere(int x, int y, int z, int radius) {
	super(x, y, z, radius);
	doubledRadius = radius * radius;
	smallRadius = (int) (Math.sqrt(2) / 2 * radius);
    }

    @Override
    public Sphere increaseRadius(int delta) {
	radius += delta;
	doubledRadius = radius * radius;
	return this;
    }

    @Override
    public boolean contains(Cube cube) {
	return cube.radius <= radius && contains(cube.x, cube.y, cube.z);
//	return cube.radius <= smallRadius;
    }

    @Override
    public boolean contains(int x, int y, int z) {
	if (!super.contains(x, y, z)) {
	    return false;
	}
	final int xC = x - this.x;
	final int yC = y - this.y;
	final int zC = z - this.z;
	return (xC * xC
		+ yC * yC
		+ zC * zC <= doubledRadius);
    }

    @Override
    public boolean contains(Color color) {
	return contains(color.red, color.green, color.blue);
    }

}

package me.oak.imgcolor.octo;

import lombok.*;
import me.oak.imgcolor.Color;

/**
 *
 * @author White Oak
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Cube {

    final Color color;
    short radius;

    public static Cube centeredAround(int x, int y, int z, int halfSize) {
	return new Cube(x, y, z, (short) halfSize);
    }

    public static Cube centeredAround(Color color, int halfSize) {
	return centeredAround(color.red, color.green, color.blue, halfSize);
    }

    public static Cube centeredAround(Cube cube, int halfSize) {
	return centeredAround(cube.getX(), cube.getY(), cube.getZ(), halfSize);
    }

    protected Cube(int x, int y, int z, short radius) {
	this(Color.of(x, y, z), radius);
    }

    public int getX() {
	return color.red;
    }

    public int getY() {
	return color.green;
    }

    public int getZ() {
	return color.blue;
    }

    public Cube increaseRadius(int delta) {
	radius += delta;
	return this;
    }

    public boolean contains(Color color) {
	return contains(color.red, color.green, color.blue);
    }

    //The method I fucked up
    public boolean contains(Cube cube) {
	int x = Math.abs(getX() - cube.getX());
	int y = Math.abs(getY() - cube.getY());
	int z = Math.abs(getZ() - cube.getZ());
	int radDiff = cube.radius - radius;
	return ((radDiff - x) < 0)
		&& ((radDiff - y) < 0)
		&& ((radDiff - z) < 0);
    }

    public boolean contains(int x, int y, int z) {
	return ((Math.abs(x - getX()) < radius) || (getX() - x == radius))
		&& ((Math.abs(y - getY()) < radius) || (getY() - y == radius))
		&& ((Math.abs(z - getZ()) < radius) || (getZ() - z == radius));
    }

    public boolean intersects(Cube cube) {
	final int sizeSum = radius + cube.radius;
	return (Math.abs(getX() - cube.getX()) <= sizeSum)
		&& (Math.abs(getY() - cube.getY()) <= sizeSum)
		&& (Math.abs(getZ() - cube.getZ()) <= sizeSum);
    }

    public boolean nearby(Cube cube) {
	final int sizeSum = radius + cube.radius;
	return intersects(cube)
		&& ((Math.abs(cube.getX() - getX()) == sizeSum)
		    || (Math.abs(cube.getY() - getY()) == sizeSum)
		    || (Math.abs(cube.getZ() - getZ()) == sizeSum));
    }
}

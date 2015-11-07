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

    final int x, y, z;
    int radius;

    public static Cube centeredAround(int x, int y, int z, int halfSize) {
	return new Cube(x, y, z, halfSize);
    }

    public static Cube centeredAround(Color color, int halfSize) {
	return centeredAround(color.red, color.green, color.blue, halfSize);
    }

    public static Cube centeredAround(Cube cube, int halfSize) {
	return centeredAround(cube.x, cube.y, cube.z, halfSize);
    }

    public Cube increaseRadius(int delta) {
	radius += delta;
	return this;
    }

    public boolean contains(Color color) {
	return contains(color.red, color.green, color.blue);
    }

    public boolean contains(Cube cube) {
	int x = Math.abs(this.x - cube.x);
	int y = Math.abs(this.y - cube.y);
	int z = Math.abs(this.z - cube.z);
	int radDiff = cube.radius - radius;
	return ((radDiff - x) < 0)
		&& ((radDiff - y) < 0)
		&& ((radDiff - z) < 0);
    }

    public boolean contains(int x, int y, int z) {
	return ((Math.abs(x - this.x) < radius) || (this.x - x == radius))
		&& ((Math.abs(y - (this.y)) < radius) || (this.y - y == radius))
		&& ((Math.abs(z - (this.z)) < radius) || (this.z - z == radius));
    }

    public boolean intersects(Cube cube) {
	final int sizeSum = radius + cube.radius;
	return (Math.abs(x - cube.x) <= sizeSum)
		&& (Math.abs(y - cube.y) <= sizeSum)
		&& (Math.abs(z - cube.z) <= sizeSum);
    }

    public boolean nearby(Cube cube) {
	final int sizeSum = radius + cube.radius;
	return intersects(cube)
		&& ((Math.abs(cube.x - x) == sizeSum)
		    || (Math.abs(cube.y - y) == sizeSum)
		    || (Math.abs(cube.z - z) == sizeSum));
    }
}

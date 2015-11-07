package me.oak.imgcolor;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class Cube {

    final int x, y, z, size;

    public static Cube centeredAround(int x, int y, int z, int halfSize) {
	return new Cube(x - halfSize, y - halfSize, z - halfSize, halfSize * 2);
    }

    public static Cube centeredAround(Color color, int halfSize) {
	return centeredAround(color.red, color.green, color.blue, halfSize);
    }

    public static Cube centeredAround(Cube cube, int halfSize) {
	final int halfSize2 = cube.size / 2;
	return centeredAround(cube.x + halfSize2, cube.y + halfSize2, cube.z + halfSize2, halfSize);
    }

    public boolean contains(Color color) {
	return contains(color.red, color.green, color.blue);
    }

    public boolean contains(Cube cube) {
	return cube.size <= size && contains(cube.x, cube.y, cube.z);
    }

    public boolean contains(int x, int y, int z) {
	return (x >= this.x && x < this.x + size)
		&& (y >= this.y && y < this.y + size)
		&& (z >= this.z && z < this.z + size);
    }

    public boolean intersects(Cube cube) {
	int halfSize = size / 2;
	int halfSize2 = cube.size / 2;
	final int sizeSum = halfSize + halfSize2;
	final int sizeDiff = halfSize - halfSize2;
	return (Math.abs(x - cube.x + sizeDiff) <= sizeSum)
		&& (Math.abs(y - cube.y + sizeDiff) <= sizeSum)
		&& (Math.abs(z - cube.z + sizeDiff) <= sizeSum);
    }

    public boolean nearby(Cube cube) {
	int halfSize = size / 2;
	int halfSize2 = cube.size / 2;
	final int sizeSum = halfSize + halfSize2;
	final int sizeDiff = halfSize - halfSize2;
	return intersects(cube)
		&& ((Math.abs(cube.x - x + sizeDiff) == sizeSum)
		    || (Math.abs(cube.y - y + sizeDiff) == sizeSum)
		    || (Math.abs(cube.z - z + sizeDiff) == sizeSum));
    }
}

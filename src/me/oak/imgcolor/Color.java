package me.oak.imgcolor;

import lombok.EqualsAndHashCode;

/**
 *
 * @author White Oak
 */
@EqualsAndHashCode(of = "color")
public class Color implements Comparable<Color> {

    private static final Color[][][] CACHE;

    public final short red, green, blue;
    public final int color;

    public static int getColor(int color) {
	return getColor(getRed(color), getGreen(color), getBlue(color));
    }

    public static Color of(int red, int green, int blue) {
	return CACHE[red][green][blue];
    }

    public static Color of(int color) {
	return CACHE[getRed(color)][getGreen(color)][getBlue(color)];
    }

    static short getRed(int color) {
	return (short) ((color >>> 16) & 0xff);
    }

    static short getGreen(int color) {
	return (short) ((color >>> 8) & 0xff);
    }

    static short getBlue(int color) {
	return (short) (color & 0xff);
    }

    private static int getColor(int red, int green, int blue) {
	return (0xff000000) | (red << 16) | (green << 8) | blue;
    }

    static {
	CACHE = new Color[256][256][256];
	for (short i = 0; i < CACHE.length; i++) {
	    Color[][] colorses = CACHE[i];
	    for (short j = 0; j < colorses.length; j++) {
		Color[] colorse = colorses[j];
		for (short k = 0; k < colorse.length; k++) {
		    Color color = new Color(i, j, k);
		    colorse[k] = color;
		    assert getColor(color.red, color.green, color.blue) == color.color;
		    assert getColor(getRed(color.color), getGreen(color.color), getBlue(color.color)) == color.color;
		}
	    }
	}
    }

    private Color(short red, short green, short blue) {
	this.red = red;
	this.green = green;
	this.blue = blue;
	this.color = getColor(red, green, blue);
    }

    private Color(int color) {
	this.red = getRed(color);
	this.green = getGreen(color);
	this.blue = getBlue(color);
	this.color = color;
    }

    @Override
    public int compareTo(Color o) {
	return Integer.compare(color, o.color);
    }

}

package me.oak.imgcolor;

/**
 *
 * @author White Oak
 */
public abstract class Store {

    protected static final int NEAREST_DELTA = 5 * 5 * 3;

    protected static int calculateDifference(Color origin, Color x) {
	int r = x.red - origin.red;
	int g = x.green - origin.green;
	int b = x.blue - origin.blue;
	r *= r;
	g *= g;
	b *= b;
	return r + g + b;
    }

    protected static int calculateDifference(int origin, Color x) {
	return calculateDifference(Color.of(origin), x);
    }

    public abstract void addColor(int color);

    public abstract int getNearest(int color);
}

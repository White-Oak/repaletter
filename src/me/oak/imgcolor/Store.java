package me.oak.imgcolor;

/**
 *
 * @author White Oak
 */
public abstract class Store {

    protected static final int NEAREST_DELTA = 5 * 5 * 3;

    protected static int calculateDifference(Color origin, Color x) {
//	int r = x.red - origin.red;
//	int g = x.green - origin.green;
//	int b = x.blue - origin.blue;
//	r *= r;
//	g *= g;
//	b *= b;
	//Jesus why is it faster than the next line?
//	return r * r + g * g + b * b;
	return S[x.red - origin.red + 257]
		+ S[x.green - origin.green + 257]
		+ S[x.blue - origin.blue + 257];
    }

    private final static int[] S = new int[257 * 2];

    static {
	for (int i = 0; i < S.length; i++) {
	    final int name = (i - 514);
	    S[i] = name * name;
	}
    }

    protected static int calculateDifference(int origin, Color x) {
	return calculateDifference(Color.of(origin), x);
    }

    public abstract void addColor(int color);

    public abstract int getNearest(int color);

    public abstract int size();

    public void finishedAdding() {

    }

    public void finished() {

    }
}

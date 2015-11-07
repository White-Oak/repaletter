package me.oak.imgcolor;

import java.util.Collection;
import lombok.Getter;

/**
 *
 * @author White Oak
 */
public class Storev3 extends Store {

    @Getter private final OctoTreeColor otc = new OctoTreeColor();

    @Override
    public void addColor(int color) {
	otc.insert(Color.of(color));
    }

    @Override
    public int getNearest(int color) {
	Collection<Bag<Color>> colors = null;
	final Color of = Color.of(color);
	int DELTA = 6;
	do {
	    assert DELTA < 10000 : "The shit is real";
	    colors = otc.getAllIn(Cube.centeredAround(of, DELTA));
	    DELTA *= 2;
	} while (colors.isEmpty());
	int min = Integer.MAX_VALUE;
	Bag<Color> minColorBag = null;
	for (Bag<Color> color1 : colors) {
	    int diff = calculateDifference(of, color1.value);
	    if (diff < min) {
		assert diff >= 0 : diff + "";
		min = diff;
		minColorBag = color1;
	    }
	}
	boolean remove = otc.remove(minColorBag.value);
	assert remove;
	return minColorBag.value.color;
    }

}

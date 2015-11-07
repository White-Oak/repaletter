package me.oak.imgcolor.octo;

import java.util.Collection;
import me.oak.imgcolor.Bag;
import me.oak.imgcolor.Color;

/**
 *
 * @author White Oak
 */
public class FastOctoStore extends OctoStore {

    @Override
    public void addColor(int color) {
	otc.insert(Color.of(color));
    }

    @Override
    public int getNearest(int color) {
	Collection<Bag<Color>> colors = null;
	final Color of = Color.of(color);
	OctoNode node = otc.getNode(of);
	int DELTA = 0;
	if (node.size() == 0) {
	    DELTA = node.bounds.radius + 1;
	} else {
	    DELTA = 2;
	}
	Cube centeredAround = Cube.centeredAround(of, DELTA);
	do {
	    assert DELTA < 10000 : "The shit is real";
	    colors = otc.getAllIn(centeredAround.increaseRadius(1));
	    DELTA += 1;
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

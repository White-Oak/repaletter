package me.oak.imgcolor.octo;

import java.util.List;
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
	Bag<Color> instant = otc.get(Color.of(color));
	if (instant != null) {
	    boolean remove = otc.remove(instant.value);
	    assert remove;
	    return instant.value.color;
	}

	List<Bag<Color>> colors = null;
	final Color of = Color.of(color);
	OctoNode node = otc.getNode(of);
	Cube centeredAround = Cube.centeredAround(of, node.bounds.radius);
	do {
	    assert centeredAround.radius < 1000 : "The shit is real";
	    colors = otc.getAllIn(centeredAround.increaseRadius(2));
	} while (colors.isEmpty());

	Bag<Color> minColorBag = null;
	if (colors.size() == 1) {
	    minColorBag = colors.get(0);
	} else {
	    int min = Integer.MAX_VALUE;
	    for (Bag<Color> color1 : colors) {
		int diff = calculateDifference(of, color1.value);
		if (diff < min) {
		    min = diff;
		    minColorBag = color1;
		}
	    }
	}
	boolean remove = otc.remove(minColorBag.value);
	assert remove;
	return minColorBag.value.color;
    }

}

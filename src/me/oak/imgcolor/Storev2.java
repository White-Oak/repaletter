package me.oak.imgcolor;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author White Oak
 */
public class Storev2 extends Store {

    @Getter private final TIntObjectMap<Bag<Color>> map = new TIntObjectHashMap<>();

    @Override
    public void addColor(int color) {
	Bag<Color> bag = map.get(Color.getColor(color));
	if (bag == null) {
	    Color color1 = Color.of(color);
	    bag = new Bag(color1);
	}
	bag.amount++;
	map.put(bag.value.color, bag);
	assert map.get(bag.value.color) != null;
    }

    private Bag<Color> getNearestBag(int color) {
	int min = Integer.MAX_VALUE;
	Bag<Color> minColorBag = null;
//	assert map.size() != 0;
	for (Bag<Color> bag : map.valueCollection()) {
	    Color value = bag.value;
	    int diff = calculateDifference(color, value);
	    if (diff <= NEAREST_DELTA) {
		return bag;
	    }
	    if (diff < min && diff > 0) {
		min = diff;
		minColorBag = bag;
	    }
	}
	return minColorBag;
    }

    @Override
    public int getNearest(int color) {
	Bag<Color> minColorBag = null;
//	minColorBag = map.get(Color.getColor(color));
//	if (minColorBag == null) {
	minColorBag = getNearestBag(color);
//	}
	pollFromBag(minColorBag);
	return minColorBag.value.color;
    }

    private void pollFromBag(@NonNull Bag<Color> bag) {
	bag.amount--;
	if (bag.amount == 0) {
	    map.remove(bag.value.color);
	}
    }

}

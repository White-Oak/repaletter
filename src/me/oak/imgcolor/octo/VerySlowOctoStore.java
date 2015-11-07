package me.oak.imgcolor.octo;

import me.oak.imgcolor.Bag;
import me.oak.imgcolor.Color;

public class VerySlowOctoStore extends OctoStore {

    @Override
    public void addColor(int color) {
	otc.insert(Color.of(color));
    }

    @Override
    public int getNearest(int color) {
	Bag<Color> nearest = otc.getNearest(Color.of(color));
	boolean remove = otc.remove(nearest.value);
	assert remove;
	return nearest.value.color;
    }

}

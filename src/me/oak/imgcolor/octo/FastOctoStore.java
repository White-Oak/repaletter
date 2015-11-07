package me.oak.imgcolor.octo;

import lombok.Getter;
import me.oak.imgcolor.Bag;
import me.oak.imgcolor.Color;
import me.oak.imgcolor.Store;

public class FastOctoStore extends Store {

    @Getter private final OctoTreeColor otc = new OctoTreeColor();

    @Override
    public void addColor(int color) {
	otc.insert(Color.of(color));
    }

    @Override
    public int getNearest(int color) {
	Bag<Color> nearest = otc.getNearest(Color.of(color));
	assert nearest != null;
	boolean remove = otc.remove(nearest.value);
	assert remove;
	return nearest.value.color;
    }

}

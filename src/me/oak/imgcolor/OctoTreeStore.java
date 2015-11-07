package me.oak.imgcolor;

import lombok.Getter;

public class OctoTreeStore extends Store {

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

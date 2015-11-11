package me.oak.imgcolor.octo;

import lombok.Getter;
import me.oak.imgcolor.Store;

/**
 *
 * @author White Oak
 */
public abstract class OctoStore extends Store {

    @Getter protected final OctoTreeColor otc = new OctoTreeColor();

    @Override
    public int size() {
	return otc.size();
    }

    @Override
    public void finishedAdding() {
	otc.printCurrentDebug();
    }

    @Override
    public void finished() {
	otc.printDebug();
    }

}

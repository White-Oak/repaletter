package me.oak.imgcolor.octo;

import lombok.Getter;
import me.oak.imgcolor.Store;

/**
 *
 * @author White Oak
 */
public abstract class OctoStore extends Store {

    @Getter protected final OctoTreeColor otc = new OctoTreeColor();
}

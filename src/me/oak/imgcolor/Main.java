package me.oak.imgcolor;

import java.io.IOException;
import me.oak.imgcolor.octo.Cube;
import me.oak.imgcolor.util.Timer;
import me.whiteoak.minlog.FileLogger;
import me.whiteoak.minlog.Log;

/**
 *
 * @author White Oak
 */
public class Main {

    public static void main(String[] args) throws IOException {
	Log.setLogger(new FileLogger());
	Log.DEBUG();
	Log.info("The image app has started (" + VersionCode.getCode() + ")");
	long time = Timer.time((Runnable) (() -> Color.of(0)));
	Log.info("Colors' cache loading is done in " + time + " ms");

	tests();
	APIDraft apiDraft = new APIDraft();
	apiDraft.work();
    }

    private static void tests() {
	assert (Cube.centeredAround(50, 50, 50, 5).contains(45, 45, 45));
	assert (Cube.centeredAround(50, 50, 50, 5).intersects(Cube.centeredAround(45, 45, 45, 2)));
	assert !(Cube.centeredAround(50, 50, 50, 5).contains(55, 55, 55));
	assert Cube.centeredAround(50, 50, 50, 50).intersects(Cube.centeredAround(50, 50, 50, 5));
	assert (Cube.centeredAround(0, 0, 0, 64).nearby(Cube.centeredAround(128, 128, 128, 64)));
    }

}

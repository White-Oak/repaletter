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
//	test();
	Log.setLogger(new FileLogger());
	Log.DEBUG();
	Log.info("The image app has started (" + VersionCode.getCode() + ")");
	long time = Timer.time((Runnable) (() -> Color.of(0)));
	Log.info("Colors' cache loading is done in " + time + " ms");

	tests();
	APIDraft apiDraft = new APIDraft();
	apiDraft.work();
    }
    private final static int[] testStatic;

    static {
	testStatic = new int[500 * 2];
	for (int i = 0; i < testStatic.length; i++) {
	    testStatic[i] = (i - 500) * (i - 500);
	}
    }

    private static void test() {
	int test[] = new int[500 * 2];
	for (int i = 0; i < test.length; i++) {
	    test[i] = (i - 500) * (i - 500);
	}
	int test2[] = new int[501];
	for (int i = 0; i < test2.length; i++) {
	    test2[i] = (i) * (i);
	}
	long result = 0;
	Timer timer = new Timer();

	result = 0;
	timer.start();
	for (int i = 0; i < 99999; i++) {
	    for (int j = -500; j < 500; j++) {
		result += testStatic[j + 500];
	    }
	}
	System.out.println(result);
	System.out.println(timer.total());

	result = 0;
	timer.start();
	for (int i = 0; i < 99999; i++) {
	    for (int j = -500; j < 500; j++) {
		result += test[j + 500];
	    }

	}
	System.out.println(result);
	System.out.println(timer.total());

	result = 0;
	timer.start();
	for (int i = 0; i < 99999; i++) {
	    for (int j = -500; j < 500; j++) {
		result += test2[Math.abs(j)];
	    }
	}
	System.out.println(result);
	System.out.println(timer.total());

	result = 0;
	timer.start();
	for (int i = 0; i < 99999; i++) {
	    for (int j = -500; j < 500; j++) {
		result += j * j;
	    }
	}
	System.out.println(result);
	System.out.println(timer.total());

    }

    private static void tests() {
	assert (Cube.centeredAround(50, 50, 50, 5).contains(45, 45, 45));
	assert (Cube.centeredAround(50, 50, 50, 5).intersects(Cube.centeredAround(45, 45, 45, 2)));
	assert !(Cube.centeredAround(50, 50, 50, 5).contains(55, 55, 55));
	assert Cube.centeredAround(50, 50, 50, 50).intersects(Cube.centeredAround(50, 50, 50, 5));
	assert (Cube.centeredAround(0, 0, 0, 64).nearby(Cube.centeredAround(128, 128, 128, 64)));
    }

}

package me.oak.imgcolor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import me.oak.imgcolor.util.Timer;
import me.whiteoak.minlog.FileLogger;
import me.whiteoak.minlog.Log;

/**
 *
 * @author White Oak
 */
public class Main {

    private static final File def = new File("in6/");

    public static void main(String[] args) throws IOException {
	assert (Cube.centeredAround(50, 50, 50, 5).intersects(new Cube(45, 45, 45, 2)));
	assert !(Cube.centeredAround(50, 50, 50, 5).contains(55, 55, 55));
	assert Cube.centeredAround(50, 50, 50, 50).intersects(Cube.centeredAround(50, 50, 50, 5));
	assert (new Cube(0, 0, 0, 64).nearby(new Cube(64, 64, 64, 64)));
	imageWork();
    }

    private static void imageWork() throws IOException {
	Log.setLogger(new FileLogger());

	Log.info("The image app has started (" + VersionCode.getCode() + ")");

	Timer.Result<Color> time = Timer.time(() -> Color.of(0));
	Log.info("Colors' cache loading is done in " + time.totalTime + " ms");

	Timer.Result<Store> result = Timer.time(() -> getStoreWithPalette());
	Store store = result.result;
	Log.info("Palette loading is done in " + result.totalTime + " ms");

	if (store instanceof OctoTreeStore) {
	    ((OctoTreeStore) store).getOtc().printCurrentDebug();
	}
	if (store instanceof Storev3) {
	    ((Storev3) store).getOtc().printCurrentDebug();
	}

	Timer.Result<int[]> result1 = Timer.time(() -> buildOriginalPictureWithPallette(store));
	int[] pixelsOutput = result1.result;
	Log.info("Resulting image is made in " + result1.totalTime + " ms");

	save(pixelsOutput);

	if (store instanceof Storev3) {
	    storev3Test((Storev3) store);
	}
	if (store instanceof OctoTreeStore) {
	    ((OctoTreeStore) store).getOtc().printDebug();
	}
    }

    private static void storev3Test(Storev3 storev3) {
	final OctoTreeColor otc = (storev3).getOtc();
	otc.printDebug();
//	Timer.Result<Optional<Bag<Color>>> time;
//	System.out.println("Size of this store is " + otc.size());
//	System.out.println("Depth of this store is " + (storev3).getOtc().depth());
////	time = Timer.time(() -> otc.getAnyIn(new Cube(210, 40, 20, 50)));
////	Log.info("Getting color is done in " + time.totalTime + " ms");
//	time = Timer.time(() -> otc.removeAnyIn(new Cube(210, 40, 20, 50)));
//	Log.info("Removing color is done in " + time.totalTime + " ms");
//	System.out.println("Size of this store is " + otc.size());
//	System.out.println("Depth of this store is " + (storev3).getOtc().depth());
    }

    private static int[] buildOriginalPictureWithPallette(Store store) throws IOException {
	Timer timer = new Timer();
	timer.start();
	long total;
	int[] pixelsOriginal = getColors("original.png");
	int[] pixelsOutput = new int[pixelsOriginal.length];

	int[] progress = new int[20];
	for (int i = 1; i < progress.length + 1; i++) {
	    progress[i - 1] = pixelsOriginal.length / 20 * i;
	}
	int progressPercent = 1;
	for (int i = 0; i < pixelsOriginal.length; i++) {
	    pixelsOutput[i] = store.getNearest(pixelsOriginal[i]);
	    if (isIn(i, progress)) {
		total = timer.total();
		System.out.println((progressPercent++) * 5 + "% in " + total + " ms");
	    }
	}
	return pixelsOutput;
    }

    private static Store getStoreWithPalette() throws IOException {
	int[] pixelsPalette = getColors("palette.png");
	Store store = new Storev2();
	for (int i : pixelsPalette) {
	    store.addColor(i);
	}
	return store;
    }

    private static boolean isIn(int i, int[] ar) {
	for (int j = 0; j < ar.length; j++) {
	    if (ar[j] == i) {
		return true;
	    }
	}
	return false;
    }

    private static void save(int[] pixelsOutput) throws IOException {
	BufferedImage image = ImageIO.read(new File(def, "original.png"));
	int width = image.getWidth();
	int height = image.getHeight();
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	image.getRaster().setDataElements(0, 0, width, height, pixelsOutput);
	ImageIO.write(image, "PNG", new File(def, "output.png"));

//	final Storev4 name = (Storev4) storev2;
//	System.out.println("Fast: " + (name.counterAlmost / (float) name.total * 100) + "%");
//	System.out.println("Slow: " + (name.counter / (float) name.total * 100) + "%");
    }

    private static int[] getColors(String string) throws IOException {
	BufferedImage image = ImageIO.read(new File(def, string));
	int w = image.getWidth();
	int h = image.getHeight();

	return image.getRGB(0, 0, w, h, null, 0, w);
    }

    private static void kek(byte[] pixelsOriginal, byte[] pixelsPalette) {
	int signDiff = (int) Math.signum(pixelsOriginal.length - pixelsPalette.length);
	assert Math.abs(signDiff) == 1 || signDiff == 0;
	if (pixelsOriginal.length != pixelsPalette.length) {
	    if (pixelsOriginal.length < pixelsPalette.length) {
		float counter = 0f;
		float increment = pixelsPalette.length / pixelsOriginal.length;
		for (int i = 0; i < pixelsPalette.length; i++) {
		    byte b = pixelsPalette[i];
		    counter += (increment - 1f);
		    if (increment > 1) {

		    }
		}
	    }
	}
    }

}

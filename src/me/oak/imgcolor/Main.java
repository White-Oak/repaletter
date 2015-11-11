package me.oak.imgcolor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import me.oak.imgcolor.octo.*;
import me.oak.imgcolor.util.Timer;
import me.whiteoak.minlog.FileLogger;
import me.whiteoak.minlog.Log;

/**
 *
 * @author White Oak
 */
public class Main {

    private static final File def = new File("ins/in5/");

    public static void main(String[] args) throws IOException {
	imageWork();
    }

    private static void imageWork() throws IOException {
	Log.setLogger(new FileLogger());

	Log.info("The image app has started (" + VersionCode.getCode() + ")");

	Timer.Result<Color> time = Timer.time(() -> Color.of(0));
	Log.info("Colors' cache loading is done in " + time.totalTime + " ms");

	tests();

	Timer.Result<Store> result = Timer.time(() -> getStoreWithPalette());
	Store store = result.result;
	Log.info("Palette loading is done in " + result.totalTime + " ms");

	if (store instanceof OctoStore) {
	    ((OctoStore) store).getOtc().printCurrentDebug();
	}

	Timer.Result<int[]> result1 = Timer.time(() -> buildOriginalPictureWithPallette(store));
	int[] pixelsOutput = result1.result;
	Log.info("Resulting image is made in " + result1.totalTime + " ms");

	save(pixelsOutput);

	if (store instanceof OctoStore) {
	    ((OctoStore) store).getOtc().printDebug();
	}
    }

    private static void tests() {
	assert (Cube.centeredAround(50, 50, 50, 5).contains(45, 45, 45));
	assert (Cube.centeredAround(50, 50, 50, 5).intersects(Cube.centeredAround(45, 45, 45, 2)));
	assert !(Cube.centeredAround(50, 50, 50, 5).contains(55, 55, 55));
	assert Cube.centeredAround(50, 50, 50, 50).intersects(Cube.centeredAround(50, 50, 50, 5));
	assert (Cube.centeredAround(0, 0, 0, 64).nearby(Cube.centeredAround(128, 128, 128, 64)));
    }

    private static int[] buildOriginalPictureWithPallette(Store store) throws IOException {
	Timer timer = new Timer();
	timer.start();
	long total;
	BufferedImage image = ImageIO.read(new File(def, "original.png"));
	int width = image.getWidth();
	int height = image.getHeight();
	int[] pixelsOriginal = image.getRGB(0, 0, width, height, null, 0, width);
	int[] pixelsOutput = new int[pixelsOriginal.length];

	int[] progress = new int[20];
	for (int i = 1; i < progress.length + 1; i++) {
	    progress[i - 1] = pixelsOriginal.length / 20 * i;
	}
	int progressPercent = 1;

	int processed = 0;
	int startX = width / 2;
	int startY = height / 2;
	for (int i = 0; i < Math.max(width, height) / 2; i++) {
	    int x = startX - i;
	    int y = startY - i;
	    for (int k = 0; k < 4; k++) {
		for (int j = 0; j < i * 2 + (k < 2 ? 1 : 2); j++) {
		    int acc = x + y * width;
		    if (acc >= 0 && acc < pixelsOriginal.length && processed < pixelsOriginal.length) {
			processed++;
			pixelsOutput[acc] = store.getNearest(pixelsOriginal[acc]);
			if (isIn(processed, progress)) {
			    System.out.printf("%d%% in %d ms\r\n", (progressPercent++) * 5, timer.total());
			}
		    }
		    if (k % 2 == 0) {
			x += k < 2 ? 1 : -1;
		    } else {
			y += k < 2 ? 1 : -1;
		    }
		}
	    }
	}
	return pixelsOutput;
    }

    private static Store getStoreWithPalette() throws IOException {
	int[] pixelsPalette = getColors("palette.png");
	Store store = new FastOctoStore();
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
    }

    private static int[] getColors(String string) throws IOException {
	BufferedImage image = ImageIO.read(new File(def, string));
	int w = image.getWidth();
	int h = image.getHeight();

	return image.getRGB(0, 0, w, h, null, 0, w);
    }

}

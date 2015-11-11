package me.oak.imgcolor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.oak.imgcolor.octo.FastOctoStore;
import me.oak.imgcolor.util.Timer;
import me.whiteoak.minlog.Log;

/**
 *
 * @author White Oak
 */
public class APIDraft {

    private static final File DEFAULT_DIR = new File("ins/in5/");

    private Image image;
    private Store store;

    private final File directory;

    public APIDraft(File directory) {
	this.directory = directory;
    }

    public APIDraft() {
	this(DEFAULT_DIR);
    }

    public void work() throws IOException {
	long time;

	loadOriginalImage();

	time = Timer.time(() -> loadPalette());
	Log.info("Palette loading is done in " + time + " ms");

	time = Timer.time(() -> buildResultingImage());
	Log.info("Resulting image is made in " + time + " ms");

	store.finished();

	save();
    }

    @SneakyThrows
    private void loadOriginalImage() {
	final BufferedImage image = ImageIO.read(new File(directory, "original.png"));
	final int width = image.getWidth();
	final int height = image.getHeight();
	final int[] pixelsOriginal = image.getRGB(0, 0, width, height, null, 0, width);
	this.image = new Image(width, height, pixelsOriginal);
    }

    @SneakyThrows
    private void loadPalette() {
	final BufferedImage image = ImageIO.read(new File(directory, "palette.png"));
	final int width = image.getWidth();
	final int height = image.getHeight();
	int[] pixelsPalette = image.getRGB(0, 0, width, height, null, 0, width);
	store = new FastOctoStore();
	final int length = this.image.pixels.length;
	if (pixelsPalette.length < length) {
	    Log.info("Provided is not big enough, but I can deal with it");
	    //If palette is smaller than an original image, some of palette's pixels will be used more than once
	    final double diff = (length - pixelsPalette.length) / pixelsPalette.length;
	    double counter = 0;
	    for (int i = 0; i < pixelsPalette.length; i++) {
		final int pixel = pixelsPalette[i];
		store.addColor(pixel);
		counter += diff;
		while (counter > 1) {
		    store.addColor(pixel);
		    counter -= 1;
		}
	    }
	} else {
	    for (int i = 0; i < pixelsPalette.length; i++) {
		store.addColor(pixelsPalette[i]);
	    }
	}
	if (store.size() < length) {
	    Log.warn(String.format("Store's size is smaller than original image's: %d to %d", store.size(), length));
	    while (store.size() < length) {
		store.addColor(0);
	    }
	}
	store.finishedAdding();
    }

    private boolean isIn(int i, int[] ar) {
	for (int j = 0; j < ar.length; j++) {
	    if (ar[j] == i) {
		return true;
	    }
	}
	return false;
    }

    public void buildResultingImage() {
	int width = image.width;
	int height = image.height;

	int[] progress = new int[20];
	final int[] pixels = image.pixels;
	for (int i = 1; i < progress.length + 1; i++) {
	    progress[i - 1] = pixels.length / 20 * i;
	}

	Timer timer = new Timer();
	timer.start();
	int progressPercent = 1;
	int processed = 0;
	int startX = width / 2;
	int startY = height / 2;
	//this cycle goes through image in spiral, starting from a center
	for (int i = 0; i < Math.max(width, height) / 2; i++) {
	    int x = startX - i;
	    int y = startY - i;
	    for (int k = 0; k < 4; k++) {
		for (int j = 0; j < i * 2 + (k < 2 ? 1 : 2); j++) {
		    int acc = x + y * width;
		    if (acc >= 0 && acc < pixels.length && processed < pixels.length) {
			processed++;
			pixels[acc] = store.getNearest(pixels[acc]);
			if (isIn(processed, progress)) {
			    System.out.printf("%d%% in %d ms" + System.lineSeparator(), (progressPercent++) * 5, timer.total());
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
    }

    @SneakyThrows
    private void save() throws IOException {
	BufferedImage imageBuf = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB);
	imageBuf.getRaster().setDataElements(0, 0, image.width, image.height, image.pixels);
	ImageIO.write(imageBuf, "PNG", new File(directory, "output.png"));
    }

    @RequiredArgsConstructor
    private class Image {

	final int width;
	final int height;
	final int[] pixels;
    }
}

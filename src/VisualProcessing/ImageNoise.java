package VisualProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageNoise {

	private static int[][] lowPassX(int[][] img, float smoothing) {
		int value = img[0][0];
		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				int currentValue = img[y][x];
				value += (currentValue - value) / smoothing;
				value = value < 0 ? 0 : value;
				value = value > 255 ? 255 : value;
				out[y][x] = value;

			}
		}
		return out;
	}

	private static int[][] lowPassY(int[][] img, float smoothing) {
		int value = img[0][0];
		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int currentValue = img[y][x];
				value += (currentValue - value) / smoothing;
				value = value < 0 ? 0 : value;
				value = value > 255 ? 255 : value;
				out[y][x] = value;

			}
		}
		return out;
	}

	public static int[][] lowPass(int[][] img, float smoothing) {

		int[][] xImg = lowPassX(img, smoothing);
		int[][] yImg = lowPassY(img, smoothing);

		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int xPixel = xImg[y][x];
				int yPixel = yImg[y][x];
				out[y][x] = (xPixel < yPixel) ? xPixel : yPixel;

			}
		}
		return out;

	}
	
	public static BufferedImage GaussianFilter(BufferedImage img,
			float dc_level, float cutoff) {

		int value = 0;
		double distance = 0;
		int sizex = img.getWidth();
		int sizey = img.getHeight();

		BufferedImage out = new BufferedImage(sizex, sizey,
				BufferedImage.TYPE_3BYTE_BGR);

		int xcenter = (sizex / 2) + 1;
		int ycenter = (sizey / 2) + 1;
		for (int y = 0; y < sizey; y++) {
			for (int x = 0; x < sizex; x++) {
				distance = Math.abs(x - xcenter) * Math.abs(x - xcenter)
						+ Math.abs(y - ycenter) * Math.abs(y - ycenter);
				distance = Math.sqrt(distance);
				value = (int) (dc_level * Math.exp((-1 * distance * distance)
						/ (1.442695 * cutoff * cutoff)));
				out.setRGB(xcenter, y, new Color(value, value, value).getRGB());

			}
		}

		return out;
	}
	
}

package VisualProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 
 * This class does operations on an image that repeat the same operation on every pixel, and only considers that pixel
 *
 */
public class FundUtil {

	/**
	 * Inverts a binary Image. Swaps black to white and white to black
	 * @param img The image to invert
	 * @return The inverted Image
	 * @throws Throws an exception if the image is not a binary image
	 */
	public static int[][] invert(int[][] img) {

		int width = img[0].length;
		int height = img.length;

		int[][] out = new int[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = img[y][x];
				if (pixel == 0) {
					pixel = 255;
				} else if (pixel == 255) {
					pixel = 0;
				} else {
					throw new RuntimeException("Must be binary image");
				}
				out[y][x] = pixel;
			}
		}
		return out;
	}
	

	/**
	 * Converts an image into a binary image based on a threshHold <br> Pixels above and equal the threshold are black, and below are white
	 * @param img The image 
	 * @param threshHold The threshHolding value
	 * @return A binary image
	 */
	public static int[][] threshHold(int[][] img, int threshHold) {
		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				out[y][x] = img[y][x] > threshHold ? 255 : 0;
			}
		}
		return out;
	}
	
	/**
	 * Gray scales an image
	 * @param img The image
	 * @return The grayScaled Image
	 */
	public static int[][] grayScale(BufferedImage img) {
		return grayScale(img, 4);
	}
	
	/**
	 * Grayscales an image with a specific channel <br>
	 *  1 -> red <br>
	 *  2 -> green <br>
	 *  3 -> blue <br>
	 *  other values average for the grayscale
	 * @param img The 
	 * @param type
	 * @return
	 */
	public static int[][] grayScale(BufferedImage img, int type) {
		int[][] out = new int[img.getHeight()][img.getWidth()];
		
		for(int y = 0; y < img.getHeight(); y++) {
			for(int x = 0; x < img.getWidth(); x++) {
				Color c = new Color(img.getRGB(x, y));
				int pixel;
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				switch(type) {
				case 1: pixel= red; break;
				case 2: pixel = green;break;
				case 3: pixel = blue;break;
				default: pixel = (red + green + blue)/3;break;
				}
				out[y][x] = pixel;
			}
		}
		return out;
	}

	
}

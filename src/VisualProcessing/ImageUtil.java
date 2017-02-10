package VisualProcessing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.*;

import com.sun.prism.image.Coords;

/**
 * 
 * Contains general useful functions for comparison, visualization, and conversions
 *
 */
public class ImageUtil {

	/**
	 * Draws a 2*2 pixel on an image
	 * @param img The image to draw the pixel on
	 * @param x The x cord of where to draw
	 * @param y The y cord of where to draw
	 * @param rgb The rgb color value
	 */
	public static void drawLargePixel(BufferedImage img, int x, int y, int rgb) {
		try {
			img.setRGB(x, y, rgb);
			img.setRGB(x + 1, y, rgb);
			img.setRGB(x - 1, y, rgb);
			img.setRGB(x, y + 1, rgb);
			img.setRGB(x, y - 1, rgb);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	/**
	 * Opens a new window containing an image
	 * @param imgs the images to view
	 */
	public static void showImage(BufferedImage... imgs) {
		for (BufferedImage img : imgs) {
			showImage(img);
		}
	}

	/**
	 * Opens up a single image
	 * @param img The image to open up
	 */
	public static void showImage(BufferedImage img) {
		showImage(img, "");
	}

	/**
	 * Opens up an image
	 * @param img The image to open up
	 * @param title The title of the window
	 */
	public static void showImage(BufferedImage img, String title) {
		JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(img)));
		frame.setTitle(title);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Converts an image array into an image
	 * @param data The image array 
	 * @return the image 
	 */
	public static BufferedImage arrayToImg(int[][] data) {
		int width = data[0].length;
		int height = data.length;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pixel = data[y][x];
				img.setRGB(x, y, (new Color(pixel, pixel, pixel).getRGB()));

			}
		}

		return img;

	}

	/**
	 * tests if two images contain the same data, and are the same size
	 * @param img1 The first image
	 * @param img2 The second image
	 * @return If the robots are the same
	 */
	public static boolean equals(BufferedImage img1, BufferedImage img2) {
		if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
			return false;
		}
		for (int x = 0; x < img1.getWidth(); x++) {
			for (int y = 0; y < img1.getHeight(); y++) {
				if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * creates a copy of an image
	 * @param img The image
	 * @return a copy of the image
	 */
	public static BufferedImage copy(BufferedImage img) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				out.setRGB(x, y, img.getRGB(x, y));
			}
		}
		return out;
	}

	/**
	 * Converts to an image array to a binary image array
	 * @param img An image array
	 * @return A binary image array
	 */
	public static int[][] imgToBinaryImgArr(BufferedImage img) {
		int[][] out = new int[img.getWidth()][img.getHeight()];
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				out[x][y] = (img.getRGB(x, y) & 0xff) == 0 ? 0 : 1;
			}
		}
		return out;
	}

	/**
	 * Converts an image array to image image
	 * @param data An image array
	 * @return An image of the image array
	 */
	public static BufferedImage arrToImg(int[][] data) {

		int width = data[0].length;
		int height = data.length;

		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// System.out.println(data[y][x]);
				int pixel = data[y][x] * 255;
				// System.out.println(pixel);
				out.setRGB(x, y, new Color(pixel, pixel, pixel).getRGB());
			}
		}
		return out;
	}

	

	/**
	 * 
	 * @param img
	 * @param error
	 *            % of black required
	 * @return
	 */

	// Superbowl Predictions: Atlanta Falcons.

}
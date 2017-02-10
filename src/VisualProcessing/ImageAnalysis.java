package VisualProcessing;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * This class is used to analyze images and find certain properties
 *
 */
public class ImageAnalysis {

	/**
	 * Starts at a given point, and moves in the given X direction until a image edge is hit
	 * <br>
	 * Used to find the boundaries of a hollow, rectangular shape
	 * @param img The image to check
	 * @param center The point to start from
	 * @param right The direction to move
	 * @return an integer array of length 2. <br>
	 * index 0 is the distance to the inner edge, index 1 is the distance to the outer edge
	 * <br> {-1, -1, -1} is returned if the edge of the image was hit
	 */
	public static int[] findImageMaxX(int[][] img, Point center, boolean right) {

		return findImageMaxX(img, center, 0, right, false, new int[2]);
		
	}

	private static int[] findImageMaxX(int[][] img, Point center, int width,
			boolean right, boolean found, int[] data) {
		try {
			if (img[center.y][center.x + width] == 255 && !found) {
				data[0] = right ? width : -width;
				return findImageMaxX(img, center, width + (right ? 1 : -1),
						right, false, data);
			} else if (img[center.y][center.x + width] == 0) {
				return findImageMaxX(img, center, width + (right ? 1 : -1),
						right, true, data);
			} else {
				data[1] = right ? width : -width;
				return data;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return new int[] { -1, -1, -1 };
		}

	}
	
	public static int findImageMaxY(int[][] img, Point center, boolean up) {
		return findImageMaxY(img, center, 0, up);
	}

	/**
	 * Starts at a given point, and moves in the given Y direction until a image edge is hit
	 * <br>
	 * Used to find the boundaries of a hollow, rectangular shape
	 * @param img The image to check
	 * @param center The point to start from
	 * @param right The direction to move
	 * @return an integer array of length 2. <br>
	 * index 0 is the distance to the inner edge, index 1 is the distance to the outer edge
	 * <br> {-1, -1, -1} is returned if the edge of the image was hit
	 */
	
	private static int findImageMaxY(int[][] img, Point center, int height,
			boolean up) {

		try {
			if (img[center.y + height][center.x] == 0
					|| img[center.y + height][center.x + 1] == 0
					|| img[center.y + height][center.x - 1] == 0) {
				return findImageMaxY(img, center, height + (up ? -1 : 1), up);
			} else {
				return up ? -height : height;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	/**
	 * Trys to fit a rectangle to an image by recursively making it bigger
	 * @param img The image to fit
	 * @param error The percent of black pixels allowed to extend rectangle
	 * @return A rectangle representing the boundaries of a rectangle
	 */
	
	public static Rectangle getRect(int[][] img, double error) {
		Point center = findCenter(img);
		if(center == null) {
			return null;
		}
		Point topLeft = new Point(center.x - 1, center.y - 1);
		Point botRight = new Point(center.x + 1, center.y + 1);
		boolean hasChanged = true;
		while (hasChanged) {
			hasChanged = false;

			try { // up
				double blacks = 0;
				for (int x = topLeft.x; x < botRight.x; x++) {
					blacks += (img[topLeft.y - 1][x] == 0) ? 1 : 0;
				}
				blacks /= (botRight.x - topLeft.x);
				 // System.out.println(blacks);
				if (blacks >= error) {
					hasChanged = true;
					topLeft.translate(0, -1);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Error ArrayIndexOutOfBoundsException at line 110 in Image Analysis");
			}

			try { // down
				double blacks = 0;
				for (int x = topLeft.x; x < botRight.x; x++) {
					blacks += (img[botRight.y + 1][x] == 0) ? 1 : 0;
				}
				blacks /= (botRight.x - topLeft.x);
				if (blacks >= error) {
					hasChanged = true;
					botRight.translate(0, 1);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}

			try { // left
				double blacks = 0;
				for (int y = topLeft.y; y < botRight.y; y++) {
					blacks += (img[y][topLeft.x - 1] == 0) ? 1 : 0;
				}
				blacks /= (botRight.y - topLeft.y);
				if (blacks >= error) {
					hasChanged = true;
					topLeft.translate(-1, 0);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}

			try { // right
				double blacks = 0;
				for (int y = topLeft.y; y < botRight.y; y++) {
					blacks += (img[y][botRight.x + 1] == 0) ? 1 : 0;
				}
				blacks /= (botRight.y - topLeft.y);
				if (blacks >= error) {
					hasChanged = true;
					botRight.translate(1, 0);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			// System.out.println(topLeft + " " + botRight);
		}

		return new Rectangle(topLeft.x, topLeft.y, botRight.x - topLeft.x,
				botRight.y - topLeft.y);
	}
	
	
	/**
	 * Finds the center of an image
	 * @param img The image
	 * @return The center of the image
	 */
	public static Point findCenter(int[][] img) {

		int xAvg = 0;
		int yAvg = 0;
		int width = img[0].length;
		int height = img.length;
		int total = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				if (img[y][x] == 0) {
					xAvg += x;
					yAvg += y;
					total++;
				}

			}
		}

		if (total == 0)
			return null;
		
		xAvg /= total;
		yAvg /= total;

		return new Point(xAvg, yAvg);
	}
	
	
}
